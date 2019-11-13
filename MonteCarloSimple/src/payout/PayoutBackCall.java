package payout;

import java.util.Iterator;

import path.DataPoint;
import path.Path;

public class PayoutBackCall implements Payout{
	
    private double _K;
	
	public PayoutBackCall(double K) {
		_K=K;
	}
	
	public double payout(Path path) {
		double max=path.getData().first().price();
		Iterator<DataPoint> it =path.getData().iterator();
		DataPoint value = null;
		while (it.hasNext()) {
		    value = it.next();
		    if(max<value.price()) {
		    	max=value.price();
		    }
		}
		double payout=Math.max(max-_K, 0);
		return payout;
	};

}
