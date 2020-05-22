package io.emqx.extension.handler.codec;

import java.util.ArrayList;
import java.util.List;

import erlport.terms.Tuple;

// The Hook specific
public class HookSpec implements Tupleable {
	// The hook point
	private String hook;
	// Callback module/class name
	private String module;
	// Callback function name
	private String function;
	// The options dictionary for hook
	private ActionOption[] actionOptions;
	
	public HookSpec(String hook, String module, String function) {
		this.hook = hook;
		this.module = module;
		this.function = function;
	}
	
	public HookSpec(String hook, String module, String function, ActionOption...actionOptions) {
		this(hook, module, function);
		this.actionOptions = actionOptions;
	}
	
	@Override
	public Tuple toTuple() {
		Tuple tuple = null;
		if (actionOptions == null) {
			tuple = Tuple.three(hook, module, function);
		} else {
			List<Tuple> actionOptTuples = new ArrayList<>();
			for (ActionOption opt : actionOptions) {
				actionOptTuples.add(opt.toTuple());
			}
			
			tuple = Tuple.four(hook, module, function, actionOptTuples);
		}
		
		return tuple;
	}
	
}
