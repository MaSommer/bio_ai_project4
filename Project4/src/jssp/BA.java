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
	
	public BA(String filename){
		this.filename = filename;
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
			ArrayList<Double> positions = new ArrayList<Double>();
			for (int i = 0; i < di.getNoOfJobs()*di.getNoOfMachines(); i++) {
				double pos = Math.random()*(VariablesBA.maxPosition-VariablesBA.minPosition)-VariablesBA.minPosition;
				positions.add(pos);
			}
			foodSourcesWeights.add(positions);
		}
		foodSourcesOperationSequence = HelpMethods.generateFoodSources(di);
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
		while (HelpMethods.percentageOfOptimalBee(filename, activeFoodSources) > 10){
			globalBestBee = HelpMethods.findBestBee(activeFoodSources);
			for (Bee bee : activeFoodSources) {
				bee.exploreFood(foodSourcesWeights, di);
			}
			if (iterations%1 == 0){
				String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimalBee(filename, activeFoodSources));
				System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");				
			}
			iterations++;
		}
		String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimalBee(filename, activeFoodSources));
		System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");
	}
	
	public static void main(String[] args) {
		new BA("6.txt");
	}
	
	 

}
