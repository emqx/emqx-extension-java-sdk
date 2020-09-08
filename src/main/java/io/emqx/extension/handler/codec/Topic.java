package io.emqx.extension.handler.codec;

public class Topic implements HandlerParameter {
	
	public final String value;
	
	public Topic(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Topic (");
		sb.append("value=" + value);
		sb.append(")");
		
		return sb.toString();
	}
}
