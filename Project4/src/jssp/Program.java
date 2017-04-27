package jssp;

import java.util.ArrayList;

public class Program {
	
	ArrayList<ArrayList<Integer>> sworm;
	
	public Program(String filename){
		DataInput di = new DataInput(filename);
		ArrayList<ArrayList<Integer>> machinesForJobs = di.getMachineForEachJob();
		ArrayList<ArrayList<Integer>> durationForJobs = di.getDurationForEachJob();
		sworm = PSOMethods.generateSworm(di.getNoOfMachines(), di.getNoOfJobs());
		ArrayList<ArrayList<Integer>> chart = HelpMethods.encodeJobs(sworm.get(0), di);
//		DrawGanttChart gc = new DrawGanttChart(chart, di.getNoOfMachines());
		new DrawGanttChart(chart, di.getNoOfMachines());
	}
	
	public static void main(String[] args) {
		new Program("1.txt");
	}

}
