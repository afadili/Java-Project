package fr.upem.captcha.images.panneaux;

import java.io.File;
import java.util.ArrayList;

import fr.upem.captcha.images.BasicImage;
import fr.upem.captcha.images.Capchable;

//La classe Panneaux hérite de la classe BasicImage, elle implémente elle aussi l'interface Capchable
public class Panneaux extends BasicImage implements Capchable{
	
	public Panneaux() {}
	
	//La seule différence est qu'on récupère le photos contenues dans le package fr.upem.capcha.images.panneaux
	//cf. architecture des fichiers
	@Override
	public ArrayList<String> getPhotos() {
		ArrayList<String> photos =  new ArrayList<String>();
		
		File folder = new File("src\\fr\\upem\\captcha\\images\\panneaux");
		String[] files = folder.list();
		for(String path : files){
			//Nos photos de panneaux ne sont pas que sous le formet jpeg, ainsi on récupère les fichiers dont le nom se termine par jpeg png ou jpg
			if (path.endsWith(".jpeg")==true || path.endsWith(".png")==true || path.endsWith(".jpg")==true) {
				photos.add("src\\fr\\upem\\captcha\\images\\panneaux\\"+path);
			}	
		}
		return photos;
	}

	//On redéfinit la méthode toString pour pouvoir afficher dynamiquement le message d'instruction du capcha : récupérer les images contenant des "panneaux"
	@Override
	public String toString() {
		return new String("panneaux");
	}
	
	
}
