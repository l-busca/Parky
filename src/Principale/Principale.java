package Principale;

import fonctions.Fonctions;


public class Principale {
	public static void main (String args[]) {
		if(Interfaces.accueil()) {
			System.out.println("Bienvenue");
			Interfaces.reservation();
		}
	}
}
