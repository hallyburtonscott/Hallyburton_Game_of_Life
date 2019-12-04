package a8;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.View;

import a8.Act.ActType;


public class GameController implements GameObserver, GameViewListener {
	private double elapsed;
	private GameModel _model;
	private GameView _view;
	private boolean _start_of_game;

	private List<Spot> live_spots = new ArrayList<Spot>();
	private int rate;
	private TimerTask advance;
	private BackgroundTimer background_timer;
	private boolean isPlaying;
;
	public GameController(GameModel model, GameView view) {
		elapsed  = 0;
		_model = model;
		_view = view;
		_view.addGameViewListener(this);
		_start_of_game = true;
		_model.addObserver(this);
		_start_of_game = true;
		rate = 100;
		background_timer = new BackgroundTimer(this, rate);
		isPlaying =false;
	}

	@Override
	public void handleGameViewEvent(GameViewEvent e) {
		if (e.isSpotEvent()) {
		if(isPlaying) {
				return;
			}
			
			Spot spot;
			SpotEvent event = (SpotEvent) e;
			spot = (JSpot) event.getSpot();
			if(spot.getSpotX()==0 || spot.getSpotY() == 0 || spot.getSpotX() == spot.getBoard().getSpotWidth()-1 || spot.getSpotY() == spot.getBoard().getSpotWidth()-1) {
				return;
			}
			_model.spotEval((spot.getSpotX()), spot.getSpotY()); 
			
		} else if (e.isResetEvent()) {
			
			_model.eval(new Act(ActType.RESET));
			
		} else if (e.isStartEvent()) {
			_model.next_gen();
			
		} else if (e.isPauseEvent()) {
			isPlaying = false;
			background_timer.halt();
			_view.unfreezeActions();
			
		} else if(e.isPlayEvent()) {
			isPlaying = true;
			background_timer = new BackgroundTimer(this, rate);
			background_timer.start();
			_view.freezeActions();
			SpotBoard board;
			PlayEvent event = (PlayEvent) e;
			board = (JSpotBoard) event.getBoard();
		} else if(e.isSliderEvent()) {
			SliderEvent event = (SliderEvent) e;
			int size = event.getValue();
			_model.eval(new Act(ActType.RESIZE, size));
		} else if(e.isSpeedSliderEvent()) {	
			SpeedSliderEvent event = (SpeedSliderEvent) e;
			int size = event.getValue();
			rate = size;
		} else if(e.isRandomEvent()) {
			_model.eval(new Act(ActType.RANDOM));
		} else if(e.isTorusEvent()) {
			TorusEvent event = (TorusEvent) e;
			boolean on = event.getStatus();
			if(event.getStatus()) {
			_model.setTorusMode(true);
			} else {
			_model.setTorusMode(false);
			}
		} else if(e.isComboBoxEvent()) {

			ComboBoxEvent event = (ComboBoxEvent) e;
			_model.boxEval(event.getBox());
		}
	}

	@Override
	public void update(GameModel model, Act act) {

		if(act.getType() == ActType.RESIZE){
			_view.setBoard(model.getBoard().getSpotHeight(), model.getBoard().getSpotWidth());
		} else if(act.getType() == ActType.RESET){
			_view.reset();
		} else if(act.getType() == ActType.SPOTCLICK) {
			JSpot spot = (JSpot) model.getCurrentSpot();
			_view.setSpot(spot.getSpotX(), spot.getSpotY(), spot.isLive());
		} else if(act.getType() == ActType.ADVANCE) {
			if(model.getTorusMode()) {
				_view.next_gen_torus();
				} else {
				_view.next_gen();
			}
			_view.setGeneration(model.getGeneration());
		} else if(act.getType() == ActType.START) {
			
		} else if(act.getType() == ActType.STOP) {
			background_timer.halt();
		} else if(act.getType() == ActType.BOX) {
			_view.boxEval();
		}
		
	}
	public void next_gen() {
		
		_model.next_gen();
	}
	
	
}
