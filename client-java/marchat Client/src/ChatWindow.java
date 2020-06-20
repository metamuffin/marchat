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

public class ChatWindow extends JFrame {
	
	public JPanel contentPanel;
	private JLabel lblNoChannel;
	private JEditorPane dtrpnEnterMessage;
	private JButton btnSend_1;
	
	/**
	 * Launch the application.
	 * @return 
	 */
	public static void startChat(JSONObject channelList) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow(channelList);
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
	public ChatWindow(JSONObject channelList) {
		UpdateChannelList(channelList);
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
		
		String[] channelsTest = {"test1", "test2", "test3"};
		
		
		for(int i = 0; i < channels.length(); i++) {
			Button btnNewButton = new Button("#" + channels.getString(i));
			btnNewButton.setBounds(10, 11 + i * 34, 238, 23);
			
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String channelName = btnNewButton.getLabel().substring(1);
					Login.client.SendJoinChannel(channelName);
				}
			});
			
			contentPanel.add(btnNewButton);
		}
		
		JPanel panel = new JPanel();
		panel.setBackground(OwnColors.grey_m);
		panel.setBounds(0, 0, 262, 691);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnCreateNewChannel = new JButton("Create new channel");
		btnCreateNewChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateChannelWindow.start();
			}
		});
		btnCreateNewChannel.setBounds(61, 657, 129, 23);
		panel.add(btnCreateNewChannel);
		
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
		
	}

	
	public void UpdateChannelList(JSONObject channelList) {
		//Login.client.SendChannelUserAdd("test2", "marchat-welcome");
		//Login.client.sendChannelCreate("marchat-welcome-test");
	}
	
	public void JoinChannelUI() {
		System.out.println("Joined Channel UI");
		dtrpnEnterMessage.setBounds(285, 646, 693, 23);
		btnSend_1.setBounds(988, 646, 65, 23);
		lblNoChannel.setText("");
	}
}
