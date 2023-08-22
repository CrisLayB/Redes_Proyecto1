package chatlive.listeners;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import chatlive.models.Colors;

public class XmppConnectionListener implements ConnectionListener {

    @Override
    public void connected(XMPPConnection connection) {
        System.out.println("Connected to the XMPP server.");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        System.out.println("Authenticated to the XMPP server. Resumed: " + resumed);
    }

    @Override
    public void connectionClosed() {
        System.out.println("Connection to the XMPP server closed.");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        System.out.println(Colors.RED + "Connection to the XMPP server closed on error: " + e.getMessage() + Colors.RESET);
    }

    @Override
    public void reconnectionSuccessful() {
        System.out.println("Reconnection to the XMPP server successful.");
    }

    @Override
    public void reconnectingIn(int seconds) {
        System.out.println("Reconnecting to the XMPP server in " + seconds + " seconds.");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        System.out.println(Colors.RED + "Reconnection to the XMPP server failed: " + e.getMessage() + Colors.RESET);
    }
    
}
