package fr.upem.captcha;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.upem.captcha.images.panneaux.Panneaux;
import fr.upem.captcha.images.panneaux.ronds.Ronds;
import fr.upem.captcha.images.panneaux.triangles.Triangles;

//classe CheckAction : elle est necessaire à la création du bouton, qui effectue cette action lorsqu'on clique dessus
//il hérite de la classe AbstractAction, qui hérite elle-même de la classe Action, c'est un objet Action qu'il faut donner en paramètre au constructeur de JButton
//Notre action constitue à verifier que les images selectionnées correspondent bien aux images à trouver
class CheckAction extends AbstractAction {
	//Pour cela on a besoin de la liste des images à trouver
	ArrayList<String> specificImages;
	//Et de la liste des images selectionnées.
	ArrayList<String> selectedImages;
	//On donne aussi la frame à l'action, pour qu'elle soit capable de la fermer.
	JFrame frame;
	
	//Constructeur de CheckAction : on lui donne la fenêtre et les listes d'images selectionnées et à trouver
	CheckAction(JFrame frame, ArrayList<String> specificImages, ArrayList<String> selectedImages){
		this.specificImages = specificImages;
		this.selectedImages = selectedImages;
		this.frame = frame;
	}
	
	//Pour faire un héritage sur Action il faut redéfinir la méthode actionPerformed
	@Override
	public void actionPerformed(ActionEvent e) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Pour réaliser notre action, tout d'abord on trie les 2 listes pour pouvoir les comparer plus aisement
				Collections.sort(specificImages);
				Collections.sort(selectedImages);
				
				//Comparaison des listes en utilisant la méthode equals de ArrayList
				if (specificImages.equals(selectedImages)) {
					//Si c'est réussi : ouverture d'une fenêtre affichant le message de succès
					JOptionPane.showMessageDialog(null,
						    "Vous avez réalisé le capcha avec succès !",
						    " Capcha",
						    JOptionPane.WARNING_MESSAGE);
					//puis fermeture de la fenetre capcha
					frame.setVisible(false);
					return;
				}
				else {
					//Si c'est raté : ouverture d'une fenêtre affichant le message d'echec, et invitant à réaliser le nouveau captcha
					JOptionPane.showMessageDialog(null,
							"FAUX ! Veuillez réessayer de résoudre un nouveau capcha",
						    " Capcha",
						    JOptionPane.WARNING_MESSAGE);
					//fermeture de la fenêtre du capcha actuel
					frame.setVisible(false);
					//on vide a liste des images selectionnées /!\ IMPORTANT sinon, une fois la fenêtre fermée, on ne peut plus cliquer sur les images pour les deselectionner
					selectedImages.clear();
					//on crée un nouveau capcha, plus difficile à résoudre
					Capcha.harderCapcha();
					return;
				}
			}
		});
	}

}
