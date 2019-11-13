package payout;

import java.time.LocalDate;

import junit.framework.TestCase;
import path.DataPoint;
import path.Path;

public class Test_Payout  extends TestCase {
	
	private Path _path;
	public Test_Payout(){
		_path=new Path();
		DataPoint dp=new DataPoint();
		String date = "2019-01-01";
		LocalDate localDate = LocalDate.parse(date);
		dp.date(localDate);	
		
		DataPoint dp2=new DataPoint();
		String date2 = "2019-09-01";
		LocalDate localDate2 = LocalDate.parse(date2);
		dp2.date(localDate2);	
		
		DataPoint dp3=new DataPoint();
		String date3 = "2022-03-01";
		LocalDate localDate3 = LocalDate.parse(date3);
		dp3.date(localDate3);
		
		_path.addDataPoint(dp2);
		_path.addDataPoint(dp);
		_path.addDataPoint(dp3);
		
		dp.price(170);
		dp2.price(160);
		dp3.price(165);
	}
	
	public void testasian() {
		double K=164;
		PayoutAsian poAsian=new PayoutAsian(K);
		assertEquals(poAsian.payout(_path),1.0);
		
		double K1=169;
		PayoutAsian poAsian1=new PayoutAsian(K1);
		assertEquals(poAsian1.payout(_path),0.0);
	}
	
	public void testbackput() {
		double K=164;
		PayoutBackPut poBackPut=new PayoutBackPut(K);
		assertEquals(poBackPut.payout(_path),4.0);
		
		double K2=150;
		PayoutBackPut poBackPut2=new PayoutBackPut(K2);
		assertEquals(poBackPut2.payout(_path),0.0);
	}
	
	public void testbackcall() {
		double K=164;
		PayoutBackCall poBackCall=new PayoutBackCall(K);
		assertEquals(poBackCall.payout(_path),6.0);
		
		double K2=175;
		PayoutBackCall poBackCall2=new PayoutBackCall(K2);
		assertEquals(poBackCall2.payout(_path),0.0);
	}
	

	
	public void testput() {
		double K=164;
		String date="2022-03-01";
		PayoutPut poPut=new PayoutPut(K,date);
		assertEquals(poPut.payout(_path),0.0);
		
		String date2="2019-09-01";
		PayoutPut poPut2=new PayoutPut(K,date2);
		assertEquals(poPut2.payout(_path),4.0);
		
		String date3="2019-01-01";
		PayoutPut poPut3=new PayoutPut(K,date3);
		assertEquals(poPut3.payout(_path),0.0);
	}


}
