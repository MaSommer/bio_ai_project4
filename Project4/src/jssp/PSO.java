package jssp;

import java.text.DecimalFormat;

import java.util.ArrayList;



public class PSO {
	
	private ArrayList<Particle> sworm;
	private Particle globalBestParticle;
	public static DataInput di;
	private String filename;
	
	public PSO(String filename){
		this.filename = filename;
		init();
		run();
	}
	
	public void init(){
		di = new DataInput(filename);
		sworm = HelpMethods.generateSworm(di);
	}
	
	public void run(){
		int iterations = 0;
		System.out.println(HelpMethods.percentageOfOptimal(filename, sworm));
		while (HelpMethods.percentageOfOptimal(filename, sworm) > 10){
			globalBestParticle = HelpMethods.findBestParticle(sworm);
			for (Particle particle : sworm) {
				particle.updateParticle(iterations, globalBestParticle);
			}
			if (iterations%100 == 0){
				String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
				System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");				
			}
			iterations++;
		}
		String percentage = new DecimalFormat("##.##").format(HelpMethods.percentageOfOptimal(filename, sworm));
		System.out.println("After " + iterations + " the best found particle is " + percentage + "% of optimal solution.");				
	}
	
	public static void main(String[] args) {
		new PSO("5.txt");
	}

}
