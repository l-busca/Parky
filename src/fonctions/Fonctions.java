package fonctions;

import Principale.Reservation;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Fonctions {
	public static Scanner sc = new Scanner(System.in);
	
	//Recupere les entrees Int en evitant les erreurs
	public static int entreeInt() {
		boolean bon = false;
		while (!(bon)) {
			if (sc.hasNextInt()) {
				bon = true;
			} else {
				System.out.println("Mauvaise entree recommencez : ");
				sc.nextLine();
			}
		}
        return Integer.parseInt(sc.next());
	}
	
	
	//Recupere les entrees String en evitant les erreurs
	public static String entreeString() {
		boolean bon = false;
		while (!(bon)) {
			if (sc.hasNextLine()) {
				bon = true;
			} else {
				System.out.println("Mauvaise entree recommencez : ");
				sc.nextLine();
			}
		}
        return sc.next();
	}
	
	//Dit si une date est dans le format valide
	public static boolean dateValide(String date) {
		if(!(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))) {
			System.out.println("Format incorrect");
			return false;
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
        	System.out.println("Date incorrecte");
            return false;
        }
        
    }
	
	//Dit si la date Entre et compris entre la date initial et la date Initial+ le temps
	public static boolean entreDates(String initial, int tempsInitial, String dateEntre) {
		Date dateConv = null;
		Date dateEntreConv = null;
		try {
			dateConv= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(initial);
			dateEntreConv= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateEntre);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar converter = Calendar.getInstance();
		converter.setTime(dateConv);
		Calendar converter2 = Calendar.getInstance();
		converter2.setTime(dateEntreConv);
		boolean res = (converter2.getTimeInMillis() >= converter.getTimeInMillis() && converter2.getTimeInMillis() < converter.getTimeInMillis()+tempsInitial*60*1000);
		return res;
	}
	
	//Dit si la date actuelle est comprise entre la date recup??r??e et la date r??cup??r??e + le temps
	public static boolean entreDatesLocalFromBdd(String dateBdd, int tempsBdd) {
		Date dateConv = null;
		//LocalDate now = LocalDate.now();
		LocalDateTime localDateTime = LocalDateTime.now();
		  
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        long now = zdt.toInstant().toEpochMilli();
  
		try {
			dateConv= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateBdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar converter = Calendar.getInstance();
		converter.setTime(dateConv);
		boolean res = (now >= converter.getTimeInMillis() && now < converter.getTimeInMillis()+tempsBdd*60*1000);
		return res;
	}
	 
	//Ajoute ?? une date (string) le nombre de minutes renseign??
	public static String addMinutestoDate(String date,int minutes) {
		if (!(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}\s[0-9]{2}:[0-9]{2}"))) {
			if (dateValide(date)) {
				date = date+" 00:00";
			} else {
				date = "2000-01-01 00:00";
			}
		}
		Date dateConv = null;
		try {
			dateConv= new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar converter = Calendar.getInstance();
		converter.setTime(dateConv);
		Date dateMilli = new Date(converter.getTimeInMillis()+minutes*60*1000);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateMilli);
	}
	
	
	//Dit si une heure est au format valide
    public static boolean heureValide(String heure) {
        if(!(heure.matches("[0-9]{2}:[0-9]{2}"))) {
			System.out.println("Format incorrect");
			return false;
		}
		if (!(Integer.parseInt(heure.split(":")[0])<24 && Integer.parseInt(heure.split(":")[1])<60)){
			System.out.println("Heure invalide");
			return false;
		}
        return true;
    }
	
	//S??curit??
	
	//Protection injection SQL pour les entr??es clavier
	public static String entreeStringSQL() {
		String entree = entreeString();
		while(entree.contains(";")) {
			System.out.println("Charact??re \';\' interdit veuillez ressayer");
			sc.nextLine();
			entree = entreeString();
		}
		sc.nextLine();
		return entree;
	}
	
	//Prend en entr??e uniquement 10 chiffres
	public static String entreeTelephone() {
		String entree = entreeStringSQL();
		while((!(entree.matches("[0-9]{10}"))) && entree.length() != 10) {
			System.out.println("Mauvais format de t??l??phone il faut 10 chiffres, veuillez ressayer");
			sc.nextLine();
			entree = entreeStringSQL();
		}
		sc.nextLine();
		return entree;
	}
	
	//Crypte le mot au format sha256
	public static String sha256(String mot) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(mot.getBytes("UTF-8"));
	        StringBuilder hexString = new StringBuilder();
	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) {
	            	hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}

    
}
