package model;

import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class XmppClient implements ChatMessageListener {
    private AbstractXMPPConnection connection;
    private final String host = "alumchat.xyz";

    public XmppClient(String username, String password){
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setHost(host)
                .setPort(5222)
                .setSecurityMode(SecurityMode.disabled)
                .build();

        connection = new XMPPTCPConnection(config);
    }

    public void connect() throws Exception{
        connection.connect();
        connection.login();
    }

    public void disconnect(){
        connection.disconnect();
    }

    public void sendMessage(String message, String to) throws XMPPException, NotConnectedException {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
		Chat chat = chatManager.createChat(to, this); // pass XmppClient instance as listener for received messages.
		chat.sendMessage(message);
    }
    
    @Override
    public void processMessage(Chat chat, Message message) {        
        if (message.getType() == Message.Type.chat) {
			System.out.println("=> " + chat.getParticipant() + ": " + message.getBody());
		}
    }
    
    public void displayContactsList(){
        Roster roster = Roster.getInstanceFor(connection);
		Collection<RosterEntry> entries = roster.getEntries();

		System.out.println("\n\n" + entries.size() + " contacts:");
		for (RosterEntry r : entries) {
			String user = r.getUser();
			Type presenceType = roster.getPresence(user).getType();
			System.out.println(user + ":" + presenceType);
		}
    }
}
