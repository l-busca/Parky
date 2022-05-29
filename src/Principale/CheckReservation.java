package Principale;

import java.util.Timer;
import java.util.TimerTask;

public class CheckReservation {
	private Timer timer;
	
	//Vérifie tout ce qu'il faut dans la base de donnée
	public CheckReservation() {
		
	}
	
	public void init() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
		  @Override
		  public void run() {
		    //Check les trucs de la bdd
		  }
		}, 0, 2000);//chaque 2 secondes

		
	}
	
	public void stop() {
		timer.cancel();//arrete le check
	}
}
