package snake;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class Application {
	public static void main(String args[]) throws MalformedURLException, IOException {
		LocalReader localReader = new LocalReader();
		List<ProxyIp> ips = localReader.readHttpProxies();
		Snake snake = new Snake(ips);
		setParams(snake, localReader);
		Thread thread = new Thread(snake);
		thread.start();
	}
	
	private static void setParams(Snake snake, LocalReader localReader){
		snake.setGap(localReader.getGap());
		snake.setTimeout(localReader.getTimeout());
		snake.setUrl(localReader.getUrl());
		snake.setReadTimeout(localReader.getReadTimeout());
	}

}
