package fr.upem.captcha.images;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

//Classe basicImage, elle implémente l'interface Capchable, elle représente les images qui n'ont pas vocation à être selectionner
public class BasicImage implements Capchable{
	
	//Constructeur : pas d'action en particulier, la classe n'est pas abstrait, ainsi on peut l'instancier et la stoquer dans des listes (cf. liste de catégories d'images --> Main.java)
	public BasicImage() {}
	
	//La méthode getPhotos renvoie une liste des chemins des photos contenues dans le package
	//On récupère tous les noms de fichier finissant par .jpeg dans le package et on stoque le chemin dans une liste
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

	//Pour cette méthode on récupère toutes les photos contenues dans le package à l'aide de la méthode getPhotos()
	//puis on choisit aléatoirement des photos dans le package, une photo peut être selectionnée plusieurs fois (Nous avons jugé que ce n'était pas problématique)
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
