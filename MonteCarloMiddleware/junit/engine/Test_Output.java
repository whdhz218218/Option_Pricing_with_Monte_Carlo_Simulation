package engine;

import junit.framework.TestCase;
import path.GBMPathGenerator;
import payout.*;

public class Test_Output extends TestCase{
	private double _initialP;
	private double _r;
	private double _sigma;
	private String _date;
	private Output output;
	
	public Test_Output() {
		_initialP=152.35;
		_r=0.0001;
		_sigma=0.01;
		_date = "2019-01-01";
		PayoutAsian option = new PayoutAsian(164);
		double errorlimit=0.1;
		double p=(1-0.95)/2;
		output = new Output(option, _initialP, _r, _sigma, _date,errorlimit,p);
	}
	
	public void testfindy() {
		assertEquals(output.gety(),1.9603949169253396);
	}


}
