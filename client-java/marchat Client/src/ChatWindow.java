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

public class ChatWindow extends JFrame {
	public JPanel contentPanel;
	/**
	 * Launch the application.
	 */
	public static void startChat(JSONObject channelList) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow(channelList);
					frame.setVisible(true);
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
		
	
	}

	
	public void UpdateChannelList(JSONObject channelList) {
		
		//Login.client.sendChannelCreate("test");
		//Login.client.SendChannelUserAdd("test2", "test");
		JSONArray channels = channelList.getJSONArray("channels");
		System.out.println(channels);
		
		for(int i = 0; i < channels.length(); i++) {
			JButton btnNewButton = new JButton(channels.getString(i));
			btnNewButton.setBounds(10, 11 + i * 34, 89, 23);
			contentPanel.add(btnNewButton);
		}
		
		//String [] splitted = channelList.split(":");
	   
		
	}
}
