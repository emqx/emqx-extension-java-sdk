package io.emqx.extension.handler.codec;

import java.util.ArrayList;
import java.util.List;

import erlport.terms.Tuple;

// {0 | 1, [{HookName, CallModule, CallFunction, Opts}]}
public class Initializer implements HandlerReturn {
	
	public static class Bootstrap implements Tupleable {

		private List<HookSpec> hookSpecs;
		private State state;
		
		public Bootstrap(List<HookSpec> hookSpecs, State state) {
			this.hookSpecs = hookSpecs;
			this.state = state;
		}

		@Override
		public Tuple toTuple() {
			List<Tuple> specs = new ArrayList<>();
			for (HookSpec hookSpec : hookSpecs) {
				specs.add(hookSpec.toTuple());
			}
			return Tuple.two(specs, state);
		}
	}

	private Bootstrap bootstrap;
	
	public Initializer(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public Tuple encode(ResultCode rc) {
		return Tuple.two(rc.getValue(), bootstrap.toTuple());
	}
}
