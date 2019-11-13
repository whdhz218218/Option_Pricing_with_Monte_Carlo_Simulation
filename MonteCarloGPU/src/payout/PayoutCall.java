package payout;

public class PayoutCall implements Payout{

    private double _K;

	
	public PayoutCall(double K) {
		_K=K;
	}
	
	
	public double payout(double ST) {
		return Math.max(0, ST-_K);
	};

}
