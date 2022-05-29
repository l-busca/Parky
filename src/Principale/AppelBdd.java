package Principale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fonctions.Fonctions;


public class AppelBdd {
	
	//Il faut mettre ICI vos identifiants de BDD mySQl
	public static String username = "root";
	public static String password = "";
	public static String url = "jdbc:mysql://localhost:3306/parky";
	
	public static ArrayList<Integer> getIdBorneReservationDispo(String date, int minutes) throws ClassNotFoundException {
		Connection con = null;
		ArrayList<Integer> bornesId = new ArrayList<Integer>();
	    try {
	    //pour regarder si la library est importée
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);
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
	
	//Requete creant une reservation dans la BDD
	public static void createReservation(int idClient, String plaque, int idBorne, String date, int temps) throws ClassNotFoundException {
			
			Connection con = null;
			int idPossede = getPossede(idClient, plaque);
			boolean res = false;
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
		}
	
	//Requete retournant l'identifiant d'une possession (association entre un clien et un véhicule)
	public static int getPossede(int idClient, String plaque) throws ClassNotFoundException {
		//pourrait return le max(id) pour donner l'id du client à garder
		Connection con = null;
		boolean res = false;

	    // on estime que y'a qu'une paire client/vehicule actif si il en fait plein de passagé ben elles sont inactifs direct à la fin, donc on retourne qu'un ID et pas une liste d'id du while rs
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

	
	///Fonction retournant le dernier client ajouté à la Bdd
	public static Client getLastClient() throws ClassNotFoundException {
		Connection con = null;
		boolean res = false;
	    Client c = null;

	    try {
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
	
	//Retourne l'id de la derniere reservation ajouté
	public static int getLastResId() throws ClassNotFoundException {
		Connection con = null;
		int res = 0;

	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      String query = "SELECT id from reservation ORDER BY id DESC LIMIT 1;";
	      try (Statement stmt = con.createStatement()) {
	    	  ResultSet rs = stmt.executeQuery(query);

			  while (rs.next()) {
				  res = rs.getInt("id");

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

	//Retourne le client correspondant aux identifiants
	public static Client connexion(int id, String mdp) throws ClassNotFoundException {
		Connection con = null;
		boolean res = false;

	    Client c = null;

	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);
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
	
	//Fonction creant un compte sur la bdd
	public static boolean createAccount(String nom, String prenom, String adresse, String numero, String carte, String mdp) throws ClassNotFoundException {
		//pourrait return le max(id) pour donner l'id du client à garder
		Connection con = null;
		boolean res = false;

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

   //Fonction retournant les plaques d'un client
    public static ArrayList<String> getPlaques(int clientId) throws ClassNotFoundException {
        Connection con = null;
        boolean res = false;
        ArrayList<String> plaques = new ArrayList<>();


        try {
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


    //Change l'état d'une borne sur la bdd
    public static void changerEtatBorne(String etat,int borneId) throws ClassNotFoundException{
        Connection con = null;
        boolean res = false;
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
    
    //Change l'etat d'une reservation sur la bdd
    public static void changerEtatReservation(String etat, int reservationId) throws ClassNotFoundException{
        Connection con = null;
        boolean res = false;
        ArrayList<String> plaques = new ArrayList<>();


        try {
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
    
    //Recupere l'id d'une borne sur la BDD
    public static Borne getBorne(int borneId) throws ClassNotFoundException {
        Connection con = null;
        boolean res = false;
        Borne borne = null;

        try {
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

    
    //Retourne un objet reservation selon un id de reservation
    public static Reservation getReservation(int reservationId) throws ClassNotFoundException {
        Connection con = null;
        Reservation res=null;
        try {
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
    
    //Retourne toute les reservations
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

    //Trouve une rservation selon une plaque
    public static Reservation trouverReservation(String plaque) throws ClassNotFoundException {
        Connection con = null;
        Reservation res=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            //Prendra la derniere reservation à corriger
            String query = "SELECT r.* FROM reservation r inner join possede p on r.possession=p.id inner join vehicule v on v.plaque=p.vehicule WHERE plaque =\""+plaque+"\"";
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
    
    //Ajoute une plaque à un client à la bdd
    public static void AjoutPlaque(int clientId,String plaqueId,int temp) throws ClassNotFoundException {
        Connection con = null;
        try {
            //pour regarder si la library est importée je crois
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            String ajoutVehicule = "INSERT INTO `vehicule`(`plaque`) VALUES (\""+plaqueId+"\")";

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
