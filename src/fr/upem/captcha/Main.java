package fr.upem.captcha;

import java.io.IOException;
import java.util.ArrayList;

import fr.upem.captcha.images.Capchable;
import fr.upem.captcha.images.panneaux.Panneaux;
import fr.upem.captcha.images.panneaux.ronds.Ronds;
import fr.upem.captcha.images.panneaux.triangles.Triangles;

//C'est � partir du main de la classe Main qu'on lance le 1er captcha
public class Main {
	//Nombre d'images minimum � trouver (entre 1 et 4, augmante si on �choue dans la r�solution du capcha)
	static int nbImageMin = 1;
	//niveau de difficult�
	static int difficulty = 0;
	//cat�gorie du capcha affich� a l'ecran : theme + difficult�
	static Capchable currentCategory;
	
	static ArrayList< ArrayList<Capchable>> categoriesByDifficulty = new ArrayList< ArrayList<Capchable>>();
	//Ici c'est les cat�gories type Panneaux par difficult�. 
	//On pourrait cr�er d'autres cat�gories et faire plusieurs listes #nomDeLaCategorie#ByDifficulty
	
	//liste des cat�gories panneaux de m�me niveau de difficult�
	static ArrayList<Capchable> level1 = new ArrayList<Capchable>();
	static ArrayList<Capchable> level2 = new ArrayList<Capchable>();
	//on pourrait imaginer un lvl 3 de difficult� : panneaux Ronds/Triangles Rouges/Bleus/Verts
	
	
	//Dans le main on initialise les listes de cat�gories, 
	//il faut instancier les classes des diff�rentes cat�gories d'images pour pouvoir les utiliser (cf. new Panneaux() ... )
	//Puis on lance un 1er captcha,  le captcha se g�re tout seul par la suite
	public static void main(String[] args) throws IOException {		
		level1.add(new Panneaux());
		
		level2.add(new Ronds());
		level2.add(new Triangles());
		
		categoriesByDifficulty.add(level1);
		categoriesByDifficulty.add(level2);
		
		currentCategory = level1.get(0);
		
		Capcha capcha = new Capcha(currentCategory);
	}
	
}


