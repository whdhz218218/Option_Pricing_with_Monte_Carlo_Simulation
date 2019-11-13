package payout;

import java.time.LocalDate;

import path.DataPoint;
import path.Path;

public class PayoutPut implements Payout {
	
	private double _K;
    private String _date;
	
	public PayoutPut(double K, String date) {
		_K=K;
		_date=date;	
	}
	
	public double payout(Path path) {
		DataPoint dp=new DataPoint();
		dp.date(LocalDate.parse(_date));
		DataPoint dp2=new DataPoint();
		LocalDate date2=LocalDate.parse(_date).plusDays(1);
		dp2.date(date2);
		double St=path.getData().subSet(dp, dp2).first().price();
		double payout=Math.max(_K-St, 0);
		return payout;
	};

	
	

}
