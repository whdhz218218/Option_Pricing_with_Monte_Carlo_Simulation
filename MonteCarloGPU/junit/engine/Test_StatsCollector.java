package engine;

import junit.framework.TestCase;

public class Test_StatsCollector extends TestCase{
	
	
	public void test1Value() throws Exception {
		StatsCollector statsc=new StatsCollector(20.0);
		assertEquals(statsc.getmean(),20.0);
		Exception excep= null;	
		try {
			statsc.getsd();
		}
		catch(Exception e){
			excep = e;
		}
		assertTrue(excep != null);
	}
	
	public void test2ValuesSame() throws Exception {
		StatsCollector statsc=new StatsCollector(20.0);
		statsc.update(20.0);
		assertEquals(statsc.getmean(),20.0);
		assertEquals(statsc.getsd(),0.0);
	}
	
	
	public void test2ValuesDiff() throws Exception {
		StatsCollector statsc=new StatsCollector(20.0);
		statsc.update(30.0);
		assertEquals(statsc.getmean(),25.0);
		assertEquals(statsc.getsd(),5.0);
	}
	
	public void test2ValuesDiff2() throws Exception {
		StatsCollector statsc=new StatsCollector();
		statsc.update(20.0);
		statsc.update(30.0);
		assertEquals(statsc.getmean(),25.0);
		assertEquals(statsc.getsd(),5.0);
	}
	
	
	public void test3ValuesDiff() throws Exception {
		StatsCollector statsc=new StatsCollector(20.0);
		statsc.update(30.0);
		statsc.update(40.0);
		assertEquals(statsc.getmean(),30.0);
		assertEquals(statsc.getsd(),8.164965809277257);
	}
	

}
