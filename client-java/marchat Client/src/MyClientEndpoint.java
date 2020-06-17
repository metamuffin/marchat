import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class MyClientEndpoint {
	
	
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        Client.ServerSession = session;
       /* try {
            String name = "Duke";
            System.out.println("Sending message to endpoint: " + name);
            session.getBasicRemote().sendText(name);
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @OnMessage
    public void processMessage(String message) {
    	
    	System.out.println("Received message in client: " + message);
    	
    	String[] splitMsg = message.split(":", 2);
    	String pckgName = splitMsg[0];
    	String pckgCont = splitMsg[1];
    	
         if(!Client.loggedIn) {
         	if(message.startsWith("channel-list")) {
         		ChatWindow.startChat();
         		System.out.println("logged in succesfully");
         		Client.loggedIn = true;
         		Login.closeLoginScreen();
         	}else if(message.startsWith("message")) {
         		String msg = Encoding.Base64decode(pckgCont);
         		
         		String[] split =  msg.split(":", 2);
         		msg = split[1];
         		msg = msg.substring(1, msg.length() - 2);
         		
         		//Login.wrongPasswordLabel.setText(msg);
         		String titleBar = "Message from Server";
         		Client.showInfoBox(titleBar, msg);
         		Login.clearInputs();
         	}else {
         		Client.loggedIn = false;
         		//System.out.println("Wrong password or username");
         		String msg = Encoding.Base64decode(pckgCont);
         		//Login.wrongPasswordLabel.setText("Wrong password or username!");
         		
         		String[] split =  msg.split(":", 2);
         		msg = split[1];
         		msg = msg.substring(1, msg.length() - 2);
         		
         		//Login.wrongPasswordLabel.setText(msg);
         		String titleBar = "Error";
         		Client.showInfoBox(titleBar, msg);
         		Login.clearInputs();
         	}
         }
    	
        Client.messageLatch.countDown();
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}
