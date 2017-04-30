package jssp;

import java.util.ArrayList;

public class Ant {
	
	public DataInput di;
	public ArrayList<ArrayList<Integer>> machineSchedules;
	public ArrayList<ArrayList<ArrayList<Double>>> transitionTable;
	public ArrayList<ArrayList<Boolean>> jobsDone;
	public ArrayList<Integer> operationSequence;
	
	public Ant(DataInput di, ArrayList<ArrayList<ArrayList<Double>>> transitionTable) {
		this.di = di;
		this.transitionTable = transitionTable;
		this.jobsDone = new ArrayList<ArrayList<Boolean>>();
		this.operationSequence = new ArrayList<Integer>();
		for(int i = 0 ; i < di.getMachineForEachJob().size(); i++){
			ArrayList<Boolean> in = new ArrayList<Boolean>();
			for(int j = 0 ; j < di.getMachineForEachJob().get(0).size(); j++){
				
				in.add(false);
			}
			jobsDone.add(in);
		}
	}
	
	public ArrayList<ArrayList<Boolean>> getJobsDone(){
		return this.jobsDone;
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> getTransitionTable() {
		return transitionTable;
	}
	
	private ArrayList<Integer> getJobStatuses() {
		ArrayList<Integer> statuses = new ArrayList<Integer>();
		for(int i = 0 ; i < jobsDone.size(); i++){
			int status = 0 ;
			for(Boolean bool:jobsDone.get(i)){
				if(bool){
					status++;
				}
			}
			statuses.add(0, status);
		}
		
		return statuses;
	}
	
	
	private ArrayList<Double> getNodeTransitions(){
		int index = 0;
		int prevJob = operationSequence.get(operationSequence.size()-1);
		ArrayList<Integer> statuses = getJobStatuses();
		for(Boolean bool:jobsDone.get(operationSequence.get(prevJob-1))){
			if(bool){
				index++;
			}
		}
		ArrayList<Double> transitions =  transitionTable.get(prevJob).get(index);
		ArrayList<Double> transitionValues = new ArrayList<Double>();
		for(int i = 0 ; i < di.getNoOfJobs() ; i++){
			int transitionIndex = i*di.getNoOfMachines() + statuses.get(i);
			double transitionValue = transitions.get(transitionIndex);
			if(statuses.get(i) == di.getNoOfMachines()){
				transitionValue = -1;
			}
			transitionValues.add(transitionValue);
		}
		return transitionValues;
		
	}
	
	private ArrayList<Double> getProbabilities() {
		ArrayList<Double> prob = new ArrayList<Double>();
		ArrayList<Double> transitions = getNodeTransitions();
		ArrayList<Double> heuristic = new ArrayList<Double>();
		double total = 0;
		for(int i = 1 ; i < di.getNoOfJobs()+1 ; i++){
			double addedTime = addedTime(i) +1;
			heuristic.add(addedTime);
		}
		ArrayList<Double> temp = new ArrayList<Double>();
		for(int i = 0 ; i < transitions.size(); i++){
			double value = Math.pow(transitions.get(i), VariablesANT.pheromoneImpact) + Math.pow((1/heuristic.get(i)), VariablesANT.heuristicImpact);
			temp.add(value);
			total+= value;
		}
		for(int i = 0 ; i< transitions.size(); i++){
			double probability = temp.get(i)/total;
			prob.add(probability);
		}
		
		return prob;
			
	}
	
	
	
	
	private int addedTime(int jobNr){
		int time = HelpMethods.calculateFitnessValue(operationSequence, di);
		operationSequence.add(jobNr);
		int newTime = HelpMethods.calculateFitnessValue(operationSequence, di);
		operationSequence.remove(operationSequence.size()-1);
		int timeAdded = newTime - time;
		if(timeAdded<=0){
			return 0;
		}
		return timeAdded;
	}
	
	public static void main(String[] args) {
		ArrayList<ArrayList<ArrayList<Double>>> transitionTable = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<ArrayList<Double>> in = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> in2 = new ArrayList<Double>();
		in.add(in2);
		transitionTable.add(in);
		DataInput di = new DataInput("Project4/1.txt");
		
		Ant ant = new Ant(di, transitionTable);
		System.out.println(ant.getJobsDone());
		System.out.println(ant.getJobsDone().size() + " antall jobbber --" + di.getNoOfJobs());
		System.out.println(ant.getJobsDone().get(0).size() + " antall maskiner --" + di.getNoOfMachines());
		System.out.println(ant.getTransitionTable());
		ArrayList<Integer> operationSequence = new ArrayList<Integer>();
	}
	
	

}
