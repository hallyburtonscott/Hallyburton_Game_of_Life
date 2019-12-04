package game_of_life;

import javax.swing.JFrame;

public class Main {

		public static void main(String[] args) {
			GameModel model = new GameModel(10, 10);
			GameView view = new GameView();
			GameController controller = new GameController(model, view);
			
			JFrame main_frame = new JFrame();
			main_frame.setTitle("Conway's Game of Life");
			main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			main_frame.setContentPane(view);

			main_frame.pack();
			main_frame.setVisible(true);
		}


}
