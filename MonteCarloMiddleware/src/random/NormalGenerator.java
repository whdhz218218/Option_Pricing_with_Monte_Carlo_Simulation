package random;

import java.util.LinkedList;

public class NormalGenerator {
	
	private UniformRandomGenerator _uniformgenerator;
	private double _gaussian;
    //private LinkedList<Double> _AntitheticGaussian; 
	
	public NormalGenerator(UniformRandomGenerator uniformgenerator) {
		_uniformgenerator=uniformgenerator;
	//	_AntitheticGaussian=new LinkedList<Double>();
	}
	
	public double getBoxMullerTrans() {
		return _gaussian;
	}
	
	public void BoxMullerTrans() {
		double uniform1=_uniformgenerator.nextRandom01();	
		double uniform2=_uniformgenerator.nextRandom01();
		double gaussian1= Math.sqrt(-2*Math.log(uniform1))*Math.cos(2*Math.PI*uniform2);
		_gaussian=gaussian1;
	}
	
	//public void setGaussian() {_AntitheticGaussian.add(_gaussian);}
	//public LinkedList<Double> getGaussian() {return _AntitheticGaussian;}
	
/*	public void AntitheticBoxMullerTrans() {
		for(int i=0;i<_AntitheticGaussian.size();i++) {
			_AntitheticGaussian.set(i,-_AntitheticGaussian.get(i));
		}
	}*/
	

}
