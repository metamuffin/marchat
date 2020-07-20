import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONObject;

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
    	
    	JSONObject obj = new JSONObject(Encoding.Base64decode(pckgCont));
    	
        
        if(message.startsWith("ok:")) {
      		
      		if(obj.getString("packet").equals("register")) {
      		//Client.showInfoBox("Message from server", "Success!");
      		RegisterWindow.closeRegisterScreen();
      		Login.showLoginScreen();
      		}else if (obj.getString("packet").equals("login")) {
      			
      			//Login.client.
      		}else if(obj.getString("packet").equals("channel_user_add")) {
      		}else if(obj.getString("packet").equals("channel_create")) {
      		}
      		
      	}else if(message.startsWith("error:") && Client.loggedIn){
     		String msg = obj.getString("message");
     		
     		String titleBar = "Error";
     		Client.showErrorBox(titleBar, msg);
     	}else if(message.startsWith("channel:")){
     		Client.chatWindow.JoinChannelUI(Client.currentChannelTryToJoin);
     	}else if (message.startsWith("message:")) {
     		String user = obj.getString("username");
     		String msg = obj.getString("text");
     		System.out.println("Received message from " + user + ": " + msg);
     		Client.chatWindow.showMessage(user, msg);
     		
     	}else if(message.startsWith("channel-list:") && Client.loggedIn) {
     		Client.activeChannelList = obj;
  			Client.chatWindow.UpdateChannelList(Client.activeChannelList, Client.currentChannelTryToJoin);
     	}
    	
        
        if(!Client.loggedIn) {
         	if(message.startsWith("channel-list")) {
         		ChatWindow.startChat(obj);
         		Client.activeChannelList = obj;
         		System.out.println("logged in succesfully");
         		Client.loggedIn = true;
         		Login.closeLoginScreen();
         	}else if(message.startsWith("ok")) {}
         	else {
         		Client.loggedIn = false;
         		String msg = obj.getString("message");
         		
         		String titleBar = "Error";
         		Client.showErrorBox(titleBar, msg);
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
