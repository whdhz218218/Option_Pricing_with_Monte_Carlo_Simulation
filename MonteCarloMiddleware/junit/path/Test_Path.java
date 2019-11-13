package path;

import java.time.LocalDate;
import java.util.Iterator;

import junit.framework.TestCase;

public class Test_Path extends TestCase {

	public void testpath() {
		Path path=new Path();

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
		
		path.addDataPoint(dp2);
		path.addDataPoint(dp);
		path.addDataPoint(dp3);
		
		dp.price(100);
		dp2.price(10);
		dp3.price(50);
		
		Iterator<DataPoint> it =path.getData().iterator();
		DataPoint value = null;

		while (it.hasNext()) {
		    value = it.next();
		    System.out.println(value.price());
		}
		
	//	System.out.println(path.getData());
		
		
		
	}
}
