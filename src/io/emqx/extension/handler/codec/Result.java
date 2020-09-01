package io.emqx.extension.handler.codec;


import com.erlport.erlang.term.Tuple;

public class Result implements HandlerReturn {
	
	public final boolean value;
	
	public Result(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Result (");
		sb.append("value=" + value);
		sb.append(")");
		
		return sb.toString();
	}

	@Override
	public Object encode(ResultCode rc) {
		return Tuple.two(rc.getValue(), value);
	}
}
