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
	public Client getClient(int id) {

		return null;
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


}
