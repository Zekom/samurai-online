/*
 * Created on 3 avr. 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package modele.objet;



/**
 * @author canonlo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjetConcret extends ObjetAbstrait{
	
	//TODO: Cette classe devrait avoir des caract�ristique et pas ObjetAbstrait
	
	//TODO: Enlever les valeurs par d�faut et les remplacer de mani�re automatis�e
	private int largeurDansInventaire = 1;
	private int hauteurDansInventaire = 2;
	
	public ObjetConcret(){
		super ();
	}
	
	public ObjetConcret(String nom){
		super (nom);
	}
	
	public int getHauteurDansInventaire(){
		return hauteurDansInventaire;
	}
	
	public int getLargeurDansInventaire(){
		return largeurDansInventaire;
	}
	
	public int[] getTailleDansInventaire(){
		int[] result = new int[2];
		result[0] = getLargeurDansInventaire();
		result[1] = getHauteurDansInventaire();
		return result;
	}
	
	public void setTailleDansInventaire(int largeur, int hauteur){
		setLargeurDansInventaire(largeur);
		setHauteurDansInventaire(hauteur);
	}
	
	public void setLargeurDansInventaire(int largeur){
		this.largeurDansInventaire = largeur;
	}
	
	public void setHauteurDansInventaire(int hauteur){
		this.hauteurDansInventaire = hauteur;
	}
}
