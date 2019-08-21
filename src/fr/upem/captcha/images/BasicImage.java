package fr.upem.captcha.images;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

//Classe basicImage, elle impl�mente l'interface Capchable, elle repr�sente les images qui n'ont pas vocation � �tre selectionner
public class BasicImage implements Capchable{
	
	//Constructeur : pas d'action en particulier, la classe n'est pas abstrait, ainsi on peut l'instancier et la stoquer dans des listes (cf. liste de cat�gories d'images --> Main.java)
	public BasicImage() {}
	
	//La m�thode getPhotos renvoie une liste des chemins des photos contenues dans le package
	//On r�cup�re tous les noms de fichier finissant par .jpeg dans le package et on stoque le chemin dans une liste
	@Override
	public ArrayList<String> getPhotos() {
		ArrayList<String> photos =  new ArrayList<String>();
		
		File folder = new File("src\\fr\\upem\\captcha\\images");
		String[] files = folder.list();
		for(String path : files){
			if (path.endsWith(".jpeg")==true) {
				photos.add("src\\fr\\upem\\captcha\\images\\"+path);
			}	
		}
		return photos;
	}

	//Pour cette m�thode on r�cup�re toutes les photos contenues dans le package � l'aide de la m�thode getPhotos()
	//puis on choisit al�atoirement des photos dans le package, une photo peut �tre selectionn�e plusieurs fois (Nous avons jug� que ce n'�tait pas probl�matique)
	@Override
	public ArrayList<String> getRandomPhotosURL(int nb) {
		ArrayList<String> randomPhotos =  new ArrayList<String>();
		ArrayList<String> photos =  getPhotos();
		int size = photos.size();
		for (int i=0; i< nb; i++) {
			randomPhotos.add(photos.get(ThreadLocalRandom.current().nextInt(size)));
		}
		return randomPhotos;
	}

}
