package io.emqx.extension.handler.codec;

import com.erlport.erlang.term.Tuple;

public interface Tupleable {

    Tuple toTuple();

}
