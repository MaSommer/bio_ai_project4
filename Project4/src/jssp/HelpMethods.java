package jssp;

import java.util.ArrayList;
import java.util.Collections;

public class HelpMethods {
	
	public static ArrayList<Particle> generateSworm(DataInput di){
		int noOfMachines = di.getNoOfMachines();
		int noOfJobs = di.getNoOfJobs();
		ArrayList<Particle> sworm = new ArrayList<Particle>();
		ArrayList<Integer> initialParticle = new ArrayList<Integer>();
		for (int i = 0; i < noOfMachines; i++) {
			for (int j = 0; j < noOfJobs; j++) {
				initialParticle.add(j+1);
			}
		}
		while (sworm.size() < VariablesPSO.swormSize){
			ArrayList<Integer> particleSequence = (ArrayList<Integer>) initialParticle.clone();
			Collections.shuffle(particleSequence);
			sworm.add(new Particle(particleSequence, di));
		}
		for (Particle particle : sworm) {
			System.out.println("OperationSequence: " +particle.getOperationSequence());
		}
		return sworm;
	}
	
	public static ArrayList<ArrayList<Integer>> generateFoodSources(DataInput di){
		int noOfMachines = di.getNoOfMachines();
		int noOfJobs = di.getNoOfJobs();
		ArrayList<ArrayList<Integer>> foodSources = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> initialParticle = new ArrayList<Integer>();
		for (int i = 0; i < noOfMachines; i++) {
			for (int j = 0; j < noOfJobs; j++) {
				initialParticle.add(j+1);
			}
		}
		while (foodSources.size() < VariablesPSO.swormSize){
			ArrayList<Integer> particleSequence = (ArrayList<Integer>) initialParticle.clone();
			Collections.shuffle(particleSequence);
			foodSources.add(particleSequence);
		}
		for (ArrayList<Integer> foodSource : foodSources) {
			System.out.println(foodSource);
		}
		return foodSources;
	}
	
	public static ArrayList<Integer> generateSingleFoodSource(DataInput di){
		int noOfMachines = di.getNoOfMachines();
		int noOfJobs = di.getNoOfJobs();
		ArrayList<Integer> initialParticle = new ArrayList<Integer>();
		for (int i = 0; i < noOfMachines; i++) {
			for (int j = 0; j < noOfJobs; j++) {
				initialParticle.add(j+1);
			}
		}
		Collections.shuffle(initialParticle);
		return initialParticle;
	}
	
