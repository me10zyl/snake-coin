package snake;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LocalReader {

	private int timeout;
	private int gap;
	private String url;
	private String httpProxiesPath;
	private String lineSeparator;
	private int readTimeout;

	public LocalReader() {
		this.lineSeparator = System.getProperty("line.separator", "\n");
		loadProperties();
	}

	public void loadProperties() {
		Map<String, String> configProperties = getConfigProperties();
		this.timeout = Integer.parseInt(configProperties.get("request.timeout"));
		this.gap = Integer.parseInt(configProperties.get("request.gap"));
		this.httpProxiesPath = configProperties.get("http.proxies.path");
		this.url = configProperties.get("request.url");
		this.readTimeout = Integer.parseInt(configProperties.get("request.read.timeout"));
	}

	public Map<String, String> getConfigProperties() {
		Map<String, String> props = new HashMap<String, String>();
		try {
			Properties p = new Properties();
			p.load(this.getClass().getResourceAsStream("/config.properties"));
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				props.put(key, p.getProperty(key));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}

	public List<ProxyIp> readHttpProxies() throws IOException {
		List<ProxyIp> proxies = new ArrayList<ProxyIp>();
		List<String> lines = readFileLines(httpProxiesPath);
		boolean commenting = false;
		for (String line : lines) {
			if (line.startsWith("-")) {
				continue;
			}
			if (line.startsWith("[deleted]")) {
				continue;
			}
			if (line.startsWith("/*")) {
				commenting = true;
				continue;
			}
			if (line.startsWith("*/")) {
				commenting = false;
				continue;
			}
			if (commenting == true) {
				continue;
			}
			String[] split = line.trim().split(":");
			String ip = split[0];
			int port = 80;
			if (split.length > 1) {
				port = Integer.parseInt(split[1]);
			}
			proxies.add(new ProxyIp(Proxy.Type.HTTP, ip, port));
		}
		return proxies;
	}

	public void ignoreHttpProxy(String ip) throws IOException {
		String path = getPath(httpProxiesPath);
		List<String> lines = readFileLines(path);

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).startsWith(ip)) {
				lines.set(i, "-" + lines.get(i));
			}
		}

		RandomAccessFile raf = new RandomAccessFile(path, "rwd");
		for (String line : lines) {
			raf.write((line + lineSeparator).getBytes());
		}
		raf.close();
	}

	public void unCommentAll() throws IOException {
		String path = getPath(httpProxiesPath);
		List<String> lines = readFileLines(path);

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).startsWith("-") && !lines.get(i).endsWith("-")) {
				lines.set(i, lines.get(i).substring(1, lines.get(i).length()));
			}
		}

		RandomAccessFile raf = new RandomAccessFile(path, "rwd");
		for (String line : lines) {
			raf.write((line + lineSeparator).getBytes());
		}
		raf.close();
	}
	
	public void deleteErrorProxies() throws IOException {
		String path = getPath(httpProxiesPath);
		List<String> lines = readFileLines(path);

		List<String> deletingLines = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			if (!lines.get(i).startsWith("-")) {
//				deletingLines.add(lines.get(i));
				lines.set(i, "[deleted]" + lines.get(i));
			}
		}
		
		for(String l : deletingLines){
			lines.remove(l);
		}

		RandomAccessFile raf = new RandomAccessFile(path, "rwd");
		for (String line : lines) {
			raf.write((line + lineSeparator).getBytes());
		}
		raf.close();
	}

	public String getPath(String p) {
		URL resource = this.getClass().getResource(httpProxiesPath);
		String path = p;
		if (resource != null) {
			path = resource.getPath();
		}
		return path;
	}

	private List<String> readFileLines(String path) throws IOException {
		List<String> strs = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getPath(path))));
		String str = null;
		while ((str = br.readLine()) != null) {
			strs.add(str);
		}
		br.close();
		return strs;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

}
