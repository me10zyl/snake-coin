package snake;

import java.net.Proxy;
import java.net.Proxy.Type;

public class ProxyIp {
	private Proxy.Type type;
	private String ip;
	private int port;

	public ProxyIp(Type type, String ip, int port) {
		super();
		this.type = type;
		this.ip = ip;
		this.port = port;
	}

	public Proxy.Type getType() {
		return type;
	}

	public void setType(Proxy.Type type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
