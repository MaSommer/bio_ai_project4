package jssp;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ACO {
	
	private ArrayList<Ant> ants;
	private DataInput di;
	private ArrayList<Integer> bestOperationSequence;
	private double bestFitnessEver = Double.MAX_VALUE;
	private ArrayList<ArrayList<ArrayList<Double>>> transitionTable;
	private ArrayList<Double> firstActionTransition;
	private String filename;
	
	
	
	public ACO(String filename){
		this.di = new DataInput(filename);
		this.transitionTable = HelpMethods.generateInitialTransitions(di);
		this.ants = new ArrayList<Ant>();
		this.filename = filename;
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
			iteration++;
			createSolutions();
			evaporate();
			updatePheromones();
			System.out.println("Best makespan after iteration " +iteration +":\t" + bestFitnessEver);
			int acceptableValue = HelpMethods.acceptableFitnessValues(filename);
			if((double) bestFitnessEver/(double) acceptableValue < 1.1){
				ArrayList<ArrayList<Integer>> chart = HelpMethods.encodeJobs(bestOperationSequence, di);
				DrawGanttChart gantt = new DrawGanttChart(chart, di);
				System.out.println("Makespan:\t\t " + bestFitnessEver);
				int optimalFitness = HelpMethods.optimalFitnessValues(filename);
				System.out.println("Optimal makespan \t" + optimalFitness);
				int acceptableFitness = HelpMethods.acceptableFitnessValues(filename);
				System.out.println("Acceptable makespan:\t" + acceptableFitness);
				System.out.println("\n\n");
				double percentageOfOptimal = (((double) bestFitnessEver/(double)optimalFitness)-1)*100;
				String percentageOfOpt = new DecimalFormat("##.##").format(percentageOfOptimal);
				
				double percentOfAcceptable = (((double) bestFitnessEver/(double)acceptableFitness)-1)*100;
				String percentOfAcpt = new DecimalFormat("##.##").format(percentOfAcceptable);
				System.out.println("Percent of optimal: \t" + percentageOfOpt+ "%");
				System.out.println("Percent of acceptable: \t" + percentOfAcpt + "%");
				break;
			}
		}
		
		System.out.println(bestFitnessEver);
	}
	
	public static void main(String[] args) {
		ACO aco = new ACO("5.txt");
		aco.run();
	}

}
