package io.emqx.extension.handler.codec;

import java.io.Serializable;

// State class demo
// The State can pass it between Java and Erlang
public class State implements Serializable {

	private static final long serialVersionUID = -8994341646371322513L;
	
	Integer times;

    public State() {
        times = 0;
    }

    public Integer incr() {
        times += 1;
        return times;
    }

    @Override
    public String toString() {
        return String.format("State(times: %d)", times);
    }
}
