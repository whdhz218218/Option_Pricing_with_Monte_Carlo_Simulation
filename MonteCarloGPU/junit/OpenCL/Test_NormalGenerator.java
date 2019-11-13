package OpenCL;

import junit.framework.TestCase;

public class Test_NormalGenerator extends TestCase {
	private float arr1[] = {(float)0.01};
	private float arr2[] = {(float)0.02};
	private float arr3[] = {(float)0.01,(float)0.02};
	private float arr4[] = {(float)0.02,(float)0.04}; 
	
    public void testConstructor() throws Exception{	
    	NormalGenerator normalGenerator = new NormalGenerator(1,arr1, arr2);
		assertEquals(normalGenerator.getUniform1(),arr1);
		assertEquals(normalGenerator.getUniform2(),arr2);
	}
	
	public void testUniformtoNormal1() throws Exception{
		NormalGenerator normalGenerator = new NormalGenerator(1,arr1, arr2);
		normalGenerator.UniformtoNormal();
		assertEquals(normalGenerator.getNormal1()[0],3.01092,0.0001);
		assertEquals(normalGenerator.getNormal2()[0],0.38036,0.0001);				
	}
	
	public void testTUniformtoNormal2() throws Exception{
		NormalGenerator normalGenerator2 = new NormalGenerator(2,arr3, arr4);
		normalGenerator2.UniformtoNormal();
		assertEquals(normalGenerator2.getNormal1()[1],2.70927,0.0001);
     	assertEquals(normalGenerator2.getNormal2()[1],0.69562,0.0001);
		
	}
}
