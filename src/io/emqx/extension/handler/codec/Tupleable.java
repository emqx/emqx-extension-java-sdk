package io.emqx.extension.handler.codec;

import erlport.terms.Tuple;

public interface Tupleable {
	
	public Tuple toTuple();

}
