package a8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LifeWidget extends JPanel implements ActionListener, SpotListener, ChangeListener {

	private JSpotBoard _board; /* SpotBoard playing area. */
	private JLabel _message; /* Label for messages. */
	private boolean _game_won; /* Indicates if games was been won already. */
	private JSlider _slider;
	private JToggleButton torusButton;
	private boolean torus;
	private int lowBirth = 3;
	private int highBirth = 3;
	private int lowSurvive = 2;
	private int highSurvive = 3;
	private int sliderValue = 10;
	private JTextField lowBirthField = new JTextField("3");
	private JTextField highBirthField = new JTextField("3");
	private JTextField lowSurviveField = new JTextField("2");
	private JTextField highSurviveField = new JTextField("3");
	int timeBetweenRuns;
	private JSlider threadSlider;
	JToggleButton startButton;
	private ThreadSzn autoRun;

	public LifeWidget() {

		/* Create SpotBoard and message label. */

		_slider = new JSlider(JSlider.HORIZONTAL, 10, 500, 10);
		_slider.addChangeListener(this);
		_slider.setMajorTickSpacing(35);
		_slider.setMinorTickSpacing(5);
		_slider.setPaintTicks(true);
		_slider.setPaintLabels(true);

		threadSlider = new JSlider(JSlider.VERTICAL, 10, 1000, 10);
		threadSlider.setMajorTickSpacing(90);
		threadSlider.setMinorTickSpacing(10);
		threadSlider.setPaintTicks(true);
		threadSlider.setPaintLabels(true);

		JButton applyButton = new JButton("Apply");
		JButton resetButton = new JButton("Reset");
		JLabel threadLabel = new JLabel("Time between updates. (ms)");
		JPanel threadPanel = new JPanel();
		threadPanel.setLayout(new GridLayout(3, 1));
		startButton = new JToggleButton("Start");
		threadPanel.add(threadLabel);
		threadPanel.add(threadSlider);
		threadPanel.add(startButton);
		threadSlider.addChangeListener(this);
		startButton.addActionListener(this);

		_board = new JSpotBoard(sliderValue, sliderValue);
		_message = new JLabel();

		/* Set layout and place SpotBoard at center. */

		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */

		JPanel thresholdPanel = new JPanel();
		thresholdPanel.setLayout(new GridLayout(5, 2));
		JPanel incrementTorusRandFillPanel = new JPanel();
		incrementTorusRandFillPanel.setLayout(new BorderLayout());
		JPanel clear_message_panel = new JPanel();
		clear_message_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */

		JLabel lowBirth = new JLabel("Lower Birth Threshold (Integers Only)");
		JLabel highBirth = new JLabel("Higher Birth Threshold (Integers Only)");
		JLabel lowSurvive = new JLabel("Lower Survival Threshold (Integers Only)");
		JLabel highSurvive = new JLabel("Higher Survival Threshold (Integers Only)");
		JButton incrementButton = new JButton("One Step");
		torusButton = new JToggleButton("Torus");
		JButton randomFillButton = new JButton("Fill Randomly");
		JButton clear_button = new JButton("Clear");

		resetButton.addActionListener(this);
		incrementButton.addActionListener(this);
		clear_button.addActionListener(this);
		torusButton.addActionListener(this);
		randomFillButton.addActionListener(this);
		lowBirthField.addActionListener(this);
		highBirthField.addActionListener(this);
		lowSurviveField.addActionListener(this);
		highSurviveField.addActionListener(this);
		applyButton.addActionListener(this);

		incrementTorusRandFillPanel.add(incrementButton, BorderLayout.CENTER);
		incrementTorusRandFillPanel.add(torusButton, BorderLayout.NORTH);
		incrementTorusRandFillPanel.add(randomFillButton, BorderLayout.SOUTH);
		clear_message_panel.add(clear_button, BorderLayout.EAST);
		clear_message_panel.add(_message, BorderLayout.CENTER);
		clear_message_panel.add(_slider, BorderLayout.SOUTH);
		thresholdPanel.add(lowBirth);
		thresholdPanel.add(lowBirth);
		thresholdPanel.add(lowBirthField);
		thresholdPanel.add(highBirth);
		thresholdPanel.add(highBirthField);
		thresholdPanel.add(lowSurvive);
		thresholdPanel.add(lowSurviveField);
		thresholdPanel.add(highSurvive);
		thresholdPanel.add(highSurviveField);
		thresholdPanel.add(applyButton);
		thresholdPanel.add(resetButton);

		/* Add subpanel in south area of layout. */

		add(incrementTorusRandFillPanel, BorderLayout.EAST);
		add(clear_message_panel, BorderLayout.SOUTH);
		add(thresholdPanel, BorderLayout.NORTH);
		add(threadPanel, BorderLayout.WEST);

		/*
		 * Add ourselves as a spot listener for all of the spots on the spot board.
		 */
		_board.addSpotListener(this);

		/* Reset game. */
		resetGame();
	}

	/*
	 * resetGame
	 * 
	 * Resets the game by clearing all the spots on the board, picking a new secret
	 * spot, resetting game status fields, and displaying start message.
	 * 
	 */

	private void resetGame() {
		/*
		 * Clear all spots on board. Uses the fact that SpotBoard implements
		 * Iterable<Spot> to do this in a for-each loop.
		 */

		_board.setVisible(false);
		remove(_board);
		_board = new JSpotBoard(sliderValue, sliderValue);
		add(_board, BorderLayout.CENTER);
		_board.addSpotListener(this);

		for (Spot s : _board) {
			s.setBackground(Color.LIGHT_GRAY);
		}

		_message.setText("Welcome to the Game of Life. Below is the size of the Grid.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Handles reset game button. Simply reset the game. */
		if (e.getActionCommand() == "Clear") {
			resetGame();
		} else if (e.getActionCommand() == "One Step") {
			this.runOneStep();
		} else if (e.getActionCommand() == "Fill Randomly") {
			for (Spot s : _board) {
				if (Math.random() >= 0.5) {
					s.setBackground(Color.BLACK);
				} else {
					s.setBackground(Color.LIGHT_GRAY);
				}
			}
		} else if (e.getActionCommand() == "Torus") {
			if (torusButton.isSelected()) {
				torus = true;
			} else {
				torus = false;
			}
		} else if (e.getActionCommand() == "Start") {
		 	autoRun = new ThreadSzn(this);
			autoRun.start();
			startButton.setText("Stop");
		} else if (e.getActionCommand() == "Stop") {
			autoRun.halt();
			try {
				autoRun.join();
			} catch (InterruptedException s) {
			}
			startButton.setText("Start");
		} else if (e.getActionCommand() == "Apply") {
			lowBirth = Integer.parseInt(lowBirthField.getText());
			highBirth = Integer.parseInt(highBirthField.getText());
			lowSurvive = Integer.parseInt(lowSurviveField.getText());
			highSurvive = Integer.parseInt(highSurviveField.getText());
		} else if (e.getActionCommand() == "Reset") {
			lowBirth = 3;
			highBirth = 3;
			lowSurvive = 2;
			highSurvive = 3;
			lowBirthField.setText("3");
			highBirthField.setText("3");
			lowSurviveField.setText("2");
			highSurviveField.setText("3");

		}
	}

	/*
	 * Implementation of SpotListener below. Implements game logic as responses to
	 * enter/exit/click on spots.
	 */

	public void runOneStep() {
		boolean[][] liveOrDie = new boolean[sliderValue][sliderValue];
		for (Spot s : _board) {
			int liveCount = 0;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (torus) {
						if (i == 0 && j == 0) {
							continue;
						} else if (s.getSpotX() + j < 0 || s.getSpotX() + j >= sliderValue || s.getSpotY() + i < 0
								|| s.getSpotY() + i >= sliderValue) {
							if (s.getSpotX() + j < 0 && s.getSpotY() + i < 0) {
								if (_board.getSpotAt(sliderValue - 1, sliderValue - 1).getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotX() + j >= sliderValue && s.getSpotY() + i >= sliderValue) {
								if (_board.getSpotAt(0, 0).getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotX() + j >= sliderValue && s.getSpotY() + i < 0) {
								if (_board.getSpotAt(0, sliderValue - 1).getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotX() + j < 0 && s.getSpotY() + i >= sliderValue) {
								if (_board.getSpotAt(sliderValue - 1, 0).getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotX() + j < 0) {
								if (_board.getSpotAt(sliderValue - 1, s.getSpotY() + i)
										.getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotX() + j >= sliderValue) {
								if (_board.getSpotAt(0, s.getSpotY() + i).getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotY() + i < 0) {
								if (_board.getSpotAt(s.getSpotX() + j, sliderValue - 1)
										.getBackground() == Color.BLACK) {
									liveCount++;
								}
							} else if (s.getSpotY() + i >= sliderValue) {
								if (_board.getSpotAt(s.getSpotX() + j, 0).getBackground() == Color.BLACK) {
									liveCount++;
								}
							}
						} else if (_board.getSpotAt(s.getSpotX() + j, s.getSpotY() + i)
								.getBackground() == Color.LIGHT_GRAY) {
							continue;
						} else {
							liveCount++;
						}
					} else {
						if (i == 0 && j == 0) {
							continue;
						} else if (s.getSpotX() + j < 0 || s.getSpotX() + j >= sliderValue || s.getSpotY() + i < 0
								|| s.getSpotY() + i >= sliderValue) {
							continue;
						} else if (_board.getSpotAt(s.getSpotX() + j, s.getSpotY() + i)
								.getBackground() == Color.LIGHT_GRAY) {
							continue;
						} else {
							liveCount++;
						}
					}
				}
			}
			if (liveCount < lowSurvive) {
				liveOrDie[s.getSpotX()][s.getSpotY()] = false;
			} else if (liveCount >= lowSurvive && liveCount <= highSurvive) {
				if (s.getBackground() == Color.BLACK) {
					liveOrDie[s.getSpotX()][s.getSpotY()] = true;
				} else if (liveCount >= lowBirth && liveCount <= highBirth) {
					liveOrDie[s.getSpotX()][s.getSpotY()] = true;
				}
			} else if (liveCount > highSurvive) {
				liveOrDie[s.getSpotX()][s.getSpotY()] = false;
			}
		}
		for (int i = 0; i < sliderValue; i++) {
			for (int j = 0; j < sliderValue; j++) {
				if (liveOrDie[j][i] == false) {
					_board.getSpotAt(j, i).setBackground(Color.LIGHT_GRAY);
				} else {
					_board.getSpotAt(j, i).setBackground(Color.BLACK);
				}
			}
		}
	}

	@Override
	public void spotEntered(Spot s) {
		/* Highlight spot if game still going on. */

		if (_game_won) {
			return;
		}
		s.highlightSpot();
	}

	@Override
	public void spotExited(Spot s) {
		/* Unhighlight spot. */

		s.unhighlightSpot();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (source == threadSlider) {
			if (!source.getValueIsAdjusting()) {
				timeBetweenRuns = (int) source.getValue();
			}
		} else {
			if (!source.getValueIsAdjusting()) {
				sliderValue = (int) source.getValue();
				resetGame();
			}
		}
	}

	@Override
	public void spotClicked(Spot spot) {
		if (spot.getBackground() == Color.LIGHT_GRAY) {
			spot.setBackground(Color.BLACK);
		} else {
			spot.setBackground(Color.LIGHT_GRAY);
		}
	}

}