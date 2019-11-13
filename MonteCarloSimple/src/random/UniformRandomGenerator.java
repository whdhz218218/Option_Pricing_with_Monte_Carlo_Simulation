package random;

/**
 *
 */
public class UniformRandomGenerator  {

    private long seed;
    private long m = (long) Math.pow(2, 32);
    private long n;

    /*
    public UniformRandomGenerator(int seed, int n) {
        this.seed = seed;
        this.n = n;
        
        
     public int nextRandom() {
        seed = (1103515245 * seed + 12345) % m;
        return (int) (seed % n);
    }
    }*/
    
    public UniformRandomGenerator(long seed, long n) {
        this.seed = seed;
        this.n = n;
    }

    public double nextRandom() {
        seed = (1103515245 * seed + 12345) % m;
        return (double) (seed % n);
    }
    
    public double nextRandom01() {
        return (double) (nextRandom()/(double)n);
    }


}
