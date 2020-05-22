package io.emqx.extension.handler.codec;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import io.emqx.extension.exceptions.InvalidParameterException;
import erlport.terms.Tuple;

public class Decoder {
	
	public <T extends HandlerParameter> List<T> decodeList(Class<T> clazz, Object listObj) throws InvalidParameterException {
		if (!(listObj instanceof List)) {
			String error = MessageFormat.format("Invalid List project: {0}", listObj);
			throw new InvalidParameterException(error);
		}
		List<?> list = (List<?>)listObj;
		List<T> result = new ArrayList<>();
		for (int i=0; i<list.size(); i++) {
			T param = decode(clazz, list.get(i));
			result.add(param);
		}
		return result;
	}
	
	public <T extends HandlerParameter> T decode(Class<T> clazz, Object paramObj) throws InvalidParameterException {
		if (clazz.equals(ConnInfo.class)) {
			return clazz.cast(decodeConnInfo(paramObj));
		}
		if (clazz.equals(ClientInfo.class)) {
			return clazz.cast(decodeClientInfo(paramObj));
		}
		if (clazz.equals(Message.class)) {
			return clazz.cast(decodeMessage(paramObj));
		}
		if (clazz.equals(Property.class)) {
			return clazz.cast(decodeProperty(paramObj));
		}
		if (clazz.equals(PubSub.class)) {
			return clazz.cast(decodePubSub(paramObj));
		}
		if (clazz.equals(Reason.class)) {
			return clazz.cast(decodeReason(paramObj));
		}
		if (clazz.equals(ReturnCode.class)) {
			return clazz.cast(decodeReturnCode(paramObj));
		}
		if (clazz.equals(SubscribeOption.class)) {
			return clazz.cast(decodeSubOption(paramObj));
		}
		if (clazz.equals(Topic.class)) {
			return clazz.cast(decodeTopic(paramObj));
		}
		if (clazz.equals(TopicFilter.class)) {
			return clazz.cast(decodeTopicFilter(paramObj));
		}
		
		return null;
	}
	
