package OpenCL;
import org.jocl.*;
import java.util.Arrays;
import static org.jocl.CL.*;

public class GBMGenerator {
	private float[] _Normals;            // array of normal random numbers from norms1 and norms2
	private float[] _pricesIn;           // array of stock prices input
	private float[] _norms1;             // array of normal random numbers
	private float[] _norms2;	         // array of normal random numbers
	private int   	_numdays;            // number of days
	private int 	_batch;              // size of batch
	private float 	_r;
	private float 	_sigma;
	private float[] _pricesOut;          // array of stock prices output
	
	public float[] getpricesIn() {return _pricesIn;}
	public float[] getNormals() {return _Normals;}
	public float[] getpricesOut() {return _pricesOut;}
	public float[] getnorms1() {return _norms1;}
	public float[] getnorms2() {return _norms2;}
	public float getr() {return _r;}
	public float getsigma() {return _sigma;}
	public int getbatch() {return _batch;}
	
	
	public GBMGenerator(float[] norms1, float[] norms2, int numdays, float r, float sigma, int batch, float S0) {
		_norms1=norms1;
		_norms2=norms2; 
		_numdays=numdays;
		_batch=batch;
		_r=r;
		_sigma=sigma;
		_pricesIn = new float[2*_batch/_numdays];
		for(int i = 0; i< 2*_batch/_numdays; i++){
			_pricesIn[i] = (float) S0;
		}
		_pricesOut=new float[2*_batch/_numdays];
		_Normals=new float[2*_batch/_numdays];	
	}
	

	/** Compute stock prices which follows a geometric brownian motion with GPU
	 */
	public void NormtoGBM(){
		int index=0;
        cl_platform_id[] platforms = new cl_platform_id[1];
        clGetPlatformIDs(1, platforms,null);
        cl_platform_id platform = platforms[0];
        cl_device_id[] devices = new cl_device_id[1];
        clGetDeviceIDs(platform,  CL_DEVICE_TYPE_GPU, 1, devices, null);
        cl_device_id device = devices[0];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Create a context for the selected device
        cl_context context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        cl_command_queue commandQueue =clCreateCommandQueue(context, device, 0, null); 
  
        
        float result = (float)_r-_sigma*_sigma/2;     
        // Read the program sources and compile them :
        // Calculate the stock price which is a geometric brownian motion
        String src = "__kernel void "
        		+ "gbm(__global const float* pricesIn, "
        		+ "	   __global const float* Normals, "
        		+ "	   __global float* pricesOut) \n" 
        		+ "{\n" 
        		+ "    int i = get_global_id(0);\n" 
        		+ "    pricesOut[i] = pricesIn[i]*exp((float) (" +result + "+" + _sigma + "* (float)Normals[i]));\n" 
                + "}\n" ;
        

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,1, new String[]{ src }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "gbm", null);
        
        // Fill in Normals with normal random numbers from norms1 and norms2
        for(int j = 0; j< _numdays; j++){	
			for(int k = 0; k<2*_batch/_numdays; k++){
				if(index < _norms1.length){					
					_Normals[k]= _norms1[index++];
				}
				else{
					_Normals[k]= _norms2[index++ -_norms1.length];
				}
			}

			
			//Create pointer pointing to the input and output arrays	
	        Pointer srcA = Pointer.to(_pricesIn);
	        Pointer srcB = Pointer.to(_Normals);
	        Pointer dst = Pointer.to(_pricesOut);
	        
	        // Allocate the memory objects for the input- and output data
	        cl_mem memObjects[] = new cl_mem[3];
	          
	        memObjects[0] = clCreateBuffer(context,
	                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
	                Sizeof.cl_float * 2*_batch/_numdays, srcA, null);	        
	        memObjects[1] = clCreateBuffer(context,
	                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
	                Sizeof.cl_float * 2*_batch/_numdays, srcB, null);        
	        memObjects[2] = clCreateBuffer(context,
	                CL_MEM_READ_WRITE, 
	                Sizeof.cl_float *2*_batch/_numdays, null, null);
	        
	        // Set the arguments for the kernel
	        clSetKernelArg(kernel, 0,
	                Sizeof.cl_mem, Pointer.to(memObjects[0]));
	        clSetKernelArg(kernel, 1,
	                Sizeof.cl_mem, Pointer.to(memObjects[1]));
	        clSetKernelArg(kernel, 2,
	                Sizeof.cl_mem, Pointer.to(memObjects[2]));
	        
	        // Set the work-item dimension
	        long global_work_size[] = new long[]{2*_batch/_numdays};
	        long local_work_size[] = new long[]{1};
	
	        // Execute the kernel
	         clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,global_work_size, local_work_size, 0, null, null);
	
	        // Read the output data
	        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
	        		2*_batch/_numdays * Sizeof.cl_float, dst, 0, null, null);
	       
	        //Evolve stock price
	        _pricesIn = Arrays.copyOf(_pricesOut, _pricesOut.length);
	        }   
	}

	
}
	
	
