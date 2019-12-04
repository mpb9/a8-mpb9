package a8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class LifeWidget extends JPanel implements ActionListener, SpotListener {

	//private enum Player {BLACK, WHITE};
	//private Player nextUp;
	// private boolean gameover;
	// private int totalMoves = 0;
	
	// NO NEED for players or for gameover in this game
	
	private JSpotBoard board;
	private int height = 6;
	private int width = 7;
	private int [][] numberOfNeighbors = new int [width][height];
	private boolean torusMode = false;
	
	private boolean automaticRunStarted = false;
	
	public LifeWidget() {
		
		board = new JSpotBoard(7, 6);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				board.getSpotAt(i, j).setBackground(Color.WHITE);
			}
		}
		
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BorderLayout());
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		
		JButton randomFillButton = new JButton("Random Fill");
		randomFillButton.addActionListener(this);
		lowerPanel.add(randomFillButton, BorderLayout.WEST);
		
		JButton setSizeButton = new JButton("Set New Size");
		setSizeButton.addActionListener(this);
		upperPanel.add(setSizeButton, BorderLayout.WEST);
		
		JButton nextTurnButton = new JButton("Next Turn");
		nextTurnButton.addActionListener(this);
		upperPanel.add(nextTurnButton, BorderLayout.EAST);
		
		JButton resetButton = new JButton("Restart");
		resetButton.addActionListener(this);
		lowerPanel.add(resetButton, BorderLayout.EAST);
		
		
		
		JButton setBirthSurviveNumberButton = new JButton("Set Birth/Survive Rate");
		setBirthSurviveNumberButton.addActionListener(this);
		lowerPanel.add(setBirthSurviveNumberButton, BorderLayout.CENTER);
		
		JButton setTorusModeButton = new JButton("Torus Mode On/Off");
		setTorusModeButton.addActionListener(this);
		lowerPanel.add(setTorusModeButton, BorderLayout.PAGE_END);
		
		JButton startStop_AutomaticRunButton = new JButton("Start/Stop");
		startStop_AutomaticRunButton.addActionListener(this);
		upperPanel.add(startStop_AutomaticRunButton, BorderLayout.CENTER);
		
		
		add(upperPanel, BorderLayout.NORTH);
		add(lowerPanel, BorderLayout.SOUTH);
		board.addSpotListener(this);
		
		clearBoard();
	}
	
	public void resize(int width, int height) {

	}
	
	private void clearBoard() {
		for (Spot s: board) {
			s.clearSpot();
			s.unhighlightSpot();
		}
		

	}
	
	@Override
	public void spotClicked(Spot spot) {
		
		if (spot.isEmpty()) {
			spot.setSpot();
			spot.setSpotColor(Color.BLACK);
			spot.highlightSpot();
		} else {
			spot.setSpotColor(Color.WHITE);
			spot.clearSpot();
			spot.unhighlightSpot();
		}
		
	}

	@Override
	public void spotEntered(Spot spot) {
		spot.highlightSpot();
		// not a needed method
	}

	@Override
	public void spotExited(Spot spot) {
		// not a needed method
		spot.unhighlightSpot();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Restart")) {
			clearBoard();
		} else if (e.getActionCommand().equals("Random Fill")) {
			randomFill();
		} else if (e.getActionCommand().equals("Next Turn")) {
			nextTurn();
		} else if (e.getActionCommand().equals("Set New Size")) { // doesn't work
			resize(15, 15); }
		
		else if (e.getActionCommand().equals("Set Birth/Survive Rate")) { // intermediate level
			// need a way to GET each of these numbers from the user.
			// text box? input window?
			int birthNumber_ForDeadCells = 0; // get number from user
			int surviveNumber_ForAliveCells = 0; // get number from user
			setBirthSurviveRates(birthNumber_ForDeadCells, surviveNumber_ForAliveCells);
		} else if (e.getActionCommand().equals("Torus Mode On/Off")) { // intermediate level
			torusMode = !torusMode;		} 
		
		else if (e.getActionCommand().equals("Start/Stop")) {
			automaticRunStarted = !automaticRunStarted;
			if (automaticRunStarted) {
				automaticallyRun();
			}
		} 
		
	}
	
	public void setBirthSurviveRates(int birthNumber_ForDeadCells, int surviveNumber_ForAliveCells) {
		
	}
	
	public void randomFill() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int x = (int) Math.round(Math.random());
				if (x == 1) {
					board.getSpotAt(i, j).setSpotColor(Color.WHITE);
					board.getSpotAt(i, j).clearSpot();
					board.getSpotAt(i, j).unhighlightSpot();
				} else {
					board.getSpotAt(i, j).setSpot();
					board.getSpotAt(i, j).setSpotColor(Color.BLACK);
					board.getSpotAt(i, j).highlightSpot();
				}
			}
		}
	}
	
	public void nextTurn() {

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				numberOfNeighbors[i][j] = checkNumberNeighbors(i,j);
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (!board.getSpotAt(i, j).isEmpty()) {
					aliveCell(numberOfNeighbors[i][j], i, j);
				} else {
					deadCell(numberOfNeighbors[i][j], i, j);
				}
			}
		}
	}
	
	public void aliveCell(int x, int i, int j) {
		int num_of_neighbors = x;
		
		if (num_of_neighbors < 2 || num_of_neighbors > 3) {
			board.getSpotAt(i, j).setSpotColor(Color.WHITE);
			board.getSpotAt(i, j).clearSpot();
			board.getSpotAt(i, j).unhighlightSpot();
		}
	}
	
	public void deadCell(int x, int i, int j) {
		int num_of_neighbors = x;
		
		if (num_of_neighbors == 3) {
			board.getSpotAt(i, j).setSpot();
			board.getSpotAt(i, j).setSpotColor(Color.BLACK);
			board.getSpotAt(i, j).highlightSpot();
		}
		
	}
	
	public int checkNumberNeighbors(int i, int j) {
		
		// top left corner = 0,0
		if (i == 0 && j == 0) { // spot is on top left corner, only check right,bottom right,and bottom center for neighbors
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors_TopLeftCorner_OfBoard(i, j);
			} else {
				return checkNumberNeighbors_TopLeftCorner_OfBoard(i, j);
			}
		} 
		
		// top right corner means i = (width - 1) and j = 0
		else if (i == (width - 1) && j == 0) { // spot is on top right corner, only check left,bottom left,and bottom center for neighbors
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors__TopRightCorner_OfBoard(i, j);
			} else {
				return checkNumberNeighbors__TopRightCorner_OfBoard(i, j);
			}
		} 
		
		// bottom right corner means i = (width - 1) and j = (height - 1)
		else if (i == (width - 1) && j == (height - 1)) { // spot is on bottom right corner, only check left,top left,and top center for neighbors
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors_BottomRightCorner_OfBoard(i, j);
			} else {
				return checkNumberNeighbors_BottomRightCorner_OfBoard(i, j);
			}
		} 
		
		// bottom left corner means i = 0 and j = (height - 1)
		else if (i == 0 && j == (height - 1)) { // spot is on bottom left corner, only check right,top right,and top center for neighbors
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors__BottomLeftCorner_OfBoard(i, j);
			} else {
				return checkNumberNeighbors__BottomLeftCorner_OfBoard(i, j);
			}
		} 
		
		// left edge means i = 0
		else if (i == 0) { // spot is along left edge, only check top center, top right, right, bottom right, bottom center
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors_LeftEdge_OfBoard(i, j);
			} else {
				return checkNumberNeighbors_LeftEdge_OfBoard(i, j);
			}
		} 
		
		// top edge means j = 0
		else if (j == 0) { // spot is along top edge, only check left, bottom left, bottom center,bottom right, right
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors__TopEdge_OfBoard(i, j);
			} else {
				return checkNumberNeighbors__TopEdge_OfBoard(i, j);
			}
		} 
		
		// right edge means i =(board width) - 1
		else if (i == (width - 1)) { // spot is along right edge, only check top center, top left,left,bottom left, bottom center
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors_RightEdge_OfBoard(i, j);
			} else {
				return checkNumberNeighbors_RightEdge_OfBoard(i, j);
			}
		} 
		
		// bottom edge means j = (board height) - 1
		else if (j == (height - 1)) {  // spot is along bottom edge, only check left,top left,top center,top right, right
			if (torusMode) {
				return TORUSMODE_checkNumberNeighbors__BottomEdge_OfBoard(i, j);
			} else {
				return checkNumberNeighbors__BottomEdge_OfBoard(i, j);
			}
		} 
		
		// everything else (if it's in the middle, then torus mode shouldn't matter.)
		else {
			return checkNumberNeighbors_MiddleOfBoard(i, j);
		}
	}
	
	public int checkNumberNeighbors_MiddleOfBoard(int i, int j) {
		int total = 0;
		
		// top left
		if (!board.getSpotAt(i - 1, j - 1).isEmpty()) {
			total++;
		}
		
		// left
		if (!board.getSpotAt(i - 1, j).isEmpty()) {
			total++;
		}
		
		// bottom left
		if (!board.getSpotAt(i - 1, j + 1).isEmpty()) {
			total++;
		}
		
		// top center
		if (!board.getSpotAt(i, j - 1).isEmpty()) {
			total++;
		}
		
		// bottom center
		if (!board.getSpotAt(i, j + 1).isEmpty()) {
			total++;
		}
		
		// top right
		if (!board.getSpotAt(i + 1, j - 1).isEmpty()) {
			total++;
		}
		
		// right
		if (!board.getSpotAt(i + 1, j).isEmpty()) {
			total++;
		}
		
		// bottom right
		if (!board.getSpotAt(i + 1, j + 1).isEmpty()) {
			total++;
		}
		
		return total;
	}
	
	public int checkNumberNeighbors_TopLeftCorner_OfBoard(int i, int j) {
		 // only check right,bottom right,and bottom center for neighbors
		 
		int total = 0;
		if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
			total++;
		}
		if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
			total++;
		}
		if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
			total++;
		}		
		return total;
	}
	
	public int checkNumberNeighbors__TopRightCorner_OfBoard(int i, int j) {
		// only check left,bottom left,and bottom center for neighbors
		
		int total = 0;
		if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
			total++;
		}
		if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
			total++;
		}
		if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
			total++;
		}		
		return total;
	}
	
	public int checkNumberNeighbors_BottomRightCorner_OfBoard(int i, int j) {
		// only check left,top left,and top center for neighbors
		
		int total = 0;
		if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
			total++;
		}
		if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
			total++;
		}
		if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
			total++;
		}		
		return total;
	}
	
	public int checkNumberNeighbors__BottomLeftCorner_OfBoard(int i, int j) {
		// only check right,top right,and top center for neighbors
		
		int total = 0;
		if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
			total++;
		}
		if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
			total++;
		}
		if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
			total++;
		}		
		return total;
	}
	
	public int checkNumberNeighbors_LeftEdge_OfBoard(int i, int j) {
		// only check top center, top right, right, bottom right, bottom center
		
		int total = 0;
		if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
			total++;
		}	
		if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
			total++;
		}	
		if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
			total++;
		}
		if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
			total++;
		}
		if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
			total++;
		}		
		return total;
	}
	
	public int checkNumberNeighbors__TopEdge_OfBoard(int i, int j) {
		// only check left, bottom left, bottom center,bottom right, right
		
		int total = 0;
		if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
			total++;
		}
		if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
			total++;
		}
		if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
			total++;
		}	
		if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
			total++;
		}
		if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
			total++;
		}
		return total;
	}
	
	public int checkNumberNeighbors_RightEdge_OfBoard(int i, int j) {
		// only check top center, top left,left,bottom left, bottom center
		
		int total = 0;
		if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
			total++;
		}	
		if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
			total++;
		}
		if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
			total++;
		}
		if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
			total++;
		}
		if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
			total++;
		}	
		return total;
	}
	
	public int checkNumberNeighbors__BottomEdge_OfBoard(int i, int j) {
		// only check left,top left,top center,top right, right
		
		int total = 0;
		if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
			total++;
		}
		if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
			total++;
		}
		if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
			total++;
		}	
		if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
			total++;
		}	
		if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
			total++;
		}
		return total;
	}
		
		// TORUS MODE versions of these check neighbor methods (for everything but the middle spots)
		
		public int TORUSMODE_checkNumberNeighbors_TopLeftCorner_OfBoard(int i, int j) {
			 // only check right,bottom right,and bottom center for neighbors
			 
			int total = 0;
			if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
				total++;
			}
			if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
				total++;
			}
			if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
				total++;
			}
			// add a check for "top left of this", "top middle from this", "top right from this", "left" from this, "bottom left" from this
			//up and left
			if (!board.getSpotAt(width - 1, height - 1).isEmpty()) {
				total++;
			}
			// just up
			if (!board.getSpotAt(i, height -1).isEmpty()) {
				total++;
			}
			//up and right
			if (!board.getSpotAt(i+ 1, height -1).isEmpty()) {
				total++;
			}
			//left
			if (!board.getSpotAt(width -1, j).isEmpty()) {
				total++;
			}
			//bottom left
			if (!board.getSpotAt(width - 1, j + 1).isEmpty()) {
				total++;
			}
			
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors__TopRightCorner_OfBoard(int i, int j) {
			// only check left,bottom left,and bottom center for neighbors
			
			int total = 0;
			if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
				total++;
			}
			if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
				total++;
			}	
			//top left top center top right, right , bottom right
			//top left
			if (!board.getSpotAt(i - 1 , height - 1).isEmpty()) { // left
				total++;
			}
			// top center
			if (!board.getSpotAt(i, height - 1).isEmpty()) { // left
				total++;
			}//top right
			if (!board.getSpotAt(0, height - 1).isEmpty()) { // left
				total++;
			}//right
			if (!board.getSpotAt(0, 0).isEmpty()) { // left
				total++;
			}// bottom right
			if (!board.getSpotAt(0, j+ 1).isEmpty()) { // left
				total++;
			}
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors_BottomRightCorner_OfBoard(int i, int j) {
			// only check left,top left,and top center for neighbors
			
			int total = 0;
			if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
				total++;
			}
			if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
				total++;
			}		
			//top right, right, bottom right, bottom center, bottom left
			if (!board.getSpotAt(0, j - 1).isEmpty()) { // top right
				total++;
			}		
			if (!board.getSpotAt(0, j).isEmpty()) { // right
				total++;
			}		
			if (!board.getSpotAt(0, 0).isEmpty()) { // bottom right
				total++;
			}		
			if (!board.getSpotAt(i, 0).isEmpty()) { // bottom center
				total++;
			}		
			if (!board.getSpotAt(i - 1, 0).isEmpty()) { // bottom left
				total++;
			}		
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors__BottomLeftCorner_OfBoard(int i, int j) {
			// only check right,top right,and top center for neighbors
			
			int total = 0;
			if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
				total++;
			}
			if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
				total++;
			}
			if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
				total++;
			}		
			// bottom right bottom center bottom left left top left
			if (!board.getSpotAt(i + 1, 0).isEmpty()) { // bottom right
				total++;
			}	
			if (!board.getSpotAt(i, 0).isEmpty()) { // bottom center
				total++;
			}	
			if (!board.getSpotAt(width- 1, 0).isEmpty()) { // bottom left
				total++;
			}	
			if (!board.getSpotAt(width - 1, j).isEmpty()) { //left 
				total++;
			}	
			if (!board.getSpotAt(width - 1, 0).isEmpty()) { // top left
				total++;
			}	
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors_LeftEdge_OfBoard(int i, int j) {
			// only check top center, top right, right, bottom right, bottom center
			
			int total = 0;
			if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
				total++;
			}	
			if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
				total++;
			}	
			if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
				total++;
			}
			if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
				total++;
			}
			if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
				total++;
			}		
			//left top left bottom left
			if (!board.getSpotAt(width -1, j + 1).isEmpty()) { // top left
				total++;
			}	
			if (!board.getSpotAt(width - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(width - 1, j - 1).isEmpty()) { // bottom left
				total++;
			}	
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors__TopEdge_OfBoard(int i, int j) {
			// only check left, bottom left, bottom center,bottom right, right
			
			int total = 0;
			if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
				total++;
			}
			if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
				total++;
			}	
			if (!board.getSpotAt(i + 1, j + 1).isEmpty()) { // bottom right
				total++;
			}
			if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
				total++;
			}
			// top left top center top right
			if (!board.getSpotAt(i - 1, height - 1).isEmpty()) { // top left
				total++;
			}
			if (!board.getSpotAt(i, height - 1).isEmpty()) { // top
				total++;
			}
			if (!board.getSpotAt(i + 1, height - 1).isEmpty()) { // top right
				total++;
			}
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors_RightEdge_OfBoard(int i, int j) {
			// only check top center, top left,left,bottom left, bottom center
			
			int total = 0;
			if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
				total++;
			}	
			if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
				total++;
			}
			if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(i - 1, j + 1).isEmpty()) { // bottom left
				total++;
			}
			if (!board.getSpotAt(i, j + 1).isEmpty()) { // bottom center
				total++;
			}	
			// top right, right, bottom right
			if (!board.getSpotAt(0, j - 1).isEmpty()) { // top right
				total++;
			}	
			if (!board.getSpotAt(0, j).isEmpty()) { // right
				total++;
			}	
			if (!board.getSpotAt(0, j + 1).isEmpty()) { // bottom right
				total++;
			}	
			return total;
		}
		
		public int TORUSMODE_checkNumberNeighbors__BottomEdge_OfBoard(int i, int j) {
			// only check left,top left,top center,top right, right
			
			int total = 0;
			if (!board.getSpotAt(i - 1, j).isEmpty()) { // left
				total++;
			}
			if (!board.getSpotAt(i - 1, j - 1).isEmpty()) { // top left
				total++;
			}
			if (!board.getSpotAt(i, j - 1).isEmpty()) { // top center
				total++;
			}	
			if (!board.getSpotAt(i + 1, j - 1).isEmpty()) { // top right
				total++;
			}	
			if (!board.getSpotAt(i + 1, j).isEmpty()) { // right
				total++;
			}
			if (!board.getSpotAt(i - 1, 0).isEmpty()) { // bottom left
				total++;
			}
			if (!board.getSpotAt(i, 0).isEmpty()) { // bottom center
				total++;
			}
			if (!board.getSpotAt(i + 1, 0).isEmpty()) { // bottom right
				total++;
			}
			return total;
	}
		
		// for the START/STOP button for ADVANCED MODE
		public void automaticallyRun() {
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			        while(automaticRunStarted){
			           try {
			               Thread.sleep(200);
			           } catch (InterruptedException e) {
			               e.printStackTrace();
			           }
			           nextTurn();
			           System.out.println("nextTurn");
			       }
			    }
			 }).start();
		}
	
}