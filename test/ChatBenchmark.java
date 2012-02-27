
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author larss
 */
public class ChatBenchmark {
	private TotallyOrderedMultiCastStressTest tester;
	
	public static void main(String[] args){
		ChatBenchmark b = new ChatBenchmark();
		b.run();
	}
	
	public ChatBenchmark(){
		tester = new TotallyOrderedMultiCastStressTest();
	}
	
	public void run(){
		benchmark();
	}
	
	private void benchmark(){
		int runs = 10;
		HashMap<Integer, Long> result = new HashMap<Integer, Long>();
		System.out.println("===================================");
		for(int i = 3; i < 30; i = i+2){
		System.out.println("\nStarting benchmark with "+i+" units...");
			long sum = 0;
			for(int j = 0; j < runs; j++){
				tester.setup(i);
				sum += tester.doesItWork(i);
			}
			
			long avg = sum / i;
			System.out.println(" Result of run: "+avg);
			result.put(i, avg);
		}
		System.out.println("Benchmark finished...");
		System.out.println("Result of benchmark: ");
		for(int p : result.keySet())
			System.out.println(String.format("  %d peers: %d ms", p, result.get(p)));
	}
}
