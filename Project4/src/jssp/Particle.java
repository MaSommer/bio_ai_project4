package jssp;

import java.util.ArrayList;

public class Particle {
	
	
	private int fitnessValue;
	private int localBestFitnessValue;
	private ArrayList<Double> positions;

	private ArrayList<Double> localBestPositions;
	private ArrayList<Double> localBestSequence;
	private ArrayList<Double> velocities;
	
	private ArrayList<Particle> neighbourhood;
	private ArrayList<Integer> operationSequence;

	public Particle(ArrayList<Integer> operationSequence){
		this.operationSequence = operationSequence;
		this.positions = new ArrayList<Double>();
		this.velocities = new ArrayList<Double>();
		this.localBestFitnessValue = Integer.MAX_VALUE;
		for (int i = 0; i < operationSequence.size(); i++) {
			double pos = Math.random()*(Variables.maxPosition-Variables.minPosition)-Variables.minPosition;
			positions.add(pos);
			double velocity = Math.random()*(Variables.maxVelocity-Variables.minVelocity)-Variables.minVelocity;
			velocities.add(velocity);
		}
		this.localBestPositions = (ArrayList<Double>) positions.clone();
		this.localBestSequence = (ArrayList<Double>) operationSequence.clone();
		updateSchedule();
		updateFitnessValue();
	}
	
	public void updateParticle(int iteration, Particle globalBestParticle){
		double lamda = Math.log(Variables.terminalOmega/Variables.initialOmega)/(-Variables.iterationsUntilOmegaTerminal);
		double omega = Variables.initialOmega*Math.exp(-lamda*iteration);
		int index = 0;
		for (Double velocity : velocities) {
			velocity = omega*velocity + Variables.c1*Math.random()*(localBestPositions.get(index) - positions.get(index)) + Variables.c2*Math.random()*(globalBestParticle.getPositions().get(index)-positions.get(index));
			positions.set(index, positions.get(index)+velocity);
			index++;
		}
		updateSchedule();
		updateFitnessValue();
	}
	
	private void updateSchedule(){
		//Sorterer listen etter positions, oppdaterer tilsvarende velocities og operationSequence
		for (int i = 0; i < positions.size(); i++) {
			for (int j = i; j < positions.size(); j++) {
				if (positions.get(i) < positions.get(j)){
					double tempPos = positions.get(i);
					positions.set(i, positions.get(j));
					positions.set(j, tempPos);
					double tempVelocity = velocities.get(i);
					velocities.set(i, velocities.get(j));
					velocities.set(j, tempVelocity);
					int subtask = operationSequence.get(i);
					operationSequence.set(i, operationSequence.get(j));
					operationSequence.set(j, subtask);
				}
			}
		}
	}
	
	private void updateFitnessValue(){
		ArrayList<ArrayList<Integer>> gantChart = HelpMethods.encodeJobs(this);
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

}
