package chatlive.listeners;

import java.util.Collection;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterListener;
import org.jxmpp.jid.Jid;

public class XmppRosterListener implements RosterListener {

    
    /** 
     * @param addresses
     */
    @Override
    public void entriesAdded(Collection<Jid> addresses) {
        for (Jid address : addresses) {
            System.out.println("Contact added: " + address);
        }
    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {
        for (Jid address : addresses) {
            System.out.println("Contact updated: " + address);
        }
    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {
        for (Jid address : addresses) {
            System.out.println("Contact deleted: " + address);
        }
    }

    @Override
    public void presenceChanged(Presence presence) {
        Jid from = presence.getFrom();
        Presence.Type type = presence.getType();
        Presence.Mode mode = presence.getMode();

        if (type == Presence.Type.available) {
            System.out.println("Contact " + from + " is available");
        } else if (type == Presence.Type.unavailable) {
            System.out.println("Contact " + from + " is unavailable");
        }

        if (mode != null) {
            System.out.println("Contact " + from + " is " + mode.toString());
        }
    }
    
}
