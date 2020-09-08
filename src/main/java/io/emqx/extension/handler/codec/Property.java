package io.emqx.extension.handler.codec;

public class Property implements HandlerParameter {
	
	public final String key;
	public final String value;
	
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Property (");
		sb.append("key=" + key);
		sb.append(", value=" + value);
		sb.append(")");
		
		return sb.toString();
	}
}
