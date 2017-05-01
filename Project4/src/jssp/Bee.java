package jssp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bee {
	
	private ArrayList<Integer> foodSourceSequence;
	private ArrayList<Double> foodSourceWeights;
	private DataInput di;

	private int bestFitnessValue;

	public Bee(ArrayList<Integer> foodSourceSequence, ArrayList<Double> foodSourceWeights, DataInput di){
		this.foodSourceSequence = foodSourceSequence;
		this.foodSourceWeights = foodSourceWeights;
		this.di = di;
		this.bestFitnessValue = -1;
	}
	
	public Bee(Bee another){
		this.foodSourceSequence = (ArrayList<Integer>) another.foodSourceSequence.clone();
		this.foodSourceWeights = (ArrayList<Double>) another.foodSourceWeights.clone();
		this.bestFitnessValue = another.bestFitnessValue;
		this.di = another.di;
	}
	
	public boolean exploreFood(ArrayList<ArrayList<Double>> foodSourcesWeights){
		int randomWeightIndex = (int)(Math.random()*foodSourceWeights.size());
		int randomFoodSourceIndex = (int)(Math.random()*foodSourcesWeights.size());
		double newWeight = foodSourceWeights.get(randomWeightIndex) + (Math.random()*(VariablesBA.maxRandomNumberEmploedBeePhase-VariablesBA.minRandomNumberEmploedBeePhase)-VariablesBA.minRandomNumberEmploedBeePhase)
				*(this.foodSourceWeights.get(randomWeightIndex)-foodSourcesWeights.get(randomFoodSourceIndex).get(randomWeightIndex));
		
		ArrayList<Double> newFoodSourcesWeights = (ArrayList<Double>) foodSourceWeights.clone();
		ArrayList<Integer> newFoodSourcesSequence = (ArrayList<Integer>) foodSourceSequence.clone();
		newFoodSourcesWeights.set(randomWeightIndex, newWeight);
		updateFoodSource(newFoodSourcesWeights, newFoodSourcesSequence);
		
		if (bestFitnessValue == -1){
			bestFitnessValue = HelpMethods.calculateFitnessValue(foodSourceSequence, di);
		}
		int newFitness = HelpMethods.calculateFitnessValue(newFoodSourcesSequence, di);
		if (newFitness < bestFitnessValue){
			this.foodSourceSequence = (ArrayList<Integer>) newFoodSourcesSequence.clone();
			this.foodSourceWeights = (ArrayList<Double>) newFoodSourcesWeights.clone();
			bestFitnessValue = newFitness;
			return true;
		}
		return false;
	}
	
	public void updateFoodSource(ArrayList<Double> foodSourceWeights, ArrayList<Integer> foodSourceSequence){
		ArrayList<Double> artificialPositions = (ArrayList<Double>) foodSourceWeights.clone();
		Collections.sort(artificialPositions);

		HashMap<Double, Integer> mapPositionToJob = new HashMap<Double, Integer>();
		int jobCounter = 1;
		for (int i = 0; i < artificialPositions.size(); i++) {
			int job = jobCounter%di.getNoOfJobs() + 1;
			mapPositionToJob.put(artificialPositions.get(i), job);
			jobCounter++;
		}
		for (int i = 0; i < artificialPositions.size(); i++) {
			foodSourceSequence.set(i, mapPositionToJob.get(foodSourceWeights.get(i)));
		}
	}
	
	public void updateFitnessValue(){
		int newFitness = HelpMethods.calculateFitnessValue(foodSourceSequence, di);
		if (newFitness < bestFitnessValue){
			bestFitnessValue = newFitness;
		}
	}
	
	public ArrayList<Integer> getFoodSourceSequence() {
		return foodSourceSequence;
	}
	
	public int getBestFitnessValue() {
		return bestFitnessValue;
	}
	
	public void setFoodSourceSequence(ArrayList<Integer> foodSourceSequence) {		
		this.foodSourceSequence = foodSourceSequence;
		bestFitnessValue = HelpMethods.calculateFitnessValue(foodSourceSequence, di);

	}

	public void setFoodSourceWeights(ArrayList<Double> foodSourceWeights) {
		this.foodSourceWeights = foodSourceWeights;
		bestFitnessValue = HelpMethods.calculateFitnessValue(foodSourceSequence, di);
	}
	
	public ArrayList<Double> getFoodSourceWeights() {
		return foodSourceWeights;
	}

}
