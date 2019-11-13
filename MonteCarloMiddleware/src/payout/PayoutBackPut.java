package payout;

import java.util.Iterator;

import path.DataPoint;
import path.Path;

public class PayoutBackPut implements Payout{

    private double _K;
	
	public PayoutBackPut(double K) {
		_K=K;
	}
	
	public double payout(Path path) {
		double min=path.getData().first().price();
		Iterator<DataPoint> it =path.getData().iterator();
		DataPoint value = null;
		while (it.hasNext()) {
		    value = it.next();
		    if(min>value.price()) {
		    	min=value.price();
		    }
		}
		double payout=Math.max(_K-min, 0);
		return payout;
	};

}
