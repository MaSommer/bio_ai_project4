package jssp;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawGanttChartButtons extends JFrame{

	private JPanel p=new JPanel();
	private JButton buttons[][];
	private int width;
	private int height;
	private ArrayList<Color> colorList = new ArrayList<Color>();
	private ArrayList<ArrayList<Integer>> chart;

	
	public DrawGanttChartButtons(ArrayList<ArrayList<Integer>> chart, int noOfMachines){
		super("GanttChart");
		this.chart = chart;
		this.height = noOfMachines;
		this.width = HelpMethods.findMaxlengthOfGanttChart(chart);
		setSize(1200,750);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		p.setLayout(new GridLayout(height, width));
		setVisible(true);	
		
		for (int i = 0; i < 20; i++) {
			colorList.add(new Color(246, 14, 14, 180));
			colorList.add(new Color(246, 169, 14, 180));
			colorList.add(new Color(246, 246, 14, 180));
			colorList.add(new Color(114, 246, 14, 180));
			colorList.add(new Color(14, 246, 184, 180));
			colorList.add(new Color(14, 200, 246, 180));
			colorList.add(new Color(14, 107, 246, 180));
			colorList.add(new Color(14, 107, 246, 180));
			colorList.add(new Color(14, 21, 246, 180));
			colorList.add(new Color(200, 14, 246, 180));
			colorList.add(new Color(246, 14, 184, 180));
			colorList.add(new Color(246, 14, 99, 180));
			colorList.add(new Color(246, 14, 14, 180));
			colorList.add(new Color(0, 0, 0, 180));
			colorList.add(new Color(131, 119, 119, 180));
			colorList.add(new Color(220, 129, 129, 180));
			colorList.add(new Color(229, 204, 255, 180));
			colorList.add(new Color(0, 153, 0, 180));
			colorList.add(new Color(153, 0, 76, 180));
		}
		
		buttons = generateBoard(true);
		
		for (int i = 0; i < height; i++) {
			int input = i+1;
			buttons[i][0].setText(input+"");
		}
		
	}
	
	public JButton[][] generateBoard(boolean showBoard) {
		JButton[][] buttons = new JButton[height][width];
			
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//Wall
				JButton b = new JButton();
				buttons[i][j] = b;
				p.add(buttons[i][j]);
				add(p);
				if (i < chart.size() && j < chart.get(i).size() && chart.get(i).get(j) != -1){
					Color c = colorList.get(chart.get(i).get(j));
					buttons[i][j].setBackground(c);
					buttons[i][j].setForeground(c);
					buttons[i][j].setBorderPainted(false);
					buttons[i][j].setOpaque(true);					
				}
			}	
		}
		return buttons;
	}
	
	
	public JButton[][] getButtons(){
		return buttons;
	}
	
	public void setButtons(JButton[][] buttons){
		this.buttons = buttons;
	}
	
	
}
