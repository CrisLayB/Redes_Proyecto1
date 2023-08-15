package chatlive.models;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

public class XmppClient {
    private AbstractXMPPConnection connection;
    private static final String XMPP_SERER_AND_DOMAIN = "alumchat.xyz";

    public XmppClient(){
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(XMPP_SERER_AND_DOMAIN)
                .setHost(XMPP_SERER_AND_DOMAIN)
                .setPort(5222)
                .setCompressionEnabled(false)
                // .setDebuggerEnabled(true)
                .setSecurityMode(SecurityMode.disabled)
                .setSendPresence(true)
                .build();

            connection = new XMPPTCPConnection(config);
        } catch (XmppStringprepException  xmppStringExc) {
            xmppStringExc.printStackTrace();
            System.err.println("Error al conectarse al servidor XMPP: " + xmppStringExc.getMessage());
        }
        catch (Exception e) {
            System.err.println("\n==> ERROR HAPPEND HERE \n");
            e.printStackTrace();
        }
    }
    
    public boolean isConnected(){
        return (connection != null) && (connection.isConnected());
    }

    public EntityFullJid getUser(){                
        return (isConnected()) ? connection.getUser() : null;
    }

    public String[] getInformationUser(){        
        Roster roster = Roster.getInstanceFor(connection);
        Presence presence = roster.getPresence(connection.getUser().asBareJid());
        String status = roster.getPresence(connection.getUser().asBareJid()).getStatus();
        
        return new String[]{
            connection.getUser().asBareJid().toString(),
            presence.getType().toString(),
            status
        };
    }

    public void login(String username, String password) throws Exception{
        connection.connect();
        connection.login(username, password);
    }

    public void createUser(String username, String password) throws Exception {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.sensitiveOperationOverInsecureConnection(true);
        accountManager.createAccount(Localpart.from(username), password);
    }

    public void disconnect(){
        if(connection != null) connection.disconnect();
    }

    public ArrayList<String[]> displayContactsList(){
        Roster roster = Roster.getInstanceFor(connection);
		Collection<RosterEntry> entries = roster.getEntries();        	

        ArrayList<String[]> list = new ArrayList<String[]>();
        list.add(new String[]{entries.size() + ""});

		for (RosterEntry r : entries) {
            BareJid user = r.getJid();
            Type presenceType = roster.getPresence(user).getType();
            String status = roster.getPresence(user).getStatus();
            
            list.add(new String[]{
                user.toString(), 
                presenceType.toString(), 
                status
            });
		}

        return list;
    }

    public void setPressenceMessage(String statusMessage) throws SmackException.NotConnectedException, InterruptedException {        
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(statusMessage);
        connection.sendStanza(presence);
    }

    public void sendMessage(String message, String to) throws XMPPException, NotConnectedException {
        ChatManager chatManager = ChatManager.getInstanceFor(connection); 
        chatManager.getClass();             
		// Chat chat = chatManager.createChat(to, this); // pass XmppClient instance as listener for received messages.
		// chat.sendMessage(message);
    }
}