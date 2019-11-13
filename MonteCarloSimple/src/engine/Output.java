package engine;

import path.*;
import payout.*;

public class Output<ValType>{
	
	private GBMPathGenerator _GBMPathGenerator;
	private Payout _option;
	private double _avg;
	private double _sigma;
	private double _y;
	private double _errorlimit;
	

	public Output(Payout option, double initialP, double r, double sigma, String date,double errorlimit, double p) {
		_GBMPathGenerator=new GBMPathGenerator(initialP, r, sigma, date);
		_option=option;
		_errorlimit=errorlimit;
		double t=Math.sqrt(Math.log(1/(p*p)));
		double c0 = 2.515517;
		double d1 = 1.432788;
		double c1 = 0.802853;
		double d2 = 0.189269;
		double c2 = 0.010328;
		double d3 = 0.001308;
		double y=t-(c0+c1*t+c2*t*t)/(1+d1*t+d2*t*t+d3*Math.pow(t, 3));
		_y=y;
	}
	
	
	public double gety() {return _y;}
	public double getmean() {return _avg;}	
	public double getsigma() {return _sigma;}	
	public void setMeanSigma(double mean, double sigma) {
		_avg=mean;
		_sigma=sigma;
	}
	
	/** Through 10000 times iteration, we get a estimated sigma 
	 * @param num
	 * @throws Exception 
	 */
	public void WarmUpSigma(int num) throws Exception {
		_GBMPathGenerator.simulate();
		StatsCollector statscollector=new StatsCollector(_option.payout( _GBMPathGenerator.getPath()));	
		for (int i=0;i<num;i++) {
			statscollector.update(_option.payout( _GBMPathGenerator.getPath()));
			_GBMPathGenerator.simulate();
		}
		setMeanSigma(statscollector.getmean(), statscollector.getsd());
	} 
	
	
	/** And then, we use the sigma we get to average our payout by iterating N times and update 
	 * our mean and sd every iteration
	 * @throws Exception 
	 */
	public void AvgOutput() throws Exception {
		//The difference is when using simulate, we use antithetic simulation when we implement simulate
		_GBMPathGenerator.simulate();
		int N=(int) Math.pow(_y*_sigma/_errorlimit, 2)+1;
		StatsCollector statscollector=new StatsCollector(_option.payout( _GBMPathGenerator.getPath()));
		for(int i=0;i<N;i++) {
			statscollector.update(_option.payout( _GBMPathGenerator.getPath()));
			_GBMPathGenerator.simulate();
		}		
		System.out.println(N+" times");
		setMeanSigma(statscollector.getmean(), statscollector.getsd());
	}


}
