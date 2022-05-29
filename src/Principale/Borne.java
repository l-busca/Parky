package Principale;

public class Borne {
    private final int id;
    private final Etat etat;

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
	
	public int getId() {
		return id;
	}

    @Override
    public String toString() {
        return "Borne{" +
                "id=" + id +
                ", etat=" + etat +
                '}';
    }
}
