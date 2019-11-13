package activemq;

import java.util.HashSet;
import java.util.Random;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import engine.Output;
import path.StatsCollector;
import payout.Payout;
import payout.PayoutAsian;
import payout.PayoutCall;

public class MCServer { 

	protected static String brokerURL = "tcp://localhost:61616";
	protected static ConnectionFactory factory;
	protected Connection connection;
	protected Session session;
	protected MessageProducer producer;
	protected static String _request="SimulationRequests";
	protected static String _result="SimulationResults";
	protected String[] _parts;
	protected String _msg;
	protected int _batch;
	protected Output _output;
	
	private double _errorlimit;

	/** The Server sends the simulation requests to Client and receives the simulation results 
	 * from Client. It stops sending requests when the estimated error is small enough
	 */
	public MCServer(double r, double sigma, double K, double S0, Payout payout, String duration, double errorlimit, int batch, double p) throws JMSException {
	    factory = new ActiveMQConnectionFactory(brokerURL);
	    connection = factory.createConnection();
	    connection.start();
	    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    producer = session.createProducer(null);
		_errorlimit= errorlimit;
		_msg= r+" "+ sigma+" "+ K+" "+ S0+" "+ payout.getClass().getSimpleName()+" "+ duration;
	    _batch=batch;
	    _output = new Output(payout, S0, r, sigma, duration,errorlimit,p);
	}
	
    /** Close the connection 
     * @throws JMSException
     */
	public void close() throws JMSException {
	    if (connection != null) {
	        connection.close();
	    }
	}
	
	
	public static void main(String[] args) throws Exception {
		double S0=152.35;                       	//Initial Stock Price
		double r=0.0001;                           	//Interest Rate
		double sigma=0.01;                          //Volatility
		String duration = "2019-01-01";             //Start Date of the Option
		double errorlimit=0.1;                      //Required Error Limit
		double K=164;                               //Strike Price for Asian option
		int batch=100;                              //num of simulation each time 
		double p=(1-0.96)/2;                        //Probability for calculating the error
		PayoutAsian asian = new PayoutAsian(K);
		MCServer MCServer = new MCServer(r, sigma, K, S0, asian, duration, errorlimit, batch, p);
		System.out.println("Asian Option");
		MCServer.run();
		System.out.println("");
		
		double K2=165;                              //Strike Price for EU Call option
		PayoutCall call = new PayoutCall(K2);
		MCServer MCServer2 = new MCServer(r, sigma, K2, S0, call, duration, errorlimit, batch, p);
		System.out.println("European Call");
		MCServer2.run();
	}
	
    /** The Server sends the requests for simulation to the Client, and it updates estimated price 
     * and accuracy using the simulation result received from Client, and it stops when it satisfies three
     * conditions.
     * @throws Exception
     */
	public void run() throws Exception{
		Destination request = session.createTopic( _request);
		Destination result = session.createTopic( _result);
		MessageConsumer messageConsumer = session.createConsumer(result);
		StatsCollector statscollector=new StatsCollector();
		HashSet<String> IDs=new HashSet<String>();
		int iteration=0;
		
		double error=Double.POSITIVE_INFINITY;
		int N=0;
		do{
			Random randomGenerator= new Random();
			// Send the simulation requests to Client
			for ( int i = 0; i < _batch; i++){
				int ID = randomGenerator.nextInt(Integer.MAX_VALUE);
				TextMessage msg = session.createTextMessage(_msg+" "+ID);
				producer.send(request,msg);
				// Save the unique ID number into a hashset in case of repeating
				IDs.add(String.valueOf(ID));
			}
			iteration+=_batch;
			// Let the Thread rest to have enough time for Server to send all the requests to Client
			Thread.sleep(1);
			
			//receive messages from the Client
			messageConsumer.setMessageListener(new MessageListener() {
		         public void onMessage(Message message) {
			            if (message instanceof TextMessage){
			               try{
			            	   _parts=((TextMessage) message).getText().split(" ") ;
			            	   String ID=_parts[0];
			            	   double payoff= Double.parseDouble(_parts[1]);
			            	   // Check if the ID match a request from the Server
			            	   if(IDs.contains(ID)) {
			            		    // updates the estimated price and accuracy
			            			statscollector.update(payoff);
			            			IDs.remove(ID);
								} 
			               } catch (Exception e ){
			                  e.printStackTrace();
			               }			         
			            } 
			         }
			      });
			// Let the Thread rest to have enough time for Server to receive all the results from Client
			Thread.sleep(300);
			
			// Calculate the estimated error
			error = (_output.gety()*statscollector.getsd())/Math.sqrt(statscollector.getn());
			// Dynamic update the necessary steps that need to be simulated
			N=statscollector.getIterNum(_output.gety(), _errorlimit);
			
        // Checks if the iteration is bigger than the necessary number, if iteration has been 
		//	warmed up, and if the estimated error is smaller than the need	
		} while(iteration <= N || iteration <=10000 || error>=_errorlimit);	 
		
		
		System.out.println(statscollector.getn()+" times");
		System.out.println("Mean: "+statscollector.getmean());
		System.out.println("Sigma: "+statscollector.getsd());
	}
	


}
