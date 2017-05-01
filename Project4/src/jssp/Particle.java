package jssp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Particle {
	
	
	private int fitnessValue;
	private int localBestFitnessValue;
	private ArrayList<Double> positions;

	private ArrayList<Double> localBestPositions;
	private ArrayList<Double> localBestSequence;
	private ArrayList<Double> velocities;

	private ArrayList<Integer> operationSequence;
	private DataInput di;

	public Particle(ArrayList<Integer> operationSequence, DataInput di){
		this.operationSequence = operationSequence;
		this.positions = new ArrayList<Double>();
		this.velocities = new ArrayList<Double>();
		this.di = di;
		this.localBestFitnessValue = Integer.MAX_VALUE;
		double maxVelocity = di.getNoOfJobs()*di.getNoOfMachines()*0.1;
//		double maxVelocity = 2;
		double minVelocity = -maxVelocity;
		for (int i = 0; i < operationSequence.size(); i++) {
			double pos = Math.random()*(VariablesPSO.maxPosition-VariablesPSO.minPosition)-VariablesPSO.minPosition;
			positions.add(pos);
			double velocity = Math.random()*(maxVelocity-minVelocity)-minVelocity;
			velocities.add(velocity);
		}
		this.localBestPositions = (ArrayList<Double>) positions.clone();
		this.localBestSequence = (ArrayList<Double>) operationSequence.clone();
		updateSchedule();
		updateFitnessValue();
	}
	
	public Particle(Particle another){
		this.operationSequence = (ArrayList<Integer>) another.operationSequence.clone();
		this.positions = (ArrayList<Double>) another.positions.clone();
		this.velocities = (ArrayList<Double>) another.positions.clone();
		this.localBestFitnessValue = another.localBestFitnessValue;
		this.localBestPositions = (ArrayList<Double>) another.localBestPositions.clone();
		this.localBestSequence = (ArrayList<Double>) another.localBestSequence.clone();
		this.fitnessValue = another.fitnessValue;
		this.di = another.di;
	}
	
	public void updateParticle(int iteration, Particle globalBestParticle){
//		double lamda = Math.log(VariablesPSO.terminalOmega/VariablesPSO.initialOmega)/(-VariablesPSO.iterationsUntilOmegaTerminal);
//		double omega = VariablesPSO.initialOmega*Math.exp(-lamda*iteration);
		double omega = VariablesPSO.initialOmega - iteration*(VariablesPSO.initialOmega-VariablesPSO.terminalOmega)/VariablesPSO.iterationsUntilOmegaTerminal;
		int index = 0;
		for (Double velocity : velocities) {
			velocity = omega*velocity + VariablesPSO.c1*Math.random()*(localBestPositions.get(index) - positions.get(index)) + VariablesPSO.c2*Math.random()*(globalBestParticle.getPositions().get(index)-positions.get(index));
			positions.set(index, positions.get(index)+velocity);
			index++;
		}
		updateSchedule();
		updateFitnessValue();
	}
	
	public void updateSchedule(){
		ArrayList<Double> artificialPositions = (ArrayList<Double>) positions.clone();
		Collections.sort(artificialPositions);

		HashMap<Double, Integer> mapPositionToJob = new HashMap<Double, Integer>();
		int jobCounter = 1;
		for (int i = 0; i < artificialPositions.size(); i++) {
			int job = jobCounter%di.getNoOfJobs() + 1;
			mapPositionToJob.put(artificialPositions.get(i), job);
			jobCounter++;
		}
		for (int i = 0; i < artificialPositions.size(); i++) {
			operationSequence.set(i, mapPositionToJob.get(positions.get(i)));
		}
	}
	
	public void updateFitnessValue(){
		ArrayList<ArrayList<Integer>> gantChart = HelpMethods.encodeJobs(operationSequence, di);
		fitnessValue = HelpMethods.findMaxlengthOfGanttChart(gantChart);
		if (fitnessValue < localBestFitnessValue){
			localBestFitnessValue = fitnessValue;
			localBestPositions = (ArrayList<Double>) positions.clone();
			this.localBestSequence = (ArrayList<Double>) operationSequence.clone();
		}
	}
	
	
	public int getFitnessValue() {
		return fitnessValue;
	}
	
	public ArrayList<Integer> getOperationSequence() {
		return operationSequence;
	}
	
	public ArrayList<Double> getPositions() {
		return positions;
	}
	
	public int getLocalBestFitnessValue() {
		return localBestFitnessValue;
	}

	public ArrayList<Double> getLocalBestPositions() {
		return localBestPositions;
	}

	public ArrayList<Double> getLocalBestSequence() {
		return localBestSequence;
	}
	
	public void setPositions(ArrayList<Double> positions) {
		this.positions = positions;
	}

	public void setVelocities(ArrayList<Double> velocities) {
		this.velocities = velocities;
	}

	public void setOperationSequence(ArrayList<Integer> operationSequence) {
		this.operationSequence = operationSequence;
	}

}
