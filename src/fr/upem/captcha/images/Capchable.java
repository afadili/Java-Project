package fr.upem.captcha.images;

import java.util.ArrayList;

//L'interface Capchable, il s'appelait Image au début, mais il y avait confusion avec la classe java.awt.Image utilisée pour créer un JLabel (image clickable)
//Il représente les images susceptibles d'être affichées dans le capcha
public interface Capchable {
	//La méthode getPhotos : permet de récupérer toutes le photos contenues dans le package (sous forme de tableau de String contenant le chemin relatif des images à partir du package fr.upem.capcha
	public ArrayList<String> getPhotos();
	
	//Ma méthode getRandomPhotosUrl(int nb) permet de récuperer de manière aléatoire nb photos parmis les photos contenues dans le package ( elle utilise la méthode getPhotos()
	public ArrayList<String> getRandomPhotosURL(int nb);
	
	
	//On utilise pas de méthode getRandomPhotoUrl, sinon on derait appeler la méthode getPhotos() pour chaque photo qu'on veut choisir.
	/*
	public String getRandomPhotoURL();
	*/
	
	//On utilise pas de méthode isPhotoCorrect : la vérification s'effectue pendant l'action contenue dans le bouton
	//public boolean isPhotoCorrect(String path);
	
}
