package serveur.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Cette classe permet la connexion avec la base de donn�es. Elle est utilis�e uniquement par
 * le serveur qui est le seul � dialoguer avec la base.
 *
 */
public class BddConn {
	public static Connection conn;
	
	// param�tres de connexion
	private final static String SERVEUR = "vds92.sivit.org";
	private final static int PORT = 3306;
	private final static String BASE = "samurai";
	private final static String UTILISATEUR = "samurai";
	private final static String PASS = "bushibushi";
	
	public BddConn(int num_base) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.err.println("Impossible d'utiliser le Driver");
		}
		
		try {
			Properties prop = new Properties();
			prop.put("user",UTILISATEUR);
			prop.put("password",PASS);
			String ACCES = "jdbc:mysql://"+SERVEUR+":"+PORT+"/"+BASE;
			
			switch(num_base){
				case 1 :
					conn = DriverManager.getConnection("jdbc:mysql://localhost/samurai", "samurai", "bushibushi");
					break;
				case 2 :
					conn = DriverManager.getConnection("jdbc:mysql://172.20.0.11/mmorpg", "md", "grosbill");
					break;
				case 3 :
					conn = DriverManager.getConnection(ACCES, prop);
					break;
				default :
					conn = DriverManager.getConnection("jdbc:mysql://localhost/samurai", "samurai", "bushibushi");
					break;
			}
				
			System.out.println("Connect� � la base de donn�es");
		} catch (Exception ex) {
			System.err.println("Impossible de se connecter � la base");
			System.err.println("Veuillez v�rifier les arguments entr�s !");
			System.exit(-1);
			//ex.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		System.out.println("Tentative de connexion � la base de donn�es...");
		System.out.println(">Serveur : "+SERVEUR);
		System.out.println(">Port : "+PORT);
		System.out.println(">Base : "+BASE);
		System.out.println(">Login : "+UTILISATEUR);
		System.out.println(">Pass : "+PASS);
		new BddConn(0);
	}
}