	private ConnInfo decodeConnInfo(Object paramObj) throws InvalidParameterException {
		String node = null;			// Atom
		String clientId = null;		// Binary
		String userName = null;		// Binary
		String peerHost = null;		// Binary
		int sockPort = -1;			// int
		String protoName = null;	// Binary
		int protoVersion = -1;		// int
		int keepalive = -1;			// int
		
		try {
			List<Tuple> contents = (List<Tuple>)paramObj;
			if (contents.size() != 8) {
				String error = MessageFormat.format("Invalid ConnInfo: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			for (Tuple tuple : contents) {
				String key = CodecUtil.atom2String(tuple.get(0));
				switch (key) {
				case "node":
					node = CodecUtil.atom2String(tuple.get(1));
					break;
				case "clientid":
					clientId = CodecUtil.binary2String(tuple.get(1));
					break;
				case "username":
					userName = CodecUtil.binary2String(tuple.get(1));
					break;
				case "peerhost":
					peerHost = CodecUtil.binary2String(tuple.get(1));
					break;
				case "sockport":
					sockPort = (Integer) tuple.get(1);
					break;
				case "proto_name":
					protoName = CodecUtil.binary2String(tuple.get(1));
					break;
				case "proto_ver":
					protoVersion = (Integer) tuple.get(1);
					break;
				case "keepalive":
					keepalive = (Integer) tuple.get(1);
					break;
				}
			}
			
			return new ConnInfo(node, clientId, userName, peerHost, sockPort, protoName, protoVersion, keepalive);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid ConnInfo: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private ClientInfo decodeClientInfo(Object paramObj) throws InvalidParameterException {
		String node = null;			// Atom
		String clientId = null;		// Binary
		String userName = null;		// Binary
		String password = null;		// Binary
		String peerHost = null;		// Binary
		int sockPort = -1;			// int
		String protocol = null;		// Atom
		String mountPoint = null;	// Binary
		Boolean isSuperUser = null;	// boolean
		Boolean anonymous = null;	// boolean
		
		try {
			List<Tuple> contents = (List<Tuple>)paramObj;
			if (contents.size() != 10) {
				String error = MessageFormat.format("Invalid ClientInfo: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			for (Tuple tuple : contents) {
				String key = CodecUtil.atom2String(tuple.get(0));
				switch (key) {
				case "node":
					node = CodecUtil.atom2String(tuple.get(1));
					break;
				case "clientid":
					clientId = CodecUtil.binary2String(tuple.get(1));
					break;
				case "username":
					userName = CodecUtil.binary2String(tuple.get(1));
					break;
				case "password":
					password = CodecUtil.binary2String(tuple.get(1));
					break;
				case "peerhost":
					peerHost = CodecUtil.binary2String(tuple.get(1));
					break;
				case "sockport":
					sockPort = (Integer) tuple.get(1);
					break;
				case "protocol":
					protocol = CodecUtil.atom2String(tuple.get(1));
					break;
				case "mountpoint":
					mountPoint = CodecUtil.binary2String(tuple.get(1));
					break;
				case "is_superuser":
					isSuperUser = (Boolean) tuple.get(1);
					break;
				case "anonymous":
					anonymous = (Boolean) tuple.get(1);
					break;
				}
			}
			
			return new ClientInfo(node, clientId, userName, password, peerHost, sockPort, 
					protocol, mountPoint, isSuperUser, anonymous);
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid ClientInfo: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private Message decodeMessage(Object paramObj) throws InvalidParameterException {
		String node = null;		// Atom
		String id = null;		// Binary
		int qos = -1;			// int
		String from = null;		// Binary
		String topic = null;	// Binary
		byte[] payload = null;	// Binary
		long timestamp = -1;
		
		try {
			List<Tuple> contents = (List<Tuple>)paramObj;
			if (contents.size() != 7) {
				String error = MessageFormat.format("Invalid Message: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			for (Tuple tuple : contents) {
				String key = CodecUtil.atom2String(tuple.get(0));
				switch (key) {
				case "node":
					node = CodecUtil.atom2String(tuple.get(1));
					break;
				case "id":
					id = CodecUtil.binary2String(tuple.get(1));
					break;
				case "qos":
					qos = (Integer)(tuple.get(1));
					break;
				case "from":
					from = CodecUtil.binary2String(tuple.get(1));
					break;
				case "topic":
					topic = CodecUtil.binary2String(tuple.get(1));
					break;
				case "payload":
					payload = CodecUtil.binary2ByteArray(tuple.get(1));
					break;
				case "timestamp":
					timestamp = ((BigInteger)(tuple.get(1))).longValue();
					break;
				}
			}
			
			return new Message(node, id, qos, from, topic, payload, timestamp);
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			String error = MessageFormat.format("Invalid Message: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}

	private Property decodeProperty(Object paramObj) throws InvalidParameterException {
		String key = null;
		String value = null;
		
		try {
			Tuple tuple = (Tuple)paramObj;
			if (tuple.length() != 2) {
				String error = MessageFormat.format("Invalid Property: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			key = CodecUtil.atom2String(tuple.get(0));
			value = CodecUtil.binary2String(tuple.get(1));
			
			return new Property(key, value);
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid Property: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private PubSub decodePubSub(Object paramObj) throws InvalidParameterException {
		try {
			String value = CodecUtil.atom2String(paramObj);
			return new PubSub(value);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid PubSub: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private Reason decodeReason(Object paramObj) throws InvalidParameterException {
		try {
			String value = CodecUtil.binary2String(paramObj);
			return new Reason(value);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid Reason: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private ReturnCode decodeReturnCode(Object paramObj) throws InvalidParameterException {
		try {
			String value = CodecUtil.atom2String(paramObj);
			return new ReturnCode(value);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid ReturnCode: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private SubscribeOption decodeSubOption(Object paramObj) throws InvalidParameterException {
		Boolean isNew = null;
		int nl  = -1;
		int qos = -1;
		int rap = -1;
		int rh = -1;
		
		try {
			List<Tuple> contents = (List<Tuple>)paramObj;
			if (contents.size() != 5) {
				String error = MessageFormat.format("Invalid SubscribeOption: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			for (Tuple tuple : contents) {
				String key = CodecUtil.atom2String(tuple.get(0));
				switch (key) {
				case "is_new":
					isNew = (Boolean)(tuple.get(1));
					break;
				case "nl":
					nl = (Integer)(tuple.get(1));
					break;
				case "qos":
					qos = (Integer)(tuple.get(1));
					break;
				case "rap":
					rap = (Integer)(tuple.get(1));
					break;
				case "rh":
					rh = (Integer)(tuple.get(1));
					break;
				}
			}
			
			return new SubscribeOption(isNew, nl, qos, rap, rh);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid SubscribeOption: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private Topic decodeTopic(Object paramObj) throws InvalidParameterException {
		try {
			String value = CodecUtil.binary2String(paramObj);
			return new Topic(value);
			
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid Topic: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	private TopicFilter decodeTopicFilter(Object paramObj) throws InvalidParameterException {
		String topic = null;
		int qos = -1;
		
		try {
			Tuple tuple = (Tuple)paramObj;
			if (tuple.length() != 2) {
				String error = MessageFormat.format("Invalid TopicFilter: {0}", paramObj);
				throw new InvalidParameterException(error);
			}
			
			topic = CodecUtil.binary2String(tuple.get(0));
			qos = (Integer)(tuple.get(1));
			
			return new TopicFilter(topic, qos);
		} catch (Exception e) {
			String error = MessageFormat.format("Invalid TopicFilter: {0}", paramObj);
			throw new InvalidParameterException(error);
		}
	}
	
	public static void main (String args[]) throws Exception {
		Decoder decoder = new Decoder();
		List<Object> list = new ArrayList<Object>();
		List<Property> props = decoder.decodeList(Property.class, list);
	}
	
}
