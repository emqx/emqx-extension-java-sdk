import emqx.extension.java.handler.ActionOptionConfig;
import emqx.extension.java.handler.ActionOptionConfig.Keys;
import emqx.extension.java.handler.DefaultCommunicationHandler;
import emqx.extension.java.handler.codec.ClientInfo;
import emqx.extension.java.handler.codec.ConnInfo;
import emqx.extension.java.handler.codec.Message;
import emqx.extension.java.handler.codec.Property;
import emqx.extension.java.handler.codec.PubSub;
import emqx.extension.java.handler.codec.Reason;
import emqx.extension.java.handler.codec.ReturnCode;
import emqx.extension.java.handler.codec.SubscribeOption;
import emqx.extension.java.handler.codec.Topic;
import emqx.extension.java.handler.codec.TopicFilter;

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
    public void on_client_connect(ConnInfo connInfo, Property[] props) {
        System.err.printf("[Java] on_client_connect: connInfo: %s, props: %s\n", connInfo, props);
    }

	@Override
    public void on_client_connack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
        System.err.printf("[Java] on_client_connack: connInfo: %s, rc: %s, props: %s\n", connInfo, rc, props);
    }

	@Override
    public void on_client_connected(ClientInfo clientInfo) {
        System.err.printf("[Java] on_client_connected: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void on_client_disconnected(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] on_client_disconnected: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

	@Override
    public boolean on_client_authenticate(ClientInfo clientInfo, boolean authresult) {
        System.err.printf("[Java] on_client_authenticate: clientinfo: %s, authresult: %s\n", clientInfo, authresult);

        return true;
    }

	@Override
    public boolean on_client_check_acl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        System.err.printf("[Java] on_client_check_acl: clientinfo: %s, pubsub: %s, topic: %s, result: %s\n", clientInfo, pubsub, topic, result);

        return true;
    }

	@Override
    public void on_client_subscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] on_client_subscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

	@Override
    public void on_client_unsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
        System.err.printf("[Java] on_client_unsubscribe: clientinfo: %s, topic: %s, props: %s\n", clientInfo, topic, props);
    }

    // Sessions
	@Override
    public void on_session_created(ClientInfo clientInfo) {
        System.err.printf("[Java] on_session_created: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void on_session_subscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
        System.err.printf("[Java] on_session_subscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

	@Override
    public void on_session_unsubscribed(ClientInfo clientInfo, Topic topic) {
        System.err.printf("[Java] on_session_unsubscribed: clientinfo: %s, topic: %s\n", clientInfo, topic);
    }

	@Override
    public void on_session_resumed(ClientInfo clientInfo) {
        System.err.printf("[Java] on_session_resumed: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void on_session_discarded(ClientInfo clientInfo) {
        System.err.printf("[Java] on_session_discarded: clientinfo: %s\n", clientInfo);
    }
	
	@Override
    public void on_session_takeovered(ClientInfo clientInfo) {
        System.err.printf("[Java] on_session_takeovered: clientinfo: %s\n", clientInfo);
    }

	@Override
    public void on_session_terminated(ClientInfo clientInfo, Reason reason) {
        System.err.printf("[Java] on_session_terminated: clientinfo: %s, reason: %s\n", clientInfo, reason);
    }

    // Messages
	@Override
    public Message on_message_publish(Message message) {
        System.err.printf("[Java] on_message_publish: message: %s\n", message);
        
        return message;
    }

	@Override
    public void on_message_dropped(Message message, Reason reason) {
        System.err.printf("[Java] on_message_dropped: message: %s, reason: %s\n", message, reason);
    }

	@Override
    public void on_message_delivered(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] on_message_delivered: clientinfo: %s, message: %s\n", clientInfo, message);
    }

	@Override
    public void on_message_acked(ClientInfo clientInfo, Message message) {
        System.err.printf("[Java] on_message_acked: clientinfo: %s, message: %s\n", clientInfo, message);
    }

}
