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


//La classe capcha permet de cr�er l'interface graphique du capcha
public class Capcha {
	//La fen�tre contenant les images le texte d'instruction et le bouton
	JFrame frame;
	//liste des images selectinn�es (on stoque le chemin relatif � partir du package fr.upem.captcha, sous forme de String)
	private static ArrayList<String> selectedImages = new ArrayList<String>();
	
	//Le constructeur de capcha, il prend en compte la cat�gorie d'images (cette cat�gorie inclue le niveau de difficult�)
	Capcha(Capchable category) {
		
		System.out.println(Main.nbImageMin+" image � trouver minimum dans la cat�gorie "+category.toString());
		
		//Initialisation de la fen�tre
		frame = new JFrame("Captcha");
		
		GridLayout layout = new GridLayout(4,3);  // Cr�ation d'un layout de type Grille avec 4 lignes et 3 colonnes
		
		frame.setLayout(layout);  // affection du layout dans la fen�tre.
		frame.setSize(1024, 768); // d�finition de la taille
		frame.setResizable(false);  // On d�finit la fen�tre comme non redimentionnable
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fen�tre on quitte le programme.
		
		//Pour utiliser des images il faut instancier la classe de leur cat�gorie, ici les images "basiques" : celles qui ne doivent pas �tre selectionn�es
		BasicImage images = new BasicImage();
		
		//g�n�ration al�atoire du nombre d'images � trouver : entre 1 et 4 sachant que 1 est la variable statique nbImageMin, qui augmante si on se trompe dans la r�solution du capcha
		int nbSpecificImages = ThreadLocalRandom.current().nextInt(Main.nbImageMin, 5);
		
		//On cr�e les listes d'images � trouver : specificImages
		ArrayList<String> specificImages = category.getRandomPhotosURL(nbSpecificImages);
		//Et les autres : bacisImages pour arriver � un total de 9 images.
		ArrayList<String> basicImages = images.getRandomPhotosURL(9-nbSpecificImages);
		
		//ici on remplit les 9 premieres cases de notre fen�tre par les photos contenues dans les listes ci-dessus
		//de mani�re al�atoire
		try {
			fillFrameRandomly(frame, specificImages, basicImages);
		} catch (IOException e) {
			//En pratique, la m�thode fillFrameRandomly ne renverra pas d'exception car des tests v�rifient qu'on ne d�passe pas la taille des listes d'images utilis�es pour remplir la fen�tre
			e.printStackTrace();
		}
		
		//texte d'instruction
		//la cat�gorie d'image est affich�e dynamiquement grace � la m�thode toString(), red�finie pour les diff�rentes cat�gories d'images
		frame.add(new JTextArea("Veuillez selectionner les images \nmontrant des " + Main.currentCategory.toString() + "\npuis cliquez sur le bouton ci-contre."));

		//bouton OK : on cr�e d'abord l'action qu'il contient puis on cr�e le bouton et on l'ajoute � la fen�tre 
		CheckAction check = new CheckAction(frame, specificImages, selectedImages);
		JButton okButton = new JButton(check);
		frame.add(okButton);
		
		//Une fois le capcha construit, on l'affiche.
		frame.setVisible(true);
	}
	
	//M�thode permettant de g�n�rer un capcha plus difficile que le pr�c�dent.
	//l'�tait du pr�c�dent capcha �tant indiqu� par les variables statiques contenues dans le Main : nbImageMin & difficulty
	static void harderCapcha() {
		//1 chance sur 2 : augmanter le nombre d'image � trouver ou augmanter la difficult�
		switch(ThreadLocalRandom.current().nextInt(2)) {
			case 0:
				//On incr�mente la variable statique nbImageMin
				if (Main.nbImageMin<4) {
					Main.nbImageMin++;
				}
				//puis on lance (on cr�e) un nouveau capcha
				Capcha capcha0 = new Capcha(Main.currentCategory);
				break;
			
			case 1:
				//on incr�mente le niveau de difficult�
				if (Main.difficulty < Main.categoriesByDifficulty.size()-1) {
					Main.difficulty++;
				}
				//puis on selectionne une cat�gorie de mani�re al�atoire parmis la liste de cat�gories de difficult� associ�e au niveau de difficult�
				ArrayList<Capchable> categories = Main.categoriesByDifficulty.get(Main.difficulty);
				Main.currentCategory = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
				//On cr�eun nouveau captcha avec la nouvelle cat�gorie
				Capcha capcha1 = new Capcha(Main.currentCategory);
				break;
				
			default:
				break;
		}
	}
	
	//Cette m�thode permet de remplir la fen�tre avec les 9 images, en r�partissant les images � trouver et les autres de mani�re al�atoire.
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
	
	//Cette m�thode permet de cr�er un objet (image) clickable, quand on click, l'image est selection�e / deselectionn�e, alors son chemin relatif est stock�e dans la liste selectedImages
	static JLabel createLabelImage(String imageLocation) throws IOException{
		File f = new File(imageLocation);
		
		
		BufferedImage img = ImageIO.read(f); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // cr�er le composant pour ajouter l'image dans la fen�tre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'�venement de souris
			private boolean isSelected = false;
			
			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous int�resse c'est lorsqu'on clique sur une image, il y a donc des choses � faire ici
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
