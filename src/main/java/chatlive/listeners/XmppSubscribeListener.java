package chatlive.listeners;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.Jid;

public class XmppSubscribeListener implements SubscribeListener {

    
    /** 
     * @param from
     * @param subscribeRequest
     * @return SubscribeAnswer
     */
    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        System.out.println("Received subscription request from: " + from);                
        return SubscribeAnswer.Approve;
    }
    
}
