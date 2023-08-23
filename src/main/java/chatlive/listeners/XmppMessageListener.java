package chatlive.listeners;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class XmppMessageListener implements IncomingChatMessageListener{

    
    /** 
     * @param from
     * @param message
     * @param chat
     */
    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        String user = from.toString();
        String body = message.getBody();
        System.out.println("Message recived from " + user + ": " + body); // Broke the MVC because is there not other way :(
    }

}