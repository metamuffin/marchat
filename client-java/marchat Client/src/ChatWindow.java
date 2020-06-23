import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window.Type;
import org.json.*;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import java.awt.ScrollPane;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Button;
import javax.swing.JEditorPane;
import javax.swing.DropMode;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;

public class ChatWindow extends JFrame {
	
	public JPanel contentPanel;
	private JLabel lblNoChannel;
	private JEditorPane dtrpnEnterMessage;
	private JButton btnSend_1;
	private Button[] allChannelButtons;
	private JLabel lblCurrentChannel;
	private JButton btnAddUserTo;
	private JTextArea textAreaMessages;
	private JPanel buttonPanel;
	private Button button;
	/**
	 * Launch the application.
	 * @return 
	 */
	public static void startChat(JSONObject channelList) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow(channelList, "");
					frame.setVisible(true);
					Client.chatWindow = frame;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatWindow(JSONObject channelList, String channelToJoin) {

		setResizable(false);
		setTitle("marchat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 720);
		contentPanel = new JPanel();
		contentPanel.setBackground(OwnColors.grey_d);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		JSONArray channels = channelList.getJSONArray("channels");
		System.out.println(channels);
		
<<<<<<< HEAD
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(OwnColors.grey_m);
		buttonPanel.setBounds(0, 0, 262, 691);
		contentPanel.add(buttonPanel);
		buttonPanel.setLayout(null);
=======
		for(int i = 0; i < channels.length(); i++) {
			Button btnNewButton = new Button("#" + channels.getString(i));
			btnNewButton.setBounds(10, 11 + i * 34, 238, 23);
			allChannelButtons[i] = btnNewButton;
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String channelName = btnNewButton.getLabel().substring(1);
					Login.client.SendJoinChannel(channelName);
				}
			});
			
			contentPanel.add(btnNewButton);
		}
>>>>>>> parent of c045a63... added update channellist funktion
		
		button = new Button("New button");
		button.setBounds(84, 10, 70, 22);
		buttonPanel.add(button);
		
		
		UpdateChannelList(channelList, channelToJoin);
		
		dtrpnEnterMessage = new JEditorPane();
		dtrpnEnterMessage.setDropMode(DropMode.INSERT);
		dtrpnEnterMessage.setBounds(285, 646, 0, 0);
		contentPanel.add(dtrpnEnterMessage);
		
		btnSend_1 = new JButton("Send");
		btnSend_1.setBounds(988, 646, 0, 0);
		btnSend_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Login.client.SendMessage(dtrpnEnterMessage.getText());
				dtrpnEnterMessage.setText("");
			}
		});
		contentPanel.add(btnSend_1);
		

		
		lblNoChannel = new JLabel("You have to join a channel before you can write messages!");
		lblNoChannel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNoChannel.setForeground(Color.WHITE);
		lblNoChannel.setBackground(Color.WHITE);
		lblNoChannel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoChannel.setBounds(265, 307, 809, 32);
		contentPanel.add(lblNoChannel);
		
		lblCurrentChannel = new JLabel("");
		lblCurrentChannel.setForeground(Color.WHITE);
		lblCurrentChannel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCurrentChannel.setBounds(272, 11, 519, 23);
		contentPanel.add(lblCurrentChannel);
		
		btnAddUserTo = new JButton("Add user to channel");
		btnAddUserTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChannelUserAddWindow().setVisible(true);;
			}
		});
		btnAddUserTo.setBounds(910, 11, 0, 0);
		contentPanel.add(btnAddUserTo);
		
		textAreaMessages = new JTextArea();
		textAreaMessages.setFont(new Font("Monospaced", Font.PLAIN, 17));
		textAreaMessages.setText("sfthshsthgahahsthsrthsrhsttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		textAreaMessages.setForeground(Color.WHITE);
		textAreaMessages.setBackground(Color.LIGHT_GRAY);
		textAreaMessages.setBounds(265, 0, 0, 0);
		//textAreaMessages.setBounds(265, 52, 809, 561);
		contentPanel.add(textAreaMessages);
		
		if(!channelToJoin.equals("")) {
			JoinChannelUI(channelToJoin);
		}
	}

	
	public void RequestUpdateChannelList(JSONObject channelList, String channelToJoin) { 
		AskForWindowReload ask = new AskForWindowReload(channelList, channelToJoin, this);
		ask.setVisible(true);
	}
	
	public void UpdateChannelList(JSONObject channelList, String channelToJoin) { 
<<<<<<< HEAD
		contentPanel.revalidate();
		contentPanel.repaint();
		JSONArray channels = channelList.getJSONArray("channels");
		System.out.println(channels);

		//buttonPanel.removeAll();

		allChannelButtons = new Button[channels.length()];
		
		for(int i = 0; i < channels.length(); i++) {
			Button btnNewButton = new Button("." + channels.getString(i));
			btnNewButton.setBounds(10, 11 + i * 34, 238, 23);
			allChannelButtons[i] = btnNewButton;
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String channelName = btnNewButton.getLabel().substring(1);
					Login.client.SendJoinChannel(channelName);
				}
			});
			
			buttonPanel.add(btnNewButton);
		}
		
		JButton btnCreateNewChannel = new JButton("Create new channel");
		btnCreateNewChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateChannelWindow.start();
			}
		});
		btnCreateNewChannel.setBounds(52, 657, 154, 23);
		buttonPanel.add(btnCreateNewChannel);
		
		buttonPanel.validate();
=======
		
		setVisible(false);
		System.out.println("Update channnel list");
		ChatWindow newChat = new ChatWindow(channelList, channelToJoin);
		Client.chatWindow = newChat;
		newChat.setVisible(true);
		
>>>>>>> parent of c045a63... added update channellist funktion
	}
	
	public void JoinChannelUI(String channelName) {
		lblCurrentChannel.setText("Current channel: " + channelName);
		Client.currentChannel = Client.currentChannelTryToJoin;
		System.out.println("Joined Channel UI");
		dtrpnEnterMessage.setBounds(285, 646, 693, 23);
		btnSend_1.setBounds(988, 646, 65, 23);
		btnAddUserTo.setBounds(910, 11, 154, 23);
		textAreaMessages.setBounds(265, 52, 809, 561);
		lblNoChannel.setText("");
	}
	
	public void ChannelUserAdd(String nameOfUser) {
		System.out.println(nameOfUser);
		Login.client.SendChannelUserAdd(nameOfUser, Login.client.currentChannel);
	}
}
