package io.emqx.extension.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.emqx.extension.handler.codec.ActionOption;

public final class ActionOptionConfig {
	
	private static final String EMPTY_VALUE = "";
	
	private Properties properties = new Properties();
	
	public class Keys {
		public static final String MESSAGE_PUBLISH_TOPICS = "MESSAGE_PUBLISH_TOPICS";
		public static final String MESSAGE_DELIVERED_TOPICS = "MESSAGE_DELIVERED_TOPICS";
		public static final String MESSAGE_ACKED_TOPICS = "MESSAGE_DELIVERED_TOPICS";
		public static final String MESSAGE_DROPPED_TOPICS = "MESSAGE_DELIVERED_TOPICS";
	}
	
	public static ActionOptionConfig getDefault() {
		ActionOptionConfig option = new ActionOptionConfig();
		option.set(Keys.MESSAGE_PUBLISH_TOPICS, "#");
		option.set(Keys.MESSAGE_DELIVERED_TOPICS, "#");
		option.set(Keys.MESSAGE_ACKED_TOPICS, "#");
		option.set(Keys.MESSAGE_DROPPED_TOPICS, "#");
		
		return option;
	}
	
	public ActionOptionConfig set(String key, String value) {
		if (key == null) {
			throw new NullPointerException("key must not be null");
		} else {
			properties.put(key, value);
			return this;
		}
	}
	
	public ActionOption buildOption(String key, String actionKey) {
		List<String> options = new ArrayList<>();
		String optionValue = properties.getProperty(actionKey, EMPTY_VALUE);
		if (!optionValue.trim().equals(EMPTY_VALUE)) {
			String[] optionVals = optionValue.split(",");
			for (String val: optionVals) {
				options.add(val);
			}
		}
		ActionOption actionOpt = new ActionOption(key, options);
		
        return actionOpt;
	}

}
