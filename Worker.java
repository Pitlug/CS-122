public class Worker extends Thread{
	static Object lock1 = new Object();
	static Object lock2 = new Object();
	static long nextNumber;
	static long max;

	public void run() {
		long n;

		while(nextNumber<=max) {
			synchronized (lock1) {
				n = nextNumber;
				nextNumber+=2;
			}
			if (n>max) break;
			if (MultiThreadedPrimes.isPrime(n)) 
				synchronized (lock2){
					MultiThreadedPrimes.primes[(int) MultiThreadedPrimes.count++]=n;
				}
		}
	}

}
