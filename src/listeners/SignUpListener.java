package listeners;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.StartScreen;

public class SignUpListener implements ActionListener {

	private JTextField user;
	private JTextField pw;
	private JTextField email;
	private JPanel screens;
	private StartScreen panel;
	private Font textFont;

	public SignUpListener(JPanel screens, Font textFont, StartScreen panel, JTextField user, JTextField pw, JTextField email) {
		this.user = user;
		this.pw = pw;
		this.email = email;
		this.screens = screens;
		this.textFont = textFont;
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		CardLayout cl = (CardLayout) screens.getLayout();
		if (e.getActionCommand() == "signup") {
			
			String user_ = user.getText();
			String pw_ = pw.getText();
			String email_ = email.getText();
			
			//check database for existing usernames
			 if (user_.equals("adam")) {
				 cl.show(screens, "NEW");
				 //store password
				 //store email
			 }
			 else {
				 JLabel label = new JLabel("This username has already been taken...");
				 label.setFont(textFont);
				 JOptionPane.showMessageDialog(panel, label,
						  "Error",JOptionPane.ERROR_MESSAGE);
				}
			
			//consider doing special characters exception
		}
		
	}

}