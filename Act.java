package game_of_life;


public class Act {
	public enum ActType {SPOTCLICK, RESET, ADVANCE, START, PAUSE, RESIZE, RESPEED, STOP, RANDOM, BOX}
	
	
	private ActType act;
	private int value;
	
	
	public Act(ActType act, int value) {
		this.act = act;
		this.value = value;
	}
	public Act(ActType act) {
		this(act, 0);
	}


	public ActType getType() {
		// TODO Auto-generated method stub
		return act;
	}
	
	public int getValue() {
		return value;
	}
}