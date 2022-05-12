package fonctions;

import java.util.Scanner;

public class Fonctions {
	public static Scanner sc = new Scanner(System.in);
	
	public static int entreeInt() {
		boolean bon = false;
		while (!(bon)) {
			if (sc.hasNextInt()) {
				bon = true;
			} else {
				System.out.println("Mauvaise entrée recommencez : ");
				sc.nextLine();
			}
		}
		int res = Integer.parseInt(sc.next());
		return res;
	}
	
	public static String entreeString() {
		boolean bon = false;
		while (!(bon)) {
			if (sc.hasNextLine()) {
				bon = true;
			} else {
				System.out.println("Mauvaise entrée recommencez : ");
				sc.nextLine();
			}
		}
		String res = sc.next();
		return res;
	}
}
