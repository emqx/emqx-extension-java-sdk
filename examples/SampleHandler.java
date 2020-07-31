import io.emqx.extension.handler.ActionOptionConfig;
import io.emqx.extension.handler.ActionOptionConfig.Keys;
import io.emqx.extension.handler.DefaultCommunicationHandler;
import io.emqx.extension.handler.codec.*;

public class SampleHandler extends DefaultCommunicationHandler {
	
	@Override
	public ActionOptionConfig getActionOption() {
		ActionOptionConfig option = new ActionOptionConfig();
		option.set(Keys.MESSAGE_PUBLISH_TOPICS, "t_sample,topic_sample");
		option.set(Keys.MESSAGE_DELIVERED_TOPICS, "t_sample,topic_sample");
		option.set(Keys.MESSAGE_ACKED_TOPICS, "t_sample,topic_sample");
		option.set(Keys.MESSAGE_DROPPED_TOPICS, "t_sample,topic_sample");
		
		return option;
	}
	
	// Clients
	@Override
    public void onClientConnect(ConnInfo connInfo, Property[] props) {
        System.err.printf("[Java] onClientConnect: connInfo: %s, props: %s\n", connInfo, props);
    }
	
	@Override
    public void onClientConnack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
        System.err.printf("[Java] onClientConnack: connInfo: %s, rc: %s, props: %s\n", connInfo, rc, props);
    }

	@Override
    public void onClientConnected(ClientInfo clientInfo) {
        System.err.printf("[Java] onClientConnected: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void onClientDisconnected(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] onClientDisconnected: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

	@Override
    public boolean onClientAuthenticate(ClientInfo clientInfo, boolean authresult) {
        System.err.printf("[Java] onClientAuthenticate: clientinfo: %s, authresult: %s\n", clientInfo, authresult);

        return true;
    }

	@Override
    public boolean onClientCheckAcl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        System.err.printf("[Java] onClientCheckAcl: clientinfo: %s, pubsub: %s, topic: %s, result: %s\n", clientInfo, pubsub, topic, result);

        return true;
    }

	@Override
    public void onClientSubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] onClientSubscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

	@Override
    public void onClientUnsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] onClientUnsubscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

    // Sessions
	@Override
    public void onSessionCreated(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionCreated: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void onSessionSubscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
        System.err.printf("[Java] onSessionSubscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

	@Override
    public void onSessionUnsubscribed(ClientInfo clientInfo, Topic topic) {
        System.err.printf("[Java] onSessionUnsubscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

	@Override
    public void onSessionResumed(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionResumed: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void onSessionDiscarded(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionDiscarded: clientinfo: %s\n", clientInfo);
    }
	
	@Override
    public void onSessionTakeovered(ClientInfo clientInfo) {
        System.err.printf("[Java] onSessionTakeovered: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void onSessionTerminated(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] onSessionTerminated: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

    // Messages
	@Override
    public Message onMessagePublish(Message message) {
        System.err.printf("[Java] onMessagePublish: message: %s\n", message);
        
        return message;
    }
	
	@Override
    public void onMessageDropped(Message message, Reason reason) {
        System.err.printf("[Java] onMessageDropped: message: %s, reason: %s\n", message, reason);
    }

	@Override
    public void onMessageDelivered(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] onMessageDelivered: clientinfo: %s, message: %s\n", clientInfo, message);
    }

	@Override
    public void onMessageAcked(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] onMessageAcked: clientinfo: %s, message: %s\n", clientInfo, message);
    }

}
