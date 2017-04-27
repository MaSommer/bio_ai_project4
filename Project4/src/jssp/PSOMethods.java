package jssp;

import java.util.ArrayList;
import java.util.Collections;

public class PSOMethods {

	public static ArrayList<Particle> generateSworm(){
		int noOfMachines = Program.di.getNoOfMachines();
		int noOfJobs = Program.di.getNoOfJobs();
		ArrayList<Particle> sworm = new ArrayList<Particle>();
		ArrayList<Integer> initialParticle = new ArrayList<Integer>();
		for (int i = 0; i < noOfMachines; i++) {
			for (int j = 0; j < noOfJobs; j++) {
				initialParticle.add(j+1);
			}
		}
		while (sworm.size() < Variables.swormSize){
			ArrayList<Integer> particleSequence = (ArrayList<Integer>) initialParticle.clone();
			Collections.shuffle(particleSequence);
			sworm.add(new Particle(particleSequence));
		}
		for (Particle particle : sworm) {
			System.out.println(particle.getOperationSequence());
		}
		return sworm;
	}
	
}
