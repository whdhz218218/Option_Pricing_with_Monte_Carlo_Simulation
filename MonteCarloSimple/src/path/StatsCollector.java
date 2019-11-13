package path;

public class StatsCollector {
	
	private double _mean;
	private double _sqmean;
	private double _n;
	private double _sd;
	
	public StatsCollector(double ST) {
		 _mean=ST;
		 _sqmean=ST*ST;
		 _n=1;
	}
	
	public double getsd() throws Exception {
		if(_n<2)
			throw new Exception("Not enough numbers to calculate the standard deviation");
		return _sd;
	}
	
	public double getmean() throws Exception {
		if(_n<1)
			throw new Exception("Not enough numbers to calculate the mean");
		return _mean;
	}
	
	public double getn() {
		return _n;
	}
	
	public void update(double ST) {
		_n++;
		_mean=((_n-1)*_mean+ST)/_n;
		_sqmean=((_n-1)*_sqmean+ST*ST)/_n;
		_sd=Math.sqrt(_sqmean-_mean*_mean);
	}
	

}
