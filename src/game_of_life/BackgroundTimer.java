package game_of_life;

import javax.swing.SwingUtilities;

//NOTE: This class came originally from KMP's lecture. I edited it to fit my needs for a timer.
//The assignment said to use the SpotBoard and relevant classes, but I wasn't sure if that was just pinned to 
//things related to spotboard. 
//If this is not what was expected, I understand, and you don't have to count it. 
public class BackgroundTimer extends Thread {
	private boolean done;
	private GameController controller;
	private int rate;
	
	
	public BackgroundTimer(GameController controller, int rate) {
		this.controller = controller;
		done = false;
		this.rate = rate;
	}

	public void halt() {
		done = true;
	}
	public boolean isDone() {
		return done;
	}
	
	public void run() {
		while (!done) {
			try {
				Thread.sleep(rate);
			} catch (InterruptedException e) {
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					controller.next_gen();
				}
			});
		}

	}
}