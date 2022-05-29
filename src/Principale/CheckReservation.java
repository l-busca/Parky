package Principale;

import java.util.ArrayList;
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
		    
		  }
		}, 0, 2000);//chaque 2 secondes

		
	}
	
	public void stop() {
		timer.cancel();//arrete le check
	}
	
	public void checkStartReservations() {
		ArrayList<Reservation> res = new ArrayList<Reservation>();
		try {
			res = AppelBdd.getAllReservations();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
