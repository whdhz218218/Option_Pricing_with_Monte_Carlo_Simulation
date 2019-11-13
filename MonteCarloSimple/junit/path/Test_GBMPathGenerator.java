package path;

import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.Iterator;
import static org.assertj.core.api.BDDAssertions.*;
import junit.framework.TestCase;
import random.NormalGenerator;


public class Test_GBMPathGenerator extends TestCase {
	private double _initialP;
	private double _r;
	private double _sigma;
	private String _date;
	private GBMPathGenerator _gbmpathGenerator;
	
	public Test_GBMPathGenerator() {
		double tolerence=0.0001;
		_initialP=152.35;
		_r=0.0001;
		_sigma=0.01;
		_date = "2019-01-01";
		_gbmpathGenerator = new GBMPathGenerator(_initialP, _r, _sigma,_date);
		
		assertEquals(_gbmpathGenerator.getinitialP(), 152.35, tolerence);
		assertEquals(_gbmpathGenerator.getSigma(), 0.01, tolerence);
		assertEquals(_gbmpathGenerator.getR(), 0.0001, tolerence);
		assertEquals(_gbmpathGenerator.getDate(), "2019-01-01");	
	}
	
	
	public void testdpPrice() {
		 double gaussian= 0.54;
		 assertEquals(_gbmpathGenerator.dpPrice(gaussian,_initialP),153.18257420391208);		
	}
	
	public void testsimulate() {
		NormalGenerator normal=mock(NormalGenerator.class);
		normal.BoxMullerTrans();
		when(normal.getBoxMullerTrans()).thenReturn(0.0);
		then(normal.getBoxMullerTrans()).isEqualTo(0.0);
		_gbmpathGenerator.setNormalGenerator(normal); 
		
		_gbmpathGenerator.simulate();
		assertEquals(_gbmpathGenerator.getPath().getData().size(),252);		
		assertEquals(_gbmpathGenerator.getPath().getData().last().date().toString(),"2019-09-09");
		
		Iterator<DataPoint> it =_gbmpathGenerator.getPath().getData().iterator();
		DataPoint value = null;
		while (it.hasNext()) {
		    value = it.next();
		    System.out.println(value.price());
		}
	}
	
	public void test() {
		NormalGenerator normal=mock(NormalGenerator.class);
		normal.BoxMullerTrans();
		when(normal.getBoxMullerTrans()).thenReturn(0.0);
		then(normal.getBoxMullerTrans()).isEqualTo(0.0);
		_gbmpathGenerator.setNormalGenerator(normal); 
	}
	

	

	

}
