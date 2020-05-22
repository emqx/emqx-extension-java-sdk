package io.emqx.extension.handler.codec;

public class SubscribeOption implements HandlerParameter {

	// Is a new subscription for EMQ X
	public final boolean isNew;

	// Qos level.
	//
	// Value: 0 | 1 | 2
	public final int qos;

	// No Local option.
	//
	// See: MQTT v5.0 - 3.8.3.1 Subscription Options
	public final int nl;

	// Retain As Published option
	//
	// See: MQTT v5.0 - 3.8.3.1 Subscription Options
	public final int rap;

	// Retain Handling option
	//
	// See: MQTT v5.0 - 3.8.3.1 Subscription Options
	public final int rh;
	
	public SubscribeOption(boolean isNew, int nl, int qos, int rap, int rh) {
		this.isNew = isNew;
		this.nl = nl;
		this.qos = qos;
		this.rap = rap;
		this.rh = rh;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SubscribeOption (");
		sb.append("isNew=" + isNew);
		sb.append(", nl=" + nl);
		sb.append(", qos=" + qos);
		sb.append(", rap=" + rap);
		sb.append(", rh=" + rh);
		sb.append(")");
		
		return sb.toString();
	}
}
