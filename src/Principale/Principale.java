package Principale;

import fonctions.*;


public class Principale {
	public static void main (String args[]) {
		System.out.println("1.Borne\n2.Terminal\nPour le debug (temporaire)");
		int choix = Fonctions.entreeInt();
		switch(choix) {
		case 1:
			//Borne
			Interfaces.accueilBorne();
			break;
		default:
			//Terminal
			if(Interfaces.accueil()) {
				Interfaces.reservation();
			} else {
				System.out.println("Echec de la connexion");
			}
			
		}
		
		

	}
	
	
}
