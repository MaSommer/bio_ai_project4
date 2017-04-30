package jssp;

import java.util.ArrayList;

public class Bee {
	
	private ArrayList<Integer> foodSourceSequence;
	private ArrayList<Double> foodSourceWeights;
	private int bestFitnessValue;

	public Bee(ArrayList<Integer> foodSourceSequence, ArrayList<Double> foodSourceWeights){
		this.foodSourceSequence = foodSourceSequence;
		this.foodSourceWeights = foodSourceWeights;
	}
	
	public Bee(Bee another){
		this.foodSourceSequence = (ArrayList<Integer>) another.foodSourceSequence.clone();
		this.foodSourceWeights = (ArrayList<Double>) another.foodSourceWeights.clone();
		this.bestFitnessValue = another.bestFitnessValue;
	}
	
	public boolean exploreFood(ArrayList<ArrayList<Double>> foodSourcesWeights, DataInput di){
		int randomWeightIndex = (int)(Math.random()*foodSourceWeights.size());
		int randomFoodSourceIndex = (int)(Math.random()*foodSourcesWeights.size());
		double newWeight = foodSourceWeights.get(randomWeightIndex) + (Math.random()*(VariablesBA.maxRandomNumberEmploedBeePhase-VariablesBA.minRandomNumberEmploedBeePhase)-VariablesBA.minRandomNumberEmploedBeePhase)
				*(this.foodSourceWeights.get(randomWeightIndex)-foodSourcesWeights.get(randomFoodSourceIndex).get(randomWeightIndex));
		
		ArrayList<Double> newFoodSourcesWeights = (ArrayList<Double>) foodSourceWeights.clone();
		ArrayList<Integer> newFoodSourcesSequence = (ArrayList<Integer>) foodSourceSequence.clone();
		newFoodSourcesWeights.set(randomWeightIndex, newWeight);
		updateFoodSource(newFoodSourcesWeights, newFoodSourcesSequence);
		
		int oldFitness = HelpMethods.calculateFitnessValue(foodSourceSequence, di);
		int newFitness = HelpMethods.calculateFitnessValue(newFoodSourcesSequence, di);
		if (newFitness < oldFitness){
			this.foodSourceSequence = (ArrayList<Integer>) newFoodSourcesSequence.clone();
			this.foodSourceWeights = (ArrayList<Double>) newFoodSourcesWeights.clone();
			bestFitnessValue = newFitness;
			return true;
		}
		bestFitnessValue = oldFitness;
		return false;
	}
	
	private void updateFoodSource(ArrayList<Double> foodSourceWeights, ArrayList<Integer> foodSourceSequence){
		for (int i = 0; i < foodSourceWeights.size(); i++) {
			for (int j = i; j < foodSourceWeights.size(); j++) {
				if (foodSourceWeights.get(i) < foodSourceWeights.get(j)){
					double tempPos = foodSourceWeights.get(i);
					foodSourceWeights.set(i, foodSourceWeights.get(j));
					foodSourceWeights.set(j, tempPos);
					int subtask = foodSourceSequence.get(i);
					foodSourceSequence.set(i, foodSourceSequence.get(j));
					foodSourceSequence.set(j, subtask);
				}
			}
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
	}

	public void setFoodSourceWeights(ArrayList<Double> foodSourceWeights) {
		this.foodSourceWeights = foodSourceWeights;
	}

}
