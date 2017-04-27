package jssp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

import javax.swing.JFrame;

public class DrawGanttChart extends JFrame {
	
	private int height;
	private int width;
	private int scaleHeight;
	private int scaleWidth;
	private ArrayList<Color> colorList = new ArrayList<Color>();
	private ArrayList<ArrayList<Integer>> chart;


    public DrawGanttChart(ArrayList<ArrayList<Integer>> chart) {
    	int noOfMachines = Program.di.getNoOfMachines();
    	this.height = noOfMachines;
    	this.width = findMaxlength(chart);
    	this.scaleWidth = 800/width;
    	if (scaleWidth == 0){
    		scaleWidth = 1;
    	}
    	this.scaleHeight = scaleWidth*width/height;
    	while (height*scaleHeight > 800){
    		scaleHeight = scaleHeight/2;
    	}
    	System.out.println(scaleHeight);
    	this.chart = chart;
        this.setPreferredSize(new Dimension(height*scaleHeight, width*scaleWidth));
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        colorList.add(Color.BLUE);
        colorList.add(Color.CYAN);
        colorList.add(Color.DARK_GRAY);
        colorList.add(Color.GREEN);
        colorList.add(Color.LIGHT_GRAY);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.ORANGE);
        colorList.add(Color.PINK);
        colorList.add(Color.RED);
        colorList.add(Color.YELLOW);
        
        colorList.add(new Color(246, 14, 14, 180));
        colorList.add(new Color(246, 169, 14, 180));
        colorList.add(new Color(246, 246, 14, 180));
        colorList.add(new Color(114, 246, 14, 180));
        colorList.add(new Color(14, 246, 184, 180));
        colorList.add(new Color(14, 200, 246, 180));
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // define the position
        for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i < chart.size() && j < chart.get(i).size() && chart.get(i).get(j) != -1){
					Color c = colorList.get(chart.get(i).get(j));
					g.setColor(c);
					for (int k = 0; k < scaleHeight; k++) {
						for (int l = 0; l < scaleWidth; l++) {
							g.fillRect(j*scaleWidth+l, i*scaleHeight+k, 1, 1);							
						}
					}
				}
			}
		}
        g.setColor(Color.BLACK);
        for (int i = 0; i < height; i++) {
        	g.fillRect(0, i*scaleHeight, width*scaleWidth, 1);
		}
        g.fillRect(0, height*scaleHeight, width*scaleWidth, 1);
        
    }

    
    public int findMaxlength(ArrayList<ArrayList<Integer>> chart){
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < chart.size(); i++) {
			if (chart.get(i).size() > max){
				max = chart.get(i).size();
			}
		}
		return max;
	}
}
