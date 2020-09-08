package io.emqx.extension.handler.codec;

public interface HandlerReturn {
	
	public Object encode(ResultCode rc);

}
