package main;

import engine.Output;
import payout.*;

public class main {

	public static void main(String[] args) throws Exception {
		double initialP=152.35;
		double r=0.0001;
		double sigma=0.01;
		String date = "2019-01-01";
		double errorlimit=0.1;
		double p=(1-0.96)/2;
		
		System.out.println("Asian Option");
		PayoutAsian asian = new PayoutAsian(164);
		Output output = new Output(asian, initialP, r, sigma, date,errorlimit,p);
		output.WarmUpSigma(10000);
		output.AvgOutput();
		System.out.println("Mean: "+output.getmean());
		System.out.println("Sd: "+output.getsigma()); 
		
		System.out.println(); 

		System.out.println("European Call");
		PayoutCall call = new PayoutCall(165,"2019-09-09");
		Output output2 = new Output(call, initialP, r, sigma, date,errorlimit,p);
		output2.WarmUpSigma(10000);
		output2.AvgOutput();
		System.out.println("Mean: "+output2.getmean());
		System.out.println("Sd: "+output2.getsigma());
		

	}

}
