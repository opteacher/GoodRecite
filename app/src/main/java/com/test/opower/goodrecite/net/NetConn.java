package com.test.opower.goodrecite.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by opower on 16-6-4.
 */
public class NetConn
{
	private static NetConn netConn = null;
	private NetConn() {}
	public static NetConn getNet()
	{
		if(netConn == null)
		{
			netConn = new NetConn();
		}
		return netConn;
	}

	private HttpURLConnection conn = null;
	public static final String UTF8 = "utf-8";

	public InputStream getInputStream(String urlPath, String reqMthd) throws Exception
	{
		URL url = new URL(urlPath);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setRequestMethod(reqMthd);
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("Content-Type", "text/plain; charset=" + UTF8);
		conn.setChunkedStreamingMode(5);
		conn.setReadTimeout(200000);
		conn.connect();
		if(conn.getResponseCode() != 200)
		{
			throw new Exception("Connect failed");
		}
		return conn.getInputStream();
	}

	public void disconnect()
	{
		if(conn != null)
		{
			conn.disconnect();
			conn = null;
		}
	}
}
