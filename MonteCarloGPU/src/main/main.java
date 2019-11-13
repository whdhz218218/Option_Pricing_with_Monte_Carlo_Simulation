package main;

import engine.Output;
import payout.*;

public class main {

	public static void main(String[] args) throws Exception {
		double initialP=152.35;
		double r=0.0001;
		double sigma=0.01;
		double errorlimit=0.1;
		double p=(1-0.96)/2;
		double K= 165;
		int numdays= 252;	
		int batch= 1000000;

		System.out.println("European Call");
		PayoutCall call = new PayoutCall(K);
		Output output = new Output(call,initialP, r, sigma, errorlimit,p, batch,numdays);
		output.output();
		System.out.println("Mean: "+output.getmean());
		System.out.println("Sd: "+output.getsigma());
	}

}
