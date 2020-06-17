import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterWindow extends JFrame {
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldRepeat;
	public static RegisterWindow window;
	private JLabel lblErrormsg;

	/**
	 * Launch the application.
	 */
	public static void openRegWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 window= new RegisterWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterWindow() {
		setTitle("Sign Up");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setForeground(Color.RED);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(new Color(44, 62, 80));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(89, 76, 63, 20);
		contentPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(89, 119, 63, 20);
		contentPanel.add(lblPassword);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setColumns(10);
		textFieldUsername.setBounds(173, 76, 152, 20);
		contentPanel.add(textFieldUsername);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(173, 119, 152, 20);
		contentPanel.add(passwordField);
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				trySignUp(textFieldUsername.getText(), passwordField.getText(), passwordFieldRepeat.getText());
				
			}
		});
		btnSignUp.setBounds(210, 205, 89, 23);
		contentPanel.add(btnSignUp);
		
		JLabel lblSignUp = new JLabel("Sign up");
		lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignUp.setForeground(Color.WHITE);
		lblSignUp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSignUp.setBounds(104, 23, 230, 23);
		contentPanel.add(lblSignUp);
		
		passwordFieldRepeat = new JPasswordField();
		passwordFieldRepeat.setToolTipText("");
		passwordFieldRepeat.setBounds(173, 160, 152, 20);
		contentPanel.add(passwordFieldRepeat);
		
		JLabel lblRepeatPassword = new JLabel("Repeat password");
		lblRepeatPassword.setForeground(Color.WHITE);
		lblRepeatPassword.setBounds(56, 160, 96, 20);
		contentPanel.add(lblRepeatPassword);
		
		lblErrormsg = new JLabel("");
		lblErrormsg.setForeground(Color.RED);
		lblErrormsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblErrormsg.setBounds(0, 187, 444, 14);
		contentPanel.add(lblErrormsg);
		
		JButton btnBackToSign = new JButton("back to sign in");
		btnBackToSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Login.showLoginScreen();
				closeRegisterScreen();
				
			}
		});
		btnBackToSign.setBounds(194, 239, 118, 23);
		contentPanel.add(btnBackToSign);
	}
	
	  public static void closeRegisterScreen() {
		  window.setVisible(false);
	  }
	
	  public static void openRegisterScreen() {
		  window.setVisible(true);
	  }
	  
	  private void trySignUp(String username, String password ,String repeatPassword) {
		  
		  System.out.println(password + " " + repeatPassword);
		  if(!password.equals(repeatPassword)) {
			  Client.showInfoBox("Error", "The passwords must match!");
		  }else {
			  Login.client.SendRegister(username, password);
		  }
	  }
	  
	  
}
