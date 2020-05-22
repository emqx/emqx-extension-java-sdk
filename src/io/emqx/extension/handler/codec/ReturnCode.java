package io.emqx.extension.handler.codec;

public class ReturnCode implements HandlerParameter {
	
	public final String value;
	
	public ReturnCode(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ReturnCode (");
		sb.append("value=" + value);
		sb.append(")");
		
		return sb.toString();
	}
}
