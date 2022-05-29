package Principale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fonctions.Fonctions;


public class AppelBdd {
	public static String username = "root";
	public static String password = "";
	public static String url = "jdbc:mysql://localhost:3306/parky";

	//recuperation des infos de la bdd pour connexion etc
    //todo getClient
	public Client getClient(int id) {

		return null;
	}
	
	public static ArrayList<Integer> getIdBorneReservationDispo(String date, int minutes) throws ClassNotFoundException {
		// RETOURNER LA LISTE DES BORNES DISPO ????? plutot
		Connection con = null;
	    // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
		ArrayList<Integer> bornesId = new ArrayList<Integer>();
	    try {
	    	//pour regarder si la library est importée je crois
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);
	      //peut etre faire addminutestodate(date, -15) pour la date de base pour etre sur que y'a pas de truc dans les 15 minutes precedente si on fait ça pas besoin de verifier pour apres du coup (quoique si peut etre ajouter aussi un peu à l'heure de base)
	      //le temps de depart entre les bornes quoi, ça pourrait etre un parametre
	      String query = "SELECT borne.id FROM borne \r\n"
	      		+ "WHERE borne.id NOT IN (SELECT borne.id FROM borne INNER JOIN reservation ON borne.id = reservation.borne WHERE date between \""+date+"\" and \""+Fonctions.addMinutestoDate(date, minutes)+"\");";
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery(query);
		  while (rs.next()) {
			  bornesId.add(rs.getInt("id"));

		  }

	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } finally {
	      try {
	        if (con != null) {
	            con.close();
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
	    }
	    return bornesId;
	}
	
	public static boolean createReservation(int idClient, String plaque, int idBorne, String date, int temps) throws ClassNotFoundException {
			
			Connection con = null;
			int idPossede = getPossede(idClient, plaque);
			boolean res = false;

		    // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense

		    try {
		      Class.forName("com.mysql.cj.jdbc.Driver");
		      con = DriverManager.getConnection(url, username, password);

		      String query = "INSERT INTO `reservation` (`termine`, `etat`, `borne`, `possession`, `prix`, `temps`, `date`, `prolonge`) VALUES ('0', 'attente', "+idBorne+", "+idPossede+", 10, "+temps+", \""+date+"\", '0');";

		      try (Statement stmt = con.createStatement()) {
			     stmt.executeUpdate(query);
			     res = true;
			  } catch (SQLException e) {
			      System.out.println(e);
			  }


		    } catch (SQLException ex) {
		        throw new Error("Error ", ex);
		    } finally {
		      try {
		        if (con != null) {
		            con.close();
		        }
		      } catch (SQLException ex) {
		          System.out.println(ex.getMessage());
		      }
		    }
		    return res;
		}
	
	public static int getPossede(int idClient, String plaque) throws ClassNotFoundException {
		//pourrait return le max(id) pour donner l'id du client à garder
		Connection con = null;
		boolean res = false;

	    // on estime que y'a qu'une paire client/vehicule actif si il en fait plein de passagé ben elles sont inactifs direct à la fin, donc on retourne qu'un ID et pas une liste d'id du while
		int idPossede = 0; //dans la base id commence à 1 donc impossible 0
	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      String query = "SELECT * FROM possede where client = "+idClient+" AND vehicule = \""+plaque+"\" AND actif = 1";
	      
	      try (Statement stmt = con.createStatement()) {
	    	 ResultSet rs = stmt.executeQuery(query);
		     res = true;
		     while (rs.next()) {
				  idPossede = rs.getInt("id");

			 }
		  } catch (SQLException e) {
		      System.out.println(e);
		  }


	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } finally {
	      try {
	        if (con != null) {
	            con.close();
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
	    }
	    return idPossede;
	}

	public static Client getLastClient() throws ClassNotFoundException {
		Connection con = null;
		boolean res = false;
	    // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
	    Client c = null;

	    try {
	    	//pour regarder si la library est importée je crois
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      String query = "SELECT * from client ORDER BY id DESC LIMIT 1;";
	      try (Statement stmt = con.createStatement()) {
	    	  ResultSet rs = stmt.executeQuery(query);

			  while (rs.next()) {
				  int idBdd = rs.getInt("id");
			      String nom = rs.getString("nom");
			      String prenom = rs.getString("prenom");
			      String adresse = rs.getString("adresse");
			      String tel = rs.getString("numero");
			      c = new Client(idBdd, nom, prenom, tel, adresse);

			  }
			  res = true;
		  } catch (SQLException e) {
		      System.out.println(e);
		  }


	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } finally {
	      try {
	        if (con != null) {
	            con.close();
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
	    }
	    return c;
	}

	public static Client connexion(int id, String mdp) throws ClassNotFoundException {
		Connection con = null;
		boolean res = false;

	    // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
	    Client c = null;

	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      //Version + sécurisé à creuser
	      /*
	      String query = "select * from client where id = ? AND mdp = ?";
	      PreparedStatement stmt = con.prepareStatement(query);
	      stmt.setInt(1, id);
	      stmt.setString(2, Fonctions.sha256(mdp));*/
	      //Ancienne version pour essayer la version plus sécurisé
	      String query = "select * from client where id ="+id+" AND mdp = \""+Fonctions.sha256(mdp)+"\"";
	      Statement stmt = con.prepareStatement(query);
    	  ResultSet rs = stmt.executeQuery(query);

		  while (rs.next()) {
			  int idBdd = rs.getInt("id");
		      String nom = rs.getString("nom");
		      String prenom = rs.getString("prenom");
		      String adresse = rs.getString("adresse");
		      String tel = rs.getString("numero");
		      c = new Client(idBdd, nom, prenom, tel, adresse);

		  }
		  res = true;


	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } finally {
	      try {
	        if (con != null) {
	            con.close();
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
	    }
	    return c;
	}

	public static boolean createAccount(String nom, String prenom, String adresse, String numero, String carte, String mdp) throws ClassNotFoundException {
		//pourrait return le max(id) pour donner l'id du client à garder
		Connection con = null;
		boolean res = false;

	    // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense

	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      String query = "INSERT INTO client (nom, prenom, adresse, numero, carte, mdp)\r\n"
	      		+ "VALUES (\""+nom+"\", \""+prenom+"\", \""+adresse+"\", \""+numero+"\",\""+Fonctions.sha256(carte)+"\",\""+Fonctions.sha256(mdp)+"\")";

	      try (Statement stmt = con.createStatement()) {
		     stmt.executeUpdate(query);
		     res = true;
		  } catch (SQLException e) {
		      System.out.println(e);
		  }


	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } finally {
	      try {
	        if (con != null) {
	            con.close();
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
	    }
	    return res;
	}

   //Prends en param
    public static ArrayList<String> getPlaques(int clientId) throws ClassNotFoundException {
        Connection con = null;
        boolean res = false;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        ArrayList<String> plaques = new ArrayList<>();


        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String query = "SELECT v.plaque FROM vehicule v inner join possede p on v.plaque=p.vehicule WHERE client="+clientId;
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    plaques.add(rs.getString("plaque"));
                }
                res = true;
            } catch (SQLException e) {
                System.out.println(e);
            }


        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return plaques;
    }


    public static void changerEtatBorne(String etat,int borneId) throws ClassNotFoundException{
        Connection con = null;
        boolean res = false;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        ArrayList<String> plaques = new ArrayList<>();


        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String query ="UPDATE `borne` SET `etat` = \""+etat+"\"  WHERE id ="+borneId;
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
    public static void changerEtatReservation(String etat, int reservationId) throws ClassNotFoundException{
        Connection con = null;
        boolean res = false;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        ArrayList<String> plaques = new ArrayList<>();


        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String query ="UPDATE `reservation` SET `etat` = \""+etat+"\"  WHERE id ="+reservationId;
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static Borne getBorne(int borneId) throws ClassNotFoundException {
        Connection con = null;
        boolean res = false;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        Borne borne = null;

        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            String query = "SELECT * from borne WHERE id="+borneId;
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String etat = rs.getString("etat");
                    borne = new Borne(id,Borne.Etat.valueOf(etat));
                }

            } catch (SQLException e) {
                System.out.println(e);
            }


        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return borne;
    }


    public static Reservation getReservation(int reservationId) throws ClassNotFoundException {
        Connection con = null;
        Reservation res=null;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM reservation WHERE id="+reservationId;
            try (Statement stmt = con.createStatement()) {
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int idResa = rs.getInt("id");
                    int termine = rs.getInt("termine");
                    String etat = rs.getString("etat");
                    int idborne =  rs.getInt("borne");
                    Borne b=getBorne(idborne);
                    int possession = rs.getInt("possession");
                    double prix = rs.getDouble("prix");
                    int prolonge=rs.getInt("prolonge");
                    int temps = rs.getInt("temps");
                    String date = rs.getString("date");
                    res = new Reservation(idResa, (termine!=0), Reservation.Etat.valueOf(etat),b, (possession!=0), prix, prolonge, date, temps);

                }
            } catch (SQLException e) {
                System.out.println(e);
            }


        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return res;
    }
    
    public static ArrayList<Reservation> getAllReservations() throws ClassNotFoundException {
        Connection con = null;
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM reservation";
            try (Statement stmt = con.createStatement()) {
                stmt.executeQuery(query);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int idResa = rs.getInt("id");
                    int termine = rs.getInt("termine");
                    String etat = rs.getString("etat");
                    int idborne =  rs.getInt("borne");
                    Borne b=getBorne(idborne);
                    int possession = rs.getInt("possession");
                    double prix = rs.getDouble("prix");
                    int prolonge=rs.getInt("prolonge");
                    int temps = rs.getInt("temps");
                    String date = rs.getString("date");
                    reservations.add(new Reservation(idResa, (termine!=0), Reservation.Etat.valueOf(etat),b, (possession!=0), prix, prolonge, date, temps));

                }
            } catch (SQLException e) {
                System.out.println(e);
            }


        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return reservations;
    }

    public static void AjoutPlaque(int clientId,String plaqueId,int temp) throws ClassNotFoundException {
        Connection con = null;
        // pourrait gérer les utilisateurs de la base à voir si on a le temps et ça fait bcp de gérer ça + l'app etc en 1 mois qd meme donc pas obligatoire je pense
        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String ajoutVehicule = "INSERT INTO `vehicule`(`plaque`) VALUES (\""+plaqueId+"\")";

            //requete qui fait le lien entre un vehicule et le client

            String lien = "INSERT INTO `possede`(`client`, `vehicule`, `temporaire`, `actif`) VALUES (\""+clientId+"\",\""+plaqueId+"\","+temp+",0)";

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(ajoutVehicule);
            } catch (SQLException e) {
                System.out.println(e);
            }
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(lien);
            } catch (SQLException e) {
                System.out.println(e);
            }


        } catch (SQLException ex) {
            throw new Error("Error ", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }



}
