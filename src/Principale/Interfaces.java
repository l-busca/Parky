package Principale;

import fonctions.Fonctions;

public class Interfaces {
	//plutot interface que accueil
	
	public static boolean accueil() {
		System.out.println("1. Connexion\n2. Inscription");
		int choix = Fonctions.entreeInt();
		//Switch au cas ou l'accueil ajoute d'autres paramètres à l'avenir
		switch(choix) {
		case 1:
			//Todo connexion
			return connexion(/*paramètres*/);
		case 2:
			//Todo inscription
			return inscription(/*paramètres*/);
		default:
			return false;
		}
		
	}
	
	//
	
	public static void reservation() {
		//nuance avec 
		System.out.println("Voulez vous réserver une borne ? \n1.Oui\n2.Non");
		int choix = Fonctions.entreeInt();
		if (choix == 1) {
			
		}
	}
	
	
	
	//methodes ?

	public static boolean connexion() {
		//appel dans appelBdd
		return true;
	}
	
	public static boolean inscription() {
		String nom = Fonctions.entreeString();
		String prenom = Fonctions.entreeString();
		String tel = Fonctions.entreeString();
		//faire tests numero de telephone, nom et prenom valide
		System.out.println(nom+" "+prenom+" "+tel);
		//appel dans appelBdd
		return true;
	}
}
