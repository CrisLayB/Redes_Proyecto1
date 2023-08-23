package chatlive.listeners;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp Group Chat Listener </h2>
 * Listener for get the messages of the group
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppGroupChatListener implements MessageListener {

    @Override
    public void processMessage(Message message) {
        String from = message.getFrom().getResourceOrEmpty().toString();
        String body = message.getBody();
        System.out.println("Message from " + from + ": " + body);
    }
    
}
