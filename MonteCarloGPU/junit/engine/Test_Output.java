package engine;

import junit.framework.TestCase;
import payout.*;
import random.UniformRandomGenerator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.BDDAssertions.*;


public class Test_Output extends TestCase {

	private double initialP = 152.35;
	private double sigma = 0.01;
	private double r = 0.0001;
	private double K = 150;
	private double p = (1-0.96)/2;
	private double errorlimit = 0.1;
	private PayoutCall option=new PayoutCall(K);
	private int batch = 1;
	private int numdays=1;
	private int batch2 = 3;
	private int numdays2= 2;

	public void testConstructor() {
		Output output=new Output(option, initialP, r, sigma, errorlimit, p, batch, numdays);
		assertEquals(output.gety(),2.0541,0.001);	
	}
	
	public void testOneBatch1() throws Exception{
		Output output=new Output(option, initialP, r, sigma, errorlimit, p, batch, numdays);
		UniformRandomGenerator uniformRandomGenerator = mock(UniformRandomGenerator.class);	
		when(uniformRandomGenerator.nextRandom01()).thenReturn(0.2);
		then(uniformRandomGenerator.nextRandom01()).isEqualTo(0.2);
		
		output.OneBatch(uniformRandomGenerator);
		
		assertEquals(output.getStatsCollector().getmean(),4.09214,0.0001);	
		assertEquals(output.getStatsCollector().getsd(),0.88748,0.0001);
	}

}
