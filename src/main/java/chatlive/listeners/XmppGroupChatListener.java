package chatlive.listeners;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class XmppGroupChatListener implements MessageListener {

    @Override
    public void processMessage(Message message) {
        String from = message.getFrom().getResourceOrEmpty().toString();
        String body = message.getBody();
        System.out.println("Message from " + from + ": " + body);
    }
    
}
