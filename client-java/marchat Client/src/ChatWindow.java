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
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class ChatWindow extends JFrame {
	
	public JPanel panelButtons;
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
		setTitle("marchat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.RED);
		setSize(800, 400);
		
		panelButtons = new JPanel();
		panelButtons.setBackground(Color.YELLOW);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(558, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelButtons, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
		);
		
		
		UpdateChannelList(channelList, channelToJoin);
		
		getContentPane().setLayout(groupLayout);

		
	}

	
	public void UpdateChannelList(JSONObject channelList, String channelToJoin) { 

		JButton btnChannel = new JButton("marchat-welcome");
		btnChannel.setBounds(33, 5, 117, 23);
		panelButtons.setLayout(null);
		panelButtons.add(btnChannel);
		panelButtons.validate();
		
	}
	
	public void JoinChannelUI(String channelName) {
		
	}
	
	public void ChannelUserAdd(String nameOfUser) {
		System.out.println(nameOfUser);
		Login.client.SendChannelUserAdd(nameOfUser, Login.client.currentChannel);
	}
}
