package io.emqx.extension.handler.codec;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.erlport.erlang.term.Atom;
import com.erlport.erlang.term.Binary;
import com.erlport.erlang.term.Tuple;

public class Message implements HandlerParameter, HandlerReturn {
	
	public String node;
	public String id;
	public int qos;
	public String from;
	public String topic;
	public byte[] payload;
	public long timestamp;

	public Message(String node, String id, int qos, String from, String topic, byte[] payload, long timestamp) {
		this.node = node;
		this.id = id;
		this.qos = qos;
		this.from = from;
		this.topic = topic;
		this.payload = payload;
		this.timestamp = timestamp;
	}

	@Override
	public Tuple encode(ResultCode rc) {
		List<Tuple> tuples = new ArrayList<>();
		
		tuples.add(Tuple.two(new Atom("node"), new Atom(node)));
		tuples.add(Tuple.two(new Atom("id"), new Binary(id)));
		tuples.add(Tuple.two(new Atom("qos"), qos));
		tuples.add(Tuple.two(new Atom("from"), new Binary(from)));
		tuples.add(Tuple.two(new Atom("topic"), new Binary(topic)));
		tuples.add(Tuple.two(new Atom("payload"), new Binary(payload)));
		tuples.add(Tuple.two(new Atom("timestamp"), BigInteger.valueOf(timestamp)));
		
		return Tuple.two(rc.getValue(), tuples);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message (");
		sb.append("node=" + node);
		sb.append(", id=" + id);
		sb.append(", qos=" + qos);
		sb.append(", from=" + from);
		sb.append(", topic=" + topic);
		sb.append(", payload=" + payload);
		sb.append(", timestamp=" + timestamp);
		sb.append(")");
		
		return sb.toString();
	}
}
