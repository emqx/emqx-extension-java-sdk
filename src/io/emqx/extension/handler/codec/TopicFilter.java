package io.emqx.extension.handler.codec;

public class TopicFilter implements HandlerParameter {

	public final String topic;
	public final int qos;
	
	public TopicFilter(String topic, int qos) {
		this.topic = topic;
		this.qos = qos;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TopicFilter (");
		sb.append("topic=" + topic);
		sb.append(", qos=" + qos);
		sb.append(")");
		
		return sb.toString();
	}
}
