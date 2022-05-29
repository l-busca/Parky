package Principale;

import java.util.ArrayList;


import fonctions.*;

public class Interfaces {
	
	public static Client clientSession;

    //interface de l'accueil
	public static boolean accueil() {
		System.out.println("1. Connexion\n2. Inscription");
		int choix = Fonctions.entreeInt();
		//Switch au cas ou l'accueil ajoute d'autres param�tres � l'avenir
		switch(choix) {
		case 1:
			return connexion(/*param�tres*/);
		case 2:
			return inscription();
		default:
			return false;
		}
		
	}

	//interface pour reserver
	public static void reservationPlaque() {
		//nuance avec 
		System.out.println("Voulez vous reserver une borne ? \n1.Oui\n2.Non");
		int choix = Fonctions.entreeInt();
		if (choix == 1) {
			ArrayList<String> plaques = new ArrayList<>();
			//verifier si il a des plaques sinon il va rester bloqué la 
			try {
				plaques = AppelBdd.getPlaques(clientSession.getId());
			} catch (Exception e) {
				System.out.println(e);
			}
			//Si il a des plaques
			if (plaques.size() > 0) {
				// si il a des plaques
				System.out.println("0.Nouvelle plaque");
				afficherPlaques();
				int choixPlaque = Fonctions.entreeInt()-1;
				while (choixPlaque < -1 || choixPlaque >= plaques.size()) {
					System.out.println("Choix invalide ");
					choixPlaque = Fonctions.entreeInt()-1;
				}
                if(choixPlaque==-1){
                    ajouterPlaque();
                }else {
                    reservationHoraire(plaques.get(choixPlaque));
                }
				

			} else {
                System.out.println("0.Nouvelle plaque \n1.Accueil");
                int choixPlaque = Fonctions.entreeInt();
                if(choixPlaque==0)
                    ajouterPlaque();
                else
                    accueil();
			}
			
		}
	}
	
	public static void reservationHoraire(String plaque) {
		System.out.println("Plaque "+plaque+" choisie.");
		System.out.println("Quelle date désirez vous reserver ? format yyyy-MM-dd");
		//tant que date pas disponible refaire ou quitter, et qui matche pas la regex et qui est pas da
		String date = Fonctions.entreeStringSQL();
		while (!(Fonctions.dateValide(date))) {
			//refaire ?etc ? ou donne une date valide puis apres si tu veux reviens en arriere
			System.out.println("Veuillez ressayer : ");
			date = Fonctions.entreeStringSQL();
		}
		
		System.out.println("Quelle heure désirez vous reserver ? format HH:MM");
		//pour heures
		String heure = Fonctions.entreeStringSQL();
		while (!(Fonctions.heureValide(heure))) {
			//refaire ?etc ?
			System.out.println("Veuillez ressayer :");
			heure = Fonctions.entreeStringSQL();
		}
		//pareil pour lheure avec cette date, recommencer donc reservationHorraire(plaque) ou refaire ou quitter

		System.out.println("Combien de temps ? (en minutes) :");
		int temps = Fonctions.entreeInt();
		//temps min max ??
		//peut etre a ajouter dans param dans la bdd
		while (temps < 15 || temps > 600) {
			System.out.println("Doit etre une valeure comprise entre 15 et 600, veuillez ressayer :");
			temps = Fonctions.entreeInt();
		}
		
		//AppelBdd
		//faire appelbdd de la liste des borne dispo, avec l'ajout de la nouvelle fonction a la requette et continuer ça
		ArrayList<Integer> idBorneDispo = null;
		try {
			idBorneDispo = AppelBdd.getIdBorneReservationDispo(date+" "+heure, temps);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (idBorneDispo.size() > 0) {
			try {
				AppelBdd.createReservation(clientSession.getId(), plaque, temps, date+" "+heure, temps);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Aucune borne n'est disponible pour cette date...\n1.Essayer une autre date\n2.Accueil");
			int choix = Fonctions.entreeInt();
			if (choix == 1) reservationHoraire(plaque);
		}
		
	}
	
	//fonction qui affiche les plaques du client connecté à la borne
	public static void afficherPlaques() {
        ArrayList<String> plaques;
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
        Reservation reservation=null;
		System.out.println("Bienvenue à Parky !");
		System.out.println("Veuillez indiquer le numero de reservation ou la plaque du véhicule (au format AA-AAA-AA) :");
		String res = Fonctions.entreeStringSQL();
		if(res.matches("[0-9A-Z]{2}-[0-9A-Z]{3}-[0-9A-Z]{2}")) {
			//plaque
            try {
                reservation=AppelBdd.trouverReservation(res);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (reservation==null){
                System.out.println("La reservation n'existe pas");
                accueilBorne();
            }
        }else {
			if (res.matches("[0-9]+")) {
                try {
                    reservation=AppelBdd.getReservation(Integer.parseInt(res)) ;
                    if(reservation==null){
                        System.out.println("La reservation n'existe pas");
                        accueilBorne();
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
				//entrée erreur
				//passagé à refaire :
				System.out.println("Erreur sur l'entree, retour à l'accueil\n");
				accueilBorne();
			}
		}
        if(reservation!=null){
            if(Fonctions.entreDatesLocalFromBdd(reservation.getDate(),reservation.getTemps())){
                try {
                    AppelBdd.changerEtatReservation("present",reservation.getId());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("avant l'heure c'est pas l'heure après l'heure c'est plus l'heure...");
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
		} else {
			System.out.println("Heureux de vous revoir "+clientSession.getPrenom()+" !");
		}
		
		return true;
	}


    //interface qui permet d'ajouter une plaque d'immatriculation
    public static void ajouterPlaque() {
        String res;
		if (clientSession != null) {
            System.out.println("Veuillez indiquer le numero de la nouvelle plaque du véhicule (au format AA-AAA-AA) :");
            res = Fonctions.entreeStringSQL();
            if(res.matches("[0-9A-Z]{2}-[0-9A-Z]{3}-[0-9A-Z]{2}")) {
                try {
                    AppelBdd.AjoutPlaque(clientSession.getId(),res,1);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                accueil();
            }else {
                System.out.println("Erreur sur l'entree : reessayer \n");
                ajouterPlaque();
            }
		} else {
            System.out.println("Connexion requise");
            accueil();
		}

	}


    //interface pour s'inscrire
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
		
		
		return res;
	}
}
