package jssp;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.util.converter.PercentageStringConverter;

public class Program {
	
	private ArrayList<Particle> sworm;
	private Particle globalBestParticle;
	public static DataInput di;
	private String filename;
	
	public Program(String filename){
		this.filename = filename;
		init();
		run();
//		ArrayList<ArrayList<Integer>> chart = HelpMethods.encodeJobs(sworm.get(0));
//		new DrawGanttChart(chart);
	}
	
	public void init(){
		di = new DataInput(filename);
		sworm = PSOMethods.generateSworm();
	}
	
	public void run(){
		int iterations = 0;
		System.out.println(HelpMethods.percentageOfOptimal(filename, sworm));
		while (HelpMethods.percentageOfOptimal(filename, sworm) > 20){
			globalBestParticle = HelpMethods.findBestParticle(sworm);
			for (Particle particle : sworm) {
				particle.updateParticle(iterations, globalBestParticle);
			}
			String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
			System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");
			iterations++;
		}
	}
	
	public static void main(String[] args) {
		new Program("1.txt");
	}

}
