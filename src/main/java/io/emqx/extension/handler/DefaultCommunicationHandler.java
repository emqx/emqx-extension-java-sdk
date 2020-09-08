package io.emqx.extension.handler;

import com.erlport.erlang.term.Tuple;
import io.emqx.extension.exceptions.InvalidParameterException;
import io.emqx.extension.handler.codec.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultCommunicationHandler implements CommunicationHandler {
	
	private Decoder decoder = new Decoder();
	private Set<String> declaredMethods = new HashSet<>();
	
	public DefaultCommunicationHandler() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method m : methods) {
			declaredMethods.add(m.getName());
		}
	}
	
	@Override
	public Object init() {
		System.err.printf("Initiate driver...\n");
		
		ActionOptionConfig actionOption = getActionOption();
		
        String module = this.getClass().getCanonicalName();
        
        //Add hook specs
        List<HookSpec> actions = new ArrayList<>();
        addHookSpec(actions, module, "client_connect");
		addHookSpec(actions, module, "client_connack");
		addHookSpec(actions, module, "client_connected");
		addHookSpec(actions, module, "client_disconnected");
		addHookSpec(actions, module, "client_authenticate");
		addHookSpec(actions, module, "client_check_acl");
		addHookSpec(actions, module, "client_subscribe");
		addHookSpec(actions, module, "client_unsubscribe");
		
		addHookSpec(actions, module, "session_created");
		addHookSpec(actions, module, "session_subscribed");
		addHookSpec(actions, module, "session_unsubscribed");
		addHookSpec(actions, module, "session_resumed");
		addHookSpec(actions, module, "session_discarded");
		addHookSpec(actions, module, "session_takeovered");
		addHookSpec(actions, module, "session_terminated");
		
		addHookSpec(actions, module, "message_publish", 
				actionOption.buildOption("topics", ActionOptionConfig.Keys.MESSAGE_PUBLISH_TOPICS));
		addHookSpec(actions, module, "message_delivered",
				actionOption.buildOption("topics", ActionOptionConfig.Keys.MESSAGE_DELIVERED_TOPICS));
		addHookSpec(actions, module, "message_acked", 
				actionOption.buildOption("topics", ActionOptionConfig.Keys.MESSAGE_ACKED_TOPICS));
		addHookSpec(actions, module, "message_dropped",
				actionOption.buildOption("topics", ActionOptionConfig.Keys.MESSAGE_DROPPED_TOPICS));
        	
		//create state
		State state = new State();

		Initializer.Bootstrap bootstrap = new Initializer.Bootstrap(actions, state);
		Initializer result = new Initializer(bootstrap);
		
//		System.err.printf(result.encode(ResultCode.SUC).toString());
		return result.encode(ResultCode.SUC);
	}

	@Override
	public void deinit() {
		declaredMethods.clear();
	}
	
	public ActionOptionConfig getActionOption() {
		return ActionOptionConfig.getDefault();
	}
	
	/* Raw callbacks start */
	
	// Clients
    public void onClientConnectRaw(Object connInfoObj, Object propsObj, Object stateObj) {
    		try {
    			ConnInfo connInfo = decoder.decode(ConnInfo.class, connInfoObj);
    			List<Property> props = decoder.decodeList(Property.class, propsObj);
        	
        		onClientConnect(connInfo, props.toArray(new Property[props.size()]));
        		
    		} catch (InvalidParameterException e) {
    			e.printStackTrace(System.err);
    		}
    }

    public void onClientConnackRaw(Object connInfoObj, Object rcObj, Object propsObj, Object stateObj) {
    		try {
			ConnInfo connInfo = decoder.decode(ConnInfo.class, connInfoObj);
			ReturnCode rc = decoder.decode(ReturnCode.class, rcObj);
	    		List<Property> props = decoder.decodeList(Property.class, propsObj);
	    	
	    		onClientConnack(connInfo, rc, props.toArray(new Property[props.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onClientConnectedRaw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
	    		onClientConnected(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onClientDisconnectedRaw(Object clientInfoObj, Object reasonObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
	    		onClientDisconnected(clientInfo, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}    	
    }

    public Object onClientAuthenticateRaw(Object clientInfoObj, Object authresultObj, Object stateObj) {
		try {
			ClientInfo clientInfo =decoder.decode(ClientInfo.class, clientInfoObj);
			boolean authresult = (Boolean) authresultObj;
	    	
	    		boolean result = onClientAuthenticate(clientInfo, authresult);
	    		return Tuple.two(ResultCode.SUC.getValue(), result);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), false);
		}
    }

    public Object onClientCheckAclRaw(Object clientInfoObj, Object pubsubObj, Object topicObj, Object resultObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			PubSub pubsub = decoder.decode(PubSub.class, pubsubObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
			boolean rs = (Boolean) resultObj;
	    	
	    		boolean result = onClientCheckAcl(clientInfo, pubsub, topic, rs);
	    		return Tuple.two(ResultCode.SUC.getValue(), result);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), false);
		}
    }

    public void onClientSubscribeRaw(Object clientInfoObj, Object propsObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			List<Property> props = decoder.decodeList(Property.class, propsObj);
			List<TopicFilter> topicFilters = decoder.decodeList(TopicFilter.class, topicObj);
	    	
			onClientSubscribe(clientInfo, props.toArray(new Property[props.size()]), 
					topicFilters.toArray(new TopicFilter[topicFilters.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onClientUnsubscribeRaw(Object clientInfoObj, Object propsObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			List<Property> props = decoder.decodeList(Property.class, propsObj);
			List<TopicFilter> topicFilters = decoder.decodeList(TopicFilter.class, topicObj);
	    	
			onClientUnsubscribe(clientInfo, props.toArray(new Property[props.size()]), 
					topicFilters.toArray(new TopicFilter[topicFilters.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    // Sessions
    public void onSessionCreatedRaw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			onSessionCreated(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onSessionSubscribedRaw(Object clientInfoObj, Object topicObj, Object optsObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
			SubscribeOption opts = decoder.decode(SubscribeOption.class, optsObj);
	    	
			onSessionSubscribed(clientInfo, topic, opts);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onSessionUnsubscribedRaw(Object clientInfoObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
	    	
			onSessionUnsubscribed(clientInfo, topic);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onSessionResumedRaw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			onSessionResumed(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onSessionDiscardedRaw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			onSessionDiscarded(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void onSessionTakeoveredRaw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			onSessionTakeovered(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}   	
    }

    public void onSessionTerminatedRaw(Object clientInfoObj, Object reasonObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
			onSessionTerminated(clientInfo, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}       	
    }

    // Messages
    public Object onMessagePublishRaw(Object messageObj, Object stateObj) {
		try {
			Message message = decoder.decode(Message.class, messageObj);
	    	
			Message result = onMessagePublish(message);
			return result.encode(ResultCode.SUC);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), new ArrayList<>());
		}   
    }

    public void onMessageDroppedRaw(Object messageObj, Object reasonObj, Object stateObj) {
		try {
			Message message = decoder.decode(Message.class, messageObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
			onMessageDropped(message, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}  
    }

    public void onMessageDeliveredRaw(Object clientInfoObj, Object messageObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Message message = decoder.decode(Message.class, messageObj);
	    	
			onMessageDelivered(clientInfo, message);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		} 
    }

    public void onMessageAckedRaw(Object clientInfoObj, Object messageObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Message message = decoder.decode(Message.class, messageObj);
	    	
			onMessageAcked(clientInfo, message);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		} 
    }	
	
    /* Raw callbacks end */
    
    /* Deprecated overriden callbacks start */
	
	// Clients
    @Deprecated
    public void on_client_connect(ConnInfo connInfo, Property[] props) {
    }

    @Deprecated
    public void on_client_connack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
    }

    @Deprecated
    public void on_client_connected(ClientInfo clientInfo) {
    }

    @Deprecated
    public void on_client_disconnected(ClientInfo clientInfo, Reason reason) {
    }

    @Deprecated
    public boolean on_client_authenticate(ClientInfo clientInfo, boolean authresult) {
        return true;
    }

    @Deprecated
    public boolean on_client_check_acl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        return true;
    }

    @Deprecated
    public void on_client_subscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    }

    @Deprecated
    public void on_client_unsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    }

    // Sessions
    @Deprecated
    public void on_session_created(ClientInfo clientInfo) {
    }

    @Deprecated
    public void on_session_subscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
    }

    @Deprecated
    public void on_session_unsubscribed(ClientInfo clientInfo, Topic topic) {
    }

    @Deprecated
    public void on_session_resumed(ClientInfo clientInfo) {
    }

    @Deprecated
    public void on_session_discarded(ClientInfo clientInfo) {
    }

    @Deprecated
    public void on_session_takeovered(ClientInfo clientInfo) {
    }

    @Deprecated
    public void on_session_terminated(ClientInfo clientInfo, Reason reason) {
    }

    // Messages
    @Deprecated
    public Message on_message_publish(Message message) {
        return message;
    }

    @Deprecated
    public void on_message_dropped(Message message, Reason reason) {
    }

    @Deprecated
    public void on_message_delivered(ClientInfo clientInfo, Message message) {
    }

    @Deprecated
    public void on_message_acked(ClientInfo clientInfo, Message message) {
    }	
	
    /* Deprecated overriden callbacks end */
    
    /* Active overriden callbacks start */
    // Clients
    public void onClientConnect(ConnInfo connInfo, Property[] props) {
    		on_client_connect(connInfo, props);
    }

    public void onClientConnack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
    		on_client_connack(connInfo, rc, props);
    }

    public void onClientConnected(ClientInfo clientInfo) {
    		on_client_connected(clientInfo);
    }

    public void onClientDisconnected(ClientInfo clientInfo, Reason reason) {
    		on_client_disconnected(clientInfo, reason);
    }

    public boolean onClientAuthenticate(ClientInfo clientInfo, boolean authresult) {
        return on_client_authenticate(clientInfo, authresult);
    }

    public boolean onClientCheckAcl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        return on_client_check_acl(clientInfo, pubsub, topic, result);
    }

    public void onClientSubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    		on_client_subscribe(clientInfo, props, topic);
    }

    public void onClientUnsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    		on_client_unsubscribe(clientInfo, props, topic);
    }

    // Sessions
    public void onSessionCreated(ClientInfo clientInfo) {
    		on_session_created(clientInfo);
    }

    public void onSessionSubscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
    		on_session_subscribed(clientInfo, topic, opts);
    }

    public void onSessionUnsubscribed(ClientInfo clientInfo, Topic topic) {
    		on_session_unsubscribed(clientInfo, topic);
    }

    public void onSessionResumed(ClientInfo clientInfo) {
    		on_session_resumed(clientInfo);
    }

    public void onSessionDiscarded(ClientInfo clientInfo) {
    		on_session_discarded(clientInfo);
    }

    public void onSessionTakeovered(ClientInfo clientInfo) {
    		on_session_takeovered(clientInfo);
    }

    public void onSessionTerminated(ClientInfo clientInfo, Reason reason) {
    		on_session_terminated(clientInfo, reason);
    }

    // Messages
    public Message onMessagePublish(Message message) {
        return on_message_publish(message);
    }

    public void onMessageDropped(Message message, Reason reason) {
    		on_message_dropped(message, reason);
    }

    public void onMessageDelivered(ClientInfo clientInfo, Message message) {
    		on_message_delivered(clientInfo, message);
    }

    public void onMessageAcked(ClientInfo clientInfo, Message message) {
    		on_message_acked(clientInfo, message);
    }	
	
    /* Active overriden callbacks end */
    
	private HookSpec addHookSpec(List<HookSpec> actions, String module, String hookName, ActionOption... actionOpts) {

		String oldFunction = "on_" + hookName;
		String function = "on" + toCamelCase(hookName);
		String cb = function + "Raw";

		//only add those callback functions which are actually declared in the sub class
		if (!declaredMethods.contains(function)) {
			if (!declaredMethods.contains(oldFunction)) {
				return null;
			}
		}
		
		if (actionOpts == null || actionOpts.length == 0) {
			actionOpts = null;
		}
		HookSpec hookSpec = new HookSpec(hookName, module, cb, actionOpts);
		actions.add(hookSpec);
		return hookSpec;
	}
	
	private String toCamelCase(String hookName) {
		String[] segs = hookName.split("_");
		StringBuilder sb = new StringBuilder();
		for (String seg : segs) {
			char first = (char) (seg.charAt(0) - 32);
			seg = String.valueOf(first) + seg.substring(1);
			sb.append(seg);
		}
		return sb.toString();
	}
}
