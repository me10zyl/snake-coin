package snake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Snake implements Runnable {

	private List<ProxyIp> ips;
	private int timeout = 3000;
	private int gap = 1000;
	private int readTimeout = 5000;
	private String url;
	private LocalReader localReader;

	public Snake(List<ProxyIp> ips) {
		// TODO Auto-generated constructor stub
		this.ips = ips;
		this.localReader = new LocalReader();
	}

	public void run() {
		double minTime = (gap * ips.size()) / 1000.0;
		double maxTime = ((gap * ips.size()) + (timeout * ips.size())) / 1000.0;
		System.out.println("总请求数�? + ips.size());
		System.out.println("请求超时时间�? + timeout / 1000.0 + "(s)");
		System.out.println("每次请求间隔�? + gap / 1000.0 + "(s)");
		System.out.println("预计�?��完成时间�? + minTime + "(s) = " + String.format("%.2f", minTime / 60) + "(min)");
		System.out.println("预计�?��完成时间�? + maxTime + "(s) = " + String.format("%.2f", maxTime / 60) + "(min)");
		System.out.println("是否继续? y/n");
		Scanner scan = new Scanner(new InputStreamReader(System.in));
		String nextLine = scan.nextLine();
		if (nextLine.equals("yes") || nextLine.equals("y")) {
			scan.close();
			System.out.println("程序�?��...");
			int success = 0;
			int fail = 0;
			int step = 0;

			for (ProxyIp ip : ips) {
				step++;
				try {
					SocketAddress addr = new InetSocketAddress(ip.getIp(), ip.getPort());
					Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
					if (ip.getIp().equals("127.0.0.1")) {
						proxy = Proxy.NO_PROXY;
					}
					connect(proxy);
					success++;
					localReader.ignoreHttpProxy(ip.getIp());
					System.out.println(String.format("%d/%d 请求 %s 成功，当前成功次�?%d", step, ips.size(),
							ip.getIp() + ":" + ip.getPort(), success));
				} catch (Exception e) {
					fail++;
					System.out.println(String.format("%d/%d 请求 %s 失败，当前失败次�?%d - 错误信息�?s", step, ips.size(),
							ip.getIp() + ":" + ip.getPort(), fail, e.getMessage()));
				}
			}
			System.out.println(String.format("程序结束.成功%d次，失败%d次，总共%d�?, success, fail, ips.size()));
		} else {
			System.out.println("程序结束...");
			scan.close();
		}
	}

	public void connect(Proxy proxy) throws IOException, MalformedURLException, ProtocolException,
			UnsupportedEncodingException, InterruptedException {
		URI uri = URI.create(url);
		HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection(proxy);
		conn.setConnectTimeout(timeout);
		conn.setReadTimeout(readTimeout);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
		conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		conn.getResponseCode();
		/*
		 * InputStream inputStream = conn.getInputStream(); BufferedReader br =
		 * new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		 * br.close();
		 */
		Thread.sleep(gap);
	}

	public List<ProxyIp> getIps() {
		return ips;
	}

	public void setIps(List<ProxyIp> ips) {
		this.ips = ips;
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
