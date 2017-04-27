package jssp;

import java.util.ArrayList;

public class HelpMethods {
	
	public static ArrayList<ArrayList<Integer>> encodeJobs(ArrayList<Integer> particle, DataInput di){
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
		System.out.println("Duration");
		for (ArrayList<Integer> dur : durationForJobs) {
			System.out.println(dur);
		}
		System.out.println("Machine");
		for (ArrayList<Integer> mac : machineForJobs) {
			System.out.println(mac);
		}
		for (Integer subtask : particle) {
			System.out.println(nextSubTaskToDo[subtask-1]);
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
		System.out.println("GanttChart");
		for (ArrayList<Integer> part : gantChart) {
			System.out.println(part);
		}
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

}
