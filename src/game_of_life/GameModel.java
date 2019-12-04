package game_of_life;

import java.awt.Color;
import java.util.List;

import javax.swing.JComboBox;

import game_of_life.Act.ActType;

import java.util.ArrayList;

public class GameModel {
	
	private SpotBoard _board;

	private int _generation;
	private List<GameObserver> _observers;
	private List<Spot> live_spots = new ArrayList<Spot>();
	private JSpot current_spot;
	private boolean is_torus_mode = false;
	private int _birth;
	private int _survives;
	private int _and;

	public GameModel(int width, int height) {
		_observers = new ArrayList<GameObserver>();
		_board = new JSpotBoard(width, height);
		
		for(Spot s: _board) {
			s.setBackground(Color.LIGHT_GRAY);
		}
		_generation = 0;
		_birth = 3;
		_survives = 2;
		_and = 3;
	}
	
	public SpotBoard getBoard() {
		return _board;
	}
	
	public List<Spot> getLiveSpots() {
		return live_spots;
	}
	
	public Spot getCurrentSpot() {
		return current_spot;
	}
	
	public void spotEval(int x, int y) {
		JSpot spot = (JSpot) _board.getSpotAt(x, y);
		if(spot.isLive()) {
			
			spot.setLife(false);
			live_spots.remove(spot);
		} else {
			spot.setLife(true);
			live_spots.add(spot);
			
		}
		
		spot.swap_color();
		current_spot = spot;
	
		eval(new Act(ActType.SPOTCLICK));
	} 
	public void eval(Act act) {
		switch (act.getType()) {
		case RESET:
			_generation =0;
			for(Spot s: live_spots) {
				s.setLife(false);
				
			}
			live_spots.clear();
			break;
		case ADVANCE:
			_generation++;
			break;
		case START:
			break;
		case PAUSE:
			break;
		case RESIZE:
			_board = new JSpotBoard(act.getValue()+2, act.getValue()+2);
			break;
		case RESPEED:
			break;
		case SPOTCLICK:
			break;
		case RANDOM:
			for(Spot s: _board) {
				
				int x = s.getSpotX();
				int y = s.getSpotY();
				if(x > 0 && y > 0 && x < _board.getSpotWidth() - 1 && y < _board.getSpotHeight() - 1) {
					int rand = (int)( Math.random()* 100);
				
					if((rand % 2) == 0) {
						
						spotEval(x, y);
					}
				}
			}
				
		default:
			break;
			
		}
		notifyObservers(act);
	}
	
	public void addObserver(GameObserver o) {
		_observers.add(o);
	}
	
	public void removeObservers(GameObserver o) {
		_observers.remove(o);
	}
	
	private void notifyObservers(Act act) {
		for(GameObserver o: _observers) {
			o.update(this, act);
		}
	}
	
	public void next_gen() {
		//System.out.println("method test");
		for (Spot s : _board) {
			int x = s.getSpotX();
			int y = s.getSpotY();

			if (x > 0 && y > 0 && x < _board.getSpotWidth() - 1 && y < _board.getSpotHeight() - 1) {
				if(!is_torus_mode) {
					//endpoint logic
					
					int count = 0;
					if (_board.getSpotAt(x + 1, y).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x - 1, y).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x, y + 1).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x, y - 1).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x + 1, y + 1).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x + 1, y - 1).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x - 1, y + 1).isLive()) {
						count++;
					}
					if (_board.getSpotAt(x - 1, y - 1).isLive()) {
						count++;
					}
					s.setSurroundCount(count);
					
				} else {
					//torus mode logic
					int count = 0;
					if (_board.getSpotAt(mod(x + 1), mod(y)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x - 1), mod(y)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x), mod(y + 1)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x), mod(y - 1)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x + 1), mod(y + 1)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x + 1), mod(y - 1)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x - 1), mod(y + 1)).isLive()) {
						count++;
					}
					if (_board.getSpotAt(mod(x - 1), mod(y - 1)).isLive()) {
						count++;
					}
					s.setSurroundCount(count);
					
				}
				
				
			}
			
		}

		for (Spot s : _board) {
			int x = s.getSpotX();
			int y = s.getSpotY();
			if (x > 0 && y > 0 && x < _board.getSpotWidth() - 1 && y < _board.getSpotHeight() - 1) {
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
		_generation++;
		eval(new Act(ActType.ADVANCE));
		
	}

	private int mod(int i) {
		
		int h = _board.getSpotHeight() -1 ;
		
		if(i == h) {
			return 1;
		} else if(i==0) {
			return h-1;
		}
		
		return i;
	}
	public boolean getTorusMode() {
		return is_torus_mode;
	}

	public void setTorusMode(boolean b) {
		is_torus_mode = b;
		
	}

	public void boxEval(JComboBox<Integer> box) {
		if (box.getName() == "birth") {
			_birth = (int) box.getSelectedItem();
		} else if (box.getName() == "survies") {
			_survives = (int) box.getSelectedItem();
		} else if (box.getName() == "and") {
			_and = (int) box.getSelectedItem();
		}
		eval(new Act(ActType.BOX));
	}

	public int getGeneration() {
		
		return _generation;
	}

}
