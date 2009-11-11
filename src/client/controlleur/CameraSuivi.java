package client.controlleur;

import client.modele.Monde;

import com.jme.input.ChaseCamera;

/**
 * Cam�ra de suivi (3�me personne) attach�e au personnage joueur
 * @author Arnaud
 *
 */
public class CameraSuivi extends ChaseCamera {
	
	private boolean isAttached;

	/**
	 * Constructeur de la cam�ra, attachement r�alis� au personnage dans le monde
	 * @param monde
	 */
	public CameraSuivi(Monde monde) {
        super(monde.getCam(), monde.getPersonnage());
        setAttachment(true);
	}
	
	/**
	 * D�finit si la cam�ra doit rester attach�e au personnage
	 * @param attach
	 */
	public void setAttachment(boolean attach){
		isAttached = attach;
		if(isAttached){
			this.setEnableSpring(true);
			this.setEnabledOfAttachedHandlers(true);
			this.setEnabled(true);
		} else {
			this.setEnableSpring(false);
			this.setEnabledOfAttachedHandlers(false);
			this.setEnabled(false);
		}
	}
}
