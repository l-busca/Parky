package Principale;

public class Borne {
    private int id;
    private Etat etat;
	//reserv�/disponible selon l'heure d'utilisation voulu

    public Borne(int id, Etat etat){
        this.id=id;
        this.etat=etat;
    }
	enum Etat {
	    occupe,
	    disponible,
	    reserve,
	    indisponible;
    }

    @Override
    public String toString() {
        return "Borne{" +
                "id=" + id +
                ", etat=" + etat +
                '}';
    }
}
