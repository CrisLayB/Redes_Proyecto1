package chatlive.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucNotJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

public class XmppClient {
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;
    private Chat chatOneToOne;
    private MultiUserChat chatGroup;
    private AccountManager accountManager;
    private static final String XMPP_SERER_AND_DOMAIN = "alumchat.xyz";

    public XmppClient(){
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(XMPP_SERER_AND_DOMAIN)
                .setHost(XMPP_SERER_AND_DOMAIN)
                .setPort(5222)
                .setCompressionEnabled(false)
                .setSendPresence(true)                
                // .setDebuggerEnabled(true)
                .setSecurityMode(SecurityMode.disabled)
                .setSendPresence(true)
                .build();

            connection = new XMPPTCPConnection(config);

            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(new XmppMessageListener());

            accountManager = AccountManager.getInstance(connection);            
            
            chatOneToOne = null;
            chatGroup = null;
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
        if(!connection.isConnected()) connection.connect();
        connection.login(username, password);
    }

    public String createUser(HashMap<String, String> attributes) {
        try {
            if(!connection.isConnected()) connection.connect();
            
            if(!accountManager.supportsAccountCreation()) return "-1";

            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(
                Localpart.from(attributes.get("username")), 
                attributes.get("password"), 
                attributes
            );            
        } 
        catch (SmackException.NoResponseException NoResponse) {
            return NoResponse.getMessage();
        }
        catch (XMPPException.XMPPErrorException XErrorEx) {
            return XErrorEx.getMessage();
        }
        catch (SmackException.NotConnectedException NotConnected) {
            return NotConnected.getMessage();
        }
        catch (InterruptedException ie) {
            return ie.getMessage();
        }
        catch(XmppStringprepException xStrEx){
            return xStrEx.getMessage();
        }
        catch(IOException ioExp){
            return ioExp.getMessage();
        }
        catch(SmackException smackException){
            return smackException.getMessage();
        }
        catch(XMPPException xmppException){
            return xmppException.getMessage();
        }

        return "1";
    }

    public String deleteUser() {
        try {
            accountManager.deleteAccount();            
        } 
        catch (SmackException.NoResponseException NoResponse) {
            return NoResponse.getMessage();
        }
        catch (XMPPException.XMPPErrorException XErrorEx) {
            return XErrorEx.getMessage();
        }
        catch (SmackException.NotConnectedException NotConnected) {
            return NotConnected.getMessage();
        }
        catch (InterruptedException ie) {
            return ie.getMessage();
        }

        return "1";
    }

    public void disconnect(){
        if(connection.isConnected()){
            connection.disconnect();
        }
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

    public void addContactToList(String contact) throws Exception{
        Roster roster = Roster.getInstanceFor(connection);
        EntityBareJid jid = JidCreate.entityBareFrom(contact + "@" + XMPP_SERER_AND_DOMAIN);        
        roster.createEntry(jid, null, null);
    }

    public String[] getContactDetail(String contact) throws XmppStringprepException,Exception {
        Roster roster = Roster.getInstanceFor(connection);
        EntityBareJid jid = JidCreate.entityBareFrom(contact + "@" + XMPP_SERER_AND_DOMAIN);
        RosterEntry entry = roster.getEntry(jid);
        
        return (entry == null) 
            ? new String[]{"Contact not found"} 
            : new String[]{
                "username: " + entry.getJid(),                
                "Name: " + entry.getName(),
                "Type: " + entry.getType()
            };
    }

    public boolean createChat(String contact) throws XmppStringprepException {
        EntityBareJid jid = JidCreate.entityBareFrom(contact + "@" + XMPP_SERER_AND_DOMAIN);
        Roster roster = Roster.getInstanceFor(connection);
        RosterEntry entry = roster.getEntry(jid);

        if(entry == null) return false;
        
        chatOneToOne = chatManager.chatWith(jid);        
        return true;
    }

    public void joinGroupChat(String room, String nickname) throws XmppStringprepException, MultiUserChatException.MucAlreadyJoinedException, SmackException.NotConnectedException, InterruptedException, NotAMucServiceException, XMPPErrorException, NoResponseException  {
        EntityBareJid jid = JidCreate.entityBareFrom(room + "@conference." + XMPP_SERER_AND_DOMAIN);
        chatGroup = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
        chatGroup.join(Resourcepart.from(nickname));
        
        chatGroup.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                String from = message.getFrom().getResourceOrEmpty().toString();
                String body = message.getBody();
                System.out.println("Message from " + from + ": " + body);
            }
        });
    }

    public void sendMessage(String message, boolean forGrupalChat) throws Exception {
        if(forGrupalChat){
            chatGroup.sendMessage(message);
            return;
        }
        
        chatOneToOne.send(message);        
    }

    public ArrayList<String> incomingMessagesGroup() throws MucNotJoinedException, InterruptedException {
        ArrayList<String> messages = new ArrayList<String>();

        while (true) {
            Message incomingMessage = chatGroup.nextMessage();

            if(incomingMessage == null) break;
                        
            messages.add(
                "Message from " + incomingMessage.getFrom() + 
                ": " + Colors.CYAN + incomingMessage.getBody() + Colors.RESET
            );
        }
        
        return messages;
    }
    
    public void setPressenceMessage(String statusMessage) throws SmackException.NotConnectedException, InterruptedException {        
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(statusMessage);
        connection.sendStanza(presence);
    }

    class XmppMessageListener implements IncomingChatMessageListener{

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            String user = from.toString();
            String body = message.getBody();
            System.out.println("Message recived from " + user + ": " + body); // Broke the MVC because is there not other way :(
        }

    }
}
