package chatlive.models;

import chatlive.listeners.XmppMessageListener;
import chatlive.listeners.XmppRosterListener;
import chatlive.listeners.XmppSubscribeListener;
import chatlive.listeners.XmppConnectionListener;
import chatlive.listeners.XmppEventPresenceEventListener;
import chatlive.listeners.XmppFileTransferListener;
import chatlive.listeners.XmppGroupChatListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
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
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp Client </h2>
 * This is the model of the program that is really the client to connecto to the server xmpp.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppClient {
    private AbstractXMPPConnection connection;
    private ChatManager chatManager;
    private Chat chatOneToOne;
    private MultiUserChat chatGroup;
    private AccountManager accountManager;
    private Roster roster;
    private FileTransferManager fileTransferManager;
    private EntityBareJid entityUserJid;
    private boolean listenerChatGroupCreated;
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
            connection.addConnectionListener(new XmppConnectionListener());

            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(new XmppMessageListener());

            accountManager = AccountManager.getInstance(connection);

            roster = Roster.getInstanceFor(connection);
            roster.addPresenceEventListener(new XmppEventPresenceEventListener());
            roster.addRosterListener(new XmppRosterListener());
            roster.addSubscribeListener(new XmppSubscribeListener());

            fileTransferManager = FileTransferManager.getInstanceFor(connection);
            fileTransferManager.addFileTransferListener(new XmppFileTransferListener());
            
            chatOneToOne = null;
            chatGroup = null;

            listenerChatGroupCreated = false;
        } catch (XmppStringprepException  xmppStringExc) {
            xmppStringExc.printStackTrace();
            System.err.println("Error to connect to the server XMPP: " + xmppStringExc.getMessage());
        }
        catch (Exception e) {
            System.err.println("\n==> ERROR HAPPEND HERE \n");
            e.printStackTrace();
        }
    }
    
    
    /** 
     * To Get the connection status of the user logged.
     * 
     * @return boolean
     */
    public boolean isConnected(){
        return (connection != null) && (connection.isConnected());
    }

    /**
     * Get the current user.
     * 
     * @return EntityFullJid : The User with Gid credentials.
     */
    public EntityFullJid getUser(){                
        return (isConnected()) ? connection.getUser() : null;
    }

    /**
     * Get information of the user logged.
     * 
     * @return String[] : The information of the user
     */
    public String[] getInformationUser(){        
        Presence presence = roster.getPresence(connection.getUser().asBareJid());
        String status = roster.getPresence(connection.getUser().asBareJid()).getStatus();
        
        return new String[]{
            connection.getUser().asBareJid().toString(),
            presence.getType().toString(),
            status
        };
    }

    /**
     * For connect and login with the correspondent username and password.
     * 
     * @param username
     * @param password
     * @throws Exception
     */
    public void login(String username, String password) throws Exception{
        if(!connection.isConnected()) connection.connect();
        connection.login(username, password);
    }

    /**
     * Crear usuario con sus atributos correspondientes.
     * 
     * @param attributes
     * @return String : The confirmation.
     */
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

    /**
     * For Delete the user.
     * 
     * @return : Confirmation of delete
     */
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
            chatOneToOne = null;
            chatGroup = null;
        }
    }

    /**
     * Get all the contacts of the follow user.
     * 
     * @return ArrayList<String[]> : All the users of the list
     */
    public ArrayList<String[]> displayContactsList(){
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

    /**
     * Add the contacts.
     * 
     * @param contact
     * @throws Exception
     */
    public void addContactToList(String contact) throws Exception{
        EntityBareJid jid = JidCreate.entityBareFrom(contact);
        roster.createEntry(jid, null, null);
        entityUserJid = jid;
    }

    /**
     * Get the details of the contacts
     * 
     * @param contact
     * @return
     * @throws XmppStringprepException
     * @throws Exception
     */
    public String[] getContactDetail(String contact) throws XmppStringprepException,Exception {
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

    /**
     * Create a new chat with the follow username and domain.
     * 
     * @param contact
     * @return
     * @throws XmppStringprepException
     */
    public boolean createChat(String contact) throws XmppStringprepException {
        EntityBareJid jid = JidCreate.entityBareFrom(contact);
        RosterEntry entry = roster.getEntry(jid);

        if(entry == null) return false;
        entityUserJid = jid;
        
        chatOneToOne = chatManager.chatWith(jid);
        return true;
    }

    /**
     * The user can join to a new group.
     * 
     * @param room
     * @param nickname
     * @throws XmppStringprepException
     * @throws MultiUserChatException.MucAlreadyJoinedException
     * @throws SmackException.NotConnectedException
     * @throws InterruptedException
     * @throws NotAMucServiceException
     * @throws XMPPErrorException
     * @throws NoResponseException
     */
    public void joinGroupChat(String room, String nickname) throws XmppStringprepException, MultiUserChatException.MucAlreadyJoinedException, SmackException.NotConnectedException, InterruptedException, NotAMucServiceException, XMPPErrorException, NoResponseException  {
        EntityBareJid jid = JidCreate.entityBareFrom(room + "@conference." + XMPP_SERER_AND_DOMAIN);
        chatGroup = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
        chatGroup.join(Resourcepart.from(nickname));
        
        entityUserJid = jid;

        if(!listenerChatGroupCreated){
            chatGroup.addMessageListener(new XmppGroupChatListener());
            listenerChatGroupCreated = true;
        }
    }

    /**
     * Send message to an individual chat or a group.
     * 
     * @param message
     * @param forGrupalChat
     * @throws Exception
     */
    public void sendMessage(String message, boolean forGrupalChat) throws Exception {
        if(forGrupalChat){
            chatGroup.sendMessage(message);
            return;
        }
        
        chatOneToOne.send(message);
    }

    /**
     * Get status of the actual user
     * 
     * @return
     */
    public String checkStatus(){        
        Type presence = roster.getPresence(entityUserJid).getType();
        return presence.toString();
    }

    /**
     * Recibe all the messagues for the group.
     * 
     * @return ArrayList<String> : Get all the messages
     * @throws MucNotJoinedException
     * @throws InterruptedException
     */
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
    
    /**
     * Set the presence message for the user.
     * 
     * @param statusMessage
     * @throws SmackException.NotConnectedException
     * @throws InterruptedException
     */
    public void setPressenceMessage(String statusMessage) throws SmackException.NotConnectedException, InterruptedException {        
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(statusMessage);
        connection.sendStanza(presence);
    }

    /**
     * Send the file
     * 
     * @param pathFile
     * @param description
     * @throws SmackException
     * @throws XmppStringprepException
     */
    public void sendFile(String pathFile, String description) throws SmackException, XmppStringprepException, InterruptedException, IOException {
        File fileToSend = new File(pathFile);

        String base64File = encodeFileToBase64(fileToSend);

        Message message = new Message(entityUserJid, Message.Type.chat);
        message.setBody(description);
        message.setSubject("Base64File");
        message.addBody("base64", base64File);

        connection.sendStanza(message);
    }

    private static String encodeFileToBase64(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            inputStream.read(fileBytes);
            return Base64.getEncoder().encodeToString(fileBytes);
        }
    }
}
