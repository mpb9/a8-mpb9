package a8;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LifeGame {
	public static void main(String[] args) {
		
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Conway's Game of Life");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		main_frame.setContentPane(content);
		
		LifeWidget cfw = new LifeWidget();
		content.add(cfw, BorderLayout.CENTER);
		
		main_frame.pack();
		main_frame.setVisible(true);
	}
} 