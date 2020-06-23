import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ChannelUserAddWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChannelUserAddWindow dialog = new ChannelUserAddWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChannelUserAddWindow() {
		setResizable(false);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setTitle("Add user");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(OwnColors.grey_d);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setToolTipText("");
		textField.setDropMode(DropMode.INSERT);
		textField.setBounds(84, 111, 255, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblEnterAName = new JLabel("Enter the name of the user you want to add to the channel:");
		lblEnterAName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterAName.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterAName.setForeground(Color.WHITE);
		lblEnterAName.setBounds(10, 55, 414, 30);
		contentPanel.add(lblEnterAName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(OwnColors.grey_d);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Login.client.chatWindow.ChannelUserAdd(textField.getText());
						setVisible(false);
					}
				});
				okButton.setIcon(null);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
