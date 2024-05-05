import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MultiThreadedPrimes {
	public static long[] primes;
	public static long count=0;
	public static long min,max;
	static String name;
	
	public static void main(String[] args) throws InterruptedException {
		long n;
		int nThreads;
		int i;
		long startTime,runTime;
//		File outfile;
		
		Scanner kb = new Scanner(System.in);
		System.out.println("Prime Number Finder");
		System.out.print("Enter ending point: ");
		max = kb.nextLong();
		kb.nextLine();
		System.out.println("Enter number of threads to use: ");
		nThreads = kb.nextInt();
		kb.nextLine();
		
		
//		outfile = new File("primes.txt");
//		PrintWriter write = null;
//		try {
//			write = new PrintWriter(outfile);
//		} catch (FileNotFoundException e) {
//			System.out.println("error could not write to file");
//			e.printStackTrace();
//		}
		Worker[] thread = new Worker[nThreads];
		
		
		startTime= System.currentTimeMillis();
		primes = new long[(int)(max/2-max/6)];
		primes[0] = 2;
		primes[1] = 3;
		count = 2;
		for (n=5; n<=Math.sqrt(max)+1; n+=2) {
			if (isPrime(n)) {
				primes[(int) count++]=n;
			}
		}
		Worker.nextNumber = n;
		Worker.max = max;
		for (i=0; i<nThreads; i++) {
			thread[i] = new Worker();
			thread[i].start();
		}
		for (i=0; i<nThreads; i++) {
			thread[i].join();
		}
		runTime = System.currentTimeMillis()-startTime;
//		bubbleSort(primes, count);
//		for (i=0; i<count-1; i++) {
//			if (primes[i] > primes[i+1]) {
//					System.out.println("Out of order: "+i+": "+primes[i]+" - "+primes[i-1]);
//			}
//		}
//		for (i=0; i<count; i++)
//			write.println(""+i+": "+primes[i]);
//		write.close();
		System.out.println("Found "+count+" prime numbers");
		System.out.println("Number of threads: "+nThreads+" Run time: "+runTime);
	}
	
	public static void bubbleSort(long[] primes2, long count2) {
		int i,j;
		long t;
		int pass=0;
		boolean inOrder=false;
		
		while (!inOrder) {
			inOrder=true;
			pass++;
			for (i=0; i<count2-pass; i++) {
				if (primes2[i]>primes2[i+1]) {
					inOrder=false;
					t = primes2[i];
					primes2[i] = primes2[i+1];
					primes2[i+1] = t;
				}
			}
		}
		System.out.println("Number of passes: "+pass);
	}
	
	public static boolean isPrime(long n) {
		int i;
		for (i=0; Math.sqrt(n)>=primes[i]; i++)
			if (n%primes[i]==0) return false;
		return true;
	}
}
