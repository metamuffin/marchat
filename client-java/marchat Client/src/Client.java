import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;

public class Client {

	public static boolean loggedIn = false;
	final static String uri = "ws://marchat.zapto.org:5555/ws";
	//final static String uri = "ws://25.86.154.196:5555/ws";
    final static CountDownLatch messageLatch = new CountDownLatch(1);
    
    public static Session ServerSession;

    public static void connectToServer() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            System.out.println("Connecting to " + uri);
            container.connectToServer(MyClientEndpoint.class, URI.create(uri));
            //messageLatch.await(100, TimeUnit.SECONDS);
        } catch (DeploymentException /*| InterruptedException */| IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean SendLogin(String username, String password) {
    	try {
    		
    		
            String login = "{\"username\":\"" + username + "\",\"password\":\"" + Encoding.hashSHA256(password.getBytes()) + "\",\"anti_replay\":\"" + Encoding.hashSHA256((Encoding.hashSHA256(password.getBytes()) + " " + Instant.now().getEpochSecond()).getBytes()) + "\",\"timestamp\":" + Instant.now().getEpochSecond() + "}";
            String loginB64 = "login:" + Encoding.Base64encode(login);
            
            
            System.out.println("Sending message to endpoint: " + loginB64);
            ServerSession.getBasicRemote().sendText(loginB64);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    
    public boolean SendRegister(String username, String password) {
    	try {
    		
    		
            String register = "{\"username\":\"" + username + "\",\"password\":\"" + Encoding.hashSHA256(password.getBytes()) + "\"}";
            String registerB64 = "register:" + Encoding.Base64encode(register);
            
            
            System.out.println("Sending message to endpoint: " + registerB64);
            ServerSession.getBasicRemote().sendText(registerB64);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    
    
    public static void showInfoBox(String title, String msg) {

 		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    	
    }
    
    
}