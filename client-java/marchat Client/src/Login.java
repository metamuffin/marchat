import java.awt.BorderLayout;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Color;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.awt.Button;
import java.awt.Label;
import java.awt.Window.Type;

public class Login extends JFrame {
  private static final JPanel contentPanel = new JPanel();
  private static JTextField usernameTxtField;
  private static JPasswordField password;
  public static Label wrongPasswordLabel;
  public static Login dialog;
  
  
  public static Client client = new Client();
  /**
   * Launch the application.
   */
  
  
  public static void main(String[] args) {
	  
    try {
      dialog = new Login(true);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void main() {
	 
	  
	    try {
	      dialog = new Login(false);
	      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	      dialog.setVisible(true);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
  /**
   * Create the dialog.
   */
  
  public Login(boolean isFirstStart) {
  	setTitle("Login for marchat");
  	setResizable(false);
	  if(isFirstStart) {
		  connectToServer();
	  }
	  
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBackground(OwnColors.grey_d);
    contentPanel.setForeground(Color.RED);
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(null);
    {
      JLabel lblusernameTxtField = new JLabel("usernameTxtField");
      lblusernameTxtField.setForeground(Color.WHITE);
      lblusernameTxtField.setBounds(89, 76, 63, 20);
      contentPanel.add(lblusernameTxtField);
    }
    {
      JLabel lblPassword = new JLabel("Password");
      lblPassword.setForeground(Color.WHITE);
      lblPassword.setBounds(89, 119, 63, 20);
      contentPanel.add(lblPassword);
    }
    
    usernameTxtField = new JTextField();
    usernameTxtField.setBounds(173, 76, 152, 20);
    contentPanel.add(usernameTxtField);
    usernameTxtField.setColumns(10);
    
    password = new JPasswordField();
    password.setBounds(173, 119, 152, 20);
    contentPanel.add(password);
    
    JButton btnLogin = new JButton("Login");
    btnLogin.addActionListener(new ActionListener() {
    	
      public void actionPerformed(ActionEvent arg0) {
    	  
    	  if(tryLogin(usernameTxtField.getText(), password.getText())) {
    		  
    	  }else {
    		 
    
    	  }
        /*if(usernameTxtField.getText().equals("HELLO") && password.getText().equals("WORLD") ) {
          JOptionPane.showMessageDialog(null, "Login Sucessful ");
          
        }else
        {
          JOptionPane.showMessageDialog(null, "Wrong Inputs", "Please Check", JOptionPane.WARNING_MESSAGE);
          return;
        }*/
      }
    });
    btnLogin.setBounds(205, 173, 89, 23);
    contentPanel.add(btnLogin);
    
    JButton btnSignUp = new JButton("Sign up");
    btnSignUp.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent arg0) {
    		
    		RegisterWindow.openRegWindow();
    		closeLoginScreen();
    	}
    });
    btnSignUp.setBounds(205, 207, 89, 23);
    contentPanel.add(btnSignUp);
    
    JLabel lblLogin = new JLabel("Login");
    lblLogin.setForeground(Color.WHITE);
    lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
    lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
    lblLogin.setBounds(104, 23, 230, 23);
    contentPanel.add(lblLogin);
    
    wrongPasswordLabel = new Label("");
    wrongPasswordLabel.setAlignment(Label.CENTER);
    wrongPasswordLabel.setBounds(10, 145, 424, 22);
    contentPanel.add(wrongPasswordLabel);
  }
  
  private boolean tryLogin(String usernameTxtField, String password) {
	  return client.SendLogin(usernameTxtField, password);
  }
  
  public static void closeLoginScreen() {
	  dialog.setVisible(false);
  }
  
  public static void showLoginScreen() {
	  dialog.setVisible(true);
  }
  
  public static void clearInputs() {
	  usernameTxtField.setText("");
	  password.setText("");
  }
  
  public boolean connectToServer() {
	  try {
		  Client.connectToServer();
		  return true;
	  }catch (Exception e){
		  System.err.println(e);
		  return false;
	  }
  }
}