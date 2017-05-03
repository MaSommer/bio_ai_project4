package jssp;

import java.text.DecimalFormat;

import java.util.ArrayList;



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
		long endTime = System.nanoTime();
		long duration = (long) ((endTime-startTime)/Math.pow(10, 9));
		int bestFitnessFound = Integer.MAX_VALUE;
		ArrayList<Integer> bestOperationSequenceFound = new ArrayList<Integer>();
		
		int optimalFitness = HelpMethods.optimalFitnessValues(filename);
		double percent = ((double)(bestFitnessFound)/(double)(optimalFitness)-1)*100;
		
		while (percent > 2 && duration < 300){
			if (globalBestParticle.getLocalBestFitnessValue() < bestFitnessFound){
				bestFitnessFound = globalBestParticle.getLocalBestFitnessValue();
				bestOperationSequenceFound = globalBestParticle.getOperationSequence();
			}
			for (Particle particle : sworm) {
				double random = Math.random();
				if (random < VariablesPSO.multiTypeIndividualEnhancementSchemeRate){
					HelpMethods.multiTypeIndividualEnhancementSchemeSimulatedAnnealing(particle, filename);					
				}
				else if (random < VariablesPSO.multiTypeIndividualEnhancementSchemeRate + VariablesPSO.swapBasedTreeSearchRate){
					particle.setPositions(HelpMethods.treeSearchImprovedBeesAlgorithmImproved(particle.getLocalBestPositions(), di));				
				}
			}
			globalBestParticle = HelpMethods.findBestParticle(sworm);
			for (Particle particle : sworm) {
				particle.updateParticle(iterations, globalBestParticle);				
			}
			endTime = System.nanoTime();
			duration = (long) ((endTime-startTime)/Math.pow(10, 9));
			if (iterations%1 == 0){
				String percentageBestFound =  new DecimalFormat("##.##").format(((double)(bestFitnessFound)/(double)(optimalFitness)-1)*100);
				String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
				System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution. Global best particle is " + percentageBestFound + "%. Total duration: " + duration + " sec");				
			}
			if (globalBestParticle.getLocalBestFitnessValue() < bestFitnessFound){
				bestFitnessFound = globalBestParticle.getLocalBestFitnessValue();
				bestOperationSequenceFound = globalBestParticle.getOperationSequence();
			}
			percent = ((double)(bestFitnessFound)/(double)(optimalFitness)-1)*100;
			iterations++;
		}

		globalBestParticle = HelpMethods.findBestParticle(sworm);
		if (globalBestParticle.getLocalBestFitnessValue() < bestFitnessFound){
			bestFitnessFound = globalBestParticle.getLocalBestFitnessValue();
			bestOperationSequenceFound = globalBestParticle.getOperationSequence();
		}
		String percentage =  new DecimalFormat("##.##").format(((double)(bestFitnessFound)/(double)(optimalFitness)-1)*100);
		System.out.println("Found a solution which is " + percentage + "% of optimal solution.");
		System.out.println("Fitness: " + bestFitnessFound);
		new DrawGanttChart(HelpMethods.encodeJobs(bestOperationSequenceFound, di), di);
	}
	
	public static void main(String[] args) {
		new PSO("3.txt");
	}

}
