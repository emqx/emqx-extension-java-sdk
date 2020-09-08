package io.emqx.extension.handler.codec;

// The client info for a EMQ X client
public class ClientInfo implements HandlerParameter {

	// The client connected node name
	public final String node;
	// Client Id
	public final String clientId;
	// Username
	public final String userName;
	// Password
	public final String password;
	// The peer host of client
	public final String peerHost;
	// The peer port of client
	public final int sockPort;
	// The client protocol, it can be 'mqtt' | 'coap' | 'lwm2m' etc.
	public final String protocol;
	// The mountpoint of client
	public final String mountPoint;
	// Is a super user client
	public final boolean isSuperUser;
	// Is a anonymous client
	public final boolean anonymous;
	
	public ClientInfo(String node, String clientId, String userName, String password, String peerHost,
			int sockPort, String protocol, String mountPoint, boolean isSuperUser, boolean anonymous) {
		this.node = node;
		this.clientId = clientId;
		this.userName = userName;
		this.password = password;
		this.peerHost = peerHost;
		this.sockPort = sockPort;
		this.protocol = protocol;
		this.mountPoint = mountPoint;
		this.isSuperUser = isSuperUser;
		this.anonymous = anonymous;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ClientInfo (");
		sb.append("node=" + node);
		sb.append(", clientId=" + clientId);
		sb.append(", userName=" + userName);
		sb.append(", password=" + password);
		sb.append(", peerHost=" + peerHost);
		sb.append(", sockPort=" + sockPort);
		sb.append(", protocol=" + protocol);
		sb.append(", mountPoint=" + mountPoint);
		sb.append(", isSuperUser=" + isSuperUser);
		sb.append(", anonymous=" + anonymous);
		sb.append(")");
		
		return sb.toString();
	}
}
