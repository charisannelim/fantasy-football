package listeners;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ui.ChangeLineup;
import ui.FirstDraft;
import ui.MakePlayer;
import ui.PitchView;

public class DraftListener implements  MouseListener, MouseMotionListener {

	private JPanel screens;
	private Font headerFont;
	private JPanel dest;
	private int id, counter = 0;
	private int[] bigarr, arr;
	private FirstDraft draft;
	
	public DraftListener(int[] bigarr, FirstDraft draft, JPanel dest, JPanel screens, Font headerFont) {
		this.draft = draft;
		this.dest = dest;
		this.headerFont = headerFont;
		this.screens = screens;
		this.bigarr = bigarr;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

		Object sth = e.getSource();
		if (sth instanceof MakePlayer) {
			MakePlayer player = (MakePlayer) e.getSource();
			System.out.println("Player "+ player.getName()); 
			id = Integer.parseInt(player.getName());
		
			//increment team counter
			counter++;
			if (counter > 17) {
				JOptionPane.showMessageDialog(draft, "Too many players selected. Please save and exit.",
					  "Error",JOptionPane.ERROR_MESSAGE);
			}
			System.out.println(counter);
			
			//mark player as taken in array
			for (int i = 0; i < 203; i++) {
				if (player.getID() == bigarr[i]) {
					bigarr[i] = 0;
					break;
				}
			}
			
			player.setVisible(false);
			dest.add(player);
			player.setMaximumSize(new Dimension (150, 100));
			player.setVisible(true);
			player.setEnabled(false);
			//arr[counter-1] = id;
			//System.out.println(arr[counter-1]);
				
	
				if (counter == 17) {
					ButtonListener bl = new ButtonListener(screens);
						
					JButton save = new JButton("SAVE");
					save.setFont(headerFont);
					save.setMaximumSize(new Dimension(100, 35));
					save.setAlignmentX(Component.CENTER_ALIGNMENT);
					save.addActionListener(bl);
					save.setActionCommand("save");
					
					dest.add(Box.createRigidArea(new Dimension(0, 20)));
					dest.add(save);
					
					draft.setArr(arr);
					draft.setDrafted(true);
					//returns an array of the 17 Player IDs
			}
		}
	}		


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}