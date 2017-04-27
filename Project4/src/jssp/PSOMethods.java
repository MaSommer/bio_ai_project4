package jssp;

import java.util.ArrayList;
import java.util.Collections;

public class PSOMethods {

	public static ArrayList<ArrayList<Integer>> generateSworm(int noOfMachines, int noOfJobs){
		ArrayList<ArrayList<Integer>> sworm = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> initialParticle = new ArrayList<Integer>();
		for (int i = 0; i < noOfMachines; i++) {
			for (int j = 0; j < noOfJobs; j++) {
				initialParticle.add(j+1);
			}
		}
		while (sworm.size() < Variables.swormSize){
			ArrayList<Integer> particle = (ArrayList<Integer>) initialParticle.clone();
			Collections.shuffle(particle);
			sworm.add(particle);
		}
		for (ArrayList<Integer> particle : sworm) {
			System.out.println(particle);
		}
		return sworm;
	}
	
}
