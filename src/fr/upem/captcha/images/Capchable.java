package fr.upem.captcha.images;

import java.util.ArrayList;

//L'interface Capchable, il s'appelait Image au d�but, mais il y avait confusion avec la classe java.awt.Image utilis�e pour cr�er un JLabel (image clickable)
//Il repr�sente les images susceptibles d'�tre affich�es dans le capcha
public interface Capchable {
	//La m�thode getPhotos : permet de r�cup�rer toutes le photos contenues dans le package (sous forme de tableau de String contenant le chemin relatif des images � partir du package fr.upem.capcha
	public ArrayList<String> getPhotos();
	
	//Ma m�thode getRandomPhotosUrl(int nb) permet de r�cuperer de mani�re al�atoire nb photos parmis les photos contenues dans le package ( elle utilise la m�thode getPhotos()
	public ArrayList<String> getRandomPhotosURL(int nb);
	
	
	//On utilise pas de m�thode getRandomPhotoUrl, sinon on derait appeler la m�thode getPhotos() pour chaque photo qu'on veut choisir.
	/*
	public String getRandomPhotoURL();
	*/
	
	//On utilise pas de m�thode isPhotoCorrect : la v�rification s'effectue pendant l'action contenue dans le bouton
	//public boolean isPhotoCorrect(String path);
	
}
