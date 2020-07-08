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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.SystemColor;

public class ChatWindow extends JFrame {
	
	public JPanel panelButtons;
	private JTextField textField;
    public static JLabel lblCurrentChannel;
    private JLabel lblYouNeedTo;
    private JButton btnSend;
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
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public ChatWindow(JSONObject channelList, String channelToJoin) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		
		getContentPane().setBackground(OwnColors.grey_d);
		setTitle("marchat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(OwnColors.grey_d);
		setSize(1310, 853);
		
		panelButtons = new JPanel();
		panelButtons.setBackground(OwnColors.grey_m);
		
		JPanel ChatPanel = new JPanel();
		ChatPanel.setBackground(OwnColors.grey_d);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ChatPanel, GroupLayout.DEFAULT_SIZE, 1080, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(ChatPanel, GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
				.addComponent(panelButtons, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
		);
		
		textField = new JTextField();
		textField.setDropMode(DropMode.INSERT);
		textField.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Login.client.SendMessage(textField.getText());;
				textField.setText("");
				
			}
		});
		
		lblCurrentChannel = new JLabel("");
		lblCurrentChannel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCurrentChannel.setForeground(Color.WHITE);
		
		lblYouNeedTo = new JLabel("You need to join a channel before you can write messages!");
		lblYouNeedTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblYouNeedTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouNeedTo.setForeground(Color.WHITE);
		GroupLayout gl_ChatPanel = new GroupLayout(ChatPanel);
		gl_ChatPanel.setHorizontalGroup(
			gl_ChatPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ChatPanel.createSequentialGroup()
					.addGroup(gl_ChatPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ChatPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_ChatPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCurrentChannel, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_ChatPanel.createSequentialGroup()
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))))
						.addComponent(lblYouNeedTo, GroupLayout.DEFAULT_SIZE, 1094, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_ChatPanel.setVerticalGroup(
			gl_ChatPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ChatPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCurrentChannel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblYouNeedTo, GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_ChatPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSend))
					.addContainerGap())
		);
		ChatPanel.setLayout(gl_ChatPanel);
		
		textField.setVisible(false);
		btnSend.setVisible(false);
		
		UpdateChannelList(channelList, channelToJoin);
		
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		setJMenuBar(menuBar);
		
		JMenu mnChannel = new JMenu("Channel");
		mnChannel.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnChannel);
		
		JMenuItem mntmCreateNewChannel = new JMenuItem("Create new channel");
		mntmCreateNewChannel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CreateChannelWindow window = new CreateChannelWindow();
				window.setVisible(true);
				
			}
		});
		mntmCreateNewChannel.setHorizontalAlignment(SwingConstants.CENTER);
		mnChannel.add(mntmCreateNewChannel);
		
		JMenuItem mntmAddUserTo = new JMenuItem("Add user to the active channel");
		mntmAddUserTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ChannelUserAddWindow window = new ChannelUserAddWindow();
				window.setVisible(true);	
			}
		});
		mnChannel.add(mntmAddUserTo);

		
	}
	
	
	public void UpdateChannelList(JSONObject channelList, String channelToJoin) {
		panelButtons.setLayout(null);
		
		RoundedButton button = new RoundedButton ("asas");
		button.setFont(new Font("Tahoma", Font.PLAIN, 20));
		button.setBackground(Color.WHITE);
		button.setBounds(29, 10, 147, 22);
		panelButtons.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setSize(new Dimension(100, 100));
		scrollPane.setBounds(0, 0, 224, 793);
		panelButtons.add(scrollPane);
		
		System.out.println(channelList);
		JSONArray channels = channelList.getJSONArray("channels");
		
		panelButtons.removeAll();
		
		for(int i = 0; i < channels.length(); i++) {
			Button btnChannel = new Button("#" + channels.getString(i));
			btnChannel.setBackground(Color.WHITE);
			btnChannel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnChannel.setBounds(10, 5 + i * 34, 204, 23);
			btnChannel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Login.client.SendJoinChannel(btnChannel.getLabel().substring(1));
				}
			});
			panelButtons.setLayout(null);
			panelButtons.add(btnChannel);
		}
		
		
		panelButtons.validate();
		panelButtons.repaint();
		
	}
	
	public void JoinChannelUI(String channelName) {
		textField.setVisible(true);
		btnSend.setVisible(true);
		lblYouNeedTo.setText("");
		lblCurrentChannel.setText("Current channel: " + channelName);
		Client.currentChannel = Client.currentChannelTryToJoin;
	}
	
	public void ChannelUserAdd(String nameOfUser) {
		System.out.println(nameOfUser);
		Login.client.SendChannelUserAdd(nameOfUser, Login.client.currentChannel);
	}
	
	public void showMessage(String user ,String Message) {
		
	}
}
