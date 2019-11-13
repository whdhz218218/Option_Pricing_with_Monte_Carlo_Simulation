package OpenCL;
import org.jocl.*;
import static org.jocl.CL.*;

public class NormalGenerator {
	private float[] _uniforms1;
	private float[] _uniforms2;
	private float[] _normals1;
	private float[] _normals2;
	private int _batch;
	
	public float[] getUniform1() {return _uniforms1;}
	public float[] getUniform2() {return _uniforms2;}
	public float[] getNormal1() {return _normals1;}
	public float[] getNormal2() {return _normals2;}
	
	public NormalGenerator(int batch, float[] arr1, float[] arr2)throws Exception{		
		_batch=batch;
		_uniforms1 = new float[_batch];
		_uniforms2 = new float[_batch];
		_uniforms1 = arr1;                   //first array of uniform random numbers
		_uniforms2 = arr2;                   //second array of uniform random numbers
		_normals1 = new float[_batch];
		_normals2 = new float[_batch];
	}
	
	/** Use Box-Muller Transformation in GPU to convert uniform random numbers 
	 * to normal random numbers 
	 */
	public void UniformtoNormal(){
		//Initialize platform and device
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
                contextProperties, 1, new cl_device_id[]{device},null, null, null);

        // Create a command-queue for the selected device
        cl_command_queue commandQueue = clCreateCommandQueue(context, device, 0, null);

        // Read the program sources and compile them :
        String src = "__kernel void "
        		+ "NormalGenerator(	__global const float* uniforms1, "
        		+ "					__global const float* uniforms2, "
        		+ "					__global float* normals1, "
        		+ "					__global float* normals2) \n" 
        		+ "{\n" 
        		+ "    int i = get_global_id(0);\n" 
        		+ "    normals1[i] =sqrt((float)-2 *(float) log(uniforms1[i])) * cos((float)2 *(float)uniforms2[i]*(float)M_PI);\n"
        		+ "    normals2[i] =sqrt((float)-2 *(float) log(uniforms1[i])) * sin((float)2 *(float)uniforms2[i]*(float)M_PI);\n"
                + "}\n";
        
        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{ src }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "NormalGenerator", null);
        
        //Create pointer pointing to the input and output arrays
        Pointer srcA = Pointer.to(_uniforms1);
        Pointer srcB = Pointer.to(_uniforms2);
        Pointer dst = Pointer.to(_normals1);
        Pointer dst2 = Pointer.to(_normals2);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[4];
        memObjects[0] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * _batch, srcA, null);
        memObjects[1] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * _batch, srcB, null);
        memObjects[2] = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_float * _batch, null, null);
        memObjects[3] = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_float * _batch, null, null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0,
                Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1,
                Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2,
                Sizeof.cl_mem, Pointer.to(memObjects[2]));
        clSetKernelArg(kernel, 3,
                Sizeof.cl_mem, Pointer.to(memObjects[3]));

        // Set the work-item dimensions
        long global_work_size[] = new long[]{_batch};
        long local_work_size[] = new long[]{1};

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        // Read the output data        
        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
        		_batch * Sizeof.cl_float, dst, 0, null, null);
        clEnqueueReadBuffer(commandQueue, memObjects[3], CL_TRUE, 0,
        		_batch * Sizeof.cl_float, dst2, 0, null, null);
    } 
	
}
