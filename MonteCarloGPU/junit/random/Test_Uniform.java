package random;


import junit.framework.TestCase;
import engine.StatsCollector;

public class Test_Uniform extends TestCase {
	
	public void testrandom() throws Exception {
		long seed = System.currentTimeMillis();
		long n = Integer.MAX_VALUE;
		UniformRandomGenerator uni= new UniformRandomGenerator(seed,n);
		System.out.println(uni.nextRandom01());
		System.out.println(uni.nextRandom01());
		System.out.println(uni.nextRandom01());
		System.out.println(uni.nextRandom01());
		
		StatsCollector sc = new StatsCollector(uni.nextRandom01());
		for (int i=0; i<20000; i++){
			sc.update(uni.nextRandom01());
		}
		assertEquals(sc.getmean(),0.5,0.1);
		assertEquals(sc.getsd(),0.25,0.05);
	}
	

}
