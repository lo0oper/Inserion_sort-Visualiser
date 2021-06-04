import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class InsertionSort extends JPanel {
	private static final long serialVerisonUID = 1L;
	// this part serialVersionUID 
	private final int WIDTH = 1000, HEIGHT = WIDTH * 9 / 16;
	private final int SIZE = 200 ; // no. of bars
	// here we can't use big values as insertion sort will take too long(hrs)
	private final float BAR_WIDTH = (float)WIDTH/SIZE;// spreading the bars in full screen
	private float[] BAR_HEIGHT = new float[SIZE];
	private SwingWorker<Void, Void> shuffler,sorter ; // this is imp
	private int current_index , traversing_index ;
	
	private InsertionSort() {
		setBackground(Color.black);
		setPreferredSize(new Dimension(WIDTH ,HEIGHT));  // deciding backgrnd parameters
		initBarHeight(); // buliding bars
		initSorter(); // we call sorter before shuffler 
		initShuffler(); // shuffling the bars
		
	}
	// we are overriding the color of the bars 
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.green);
		Rectangle2D.Float bar;
		for(int i =0; i< SIZE; i++)
			// i*BAR_WIDTH to give each bar width == bar _widht
			{ bar = new Rectangle2D.Float(i*BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[i]);
			  g2d.fill(bar);
			}
		g2d.setColor(Color.RED);;
		bar = new Rectangle2D.Float(current_index*BAR_WIDTH,
										0, BAR_WIDTH,
										BAR_HEIGHT[current_index]);
		g2d.fill(bar);
		
		g2d.setColor(Color.RED);;
		bar = new Rectangle2D.Float(traversing_index*BAR_WIDTH,
										0, BAR_WIDTH, 
										BAR_HEIGHT[traversing_index]);
		g2d.fill(bar);
		
	}
	
	private void initSorter() {
		sorter = new SwingWorker<>() {
			@Override
			public Void doInBackground() throws InterruptedException {
			    // current and traversing index declared globally so that they change corlo accordingly
				for(current_index =1 ; current_index<SIZE ;current_index++) {
					traversing_index = current_index;
					while(traversing_index > 0 &&
							BAR_HEIGHT[traversing_index]< BAR_HEIGHT[traversing_index-1]) {
							
						swap(traversing_index , traversing_index-1);
							traversing_index--;
							Thread.sleep(1);
							repaint();
							
					}
				}
				// we don't do this we fill get red at lass and error at last as 
				//out of bound index will be read
				traversing_index=0;
				current_index =0;
				
				return null;
			}
		};
	}
	
	private void initShuffler() {
		shuffler = new SwingWorker<>() {
			@Override
			public Void doInBackground() throws InterruptedException {
				int middle = SIZE / 2 ;
				for(int i =0 , j = middle ; i<middle ; i++,j++) {
					int random_index = new Random().nextInt(SIZE);
					// what does nextINT do
					// random from random package  
					// using the random class
					swap(i,random_index);
					random_index = new Random().nextInt(SIZE);
					// using the random class
					swap(j,random_index);
					Thread.sleep(10);  // delay 
					repaint();
				}
				
				return null;
			}
			@Override
			// accesing the done method of SwiftWorker 
			public void done() {
				super.done();
				// here we will be calling sorter before there fore it was initiallised first
				sorter.execute(); 
			}
		};
		shuffler.execute();
	}
	
	private void initBarHeight() {
		float interval = (float)HEIGHT/ (float)(SIZE+1);
		
		for(int i =0; i< SIZE; i++) {
			BAR_HEIGHT[i]= i * interval;
		}
	}
	
	private void swap(int indexA , int indexB) {
		float temp = BAR_HEIGHT[indexA];
		BAR_HEIGHT[indexA] = BAR_HEIGHT[indexB];
		BAR_HEIGHT[indexB] = temp;
	}
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(() -> {
			// making object of JFrame for GUI
			JFrame frame = new JFrame("Insertion Sort Visualizer");
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setContentPane(new InsertionSort());
			frame.revalidate();
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}
