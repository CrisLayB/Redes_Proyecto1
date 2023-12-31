package chatlive.listeners;

import java.util.Collection;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterListener;
import org.jxmpp.jid.Jid;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp Roster Listener </h2>
 * This listener will upgrade the information of the users of the list contacts.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppRosterListener implements RosterListener {
    
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
