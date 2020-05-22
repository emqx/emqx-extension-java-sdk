package io.emqx.extension.handler.codec;

public class Reason implements HandlerParameter {

	public final String value;
	
	public Reason(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reason (");
		sb.append("value=" + value);
		sb.append(")");
		
		return sb.toString();
	}
}
