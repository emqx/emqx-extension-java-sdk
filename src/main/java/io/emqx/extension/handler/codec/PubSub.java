package io.emqx.extension.handler.codec;

public class PubSub implements HandlerParameter {
	
	public final String value;
	
	public PubSub(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PubSub (");
		sb.append("value=" + value);
		sb.append(")");
		
		return sb.toString();
	}
}
