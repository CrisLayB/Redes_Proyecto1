package chatlive.listeners;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp Message Listener </h2>
 * For recive the news messages of the contact list.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppMessageListener implements IncomingChatMessageListener{

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        String user = from.toString();
        String body = message.getBody();
        System.out.println("Message recived from " + user + ": " + body); // Broke the MVC because is there not other way :(
    }

}