package OpenCL;

import junit.framework.TestCase;

public class Test_GBMGenerator extends TestCase {
	private float[] norms1={ (float)0.4, (float) 0.5,(float)3.1,(float)1.5,(float)-3};
	private float[] norms2={(float) 3.2,(float)-2.5,(float)-4,(float)1.1, (float)0.9};
	private int numdays=2;
	private float r=(float) 0.0001;
	private float sigma= (float) 0.01; 
	private int batch=3;
	private float S0=100;
	private GBMGenerator gbm = new GBMGenerator(norms1, norms2, numdays,r, sigma, batch,S0);
	
	
    public void testConstructor() throws Exception{		
		assertEquals(2*batch/numdays,3);
		assertTrue(gbm.getnorms1()==norms1 
				&& gbm.getnorms2()==norms2);
		assertEquals(gbm.getpricesIn()[0],100.0,0.001);
		assertTrue(gbm.getpricesIn()[0]==gbm.getpricesIn()[1]
				&& gbm.getpricesIn()[0]==gbm.getpricesIn()[2]);
	}
    
    
    public void testNormtoGBM() throws Exception{
    	gbm.NormtoGBM();	
		assertTrue(gbm.getNormals()[0]== (float)1.5
				&& gbm.getNormals()[1]== (float)-3
				&& gbm.getNormals()[2]== (float)3.2);
    }

    


}
