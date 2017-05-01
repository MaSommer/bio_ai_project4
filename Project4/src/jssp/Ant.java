package jssp;

import java.util.ArrayList;
import java.util.Random;

public class Ant {
	
	private DataInput di;
	private ArrayList<ArrayList<ArrayList<Double>>> transitionTable;
	private ArrayList<ArrayList<Boolean>> jobsDone;
	private ArrayList<Integer> operationSequence;
	private ArrayList<Double> firstActionTransition;
	private Random rn;
	private double fitness;
	
	public Ant(DataInput di, ArrayList<ArrayList<ArrayList<Double>>> transitionTable) {
		this.di = di;
		this.transitionTable = transitionTable;
		this.jobsDone = new ArrayList<ArrayList<Boolean>>();
		this.operationSequence = new ArrayList<Integer>();
		this.rn = new Random();
		for(int i = 0 ; i < di.getMachineForEachJob().size(); i++){
			ArrayList<Boolean> in = new ArrayList<Boolean>();
			for(int j = 0 ; j < di.getMachineForEachJob().get(0).size(); j++){
				
				in.add(false);
			}
			jobsDone.add(in);
		}
		ArrayList<Double> temp = new ArrayList<Double>();
		for(int i = 0 ; i < di.getNoOfJobs() ; i++){
			temp.add(transitionTable.get(0).get(0).get(i));
		}
		this.firstActionTransition = temp;
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
			statuses.add(status);
		}
		
		return statuses;
	}
	
	
	public void setTransitionTable(ArrayList<ArrayList<ArrayList<Double>>> transitionTable) {
		this.transitionTable = transitionTable;
	}

	public ArrayList<Double> getFirstActionTransition() {
		return firstActionTransition;
	}

	public void setFirstActionTransition(ArrayList<Double> firstActionTransition) {
		this.firstActionTransition = firstActionTransition;
	}

	private ArrayList<Double> getNodeTransitions(){
		int index = 0;
		int prevJobLine = -1;
		if(operationSequence.size() ==0){
			return this.firstActionTransition;
		}
		prevJobLine = operationSequence.get(operationSequence.size()-1)-1;
		ArrayList<Integer> statuses = getJobStatuses();
		
		for(Boolean bool:jobsDone.get(prevJobLine)){
			if(bool){
				index++;
			}
		}
		ArrayList<Double> transitions =  transitionTable.get(prevJobLine).get(index);
		ArrayList<Double> transitionValues = new ArrayList<Double>();
		
		for(int i = 0 ; i < di.getNoOfJobs() ; i++){
			double transitionValue = 0;
			int transitionIndex = i*(di.getNoOfMachines()) + statuses.get(i);
			if(transitions.size() > transitionIndex){
				transitionValue = transitions.get(transitionIndex);
			}
			if(statuses.get(i) == di.getNoOfMachines()){
				transitionValue = -1;
			}
			transitionValues.add(transitionValue);
		}
		return transitionValues;
		
	}
	
	public void finishFirstLine() {
			for(int i = 0 ; i < jobsDone.get(0).size()-6; i++){
				jobsDone.get(0).set(i, true);
				operationSequence.add(1);
			}
			System.out.println(operationSequence);
	}
	
	public void resetOperationSequence() {
		this.operationSequence = new ArrayList<Integer>();
	}
	
	
	public ArrayList<Double> getProbabilities() {
		ArrayList<Double> prob = new ArrayList<Double>();
		if(operationSequence.size() == 0){
			double total = 0;
			for(Double d : firstActionTransition){
				total+=d;
			}
			for(int i = 0 ; i < firstActionTransition.size() ; i++){
				double value = firstActionTransition.get(i)/total;
				prob.add(value);
			}
			return prob;
		}
		ArrayList<Double> transitions = getNodeTransitions();
		ArrayList<Double> heuristic = new ArrayList<Double>();
		double total = 0;
		for(int i = 1 ; i < di.getNoOfJobs()+1 ; i++){
			if(transitions.get(i-1) == -1){
				heuristic.add(100000000.0);
				continue;
			}
			double addedTime = addedTime(i) +1;
			heuristic.add(addedTime);
		}
		ArrayList<Double> temp = new ArrayList<Double>();
		for(int i = 0 ; i < transitions.size(); i++){
			double value = Math.pow(transitions.get(i), VariablesANT.pheromoneImpact) + Math.pow((1/heuristic.get(i)), VariablesANT.heuristicImpact);
			if(transitions.get(i) == -1){
				value = -1;
			}
			temp.add(value);
			if(value > 0){
				total+= value;
			}
		}
		for(int i = 0 ; i< transitions.size(); i++){
			if(temp.get(i) < 0){
				prob.add(0.0);
				continue;
			}
			double probability = temp.get(i)/total;
			prob.add(probability);
		}
		
		return prob;
			
	}
	
	private int nextAction(){
		ArrayList<Double> probabilities = getProbabilities();
		double random = rn.nextDouble();
		double counter = 0;
		for(int i = 0 ; i < probabilities.size() ; i++){
			counter+=probabilities.get(i);
			if(random < counter){
				return i+1;
			}
		}
		return -1;
	}
	
	public void generateSolution() {
		
		for(int i = 0 ; i < di.getNoOfJobs()*di.getNoOfMachines() ; i++){
			int nextAction = nextAction();
			operationSequence.add(nextAction);
			for(int k = 0 ; k < jobsDone.get(nextAction-1).size() ; k++){
				if(!jobsDone.get(nextAction-1).get(k)){
					jobsDone.get(nextAction-1).set(k, true);
					break;
				}
			}
		}
		for(int i = 0 ; i < jobsDone.size(); i++){
			for(int j = 0 ; j < jobsDone.get(i).size(); j++){
				jobsDone.get(i).set(j, false);
			}
		}
		fitness = HelpMethods.calculateFitnessValue(operationSequence, di);
	}
	
	
	
	
	public double getFitness() {
		return fitness;
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
		DataInput di = new DataInput("5.txt");
		ArrayList<ArrayList<ArrayList<Double>>> transitionTable = HelpMethods.generateInitialTransitions(di);
		
		Ant ant = new Ant(di, transitionTable);
		System.out.println("\n");
		
		ant.generateSolution();
		System.out.println("OperationSequence: " +ant.getOperationSequence());
		System.out.println("FITNESS: " +HelpMethods.calculateFitnessValue(ant.getOperationSequence(), di));
		
	
		
	}

	public ArrayList<Integer> getOperationSequence() {
		return operationSequence;
	}
	
	

}
