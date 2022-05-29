package Principale;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fonctions.Fonctions;

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
			  checkStartReservations();
		  }
		}, 0, 2000);//chaque 2 secondes

		
	}
	
	public void stop() {
		timer.cancel();//arrete le check
	}
	
	//Regarde si une reservation est sensée commencer et met la borne en indisponible
	public static void checkStartReservations() {
		ArrayList<Reservation> res = new ArrayList<Reservation>();
		try {
			res = AppelBdd.getAllReservations();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < res.size();i++) {
			if(Fonctions.entreDatesLocalFromBdd(res.get(i).getDate(), res.get(i).getTemps())) {
				try {
					AppelBdd.changerEtatBorne("indisponible", res.get(i).getBorne().getId());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
