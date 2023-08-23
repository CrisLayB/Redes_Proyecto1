package chatlive.listeners;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.Jid;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Xmpp Subscribe Listener </h2>
 * This listener will be recive the notification of a subscription.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class XmppSubscribeListener implements SubscribeListener {

    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        System.out.println("Received subscription request from: " + from);                
        return SubscribeAnswer.Approve;
    }
    
}
