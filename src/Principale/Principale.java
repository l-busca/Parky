package Principale;
import fonctions.*;

//classe principale qui permet de lancer la simulation de la borne
public class Principale {
	public static void main (String args[]) {
		
		//Il faut mettre vos identifiants MYSQL dans appelBdd
		CheckReservation checker = new CheckReservation();
		checker.init();
		System.out.println("1.Borne\n2.Terminal\n");
		int choix = Fonctions.entreeInt();
		switch(choix) {
		case 1:
			//Borne
			Interfaces.accueilBorne();
			break;
		default:
			//Terminal
			if(Interfaces.accueil()) {
				Interfaces.reservationPlaque();
			} else {
				System.out.println("Echec de la connexion");
			}
			
		}
		
		//checker.stop();
		
		

	}
	
	
}
