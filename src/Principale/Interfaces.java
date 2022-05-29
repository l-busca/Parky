package Principale;

import java.util.ArrayList;
import java.util.Iterator;

import fonctions.*;

public class Interfaces {
	
	public static Client clientSession;
	
	
	public static boolean accueil() {
		System.out.println("1. Connexion\n2. Inscription");
		int choix = Fonctions.entreeInt();
		//Switch au cas ou l'accueil ajoute d'autres param�tres � l'avenir
		switch(choix) {
		case 1:
			//Todo connexion
			return connexion(/*param�tres*/);
		case 2:
			//Todo inscription
			return inscription();
		default:
			return false;
		}
		
	}
	
	//
	
	public static void reservationPlaque() {
		//nuance avec 
		System.out.println("Voulez vous reserver une borne ? \n1.Oui\n2.Non");
		int choix = Fonctions.entreeInt();
		if (choix == 1) {
			ArrayList<String> plaques = new ArrayList<String>();
			//verifier si il a des plaques sinon il va rester bloqué la 
			try {
				plaques = AppelBdd.getPlaques(clientSession.getId());
			} catch (Exception e) {
				System.out.println(e);
			}
			//Si il a des plaques
			if (plaques.size() > 0) {
				// si il a des plaques
				afficherPlaques();
				int choixPlaque = Fonctions.entreeInt()-1;
				while (choixPlaque < 0 || choixPlaque >= plaques.size()) {
					System.out.println("Choix invalide ");
					choixPlaque = Fonctions.entreeInt()-1;
				}
				
				reservationHoraire(plaques.get(choixPlaque));
			} else {
				
			}
			
		}
	}
	
	public static void reservationHoraire(String plaque) {
		System.out.println("Plaque "+plaque+" choisie.");
		System.out.println("Quelle date désirez vous reserver ? format yyyy-MM-dd");
		//tant que date pas disponible refaire ou quitter, et qui matche pas la regex et qui est pas da
		String date = Fonctions.entreeStringSQL();
		while (!(Fonctions.dateValide(date))) {
			//refaire ?etc ?
			System.out.println("Veuillez ressayer :");
			date = Fonctions.entreeStringSQL();
		}
		
		System.out.println("Quelle heure désirez vous reserver ? format HH:MM");
		//pareil pour lheure avec cette date, recommencer donc reservationHorraire(plaque) ou refaire ou quitter
		
	}
	
	
	public static void afficherPlaques() {
        ArrayList<String> plaques= new ArrayList<String>();
        try {
           plaques= AppelBdd.getPlaques(clientSession.getId());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<plaques.size();i++){
            System.out.println((i+1)+"."+plaques.get(i));
        }
	}
	
	//Borne
	
	public static void accueilBorne() {
		System.out.println("Bienvenue à Parky !");
		System.out.println("Veuillez indiquer le numero de reservation ou la plaque du véhicule (au format AA-AAA-AA) :");
		String res = Fonctions.entreeStringSQL();
		if(res.matches("[0-9A-Z]{2}-[0-9A-Z]{3}-[0-9A-Z]{2}")) {
			//plaque
			System.out.println("Une plaque");
		}else {
			if (res.matches("[0-9]+")) {
				System.out.println("Un numéro");
				//num reservation
			} else {
				//entrée erreur
				//passagé à refaire :
				System.out.println("Erreur sur l'entree, retour à l'accueil\n");
				accueilBorne();
			}
		}
	}
	
	
	
	//methodes ?

	public static boolean connexion() {
		clientSession = null;
		
		System.out.println("Id : ");
		int id = Fonctions.entreeInt();
		System.out.println("Mdp : ");
		String mdp = Fonctions.entreeStringSQL();
		try {
			clientSession = AppelBdd.connexion(id, mdp);
		} catch (Exception e) {
			System.out.println(e);
		}
		if(clientSession == null) {
			System.out.println("Id ou mot de passe incorrect..\n1.ressayer\n2.Mot de passe oublié\n3.Retour arriere\nSi vous avez oublié votre Id ou votre mot de passe veuillez créer un ticket");
			//TODO 
			//La fonction redirection qui rappel connexion si il ressaye, ou qui rappel accueil
			int choix = Fonctions.entreeInt();
			switch(choix) {
			case 1:
				connexion();
				break;
			case 2:
				System.out.println("Contactez un admin avec un ticket\nRetour...\n");
				accueil();
				break;
			default:
				//prend en compte tous les choix autre que 1 2 et fait le 3 aussi
				accueil();
			}
;		} else {
			System.out.println("Heureux de vous revoir "+clientSession.getPrenom()+" !");
		}
		
		return true;
	}


    public static String ajouterPlaque() {
        String res="";
		if (clientSession != null) {
            System.out.println("Veuillez indiquer le numero de la nouvelle plaque du véhicule (au format AA-AAA-AA) :");
            res = Fonctions.entreeStringSQL();
            if(res.matches("[0-9A-Z]{2}-[0-9A-Z]{3}-[0-9A-Z]{2}")) {
                try {
                    AppelBdd.AjoutPlaque(clientSession.getId(),res,1);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else {
                System.out.println("Erreur sur l'entree : reessayer \n");
                ajouterPlaque();
            }
		} else {
			//erreur connexion requise
		}
		return res;
	}
	
	public static boolean inscription() {
		boolean res = false;
		clientSession = null;
		
		System.out.println("Nom : ");
		String nom = Fonctions.entreeStringSQL();
		System.out.println("Prenom : ");
		String prenom = Fonctions.entreeStringSQL();
		System.out.println("Tel : ");
		String tel = Fonctions.entreeTelephone();
		System.out.println("Mot de passe : ");
		String mdp = Fonctions.entreeStringSQL();
		
		
		String adresse = "adresse de test";
		String carte = "0000 1111 2222 4444";
		//faire tests numero de telephone, nom et prenom valide
		//appel dans appelBdd
		try {
			res = AppelBdd.createAccount(nom, prenom, adresse, tel, carte, mdp);
			clientSession = AppelBdd.getLastClient();
			System.out.println("Bienvenue parmis nous "+clientSession.getPrenom()+" !\nVotre identifiant est le : "+clientSession.getId()+"\nRetenez le bien, vous en aurez besoin pour vous connecter.\n");
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//ajouter option ajouter une plaque : Voulez vous ajouter une plaque oui / non 
		ajouterPlaque();
		
		return res;
	}
}
