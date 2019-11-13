package path;

import java.time.LocalDate;
import random.*;

public class GBMPathGenerator implements PathGenerator {
	private Path _path;
	private DataPoint _DataPoint;
	private NormalGenerator _normalgenerator;
	private double _initialP;
	private double _r;
	private double _sigma;
	private String _date;
	
	
	public GBMPathGenerator(double initialP, double r, double sigma, String date) {
		_path=new Path();
		_DataPoint=new DataPoint();
		long seed= System.currentTimeMillis();
		long n=Integer.MAX_VALUE;
		_normalgenerator=new NormalGenerator(new UniformRandomGenerator(seed, n));
		_initialP=initialP;
		_r=r;
		_sigma=sigma;
		_date=date;	
	}
	
	public void setNormalGenerator(NormalGenerator normalgenerator) {
		 _normalgenerator=normalgenerator;
	}
	public double getinitialP()     {return _initialP;}
	public double getSigma() 	    {return _sigma;}
	public double getR()            {return _r;}	
	public String getDate() 		{return _date;}
	public Path getPath() 			{return _path;}
	
	
	/** Generate two every day's price through GBM with random gaussian Z
	 * Z1,Z2 created from Box-Muller Transform U1, U2
	 * @param gaussian
	 * @param initialP
	 * @return
	 */
	public double dpPrice(double gaussian, double initialP) {
		double datediff=1;
		double price1=initialP*Math.exp((_r-_sigma*_sigma/2)*datediff+_sigma*Math.sqrt(datediff)*gaussian);
		return price1;
	}
	
	
	/** Simulate one stock path for data points
	 */
	public void simulate() {
		_path=new Path();
		//Simulate the first point
		LocalDate localDate = LocalDate.parse(_date);
		_DataPoint.date(localDate);
		_DataPoint.price(_initialP);
		_path.addDataPoint(_DataPoint);
		double price=0;
		
		for(int i=1;i<252;i++) {
			localDate = localDate.plusDays(1);
			_normalgenerator.BoxMullerTrans();
//			_normalgenerator.setGaussian();
			price=_DataPoint.price();
			
			//The only difference between Antithetic and regular simulate is here:
			//_DataPoint=new DataPoint(localDate,dpPrice(_normalgenerator.getGaussian().get(i-1),price));
			_DataPoint=new DataPoint(localDate,dpPrice(_normalgenerator.getBoxMullerTrans(),price));
			_path.addDataPoint(_DataPoint);
		}		
//		_normalgenerator.AntitheticBoxMullerTrans();
	}
	
	
	
	
	


}
