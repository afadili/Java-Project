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

//classe CheckAction : elle est necessaire � la cr�ation du bouton, qui effectue cette action lorsqu'on clique dessus
//il h�rite de la classe AbstractAction, qui h�rite elle-m�me de la classe Action, c'est un objet Action qu'il faut donner en param�tre au constructeur de JButton
//Notre action constitue � verifier que les images selectionn�es correspondent bien aux images � trouver
class CheckAction extends AbstractAction {
	//Pour cela on a besoin de la liste des images � trouver
	ArrayList<String> specificImages;
	//Et de la liste des images selectionn�es.
	ArrayList<String> selectedImages;
	//On donne aussi la frame � l'action, pour qu'elle soit capable de la fermer.
	JFrame frame;
	
	//Constructeur de CheckAction : on lui donne la fen�tre et les listes d'images selectionn�es et � trouver
	CheckAction(JFrame frame, ArrayList<String> specificImages, ArrayList<String> selectedImages){
		this.specificImages = specificImages;
		this.selectedImages = selectedImages;
		this.frame = frame;
	}
	
	//Pour faire un h�ritage sur Action il faut red�finir la m�thode actionPerformed
	@Override
	public void actionPerformed(ActionEvent e) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Pour r�aliser notre action, tout d'abord on trie les 2 listes pour pouvoir les comparer plus aisement
				Collections.sort(specificImages);
				Collections.sort(selectedImages);
				
				//Comparaison des listes en utilisant la m�thode equals de ArrayList
				if (specificImages.equals(selectedImages)) {
					//Si c'est r�ussi : ouverture d'une fen�tre affichant le message de succ�s
					JOptionPane.showMessageDialog(null,
						    "Vous avez r�alis� le capcha avec succ�s !",
						    " Capcha",
						    JOptionPane.WARNING_MESSAGE);
					//puis fermeture de la fenetre capcha
					frame.setVisible(false);
					return;
				}
				else {
					//Si c'est rat� : ouverture d'une fen�tre affichant le message d'echec, et invitant � r�aliser le nouveau captcha
					JOptionPane.showMessageDialog(null,
							"FAUX ! Veuillez r�essayer de r�soudre un nouveau capcha",
						    " Capcha",
						    JOptionPane.WARNING_MESSAGE);
					//fermeture de la fen�tre du capcha actuel
					frame.setVisible(false);
					//on vide a liste des images selectionn�es /!\ IMPORTANT sinon, une fois la fen�tre ferm�e, on ne peut plus cliquer sur les images pour les deselectionner
					selectedImages.clear();
					//on cr�e un nouveau capcha, plus difficile � r�soudre
					Capcha.harderCapcha();
					return;
				}
			}
		});
	}

}
