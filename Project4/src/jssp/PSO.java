package jssp;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.util.converter.PercentageStringConverter;

public class PSO {
	
	private ArrayList<Particle> sworm;
	private Particle globalBestParticle;
	public static DataInput di;
	private String filename;
	
	public PSO(String filename){
		this.filename = filename;
		init();
		run();

	}
	
	public void init(){
		di = new DataInput(filename);
		sworm = HelpMethods.generateSworm(di);
	}
	
	public void run(){
		int iterations = 0;
		long startTime = System.nanoTime();
		globalBestParticle = HelpMethods.findBestParticle(sworm);
		while (HelpMethods.percentageOfOptimal(filename, sworm) > 10){
			for (Particle particle : sworm) {
				double random = Math.random();
				if (random < VariablesPSO.multiTypeIndividualEnhancementSchemeRate){
					HelpMethods.multiTypeIndividualEnhancementSchemeSimulatedAnnealing(particle, filename);					
				}
			}
			globalBestParticle = HelpMethods.findBestParticle(sworm);
			for (Particle particle : sworm) {
				particle.updateParticle(iterations, globalBestParticle);				
			}
			long endTime = System.nanoTime();
			long duration = (long) ((endTime-startTime)/Math.pow(10, 9));
			if (iterations%30 == 0){
				String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
				System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution. Total duration: " + duration + " sec");				
			}
			iterations++;
		}
		globalBestParticle = HelpMethods.findBestParticle(sworm);
		String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
		System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");	
		ArrayList<ArrayList<Integer>> chart = HelpMethods.encodeJobs(globalBestParticle.getOperationSequence(), di);
		new DrawGanttChart(chart, di);
	}
	
	public static void main(String[] args) {
		new PSO("5.txt");
	}

}
