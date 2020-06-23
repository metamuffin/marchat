import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AskForWindowReload extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public AskForWindowReload(JSONObject channelList, String channelToJoin, ChatWindow chatWindwo) {
		setType(Type.POPUP);
		setTitle("Reload window");
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 291, 164);
		contentPane = new JPanel();
		contentPane.setBackground(OwnColors.grey_d);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Reload now");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chatWindwo.UpdateChannelList(channelList, channelToJoin);
				setVisible(false);
			}
		});
		btnNewButton.setBounds(27, 84, 96, 23);
		contentPane.add(btnNewButton);
		
		JButton btnReloadLater = new JButton("Reload later");
		btnReloadLater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
			}
		});
		btnReloadLater.setBounds(153, 84, 102, 23);
		contentPane.add(btnReloadLater);
		
		JLabel lblYourChannellistHas = new JLabel("Your channellist has changed,");
		lblYourChannellistHas.setForeground(Color.WHITE);
		lblYourChannellistHas.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourChannellistHas.setBounds(0, 23, 285, 29);
		contentPane.add(lblYourChannellistHas);
		
		JLabel lblYourChannellistHas_1 = new JLabel("You have to reload the window.");
		lblYourChannellistHas_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourChannellistHas_1.setForeground(Color.WHITE);
		lblYourChannellistHas_1.setBounds(10, 44, 265, 29);
		contentPane.add(lblYourChannellistHas_1);
	}
}
