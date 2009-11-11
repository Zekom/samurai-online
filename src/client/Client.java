/*
 * Created on 3 avr. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import modele.action.Interaction;

import client.modele.Personnage;
import client.dialogue.Dialogue;
import client.dialogue.fenetre.FenetreDialogue;
import client.dialogue.fenetre.contenu.Connexion;
import client.dialogue.fenetre.contenu.Creation;
import client.dialogue.fenetre.contenu.FenetreAttente;
import client.dialogue.fenetre.contenu.Identification;
import client.modele.Monde;
import client.util.EmailValidation;

import com.jme.util.LoggingSystem;
import communication.ClientGestReceived;
import communication.CommClient;

/**
 * @author jacquema
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Client {
	
	private Monde monde;
	public CommClient commClient;
	public ClientGestReceived clientGestReceived;
	private int id = -1;
	public Personnage personnage;
	public modele.personne.Personnage perso;
	
	// Fen�tres de dialogue
	final FenetreDialogue dialogueServeur = new FenetreDialogue("Samurai Online - [Choix du serveur]", new Connexion(), true);
	final FenetreDialogue dialogueLogin = new FenetreDialogue("Samurai Online - [Identification]", new Identification(), true);
	final FenetreDialogue dialogueCreation = new FenetreDialogue("Samurai Online - [Cr�er un compte]", new Creation(), true);
	FenetreDialogue frame;
	boolean start_game = false;
	public boolean monde_init = false;
	
	/**
	 * Ce constructeur g�re les �tapes de connexion � un jeu par le joueur.
	 * D�finit les actions associ�es � chacune des boutons des fen�tres de chaque �tape.
	 *
	 */
	public Client() {
		System.out.println("Chargement du client ...");
		
		// Affichage de la premi�re fen�tre de dialogue
		dialogueServeur.makeVisible(true);
		
		// Ajout des actions associ�es aux boutons de la fen�tre de connexion
		final Connexion dialogueServeurContenu = (Connexion)dialogueServeur.getContenu();
		dialogueServeurContenu.actionBtnConnect = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionConnexion(dialogueServeurContenu);
			}
		};
		dialogueServeurContenu.actionBtnAide = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String nom = "A propos...";
				String message = "<center><b>Samurai Online</b><br><br>Site internet : <a href='http://samourai.koomy.org'>samourai.koomy.org</a></center><br><u>Cr�ateurs :</u><br>- Paul Camus<br>- Louis-Claude Canon<br>- Jean-Fran�ois Gautier<br>- Mathieu Jacques<br>- Arnaud Leymet";
				new Dialogue(nom, message);
			}
		};
		dialogueServeurContenu.addActions();
		
		// Ajout des actions associ�es aux boutons de la fen�tre de connexion
		final Identification dialogueLoginContenu = (Identification)dialogueLogin.getContenu();
		dialogueLoginContenu.actionBtnLogin = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionLogin(dialogueLoginContenu);
			}
		};
		dialogueLoginContenu.actionBtnCreer = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialogueLogin.makeVisible(false);
				dialogueCreation.makeVisible(true);
			}
		};
		dialogueLoginContenu.addActions();
		
		// Ajout des actions associ�es aux boutons de la fen�tre de cr�ation de compte
		final Creation dialogueCreationContenu = (Creation)dialogueCreation.getContenu();
		dialogueCreationContenu.actionBtnAnnuler = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialogueCreation.makeVisible(false);
				dialogueLogin.makeVisible(true);
			}
		};
		dialogueCreationContenu.actionBtnValider = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionCreerCompte(dialogueCreationContenu);
			}
		};
		dialogueCreationContenu.addActions();
	}
	
	/**
	 * Action associ�e au bouton "Se connecter" de la fen�tre de choix de Serveur de jeu.
	 * V�rifie la connexion au serveur sp�cifi�.
	 * @param dialogueServeurContenu
	 * @throws InterruptedException 
	 */
	private void actionConnexion(Connexion dialogueServeurContenu) {

		dialogueServeur.makeVisible(false);
		
		String serveur = dialogueServeurContenu.tf_serveur.getText();
		int portServeur = Integer.parseInt(dialogueServeurContenu.tf_port.getText());
		int portClient = Integer.parseInt(dialogueServeurContenu.tf_port_cl.getText());
		System.out.println(serveur+" - "+portServeur+" - "+portClient);
		
		// Barre de progression
		FenetreDialogue patience = new FenetreDialogue("Samurai Online", new FenetreAttente("<center><b>V�rification de la pr�sence du serveur</b><br>"+serveur+":"+portServeur+"<br>En attente de r�ponse du serveur...</center>"), false);
		patience.makeVisible(true);
		FenetreAttente attenteContenu = (FenetreAttente)patience.getContenu();
		
		// Connexion au serveur
		commClient = new CommClient(serveur, portServeur, portClient, this);

		// on d�marre l'analyse des paquets entrants
		clientGestReceived = new ClientGestReceived(this, commClient);
		clientGestReceived.start();
		//clientGestReceived.run();
		
		//long t1 = System.currentTimeMillis();
		
		//while(!commClient.estConnecte() && ((System.currentTimeMillis()-t1)<5000));
		
		//TODO: G�rer la r�ponse du serveur
		
	
		if(commClient.estConnecte()){
			attenteContenu.stopAttente("Connexion accept�e !");
			patience.stopAttente();
			//while(patience.isShowing());
			dialogueServeur.makeVisible(false);
			dialogueServeur.dispose();
			dialogueLogin.makeVisible(true);
			
		}else{
			commClient.close();
			attenteContenu.stopAttente("Connexion impossible !");
			patience.stopAttente();
			dialogueServeur.makeVisible(true);
			String nom = "Erreur de connexion !";
			String message = "La connexion au serveur "+dialogueServeurContenu.tf_serveur.getText()+" a �chou� !<br>Cette erreur peut provenir de l'une des raisons suivantes :<ul><li>Un pare-feu bloque l'acc�s au serveur.</li><li>Le port sp�cifi� est incorrect.</li><li>Le serveur de jeu est momentan�ment indisponible.</li></ul>";
			new Dialogue(nom, message);
		}
	}
	
	/**
	 * Action associ�e au bouton "Valider" de la fen�tre d'identification.
	 * V�rifie avec le serveur que l'identification est valide.
	 * @param dialogueLoginContenu
	 */
	private void actionLogin(Identification dialogueLoginContenu){

		String identifiant = dialogueLoginContenu.tf_identifiant.getText();
		String pass = new String(dialogueLoginContenu.tf_password.getPassword());

		commClient.authentifier(identifiant, pass);

		// Barre de progression
		FenetreDialogue patience = new FenetreDialogue("Samurai Online", new FenetreAttente("<center><b>V�rification du mot de passe pour le compte "+identifiant+"</b><br>En attente de r�ponse du serveur...</center>"), false);
		patience.makeVisible(true);
		dialogueLoginContenu.setEnabled(false);
		
		FenetreAttente attenteContenu = (FenetreAttente)patience.getContenu();
		
		//TODO: G�rer l'acceptation de l'identification
		
		//while(idJoueur==-1);
		if(commClient.estAuthentifie()){
			attenteContenu.stopAttente("Identification accept�e !");
			patience.stopAttente();
			dialogueLogin.makeVisible(false);
			// On ferme toutes les fen�tres
			dialogueServeur.dispose();
			dialogueLogin.dispose();
			dialogueCreation.dispose();
			//  ------------------------> Ici on d�marre le jeu !!
			
			//TODO: Ne pas lancer le jeu tant qu'on a pas son perso
			//while(personnage == null) {}
			setStartGame(true);
		}else{
			attenteContenu.stopAttente("Identification refus�e !");
			patience.stopAttente();
			//String nom = "Erreur d'identification !";
			//String message = "L'identification pour "+dialogueLoginContenu.tf_identifiant.getText()+" a �chou� !<br>Cette erreur peut provenir de l'une des raisons suivantes :<ul><li>Vous avez mal saisi votre mot de passe,</li><li>La touche de verrouillage des majuscules est activ�e,</li><li>Le joueur n'existe pas.</li></ul>";
			//new Dialogue(nom, message);
		}
	}
	
	/**
	 * Action associ�e au bouton "Valider" de la fen�tre de cr�ation de compte
	 * @param dialogueCreationContenu
	 */
	private void actionCreerCompte(Creation dialogueCreationContenu){

		String nom = dialogueCreationContenu.tf_nom.getText();
		String pass = new String(dialogueCreationContenu.tf_pass.getPassword());
		String email = dialogueCreationContenu.tf_email.getText();
		boolean sexe = dialogueCreationContenu.cb_sexe.getSelectedIndex()==0;
		
		if(nom.length()<6){
			String nom2 = "Erreur de saisie !";
			String message = "Votre identifiant correspond au nom du personnage que vous incarnez.<br><font color='red'>Ce nom doit �tre de 6 caract�res minimum !</font>";
			new Dialogue(nom2, message);
			
		}else if(pass.length()<6){
			String nom2 = "Erreur de saisie !";
			String message = "Le mot de passe d'un compte est extr�mement important.<br>Il permet en effet de s�curiser l'acc�s et l'utilisation d'un personnage.<br><font color='red'>Ce mot de passe doit �tre de 6 caract��res minimum !</font>";
			new Dialogue(nom2, message);
			
		}else if(!EmailValidation.check(email)){
			String nom2 = "Erreur de saisie !";
			String message = "L'adresse e-mail entr�e est <font color='red'>invalide</font>.<br>Merci de bien vouloir changer celle-ci.";
			new Dialogue(nom2, message);
			
		}else{
			// Barre de progression
			FenetreDialogue patience = new FenetreDialogue("Samurai Online", new FenetreAttente("<center><b>Cr�ation du compte "+nom+"</b><br>En attente de r�ponse du serveur...</center>"), false);
			patience.makeVisible(true);
			dialogueCreation.setEnabled(false);
			
			// Demande de cr�ation effective du compte
			commClient.creerCompte(nom, pass, email, sexe);
			
			//TODO: G�rer l'acceptation de la cr�ation de compte
		}
	}
	
	/**
	 * Lance une instance du jeu.
	 * (Interface cliente graphique)
	 *
	 */
	private void startGame(){
		// Param�trage du log du moteur
        LoggingSystem.getLogger().setLevel(Level.SEVERE);
        
        // Instanciantion du monde
		monde = new Monde(this, commClient);
		monde_init = true;
		
		//commClient.deplacerPersonnage(perso, new Vector3f(5,0,2));
		//perso.seDeplacer(1,1);
		//perso.seDeplacer(1,0);
		
		// Affichage de la fen�tre de s�lection de la r�solution
		monde.setDialogBehaviour(2, Monde.class.getClassLoader().getResource("data/images/splash.png"));

		
		
		// D�marrage du jeu
		monde.start();
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Monde getMonde(){
		return monde;
	}
	
	public Personnage getPersonnage(int id) {
		return monde.getPersonnage(id);
	}
	
	public Interaction getInter(int id) {
		return monde.getInter(id);
	}
	
  	/**
	 * Permet de changer de LookAndFeel si le LookAndFeel par d�faut est le Metal.
	 *
	 */
	private static void resetLookAndFeelIfMetal(){
		//System.err.println(UIManager.getLookAndFeel().getName());
		if(UIManager.getLookAndFeel().getName().equals("Metal"))
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
	}
	
	public void setStartGame(boolean state) {
		start_game=state;
	}
	
	public boolean getStartGame() {
		return start_game;
	}
	
	public static void main(String[] args) {
		resetLookAndFeelIfMetal();
		Client client = new Client();
		while (!client.getStartGame())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		client.startGame();
	}
}
