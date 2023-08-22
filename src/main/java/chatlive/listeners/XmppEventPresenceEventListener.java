package chatlive.listeners;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;

import chatlive.models.Colors;

public class XmppEventPresenceEventListener implements PresenceEventListener {

    @Override
    public void presenceAvailable(FullJid address, Presence availablePresence) {
        System.out.println("Contact " + address + " is available.");
    }

    @Override
    public void presenceUnavailable(FullJid address, Presence presence) {
        System.out.println("Contact " + address + " is unavailable.");
    }

    @Override
    public void presenceError(Jid address, Presence errorPresence) {
        System.out.println(Colors.RED + "Error in presence for contact " + address + ": " + errorPresence.getError() + Colors.RESET);
    }

    @Override
    public void presenceSubscribed(BareJid address, Presence subscribedPresence) {
        System.out.println("You are subscribed to contact " + address);
    }

    @Override
    public void presenceUnsubscribed(BareJid address, Presence unsubscribedPresence) {
        System.out.println("You are unsubscribed from contact " + address);
    }
    
}
