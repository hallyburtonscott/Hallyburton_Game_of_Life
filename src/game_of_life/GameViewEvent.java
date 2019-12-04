package game_of_life;

import javax.swing.JComboBox;

public abstract class GameViewEvent {

	public boolean isResetEvent() {
		return false;
	}

	public boolean isSpeedEvent() {
		return false;
	}

	public boolean isResizeEvent() {
		return false;
	}

	public boolean isPointEvent() {
		return false;
	}
	
	public boolean isSpotEvent() {
		return false;
	}
	
	public boolean isStartEvent() {
		return false;
	}
	
	public boolean isPauseEvent() {
		return false;
	}

	public boolean isPlayEvent() {
		return false;
	}
	public boolean isSliderEvent() {
		return false;
	}
	public boolean isSpeedSliderEvent() {
		return false;
	}
	public boolean isRandomEvent() {
		return false;
	}
	public boolean isTorusEvent() {
		return false;
	}
	public boolean isConditionEvent() {
		return false;
	}
	public boolean isComboBoxEvent() {
		return false;
	}
}


class SpotEvent extends GameViewEvent{
	private Spot spot;
	
	public SpotEvent(Spot s) {
		spot = s;
		//System.out.println(spot.getBackground());
		//System.out.println("Spot event Class Test");
	}
	
	public Spot getSpot() {
		return spot;
	}
	
	public boolean isSpotEvent() {
		return true;
	}
}

class ResetEvent extends GameViewEvent{
	private SpotBoard board;
	
	public ResetEvent(SpotBoard b) {
		board = b;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isResetEvent() {
		return true;
	}
}

class StartEvent extends GameViewEvent{
	private SpotBoard board;
	
	public StartEvent(SpotBoard b) {
		board = b;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isStartEvent() {
		return true;
	}
}

class PauseEvent extends GameViewEvent{
	private SpotBoard board;
	
	public PauseEvent(SpotBoard b) {
		board = b;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isPauseEvent() {
		return true;
	}
}

class PlayEvent extends GameViewEvent{
	private SpotBoard board;
	
	public PlayEvent(SpotBoard b) {
		board = b;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isPlayEvent() {
		return true;
	}
}

class SliderEvent extends GameViewEvent{
	private int value;
	private SpotBoard board;
	public SliderEvent(int value, SpotBoard board) {
		this.value = value;
		this.board = board;
	}
	
	public int getValue() {
		return value;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isSliderEvent() {
		return true;
	}
}

class SpeedSliderEvent extends GameViewEvent{
	private int value;
	private SpotBoard board;
	public SpeedSliderEvent(int value, SpotBoard board) {
		this.value = value;
		this.board = board;
	}
	
	public int getValue() {
		return value;
	}
	
	public SpotBoard getBoard() {
		return board;
	}
	
	public boolean isSpeedSliderEvent() {
		return true;
	}
}

class RandomEvent extends GameViewEvent{
	public boolean isRandomEvent() {
		return true;
	}
}

class TorusEvent extends GameViewEvent{
	private boolean on;
	
	public TorusEvent(boolean on) {
		this.on = on;
	}
	
	public boolean isTorusEvent() {
		return true;
	}
	
	public boolean getStatus() {
		return this.on;
	}
}

class ConditionEvent extends GameViewEvent {
	public boolean isConditionsEvent() {
		return true;
	}
}

class ComboBoxEvent extends GameViewEvent {
	
	private JComboBox<Integer> box;
	private int value;
	
	public ComboBoxEvent(JComboBox<Integer> box, int value) {
		this.box = box;
		this.value = value;
		
	}
	public boolean isComboBoxEvent(){
		return true;
	}
	
	public int getValue() {
		return value;
	}
	
	
	public JComboBox<Integer> getBox(){
		return box;
	}
	
	
	
	
}
