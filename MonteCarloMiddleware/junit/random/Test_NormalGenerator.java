package random;

import junit.framework.TestCase;
import path.*;

public class Test_NormalGenerator extends TestCase{
	
	private UniformRandomGenerator _uniformgenerator;
	private NormalGenerator _normalGenerator;
	
	public Test_NormalGenerator() {
		long seed= System.currentTimeMillis();
		long n=Integer.MAX_VALUE;
		_uniformgenerator=new UniformRandomGenerator(seed,n);
		_normalGenerator= new NormalGenerator(_uniformgenerator);
	}
	
	public void testBoxMullerTrans() throws Exception {
		StatsCollector sc = new StatsCollector(3.0);
		for (int i=0; i<5000; i++) {
			_normalGenerator.BoxMullerTrans();
			sc.update(_normalGenerator.getBoxMullerTrans());
		}
		assertEquals(sc.getmean(),0.0,0.1);
		assertEquals(sc.getsd(),1.0,0.01);
		
	}

}
