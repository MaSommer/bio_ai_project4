package jssp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

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
			System.out.println(particle.getOperationSequence());
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
	
	public static void takeOutRandomSubtaskAndPlaceItInAtBestFeasible(Bee bee, DataInput di){
		ArrayList<Integer> operationSequence = bee.getFoodSourceSequence();
		int randomSubtask = (int)(Math.random()*operationSequence.size());
		int subtask = operationSequence.get(randomSubtask);
		operationSequence.remove(randomSubtask);
		int bestFeasibleFitness = Integer.MAX_VALUE;
		int bestInsertion = -1;
		for (int i = 0; i < operationSequence.size(); i++) {
			operationSequence.add(i, subtask);
			int fitness = calculateFitnessValue(operationSequence, di);
			if (fitness < bestFeasibleFitness){
				bestInsertion = i;
				bestFeasibleFitness = fitness;
			}
			operationSequence.remove(i);
		}
		operationSequence.add(bestInsertion, subtask);
	}
	
	public static void takeOutBestSubtaskAndPlaceItInAtBestFeasible(Bee bee, DataInput di){
		ArrayList<Integer> operationSequence = bee.getFoodSourceSequence();
		boolean[] checkedSubtask = new boolean[di.getNoOfJobs()];
		for (int i = 0; i < checkedSubtask.length; i++) {
			checkedSubtask[i] = false;
		}
		int bestFeasibleFitness = Integer.MAX_VALUE;
		int bestInsertionIndex = -1;
		int indexToRemove = -1;
		int bestSubtask = -1;
		for (int j = 0; j < checkedSubtask.length; j++) {
			int index = 0;
			for (int i = 0; i < operationSequence.size(); i++) {
				if (operationSequence.get(i) == (j+1)){
					index = i;
					break;
				}
			}
			int subtask = j+1;
			operationSequence.remove(index);
			for (int i = 0; i < operationSequence.size(); i++) {
				operationSequence.add(i, subtask);
				int fitness = calculateFitnessValue(operationSequence, di);
				if (fitness < bestFeasibleFitness){
					bestInsertionIndex = i;
					bestFeasibleFitness = fitness;
					indexToRemove = index;
					bestSubtask = subtask;
				}
				operationSequence.remove(i);
			}
			operationSequence.add(index, subtask);			
		}
		operationSequence.remove(indexToRemove);
		operationSequence.add(bestInsertionIndex, bestSubtask);
	}
	
	public static ArrayList<Integer> treeSearchImprovedBeesAlgorithm(ArrayList<Integer> originalOpSeq, DataInput di){
		int l = 1;
		Queue<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
		ArrayList<Integer> bestOpSeq = (ArrayList<Integer>) originalOpSeq.clone();
		int bestFitness = calculateFitnessValue(bestOpSeq, di);
		for (int i = 0; i < VariablesBA.numberOfDifferentLocationsCriticalBox; i++) {
			ArrayList<Integer> opSeq = (ArrayList<Integer>) originalOpSeq.clone();
			swapTwoSubtasks(opSeq);
			queue.add(opSeq);
			int fitness = calculateFitnessValue(opSeq, di);
			if (fitness < bestFitness){
				bestOpSeq = opSeq;
				bestFitness = fitness;
			}
		}
		while (l < VariablesBA.numberOfLevelsTreeSearch){
			Queue<ArrayList<Integer>> newQueue = treeSearchLoop(queue, di);
			for (ArrayList<Integer> opSeq : newQueue) {
				int fitness = calculateFitnessValue(opSeq, di);
				if (fitness < bestFitness){
					bestOpSeq = opSeq;
					bestFitness = fitness;
				}
			}
			l++;
		}
		return bestOpSeq;
	}
	
	private static Queue<ArrayList<Integer>> treeSearchLoop(Queue<ArrayList<Integer>> queue, DataInput di){
		for (int i = 0; i < VariablesBA.numberOfDifferentLocationsCriticalBox; i++) {
			ArrayList<Integer> opsSeq = queue.poll();
			for (int j = 0; j < VariablesBA.numberOfTraialsForEachSolution; j++) {
				ArrayList<Integer> newOpSeq = (ArrayList<Integer>) opsSeq.clone();
				swapTwoSubtasks(newOpSeq);
				queue.add(newOpSeq);
			}
		}
		Queue<ArrayList<Integer>> newQueue = new LinkedList<ArrayList<Integer>>();
		int worstOpSeqFitness = -Integer.MAX_VALUE;
		ArrayList<Integer> worstOpSeq = null;
		for (ArrayList<Integer> opSeq : queue) {
			int fitness = calculateFitnessValue(opSeq, di);
			if (newQueue.size() < VariablesBA.numberOfDifferentLocationsCriticalBox){
				newQueue.add(opSeq);
				if (fitness > worstOpSeqFitness){
					worstOpSeqFitness = fitness;
					worstOpSeq = opSeq;
				}
				continue;
			}
			else if (fitness < worstOpSeqFitness){
				newQueue.remove(worstOpSeq);
				worstOpSeq = opSeq;
				worstOpSeqFitness = fitness;
				newQueue.add(opSeq);
			}
		}
		return newQueue;
	}
	
	public static void swapTwoSubtasks(ArrayList<Integer> opSeq){
		int randomSwapIndex1 = (int)(Math.random()*opSeq.size());
		int randomSwapIndex2 = (int)(Math.random()*opSeq.size());
		while (randomSwapIndex1 == randomSwapIndex2){
			randomSwapIndex2 = (int)(Math.random()*opSeq.size());
		}
		int tempValue = opSeq.get(randomSwapIndex1);
		opSeq.set(randomSwapIndex1, opSeq.get(randomSwapIndex2));
		opSeq.set(randomSwapIndex2, tempValue);
	}
		

}