	public static ArrayList<ArrayList<Integer>> encodeJobs(ArrayList<Integer> operationSequence, DataInput di){
		ArrayList<ArrayList<Integer>> machineForJobs = di.getMachineForEachJob();
		ArrayList<ArrayList<Integer>> durationForJobs = di.getDurationForEachJob();
		int noOfMachines = di.getNoOfMachines();
		int noOfJobs = di.getNoOfJobs();
		
		ArrayList<ArrayList<Integer>> gantChart = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < noOfMachines; i++) {
			gantChart.add(new ArrayList<Integer>());
		}
		//holder styr paa hvilken subtask innenfor jobben som er den neste
		int[] nextSubTaskToDo = new int[noOfJobs];
		int[] durationSpentOnEachJob = new int[noOfJobs];
		for (int i = 0; i < nextSubTaskToDo.length; i++) {
			nextSubTaskToDo[i] = 0;
			durationSpentOnEachJob[i] = 0;
		}
//		System.out.println("Duration");
//		for (ArrayList<Integer> dur : durationForJobs) {
//			System.out.println(dur);
//		}
//		System.out.println("Machine");
//		for (ArrayList<Integer> mac : machineForJobs) {
//			System.out.println(mac);
//		}
		for (Integer subtask : operationSequence) {
			int durationOfSubTask = durationForJobs.get(subtask-1).get(nextSubTaskToDo[subtask-1]);
			int machineOfSubTask = machineForJobs.get(subtask-1).get(nextSubTaskToDo[subtask-1]);
			nextSubTaskToDo[subtask-1]++;
			for (int i = 0; i < durationOfSubTask; i++) {
				if (gantChart.get(machineOfSubTask).size() < durationSpentOnEachJob[subtask-1]){
					while (gantChart.get(machineOfSubTask).size() < durationSpentOnEachJob[subtask-1]){
						gantChart.get(machineOfSubTask).add(-1);
					}					
				}
				if (checkIfSubtaskFitsInAlreadyBlankSpotAndPlaceInIfItFits(durationOfSubTask, durationSpentOnEachJob[subtask-1], gantChart.get(machineOfSubTask), subtask, durationSpentOnEachJob)){
					break;
				}
				gantChart.get(machineOfSubTask).add(subtask);
				durationSpentOnEachJob[subtask-1] = gantChart.get(machineOfSubTask).size();
			}
		}
//		System.out.println("GanttChart");
//		for (ArrayList<Integer> part : gantChart) {
//			System.out.println(part);
//		}
		return gantChart;
	}
	
	private static boolean checkIfSubtaskFitsInAlreadyBlankSpotAndPlaceInIfItFits(int durationOfSubTask, int durationSpentOnJob, ArrayList<Integer> machine, int subtask, int[] durationSpentOnEachJob){
		int duration = 0;
		for (int i = durationSpentOnJob; i < machine.size(); i++) {
			if (machine.get(i) == -1){
				duration++;
			}
			else{
				duration = 0;
			}
			if (duration == durationOfSubTask){
				int count = 0;
				while(count != duration){
					machine.set(i-duration+1+count, subtask);
					count++;
				}
				durationSpentOnEachJob[subtask-1] = i-duration+1+count;
				return true;
			}
		}
		return false;
	}
	
	public static int findMaxlengthOfGanttChart(ArrayList<ArrayList<Integer>> chart){
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < chart.size(); i++) {
			if (chart.get(i).size() > max){
				max = chart.get(i).size();
			}
		}
		return max;
	}
	
	public static Particle findBestParticle(ArrayList<Particle> sworm){
		Particle bestParticle = null;
		int bestFitness = Integer.MAX_VALUE;
		for (Particle particle : sworm) {
			if (particle.getLocalBestFitnessValue() < bestFitness){
				bestParticle = particle;
				bestFitness = particle.getLocalBestFitnessValue();
			}
		}
		return bestParticle;
	}
	
	public static Bee findBestBee(ArrayList<Bee> bees){
		Bee bestBee = null;
		int bestFitness = Integer.MAX_VALUE;
		for (Bee bee : bees) {
			if (bee.getBestFitnessValue() < bestFitness){
				bestBee = bee;
				bestFitness = bee.getBestFitnessValue();
			}
		}
		return bestBee;
	}
	
	public static int findBestFitnessValue(ArrayList<Particle> sworm){
		int bestFitness = Integer.MAX_VALUE;
		for (Particle particle : sworm) {
			if (particle.getLocalBestFitnessValue() < bestFitness){
				bestFitness = particle.getLocalBestFitnessValue();
			}
		}
		return bestFitness;
	}
	public static int findBestFitnessValueBee(ArrayList<Bee> foodSources){
		int bestFitness = Integer.MAX_VALUE;
		for (Bee bee : foodSources) {
			if (bee.getBestFitnessValue() < bestFitness){
				bestFitness = bee.getBestFitnessValue();
			}
		}
		return bestFitness;
	}
	
	public static double percentageOfOptimal(String filename, ArrayList<Particle> sworm){
		int currentFitness = findBestFitnessValue(sworm);
		int optimalFitness = optimalFitnessValues(filename);
		return ((double)(currentFitness)/(double)(optimalFitness)-1)*100;
	}
	
	public static double percentageOfOptimalBee(String filename, ArrayList<Bee> foodSources){
		int currentFitness = findBestFitnessValueBee(foodSources);
		int optimalFitness = optimalFitnessValues(filename);
		return ((double)(currentFitness)/(double)(optimalFitness)-1)*100;
	}
	
	public static int optimalFitnessValues(String filename){
		if (filename.equals("1.txt")){
			return 55;
		}
		else if (filename.equals("2.txt")){
			return 930;
		}
		else if (filename.equals("3.txt")){
			return 1165;
		}
		else if (filename.equals("4.txt")){
			return 1005;
		}
		else if (filename.equals("5.txt")){
			return 1235;
		}
		else if (filename.equals("6.txt")){
			return 943;
		}
		else{
			return -1;
		}
	}
	
	public static int calculateFitnessValue(ArrayList<Integer> operationSequence, DataInput di){
		ArrayList<ArrayList<Integer>> gantChart = HelpMethods.encodeJobs(operationSequence, di);
		int fitnessValue = HelpMethods.findMaxlengthOfGanttChart(gantChart);
		return fitnessValue;
	}
	
	public static double[] probabilityForEachFitness(ArrayList<Bee> bees, DataInput di){
		int[] fitnessValues = new int[bees.size()];
		double[] probabilities = new double[bees.size()];
		int sum = 0;
		int index = 0;
		for (Bee bee : bees) {
			int fitness = HelpMethods.calculateFitnessValue(bee.getFoodSourceSequence(), di);
			fitnessValues[index] = fitness;
			sum += fitness;
			index++;
		}
		for (int i = 0; i < fitnessValues.length; i++) {
			double prob = (double) (fitnessValues[i])/(double)(sum);
			System.out.println(prob);
			probabilities[i] = prob;
		}
		for (int i = 1; i < probabilities.length; i++) {
			probabilities[i] += probabilities[i-1];
		}
		return probabilities;
	}
		

}
