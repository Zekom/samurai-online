package util.loader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.model.XMLparser.JmeBinaryReader;
import com.jmex.model.XMLparser.Converters.AseToJme;
import com.jmex.model.XMLparser.Converters.FormatConverter;
import com.jmex.model.XMLparser.Converters.MaxToJme;
import com.jmex.model.XMLparser.Converters.Md2ToJme;
import com.jmex.model.XMLparser.Converters.Md3ToJme;
import com.jmex.model.XMLparser.Converters.MilkToJme;
import com.jmex.model.XMLparser.Converters.ObjToJme;

/**
 * Classe permettant de charger un mod�le (physique) depuis le fichier
 * de donn�es (samourai-data) et d'y appliquer une texture
 * Les mod�les, une fois charg�s, sont mis en m�moire en vue d'une �ventuelle
 * r�utilisation
 * @author Arnaud
 *
 */
public abstract class ModelLoader {

	/**
	 * Collection de mod�les ayant �t� charg�s
	 */
	private static Collection modeles = new Vector();
	
	/**
	 * Charge le mod�le physique depuis le fichier de donn�es (samourai-data)
	 * @param node Node auquel est associ� le mod�le de donn�es
	 * @param type
	 * @param nom
	 * @param extension
	 */
	protected static void loadModel(Node node, String type, String nom, String extension){
		// Cas o� le mod�le a d�ja �t� utilis�
		System.out.println("> chargement du mod�le ["+type+"/"+nom+"] : "+isAlreadyUsed(type, nom));
		if(isAlreadyUsed(type, nom)){
			byte[] is = getModelAlreadyUsed(type, nom);
    		JmeBinaryReader jbr = new JmeBinaryReader();
    		jbr.setProperty("bound", "box");
			try {
				jbr.loadBinaryFormat(node, new ByteArrayInputStream(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		// Cas o� le mod�le est charg� pour la premi�re fois
		}else{
			// Chargement du mod�le
			byte[] is = loadModelPath(node, type+"/"+nom+"."+extension);
			// Enregistrement du mod�le dans la collection
			if(is!=null) {
				addModeleLoaded(is, type, nom);
			}
		}
	}
	
	/**
	 * Charge le mod�le physique depuis le fichier de donn�es (samourai-data)
	 * @param node Node auquel est associ� le mod�le de donn�es
	 * @param path Chemin permettant d'acc�der � la donn�es (dans "data/modeles/")
	 */
	public static byte[] loadModelPath(Node node, String path){
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        URL url = ModelLoader.class.getClassLoader().getResource("data/modeles/"+path);
        byte[] inputStream = null;
        try {
    		FormatConverter convert = null;
    		String[] x = path.split("[.]");
    		String extension = x[1];
    		JmeBinaryReader jbr = new JmeBinaryReader();
    		jbr.setProperty("bound", "box");
    		// On utilise le loader appropri� en fonction du type de format
    		if(!extension.equals("jme")){
	    		if(extension.equals("obj")){
	        		convert = new ObjToJme();
	    		}else if(extension.equals("ms3d")){
	        		convert = new MilkToJme();
	    		}else if(extension.equals("3ds")){
	        		convert = new MaxToJme();
	    		}else if(extension.equals("max")){
	        		convert = new MaxToJme();
	    		}else if(extension.equals("ase")){
	        		convert = new AseToJme();
	    		}else if(extension.equals("md2")){
	        		convert = new Md2ToJme();
	    		}else if(extension.equals("ms3d")){
	        		convert = new Md3ToJme();
	    		}else{
	    			System.err.println("Extension "+extension+" inconnue !");
	    			System.exit(-1);
	    		}
	    		convert.convert(url.openStream(), BO);
	    		inputStream = BO.toByteArray();
	            //jbr.setProperty("texurl",ModelLoader.class.getClassLoader().getResource("data/modeles/"));
	    		jbr.loadBinaryFormat(node, new ByteArrayInputStream(inputStream));
    		}else{
    			//Format .JME > non support� pour le moment
    			System.err.println(node);
    			System.err.println(url);
    			System.err.println(url.openStream());
        		jbr.loadBinaryFormat(node, url.openStream());
    		}
		} catch (IOException e) {
			System.err.println("Mod�le non trouv� ! ["+path+"]");
		} catch (NullPointerException npe) {
			System.err.println("Mod�le non trouv� ! ["+path+"]");
		}
		return inputStream;
	}
	
	/**
	 * V�rifie si le mod�le a d�j� �t� charg� une fois
	 * @param type
	 * @param nom
	 * @return
	 */
	private static boolean isAlreadyUsed(String type, String nom){
		for(Iterator it = modeles.iterator(); it.hasNext();){
			ModeleLoaded modeleLoaded = (ModeleLoaded)it.next();
			if(modeleLoaded.nom.equals(type+"/"+nom)) return true;
		}
		return false;
	}
	
	/**
	 * Retourne le mod�le correspondant au type et au nom cherch�s (si mod�le d�j� charg�)
	 * @param type
	 * @param nom
	 * @return
	 */
	private static byte[] getModelAlreadyUsed(String type, String nom){
		for(Iterator it = modeles.iterator(); it.hasNext();){
			ModeleLoaded modeleLoaded = (ModeleLoaded)it.next();
			if(modeleLoaded.nom.equals(type+"/"+nom))
				return modeleLoaded.getIS();
		}
		return null;
	}
	
	/**
	 * Ajoute le mod�le dans la collection des mod�les ayant d�j� �t� charg�s
	 * @param is
	 * @param type
	 * @param nom
	 */
	//private static void addModeleLoaded(Node node, String type, String nom){
	private static void addModeleLoaded(byte[] is, String type, String nom){
		ModeleLoaded modele = new ModeleLoaded(is, type, nom);
		modeles.add(modele);
	}
	
	/**
	 * Applique la texture {type}/{nom}.{extension} sur le node 'node'
	 * @param node Node auquel est appliqu�e la texture
	 * @param display
	 * @param type
	 * @param nom
	 * @param extension
	 */
	protected static void loadTexture(Node node, DisplaySystem display, String type, String nom, String extension){
        loadTexture(node, display, type+"/"+nom+"."+extension);
	}
	
	/**
	 * Applique la texture se trouvant � {path} sur le node 'node'
	 * @param node Node auquel est appliqu�e la texture
	 * @param display
	 * @param path Chemin permettant d'acc�der � la texture (dans "data/modeles/")
	 */
	public static void loadTexture(Node node, DisplaySystem display, String path){
		System.out.println("Chargement de la texture : " + path);
		TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        try{
        	ts.setTexture(
	            TextureManager.loadTexture(
	            ModelLoader.class.getClassLoader().getResource(
	            "data/modeles/"+path),
	            Texture.MM_LINEAR,
	            Texture.FM_LINEAR));

		}catch(NullPointerException e){
			System.err.println("Texture non charg�e ! ("+path+")");
		}
        node.setRenderState(ts);
	}
	
	
	/**
	 * Classe regroupant les informations utiles � un mod�le d�j� charg�.
	 * Permet de r�cup�rer un clone du mod�le utilis�.
	 * @author Arnaud
	 *
	 */
	private static class ModeleLoaded {
		
		private byte[] inputStream;
		
		public String nom;
		
		public ModeleLoaded(byte[] is, String type, String nom){
			//this.node = node;
			inputStream = is;
			this.nom = type+"/"+nom;
		}
		
		public byte[] getIS() {
			return inputStream;
		}
	}
}
