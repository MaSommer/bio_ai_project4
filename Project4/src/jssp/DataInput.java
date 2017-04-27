package jssp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataInput {
	
	private ArrayList<ArrayList<Integer>> machineForEachJob;
	private ArrayList<ArrayList<Integer>> durationForEachJob;
	private int noOfJobs;
	private int noOfMachines;
	
	public DataInput(String filename){
		readFile(filename);
	}
	
	public void readFile(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));	
			String line = br.readLine();
			String[] list = new String[2];
			String number = "";
			int index = 0;
			for (int j = 0; j < line.length(); j++) {
				if(line.charAt(j) != ' '){
					number += line.charAt(j);
				}
				else{
					if (!number.equals("")){
						list[index] = number;
						index++;
					}
					number = "";
				}
				if(j == line.length()-1 && !number.equals("")){
					list[index] = number;
					index++;													
				}
			}	
			this.noOfJobs = Integer.parseInt(list[0]);
			this.noOfMachines = Integer.parseInt(list[1]);
			ArrayList<int[]> machineAndDurationForEachJob = addDataFilesToArray(br);
			this.machineForEachJob = new ArrayList<ArrayList<Integer>>();
			this.durationForEachJob = new ArrayList<ArrayList<Integer>>();
			for (int[] array : machineAndDurationForEachJob) {
				ArrayList<Integer> machine = new ArrayList<Integer>();
				ArrayList<Integer> duration = new ArrayList<Integer>();
				for (int i = 0; i < array.length; i++) {
					if (i%2 == 0){
						machine.add(array[i]);
					}
					else{
						duration.add(array[i]);
					}
				}
				machineForEachJob.add(machine);
				durationForEachJob.add(duration);
			}

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public ArrayList<int[]> addDataFilesToArray(BufferedReader br) throws IOException{
		String line = br.readLine();
		ArrayList<int[]> ret = new ArrayList<int[]>();
		while (!line.isEmpty()){
			ArrayList<String> list = new ArrayList<String>();
			String number = "";
			for (int j = 0; j < line.length(); j++) {
				if(line.charAt(j) != ' '){
					number += line.charAt(j);
				}
				else{
					if (!number.equals("")){
						list.add(number);
					}
					number = "";
				}
				if(j == line.length()-1 && !number.equals("")){
					list.add(number);
				}
			}
			int[] job = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				job[i] = Integer.parseInt(list.get(i));
			}
			ret.add(job);
			line = br.readLine();
			if (line == null){
				break;
			}
		}
		return ret;
	}
	

	public int getNoOfJobs() {
		return noOfJobs;
	}

	public int getNoOfMachines() {
		return noOfMachines;
	}
	
	public ArrayList<ArrayList<Integer>> getMachineForEachJob() {
		return machineForEachJob;
	}

	public ArrayList<ArrayList<Integer>> getDurationForEachJob() {
		return durationForEachJob;
	}
	
	public static void main(String[] args) {
		DataInput di = new DataInput("1.txt");
	}

}
