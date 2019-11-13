package payout;

import java.util.Iterator;

import path.DataPoint;
import path.Path;

public class PayoutAsian implements Payout{

	private double _K;
	
	public PayoutAsian(double K) {
		_K=K;
	}
	
	public double payout(Path path) {
		double sum=0;
		Iterator<DataPoint> it =path.getData().iterator();
		DataPoint value = null;
		while (it.hasNext()) {
		    value = it.next();
		    sum+=value.price();
		}
		double payout=Math.max(sum/path.getData().size()-_K, 0);
		return payout;
	};

}
