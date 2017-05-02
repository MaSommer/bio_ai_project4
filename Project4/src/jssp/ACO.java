package jssp;

import java.util.ArrayList;

public class ACO {
	
	private ArrayList<Ant> ants;
	private DataInput di;
	private ArrayList<Integer> bestOperationSequence;
	private double bestFitnessEver = Double.MAX_VALUE;
	private ArrayList<ArrayList<ArrayList<Double>>> transitionTable;
	private ArrayList<Double> firstActionTransition;
	
	
	
	public ACO(String filename){
		this.di = new DataInput(filename);
		this.transitionTable = HelpMethods.generateInitialTransitions(di);
		this.ants = new ArrayList<Ant>();
		for(int i = 0 ; i < VariablesANT.numberOfAnts ; i++){
			ants.add(new Ant(di, transitionTable));
			
		}
		
		this.firstActionTransition = ants.get(0).getFirstActionTransition();
		
	}
	
	private void evaporate() {
		for(int i = 0 ; i < transitionTable.size(); i++){
			for(int k = 0 ; k < transitionTable.get(i).size() ; k++ ){
				for(int l = 0 ; l < transitionTable.get(i).get(k).size() ; l++){
					double oldValue = transitionTable.get(i).get(k).get(l);
					double newValue = (1-VariablesANT.evaporationConstant)*oldValue;
					transitionTable.get(i).get(k).set(l, newValue);
				}
			}
		}
	}
	
	private void createSolutions() {
		for(Ant ant:ants){
			ant.generateSolution();
		}
	}
	
	private void updatePheromones() {
		double bestFitness = Double.MAX_VALUE;
		ArrayList<Integer> bestOperationSequence = null;
		for(Ant ant : ants){
			if(ant.getFitness() < bestFitness){
				bestFitness = ant.getFitness();
				bestOperationSequence = ant.getOperationSequence();
			}
			ant.resetOperationSequence();
		}
		if(bestFitness < bestFitnessEver){
			bestFitnessEver = bestFitness;
			this.bestOperationSequence = bestOperationSequence;
		}
		
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for(int i = 0 ; i < di.getNoOfJobs() ; i++){
			counts.add(0);
		}
		for(int i = 0 ; i < bestOperationSequence.size() ; i++){
			double addedValue = VariablesANT.evaporationConstant * ((double) VariablesANT.numberOfAnts / bestFitness);
			int jobNr = bestOperationSequence.get(i)-1;
			if(i == 0){
				double newValue = firstActionTransition.get(jobNr) + addedValue;
				firstActionTransition.set(jobNr, newValue);
				int oldCount = counts.get(jobNr);
				oldCount++;
				counts.set(jobNr, oldCount);
			}
			else{
				int transitionIndex = jobNr*(di.getNoOfMachines()) + counts.get(jobNr);
				double newValue = transitionTable.get(jobNr).get(counts.get(jobNr)).get(transitionIndex) + addedValue;
				transitionTable.get(jobNr).get(counts.get(jobNr)).set(transitionIndex, newValue);
				int oldCount = counts.get(jobNr);
				oldCount++;
				counts.set(jobNr, oldCount);
			}
		}
		
		for(Ant ant : ants){
			ant.setFirstActionTransition(firstActionTransition);
			ant.setTransitionTable(transitionTable);
			
		}
	}
	
	public void run() {
		int iteration = 0;
		while(iteration < VariablesANT.maxIterations){
			createSolutions();
			evaporate();
			updatePheromones();
			System.out.println("best: " + bestFitnessEver);
		}
		
		System.out.println(bestFitnessEver);
	}
	
	public static void main(String[] args) {
		ACO aco = new ACO("4.txt");
		aco.run();
	}

}
