/*
 * Created on 14 avr. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package client.modele;

import util.loader.ArmeLoader;

/**
 * Classe permettant de cr�er un objet de d�coration en y appliquant son mod�le 3d
 */
public class Arme extends modele.objet.Arme {
	
	private static final long serialVersionUID = 1L;
	
	public Arme(String nom) {
		super(nom);
		//this.lock();
	}
	
	public Arme(Monde monde, String type, String nom) {
		this(nom);
		ArmeLoader.loadModel(this, type);
		ArmeLoader.loadTexture(this, monde.getDisplay(), type);
		//monde.getRootNode().attachChild(this);
	}
	
	public Arme(modele.objet.Arme arme) {
		this(arme.getNom());
		this.id = arme.getId();
		this.setPosition(arme.getPosition());
		this.setDirection(arme.getDirection());
		this.setLocalRotation(arme.getLocalRotation());
		this.setLocalScale(arme.getLocalScale());
		this.setCaracteristiques(arme.getCaracteristiques());
	}
	
	public Arme(Monde monde, modele.objet.Arme arme) {
		this(arme);
		System.out.print("Cr�ation de l'arme : "+ getNom());
		String type = getNom();
		ArmeLoader.loadModel(this, type);
		ArmeLoader.loadTexture(this, monde.getDisplay(), type);
		System.out.println(" effectu�e !");
		//monde.getRootNode().attachChild(this);
	}
	
}
