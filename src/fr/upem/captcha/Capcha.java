package fr.upem.captcha;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.images.BasicImage;
import fr.upem.captcha.images.Capchable;


//La classe capcha permet de créer l'interface graphique du capcha
public class Capcha {
	//La fenêtre contenant les images le texte d'instruction et le bouton
	JFrame frame;
	//liste des images selectinnées (on stoque le chemin relatif à partir du package fr.upem.captcha, sous forme de String)
	private static ArrayList<String> selectedImages = new ArrayList<String>();
	
	//Le constructeur de capcha, il prend en compte la catégorie d'images (cette catégorie inclue le niveau de difficulté)
	Capcha(Capchable category) {
		
		System.out.println(Main.nbImageMin+" image à trouver minimum dans la catégorie "+category.toString());
		
		//Initialisation de la fenêtre
		frame = new JFrame("Captcha");
		
		GridLayout layout = new GridLayout(4,3);  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.
		
		//Pour utiliser des images il faut instancier la classe de leur catégorie, ici les images "basiques" : celles qui ne doivent pas être selectionnées
		BasicImage images = new BasicImage();
		
		//génération aléatoire du nombre d'images à trouver : entre 1 et 4 sachant que 1 est la variable statique nbImageMin, qui augmante si on se trompe dans la résolution du capcha
		int nbSpecificImages = ThreadLocalRandom.current().nextInt(Main.nbImageMin, 5);
		
		//On crée les listes d'images à trouver : specificImages
		ArrayList<String> specificImages = category.getRandomPhotosURL(nbSpecificImages);
		//Et les autres : bacisImages pour arriver à un total de 9 images.
		ArrayList<String> basicImages = images.getRandomPhotosURL(9-nbSpecificImages);
		
		//ici on remplit les 9 premieres cases de notre fenêtre par les photos contenues dans les listes ci-dessus
		//de manière aléatoire
		try {
			fillFrameRandomly(frame, specificImages, basicImages);
		} catch (IOException e) {
			//En pratique, la méthode fillFrameRandomly ne renverra pas d'exception car des tests vérifient qu'on ne dépasse pas la taille des listes d'images utilisées pour remplir la fenêtre
			e.printStackTrace();
		}
		
		//texte d'instruction
		//la catégorie d'image est affichée dynamiquement grace à la méthode toString(), redéfinie pour les différentes catégories d'images
		frame.add(new JTextArea("Veuillez selectionner les images \nmontrant des " + Main.currentCategory.toString() + "\npuis cliquez sur le bouton ci-contre."));

		//bouton OK : on crée d'abord l'action qu'il contient puis on crée le bouton et on l'ajoute à la fenêtre 
		CheckAction check = new CheckAction(frame, specificImages, selectedImages);
		JButton okButton = new JButton(check);
		frame.add(okButton);
		
		//Une fois le capcha construit, on l'affiche.
		frame.setVisible(true);
	}
	
	//Méthode permettant de générer un capcha plus difficile que le précédent.
	//l'était du précédent capcha étant indiqué par les variables statiques contenues dans le Main : nbImageMin & difficulty
	static void harderCapcha() {
		//1 chance sur 2 : augmanter le nombre d'image à trouver ou augmanter la difficulté
		switch(ThreadLocalRandom.current().nextInt(2)) {
			case 0:
				//On incrémente la variable statique nbImageMin
				if (Main.nbImageMin<4) {
					Main.nbImageMin++;
				}
				//puis on lance (on crée) un nouveau capcha
				Capcha capcha0 = new Capcha(Main.currentCategory);
				break;
			
			case 1:
				//on incrémente le niveau de difficulté
				if (Main.difficulty < Main.categoriesByDifficulty.size()-1) {
					Main.difficulty++;
				}
				//puis on selectionne une catégorie de manière aléatoire parmis la liste de catégories de difficulté associée au niveau de difficulté
				ArrayList<Capchable> categories = Main.categoriesByDifficulty.get(Main.difficulty);
				Main.currentCategory = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
				//On créeun nouveau captcha avec la nouvelle catégorie
				Capcha capcha1 = new Capcha(Main.currentCategory);
				break;
				
			default:
				break;
		}
	}
	
	//Cette méthode permet de remplir la fenêtre avec les 9 images, en répartissant les images à trouver et les autres de manière aléatoire.
	static void fillFrameRandomly(JFrame frame, ArrayList<String> specificImages, ArrayList<String> basicImages) throws IOException{
		int nbSpecificImg = specificImages.size();
		int specificCount = 0;
		int basicCount = 0;
		for (int i=0; i<9; i++) {
			switch(ThreadLocalRandom.current().nextInt(2)) {
				case 0:
					if (basicCount < 9-nbSpecificImg) {
						frame.add(createLabelImage(basicImages.get(basicCount)));
						basicCount++;
					}
					else {
						frame.add(createLabelImage(specificImages.get(specificCount)));
						specificCount++;
					}
					break;
				case 1:
					if (specificCount < nbSpecificImg) {
						frame.add(createLabelImage(specificImages.get(specificCount)));
						specificCount++;
					}
					else {
						frame.add(createLabelImage(basicImages.get(basicCount)));
						basicCount++;
					}
					break;
				default:
					break;
			}
		}
	}
	
	//Cette méthode permet de créer un objet (image) clickable, quand on click, l'image est selectionée / deselectionnée, alors son chemin relatif est stockée dans la liste selectedImages
	static JLabel createLabelImage(String imageLocation) throws IOException{
		File f = new File(imageLocation);
		
		
		BufferedImage img = ImageIO.read(f); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean isSelected = false;
			
			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(imageLocation);
							//for(String path : selectedImages) {System.out.println(path);}
							//System.out.println();
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(imageLocation);
							//for(String path : selectedImages) {System.out.println(path);}
							//System.out.println();
						}
						
					}
				});
				
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		return label;
	}
}
