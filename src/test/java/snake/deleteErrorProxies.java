package snake;

import java.io.IOException;

import org.junit.Test;

public class deleteErrorProxies {

	@Test
	public void deleteErrorProxies() throws IOException{
		LocalReader localReader = new LocalReader();
		localReader.deleteErrorProxies();
	}
}
