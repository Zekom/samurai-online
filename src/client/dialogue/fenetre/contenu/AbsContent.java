package client.dialogue.fenetre.contenu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Fen�tre de contenu par d�faut
 * @author Arnaud
 *
 */
public class AbsContent extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	protected JButton btn_valider;
 	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			btn_valider.doClick();
		}
	}
}
