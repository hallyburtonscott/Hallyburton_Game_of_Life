package game_of_life;

public interface GameObserver {

	public void update(GameModel game, Act act);
}
