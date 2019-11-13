package activemq;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import path.GBMPathGenerator;
import path.Path;
import payout.*;

public class MCClient {
	
	protected static String brokerURL = "tcp://localhost:61616";
	protected static ConnectionFactory factory;
	protected Connection connection;
	protected Session session;
	protected String ticker;
	protected MessageProducer producer;
	
	protected static String _request="SimulationRequests";
	protected static String _result="SimulationResults";
	protected Payout _option;
	protected double _payout;
	protected String[] _parts;
	
	
	public MCClient() throws Exception {
		  factory = new ActiveMQConnectionFactory(brokerURL);
	      connection = factory.createConnection();
	      connection.start();
	      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	      producer = session.createProducer(null);
	}
	
	/** Close the connection
     */
    public void close() throws JMSException {
    	if (connection != null) {
    		connection.close();
    	}
    }   
	
	public static void main(String[] args) throws Exception {
		 MCClient MCClient = new MCClient();
		 MCClient.run();
	}
	
	/** Client receives the requests for simulation, and it uses information in the message 
	 * to calculate the payoff of each option and send out the result with a unique ID
	 * @throws Exception
	 */
	public void run() throws Exception{
	      Destination request = session.createTopic(_request);
	      Destination result = session.createTopic(_result);
	      // Set a message consumer receiving the simulation request
	      MessageConsumer messageConsumer =session.createConsumer(request);
	      
	      messageConsumer.setMessageListener(new MessageListener() {
	         public void onMessage(Message message) {
	            if (message instanceof TextMessage){
	               try{
	            	   // Separate the parameters in the message
	            	   _parts=((TextMessage) message).getText().split(" ") ;
	            	   double initialP=  Double.parseDouble(_parts[3]);
	            	   double r=  Double.parseDouble(_parts[0]);
	               	   double sigma=  Double.parseDouble(_parts[1]);
	                   double K=Double.parseDouble(_parts[2]);
	                   String duration=_parts[5];
	                  // Compute the payoff of each one path using the parameters received
	               	   GBMPathGenerator GBMPathGenerator= new GBMPathGenerator(initialP, r, sigma, duration);
	               	   GBMPathGenerator.simulate();
	            	   Path path=GBMPathGenerator.getPath();	            	   	            	   
	            	   if(_parts[4].equals("PayoutCall")) {	       	   
	            		   _payout = new PayoutCall(K).payout(path);	            	   
	            	   }
					   else if(_parts[4].equals("PayoutAsian")) {
							_payout = new PayoutAsian(K).payout(path);
						}
  
	            	   TextMessage textMessage = session.createTextMessage(_parts[6]+" "+_payout);
	            	   // Send the payoff result with ID to Server
					   producer.send(result,textMessage);
	               } catch (Exception e ){
	                  e.printStackTrace();
	               }
	            }
	         }
	      });
	}
	
   public Session getSession() {
	    return session;
   }
	
   
	 
	
	 
}
