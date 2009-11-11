package client.dialogue.fenetre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Fen�tre de dialogue
 * @author Arnaud
 *
 */
public class FenetreDialogue extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	public Timer timer;
	
	/**
	 * Constructeur de base.
	 * Construit une fen�tre sans ni d�finir son contenu ni l'afficher.
	 * @param nom Nom de la fen�tre
	 */
	public FenetreDialogue(String nom){
		super(nom);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon(FenetreDialogue.class.getClassLoader().getResource("data/images/icone_client.png")).getImage());
		setResizable(false);
	}
	
	/**
	 * Constructeur qui renseigne le contenu de la fen�tre.
	 * @param nom Nom de la fen�tre
	 * @param contenu Contenu de la fen�tre
	 * @param exit Ferme l'application lorsque vrai
	 */
	public FenetreDialogue(String nom, JPanel contenu, boolean exit){
		this(nom);
		this.setContentPane(contenu);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * Accesseur retournant le contenu de la fen�tre.
	 * @return Retourne le contenu de la fen�tre.
	 */
	public JPanel getContenu(){
		return (JPanel) getContentPane();
	}
	
	/**
	 * Affiche la fen�tre au centre de l'�cran.
	 */
	public void makeVisible(boolean visiblility){
		if(visiblility){
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}else{
			setVisible(false);
		}
	}
	
	public void stopAttente(/*Client client*/){
		timer = new Timer (2000, this);
		timer.start();
		//ThreadAttente thread = new ThreadAttente(this/*, client*/);
		//thread.run();
	}
	
	/**
	 * Processus de temporisation
	 */
	/*private class ThreadAttente extends Thread {
		
		private final long TEMPS_START = System.currentTimeMillis();
		private final long TEMPS_TOTAL_MS = 3000;
		private FenetreDialogue dialogue;
		//private Client client;
		
		public ThreadAttente(FenetreDialogue dialogue){
			this.dialogue = dialogue;
			//this.client = client;
		}
		
		public void run(){
			while( (System.currentTimeMillis() - TEMPS_START) < TEMPS_TOTAL_MS ){
				//System.out.println("op");
			}
			System.out.println("OP !!!");
			dialogue.dispose();
			//client.notify();
		}
	}*/
	
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
