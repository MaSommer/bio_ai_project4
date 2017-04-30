package jssp;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BA {
	
	private ArrayList<ArrayList<Double>> foodSourcesWeights;
	private ArrayList<ArrayList<Integer>> foodSourcesOperationSequence;
	private ArrayList<Bee> employedBees;
	private ArrayList<Bee> activeFoodSources;
	private String filename;
	private DataInput di;
	private Bee globalBestBee;
	private long startTime;
	
	public BA(String filename){
		this.filename = filename;
		this.startTime = System.nanoTime();
		this.di = new DataInput(filename);
		initialization();
		employedBeePhase();
		onlookerBeePhase();
		theScoutBeePhase();
	}
	
	public void initialization(){
		this.di = new DataInput(filename);
		foodSourcesWeights = new ArrayList<ArrayList<Double>>();
		for (int j = 0; j < VariablesBA.numberOfFoodSources; j++) {
			ArrayList<Double> positions = initialSingleFoodSourcePosition();
			foodSourcesWeights.add(positions);
		}
		foodSourcesOperationSequence = HelpMethods.generateFoodSources(di);
	}
	
	private ArrayList<Double> initialSingleFoodSourcePosition(){
		ArrayList<Double> positions = new ArrayList<Double>();
		for (int i = 0; i < di.getNoOfJobs()*di.getNoOfMachines(); i++) {
			double pos = Math.random()*(VariablesBA.maxPosition-VariablesBA.minPosition)-VariablesBA.minPosition;
			positions.add(pos);
		}
		return positions;
	}
	
	public void employedBeePhase(){
		employedBees = new ArrayList<Bee>();
		for (int i = 0; i < VariablesBA.numberOfFoodSources; i++) {
			Bee employedBee = new Bee(foodSourcesOperationSequence.get(i), foodSourcesWeights.get(i));
			employedBees.add(employedBee);
		}
		for (Bee bee : employedBees) {
			bee.exploreFood(foodSourcesWeights, di);
		}
	}
	
	public void onlookerBeePhase(){
		double[] probabilities = HelpMethods.probabilityForEachFitness(employedBees, di);
		activeFoodSources = new ArrayList<Bee>();
		for (int i = 0; i < VariablesBA.numberOfOnlookerBees; i++) {
			double random = Math.random();
			for (int j = 0; j < probabilities.length; j++) {
				if (random < probabilities[j]){
					activeFoodSources.add(new Bee(employedBees.get(j)));
				}
			}
		}
	}
	
	public void theScoutBeePhase(){
		int iterations = 0;
		globalBestBee = HelpMethods.findBestBee(activeFoodSources);
		int bestFitnessFound = Integer.MAX_VALUE;
		while (HelpMethods.percentageOfOptimalBee(filename, activeFoodSources) > 10){
			long iterationTime = System.nanoTime();
			globalBestBee = HelpMethods.findBestBee(activeFoodSources);
			if (globalBestBee.getBestFitnessValue() < bestFitnessFound){
				bestFitnessFound = globalBestBee.getBestFitnessValue();
			}
			for (Bee bee : activeFoodSources) {
				boolean foundImprovals = false;
				long startTime2 = System.nanoTime();
				for (int i = 0; i < VariablesBA.numberOfTrialsToFindBetterFood; i++) {
					if (bee.exploreFood(foodSourcesWeights, di)){
						foundImprovals = true;
						break;
					}
				}
				long endTime2 = System.nanoTime();
				long duration1 = (long) ((endTime2-startTime2)/Math.pow(10, 6));
//				System.out.println("Explore food: " + duration1);
				if (!foundImprovals){
					double random = Math.random();
					if (random < VariablesBA.insertAtBestFeasibleRate){
						long startTime1 = System.nanoTime();
						HelpMethods.takeOutRandomSubtaskAndPlaceItInAtBestFeasible(bee, di);
						long endTime1 = System.nanoTime();
						long duration = (long) ((endTime1-startTime1)/Math.pow(10, 6));
//						System.out.println("Ran replacement: " + duration);
					}
					else if (random < (VariablesBA.insertAtBestFeasibleRate + VariablesBA.swapBasedTreeSearchRate)){
						long startTime1 = System.nanoTime();
						bee.setFoodSourceSequence(HelpMethods.treeSearchImprovedBeesAlgorithm(bee.getFoodSourceSequence(), di));
						long endTime1 = System.nanoTime();
						long duration = (long) ((endTime1-startTime1)/Math.pow(10, 6));
//						System.out.println("Tree search: " + duration);
					}
					else{
						if (bee != globalBestBee){
							bee.setFoodSourceWeights(initialSingleFoodSourcePosition());
							bee.setFoodSourceSequence(HelpMethods.generateSingleFoodSource(di));													
						}
						else{
//							System.out.println("Before: " + HelpMethods.calculateFitnessValue(bee.getFoodSourceSequence(), di));
							bee.setFoodSourceSequence(HelpMethods.treeSearchImprovedBeesAlgorithm(bee.getFoodSourceSequence(), di));
//							System.out.println("After: " + HelpMethods.calculateFitnessValue(bee.getFoodSourceSequence(), di));
						}
					}
				}
			}
			iterations++;
			if (iterations%1 == 0){
				long endTime = System.nanoTime();
				long duration = (long) ((endTime-startTime)/Math.pow(10, 9));
				long iterationDuration = (long) ((endTime-iterationTime)/Math.pow(10, 9));
				String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimalBee(filename, activeFoodSources));
				System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution. Total duration: " + duration + " sec. It. dur: " + iterationDuration + " sec");				
			}
		}
		globalBestBee = HelpMethods.findBestBee(activeFoodSources);
		String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimalBee(filename, activeFoodSources));
		System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");
		new DrawGanttChart(HelpMethods.encodeJobs(globalBestBee.getFoodSourceSequence(), di), di);
	}
	
	public static void main(String[] args) {
		new BA("5.txt");
	}
	
	 

}
