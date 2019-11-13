package payout;

import java.time.LocalDate;

import path.DataPoint;
import path.Path;

public class PayoutCall implements Payout{

    private double _K;
    private String _date;
	
	public PayoutCall(double K) {
		_K=K;
	}
	
	
	public double payout(Path path) {
/*		DataPoint dp=new DataPoint();
		dp.date(LocalDate.parse(_date));
		DataPoint dp2=new DataPoint();
		LocalDate date2=LocalDate.parse(_date).plusDays(1);
		dp2.date(date2);
		double St=path.getData().subSet(dp, dp2).first().price();*/
		double St=path.getData().last().price();
		double payout=Math.max(St-_K, 0);
		return payout;
	};

}
