package proxy;

import java.io.IOException;

import snake.LocalReader;

public class ProxiesFinder {

	public static void main(String args[]) throws IOException {
		LocalReader localReader = new LocalReader();
		localReader.ignoreHttpProxy("127.0.0.1");
	}
}
