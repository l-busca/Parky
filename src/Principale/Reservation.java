package Principale;

import java.time.LocalDateTime;

public class Reservation {
	private int id;
	private boolean termine;
	private Etat etat;
	private double tarif;
	private int prolonge;
	private LocalDateTime date;
	private int temps; //minutes
	
	
	public Reservation(int id, boolean termine, Etat etat, double tarif, int prolonge, LocalDateTime date, int temps) {
		this.id = id;
		this.termine = termine;
		this.etat = etat;
		this.tarif = tarif;
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




	public double getTarif() {
		return tarif;
	}




	public void setTarif(double tarif) {
		this.tarif = tarif;
	}




	public int getProlonge() {
		return prolonge;
	}




	public void setProlonge(int prolonge) {
		this.prolonge = prolonge;
	}




	public LocalDateTime getDate() {
		return date;
	}




	public void setDate(LocalDateTime date) {
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
		return "Reservation [id=" + id + ", termine=" + termine + ", etat=" + etat + ", tarif=" + tarif + ", prolonge="
				+ prolonge + ", date=" + date + ", temps=" + temps + "]";
	}
	
	//methode retournant la date au format voulu
	
	//methode static convertissant une date à un format pour le set ?
	
	
}
