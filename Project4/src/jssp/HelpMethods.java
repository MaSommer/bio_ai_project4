package jssp;

import java.util.ArrayList;

public class HelpMethods {
	
	public static ArrayList<ArrayList<Integer>> encodeJobs(Particle particle){
		ArrayList<ArrayList<Integer>> machineForJobs = Program.di.getMachineForEachJob();
		ArrayList<ArrayList<Integer>> durationForJobs = Program.di.getDurationForEachJob();
		int noOfMachines = Program.di.getNoOfMachines();
		int noOfJobs = Program.di.getNoOfJobs();
		
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
		for (Integer subtask : particle.getOperationSequence()) {
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
	
	public static boolean checkIfSubtaskFitsInAlreadyBlankSpotAndPlaceInIfItFits(int durationOfSubTask, int durationSpentOnJob, ArrayList<Integer> machine, int subtask, int[] durationSpentOnEachJob){
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
		int bestFitness = -Integer.MAX_VALUE;
		for (Particle particle : sworm) {
			if (particle.getFitnessValue() > bestFitness){
				bestParticle = particle;
				bestFitness = particle.getFitnessValue();
			}
		}
		return bestParticle;
	}
	
	public static int findBestFitnessValue(ArrayList<Particle> sworm){
		int bestFitness = -Integer.MAX_VALUE;
		for (Particle particle : sworm) {
			if (particle.getFitnessValue() > bestFitness){
				bestFitness = particle.getFitnessValue();
			}
		}
		return bestFitness;
	}
	
	public static double percentageOfOptimal(String filename, ArrayList<Particle> sworm){
		int currentFitness = findBestFitnessValue(sworm);
		int optimalFitness = optimalFitnessValues(filename);
		return ((double)(currentFitness)/(double)(optimalFitness)-1)*100;
	}
	
	public static int optimalFitnessValues(String filename){
		if (filename.equals("1.txt")){
			return 56;
		}
		else if (filename.equals("2.txt")){
			return 1059;
		}
		else if (filename.equals("3.txt")){
			return 1276;
		}
		else if (filename.equals("4.txt")){
			return 1130;
		}
		else if (filename.equals("5.txt")){
			return 1451;
		}
		else if (filename.equals("6.txt")){
			return 979;
		}
		else{
			return -1;
		}
	}

}
