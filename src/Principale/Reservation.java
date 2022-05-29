package Principale;

import java.time.LocalDateTime;

public class Reservation {
	private int id;
	private boolean termine;
	private Etat etat;
    private Borne borne;
    private boolean possession;
	private double prix;
	private int prolonge;
	private String date;
	private int temps; //minutes


    public Reservation(int id, boolean termine, Etat etat, Borne b, boolean possession, double prix, int prolonge, String date, int temps) {
        this.id = id;
        this.termine = termine;
        this.etat = etat;
        this.borne = b;
        this.possession = possession;
        this.prix = prix;
        this.prolonge = prolonge;
        this.date = date;
        this.temps = temps;
    }

    enum Etat {
	    attente,
	    present,
	    retard,
	    absent,
	    annule
	  }

	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public boolean isTermine() {
		return termine;
	}




	public void setTermine(boolean termine) {
		this.termine = termine;
	}




	public Etat getEtat() {
		return etat;
	}




	public void setEtat(Etat etat) {
		this.etat = etat;
	}




	public double getPrix() {
		return prix;
	}




	public void setPrix(double prix) {
		this.prix = prix;
	}




	public int getProlonge() {
		return prolonge;
	}
	
	public Borne getBorne() {
		return this.borne;
	}




	public void setProlonge(int prolonge) {
		this.prolonge = prolonge;
	}




	public String getDate() {
		return date;
	}








	public void setDate(String date) {
		this.date = date;
	}




	public int getTemps() {
		return temps;
	}




	public void setTemps(int temps) {
		this.temps = temps;
	}

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", termine=" + termine +
                ", etat=" + etat +
                ", borne=" + borne +
                ", possession=" + possession +
                ", prix=" + prix +
                ", prolonge=" + prolonge +
                ", date='" + date + '\'' +
                ", temps=" + temps +
                '}';
    }


//methode retournant la date au format voulu
	
	//methode static convertissant une date à un format pour le set ?
	
	
}
