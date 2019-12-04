package game_of_life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class GameView extends JPanel implements ActionListener, SpotListener, ChangeListener, ItemListener{

	private JLabel message;
	private JLabel display;
	private JPanel button_panel;
	private JButton reset_button;
	private JButton advance_button;
	private JButton play_button;
	private JButton pause_button;
	private JButton random_button;
	private JRadioButton torus_mode;
	private JComboBox<Integer> birth;
	private JComboBox<Integer> and;
	private JComboBox<Integer> survives;
	private JSlider size_slider;
	private JSlider speed_slider;
	private JSpotBoard board;
	private List<GameViewListener> listeners;
	private int generation;
	private JPanel game_board;
	private List<Spot> live_spots = new ArrayList<Spot>();
	private int _birth;
	private int _survives;
	private int _and;

	public GameView() {
		_birth = 3;
		_survives = 2;
		_and = 3;
		message = new JLabel();
		listeners = new ArrayList<GameViewListener>();
		generation = 0;
		board = new JSpotBoard(10, 10);
		
		game_board = new JPanel();
		game_board.add(board);
		
		
		setLayout(new BorderLayout());
		
		add(board, BorderLayout.CENTER);

		button_panel = new JPanel();
		button_panel.setLayout(new GridLayout(2, 3));
		
		//Creating the subpanel for the buttons and other accesories
		
		display = new JLabel("generation: " + generation + "   ");
		display.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		subpanel.add(display, BorderLayout.NORTH);
		subpanel.add(button_panel, BorderLayout.CENTER);
		add(subpanel, BorderLayout.SOUTH);
		
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		
		reset_button = new JButton("Reset");
		advance_button = new JButton("Advance");
		play_button = new JButton("Play");
		pause_button = new JButton("Pause");
		random_button = new JButton("Random");
		torus_mode = new JRadioButton("Torus mode");
		
		birth = new JComboBox<Integer>();
		and = new JComboBox<Integer>();;
		survives = new JComboBox<Integer>();
		birth.setName("birth");
		and.setName("name");
		survives.setName("survives");

		button_panel.add(reset_button);
		button_panel.add(advance_button);
		button_panel.add(play_button);
		button_panel.add(pause_button);
		button_panel.add(random_button);
		
		
		
		for(int i=0; i<7; i++) {
			birth.addItem(i);
			and.addItem(i);
			survives.addItem(i);
			
		}
		
		birth.setSelectedItem(3);
		survives.setSelectedItem(2);
		and.setSelectedItem(3);
		birth.addItemListener(this);
		and.addItemListener(this);
		survives.addItemListener(this);
		
		JLabel survive_mes = new JLabel("  Survives at.. ");
		JLabel and_mes = new JLabel("  and at..  ");
		JLabel birth_mes = new JLabel(".. and is born at  ");
		JLabel death_mes = new JLabel(" dies otherwise :(");
		JPanel scroll_panel = new JPanel();
		scroll_panel.setLayout(new GridLayout(3, 3));
		scroll_panel.add(survive_mes);
		scroll_panel.add(and_mes);
		scroll_panel.add(birth_mes);
		scroll_panel.add(survives);
		scroll_panel.add(and);
		scroll_panel.add(birth);
		scroll_panel.add(new JLabel("     " ));
		scroll_panel.add(death_mes);
		subpanel.add(scroll_panel, BorderLayout.WEST);
		subpanel.add(torus_mode, BorderLayout.EAST);
		//subpanel.add(birth_rate, BorderLayout.WEST);
		
		
		//Size Slider
		size_slider = new JSlider();
		size_slider.setMaximum(100);
		size_slider.setMinimum(1);
		size_slider.setMajorTickSpacing(5);
		size_slider.setMinorTickSpacing(1);
		size_slider.setPaintTicks(true);
		size_slider.setSnapToTicks(true);
		size_slider.setPaintLabels(true);
		size_slider.setValue(10);
		size_slider.setName("Size");
		//Speed Slider
		speed_slider = new JSlider();
		speed_slider.setMaximum(1000);
		speed_slider.setMinimum(10);
		speed_slider.setMajorTickSpacing(100);
		speed_slider.setMinorTickSpacing(50);
		speed_slider.setPaintTicks(true);
		speed_slider.setPaintLabels(true);
		speed_slider.setSnapToTicks(false);
		speed_slider.setValue(100);
		speed_slider.setName("Speed");
		
		JPanel slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(2, 2));
		slider_panel.add(new JLabel("                                                              SIZE"));
		slider_panel.add(size_slider);
		slider_panel.add(new JLabel("                                                              Speeeeed! (in ms)"));
		slider_panel.add(speed_slider);
		subpanel.add(slider_panel, BorderLayout.SOUTH);
		
		// Adding ourselves as an Action Listener for every button in the panel
		for(Component c: button_panel.getComponents()) {
			JButton b = (JButton) c;
			b.addActionListener(this);
		}
		torus_mode.addActionListener(this);
		size_slider.addChangeListener(this);
		speed_slider.addChangeListener(this);
		board.addSpotListener(this);
		
		//Creates fresh board
		for(Spot s: board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if(x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				s.setBackground(Color.LIGHT_GRAY);
				s.setHighlight(Color.GRAY);
				s.highlightSpot();
				s.setLife(false);
			}
		}
	}
	
	@Override
	public void spotClicked(Spot spot) {
		fireEvent(new SpotEvent(spot));
		
	}
	@Override
	public void spotEntered(Spot spot) {	
	}

	@Override
	public void spotExited(Spot spot) {
		// TODO Auto-generated method stub
	}

	
	public void setGeneration(int g) {
		generation = g;
		display.setText("generation: " + generation + "    ");
		updateUI();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == torus_mode) {
			
			if(torus_mode.isSelected()) {
				fireEvent(new TorusEvent(true));
			} else {
				fireEvent(new TorusEvent(false));
			}
		} else {
			
		JButton button = (JButton) e.getSource();
		
		if(button.getText().equals("Reset")) {
			fireEvent(new ResetEvent(board));
		} else if(button.getText().equals("Advance")) {
			
			fireEvent(new StartEvent(board));
		} else if(button.getText().equals("Pause")) {
			fireEvent(new PauseEvent(board));
		} else if(button.getText().equals("Play")) {
			fireEvent(new PlayEvent(board));
		} else if(button.getText().equals("Random")){
			fireEvent(new RandomEvent());
		} else if(button.getText().contentEquals("Change Conditions")) {
			fireEvent(new ConditionEvent());
		}
		}
		
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		int value = slider.getValue();
		
		if(slider == size_slider) {
			
			fireEvent(new SliderEvent(value, board));
		} else if(slider == speed_slider ) {
			
			fireEvent(new SpeedSliderEvent(slider.getValue(), board));
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		@SuppressWarnings("unchecked")
		JComboBox<Integer> box = (JComboBox<Integer>) e.getSource();
		int value = (int) box.getSelectedItem();
		if(box == birth) {
			fireEvent(new ComboBoxEvent(birth, value));
		} else if(box == survives){
			fireEvent(new ComboBoxEvent(survives, value));
		} else if(box == and) {
			fireEvent(new ComboBoxEvent(and, value));
		}
		
	}
	
	public void addGameViewListener(GameViewListener g){
		listeners.add(g);
		
	}
	public void fireEvent(GameViewEvent e) {
		for (GameViewListener l : listeners) {
			l.handleGameViewEvent(e);
		}
	}
	public void SetDisplay(String s) {
		
	}
	
	public void setSpeed() {
		
	}
	
	public void setSpot(int x, int y, boolean life) {
		Spot spot = board.getSpotAt(x, y);
		if(spot.isLive()) {
			spot.setLife(false);
			live_spots.remove(spot);
		} else {
			spot.setLife(true);
			live_spots.add(spot);
		}
		spot.swap_color();
		updateUI();
		
	}
	
	public void reset() {
		for(Spot s: board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if(x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				s.setBackground(Color.LIGHT_GRAY);
				s.setHighlight(Color.GRAY);
				s.highlightSpot();
				s.setLife(false);
			}
		}
		generation = 0;
		display.setText("generation: " + generation + "    ");
	}
	
	public void setBoard(int w, int h) {
		remove(board);
		
		board = new JSpotBoard(w, h);
		add(board, BorderLayout.CENTER);

		board.addSpotListener(this);
		
		for(Spot s: board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if(x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				s.setBackground(Color.LIGHT_GRAY);
				s.setHighlight(Color.GRAY);
				s.highlightSpot();
				s.setLife(false);
			}
		}
		updateUI();
	}
	
	

	public void setLiveSpots(List<Spot> liveSpots) {


	
	}
	
	public void next_gen() {

		for (Spot s : board) {
			int x = s.getSpotX();
			int y = s.getSpotY();

			if (x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				int count = 0;
				if (board.getSpotAt(x + 1, y).isLive()) {
					count++;
				}
				if (board.getSpotAt(x - 1, y).isLive()) {
					count++;
				}
				if (board.getSpotAt(x, y + 1).isLive()) {
					count++;
				}
				if (board.getSpotAt(x, y - 1).isLive()) {
					count++;
				}
				if (board.getSpotAt(x + 1, y + 1).isLive()) {
					count++;
				}
				if (board.getSpotAt(x + 1, y - 1).isLive()) {
					count++;
				}
				if (board.getSpotAt(x - 1, y + 1).isLive()) {
					count++;
				}
				if (board.getSpotAt(x - 1, y - 1).isLive()) {
					count++;
				}

				s.setSurroundCount(count);
				
			}
		}

		for (Spot s : board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if (x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				if (s.isLive()) {
					if ((s.getSurroundCount() != _survives) && (s.getSurroundCount()) != _and) {
						s.setLife(false);
						s.setBackground(Color.LIGHT_GRAY);
						live_spots.remove(s);
						
					}

				} 
				if (s.getSurroundCount() == _birth) {
					s.setLife(true);
					s.setBackground(Color.YELLOW);
					live_spots.add(s);

				}
			}

		}
	}
	
	public void next_gen_torus() {
		
		for (Spot s : board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			
			
			if (x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				int count = 0;
				if (board.getSpotAt(mod(x + 1), mod(y)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x - 1), mod(y)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x), mod(y + 1)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x), mod(y - 1)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x + 1), mod(y + 1)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x + 1), mod(y - 1)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x - 1), mod(y + 1)).isLive()) {
					count++;
				}
				if (board.getSpotAt(mod(x - 1), mod(y - 1)).isLive()) {
					count++;
				}
				s.setSurroundCount(count);
				
			}
				 
			}
		

		for (Spot s : board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if (x > 0 && y > 0 && x < board.getSpotWidth() - 1 && y < board.getSpotHeight() - 1) {
				if (s.isLive()) {
					if ((s.getSurroundCount() != _survives) && (s.getSurroundCount()) != _and) {
						s.setLife(false);
						s.setBackground(Color.LIGHT_GRAY);
						live_spots.remove(s);
						
					}

				} 
				if (s.getSurroundCount() == _birth) {
					s.setLife(true);
					s.setBackground(Color.YELLOW);
					live_spots.add(s);

				}
			}

		}
	}
	
	private int mod(int i) {
		int h = board.getSpotHeight() -1 ;
		
		if(i == h) {
			return 1;
		}else if(i == 0) {
			return h-1;
		}
		
		return i;
	}

	

	public void boxEval() {
		_birth = (int) birth.getSelectedItem();
		_survives = (int) survives.getSelectedItem();
		_and = (int) and.getSelectedItem();
		
		
	}

	public void freezeActions() {
		size_slider.setEnabled(false);
		speed_slider.setEnabled(false);
		birth.setEnabled(false);
		survives.setEnabled(false);
		and.setEnabled(false);
		torus_mode.setEnabled(false);
		random_button.setEnabled(false);
		advance_button.setEnabled(false);
		reset_button.setEnabled(false);
	}

	public void unfreezeActions() {
		size_slider.setEnabled(true);
		speed_slider.setEnabled(true);
		birth.setEnabled(true);
		survives.setEnabled(true);
		and.setEnabled(true);
		torus_mode.setEnabled(true);
		random_button.setEnabled(true);
		advance_button.setEnabled(true);
		reset_button.setEnabled(true);
		
	}

}
