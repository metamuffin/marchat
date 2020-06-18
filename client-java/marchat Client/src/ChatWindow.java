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
		contentPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPanel);
	}

	
	public void UpdateChannelList(JSONObject channelList) {
		
		JSONArray channels = channelList.getJSONArray("channels");
		System.out.println(channels);
		
		//String [] splitted = channelList.split(":");
	   
		
	}
}
