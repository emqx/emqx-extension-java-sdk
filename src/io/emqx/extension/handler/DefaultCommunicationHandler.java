package io.emqx.extension.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.emqx.extension.exceptions.InvalidParameterException;
import io.emqx.extension.handler.codec.ActionOption;
import io.emqx.extension.handler.codec.ClientInfo;
import io.emqx.extension.handler.codec.ConnInfo;
import io.emqx.extension.handler.codec.Decoder;
import io.emqx.extension.handler.codec.HookSpec;
import io.emqx.extension.handler.codec.Initializer;
import io.emqx.extension.handler.codec.Message;
import io.emqx.extension.handler.codec.Property;
import io.emqx.extension.handler.codec.PubSub;
import io.emqx.extension.handler.codec.Reason;
import io.emqx.extension.handler.codec.ResultCode;
import io.emqx.extension.handler.codec.ReturnCode;
import io.emqx.extension.handler.codec.State;
import io.emqx.extension.handler.codec.SubscribeOption;
import io.emqx.extension.handler.codec.Topic;
import io.emqx.extension.handler.codec.TopicFilter;
import erlport.terms.Tuple;

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
    public void on_client_connect_raw(Object connInfoObj, Object propsObj, Object stateObj) {
    		try {
    			ConnInfo connInfo = decoder.decode(ConnInfo.class, connInfoObj);
    			List<Property> props = decoder.decodeList(Property.class, propsObj);
        	
        		on_client_connect(connInfo, props.toArray(new Property[props.size()]));
        		
    		} catch (InvalidParameterException e) {
    			e.printStackTrace(System.err);
    		}
    }

    public void on_client_connack_raw(Object connInfoObj, Object rcObj, Object propsObj, Object stateObj) {
    		try {
			ConnInfo connInfo = decoder.decode(ConnInfo.class, connInfoObj);
			ReturnCode rc = decoder.decode(ReturnCode.class, rcObj);
	    		List<Property> props = decoder.decodeList(Property.class, propsObj);
	    	
	    		on_client_connack(connInfo, rc, props.toArray(new Property[props.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_client_connected_raw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
	    		on_client_connected(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_client_disconnected_raw(Object clientInfoObj, Object reasonObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
	    		on_client_disconnected(clientInfo, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}    	
    }

    public Object on_client_authenticate_raw(Object clientInfoObj, Object authresultObj, Object stateObj) {
		try {
			ClientInfo clientInfo =decoder.decode(ClientInfo.class, clientInfoObj);
			boolean authresult = (Boolean) authresultObj;
	    	
	    		boolean result = on_client_authenticate(clientInfo, authresult);
	    		return Tuple.two(ResultCode.SUC.getValue(), result);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), false);
		}
    }

    public Object on_client_check_acl_raw(Object clientInfoObj, Object pubsubObj, Object topicObj, Object resultObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			PubSub pubsub = decoder.decode(PubSub.class, pubsubObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
			boolean rs = (Boolean) resultObj;
	    	
	    		boolean result = on_client_check_acl(clientInfo, pubsub, topic, rs);
	    		return Tuple.two(ResultCode.SUC.getValue(), result);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), false);
		}
    }

    public void on_client_subscribe_raw(Object clientInfoObj, Object propsObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			List<Property> props = decoder.decodeList(Property.class, propsObj);
			List<TopicFilter> topicFilters = decoder.decodeList(TopicFilter.class, topicObj);
	    	
			on_client_subscribe(clientInfo, props.toArray(new Property[props.size()]), 
					topicFilters.toArray(new TopicFilter[topicFilters.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_client_unsubscribe_raw(Object clientInfoObj, Object propsObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			List<Property> props = decoder.decodeList(Property.class, propsObj);
			List<TopicFilter> topicFilters = decoder.decodeList(TopicFilter.class, topicObj);
	    	
			on_client_unsubscribe(clientInfo, props.toArray(new Property[props.size()]), 
					topicFilters.toArray(new TopicFilter[topicFilters.size()]));
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    // Sessions
    public void on_session_created_raw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			on_session_created(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_session_subscribed_raw(Object clientInfoObj, Object topicObj, Object optsObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
			SubscribeOption opts = decoder.decode(SubscribeOption.class, optsObj);
	    	
			on_session_subscribed(clientInfo, topic, opts);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_session_unsubscribed_raw(Object clientInfoObj, Object topicObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Topic topic = decoder.decode(Topic.class, topicObj);
	    	
			on_session_unsubscribed(clientInfo, topic);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_session_resumed_raw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			on_session_resumed(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_session_discarded_raw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			on_session_discarded(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}
    }

    public void on_session_takeovered_raw(Object clientInfoObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
	    	
			on_session_takeovered(clientInfo);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}   	
    }

    public void on_session_terminated_raw(Object clientInfoObj, Object reasonObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
			on_session_terminated(clientInfo, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}       	
    }

    // Messages
    public Object on_message_publish_raw(Object messageObj, Object stateObj) {
		try {
			Message message = decoder.decode(Message.class, messageObj);
	    	
			Message result = on_message_publish(message);
			return result.encode(ResultCode.SUC);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
			return Tuple.two(ResultCode.FAIL.getValue(), new ArrayList<>());
		}   
    }

    public void on_message_dropped_raw(Object messageObj, Object reasonObj, Object stateObj) {
		try {
			Message message = decoder.decode(Message.class, messageObj);
			Reason reason = decoder.decode(Reason.class, reasonObj);
	    	
			on_message_dropped(message, reason);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		}  
    }

    public void on_message_delivered_raw(Object clientInfoObj, Object messageObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Message message = decoder.decode(Message.class, messageObj);
	    	
			on_message_delivered(clientInfo, message);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		} 
    }

    public void on_message_acked_raw(Object clientInfoObj, Object messageObj, Object stateObj) {
		try {
			ClientInfo clientInfo = decoder.decode(ClientInfo.class, clientInfoObj);
			Message message = decoder.decode(Message.class, messageObj);
	    	
			on_message_acked(clientInfo, message);
	    		
		} catch (InvalidParameterException e) {
			e.printStackTrace(System.err);
		} 
    }	
	
    /* Raw callbacks end */
    
    /* Overriden callbacks start */
	
	// Clients
    public void on_client_connect(ConnInfo connInfo, Property[] props) {
    }

    public void on_client_connack(ConnInfo connInfo, ReturnCode rc, Property[] props) {
    }

    public void on_client_connected(ClientInfo clientInfo) {
    }

    public void on_client_disconnected(ClientInfo clientInfo, Reason reason) {
    }

    public boolean on_client_authenticate(ClientInfo clientInfo, boolean authresult) {
        return true;
    }

    public boolean on_client_check_acl(ClientInfo clientInfo, PubSub pubsub, Topic topic, boolean result) {
        return true;
    }

    public void on_client_subscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    }

    public void on_client_unsubscribe(ClientInfo clientInfo, Property[] props, TopicFilter[] topic) {
    }

    // Sessions
    public void on_session_created(ClientInfo clientInfo) {
    }

    public void on_session_subscribed(ClientInfo clientInfo, Topic topic, SubscribeOption opts) {
    }

    public void on_session_unsubscribed(ClientInfo clientInfo, Topic topic) {
    }

    public void on_session_resumed(ClientInfo clientInfo) {
    }

    public void on_session_discarded(ClientInfo clientInfo) {
    }

    public void on_session_takeovered(ClientInfo clientInfo) {
    }

    public void on_session_terminated(ClientInfo clientInfo, Reason reason) {
    }

    // Messages
    public Message on_message_publish(Message message) {
        return message;
    }

    public void on_message_dropped(Message message, Reason reason) {
    }

    public void on_message_delivered(ClientInfo clientInfo, Message message) {
    }

    public void on_message_acked(ClientInfo clientInfo, Message message) {
    }	
	
    /* Overriden callbacks end */
    
	private HookSpec addHookSpec(List<HookSpec> actions, String module, String hookName, ActionOption... actionOpts) {

		String function = "on_" + hookName;
		String cb = function + "_raw";

		//only add those callback functions which are actually declared in the sub class
		if (!declaredMethods.contains(function)) {
			return null;
		}
		
		if (actionOpts == null || actionOpts.length == 0) {
			actionOpts = null;
		}
		HookSpec hookSpec = new HookSpec(hookName, module, cb, actionOpts);
		actions.add(hookSpec);
		return hookSpec;
	}
}
	
