package com.test.opower.goodrecite.common_utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by opower on 16-6-4.
 */
public class BaiduTranslation
{
	private static final String UTF8 = "utf-8";
	private static final String appId = "20160604000022719";
	private static final String token = "7RbFkJF8RBdC360NFf0u";
	private static final String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
	private static final Random random = new Random();
	private static final int MaxBufferSize = 212144;

	public static String translate(String q, String from, String to) throws Exception
	{
		//用于md5加密
		int salt = random.nextInt(10000);

		// 对appId+源文+随机数+token计算md5值
		StringBuilder md5String = new StringBuilder();
		md5String.append(appId).append(q).append(salt).append(token);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(md5String.toString().getBytes(UTF8));
		byte[] bt = md5.digest();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < bt.length; ++i)
		{
			if (Integer.toHexString(0xff & bt[i]).length() == 1)
			{
				sb.append("0").append(Integer.toHexString(0xff & bt[i]));
			}
			else
			{
				sb.append(Integer.toHexString(0xff & bt[i]));
			}
		}
		String sign = sb.toString();

		//使用Post方式，组装参数
		StringBuilder urlStrBdr = new StringBuilder();
		urlStrBdr.append(url)
				.append("?q=").append(q)
				.append("&from=").append(from)
				.append("&to=").append(to)
				.append("&appid=").append(appId)
				.append("&salt=").append(salt)
				.append("&sign=").append(sign);
		URL urlPost = new URL(urlStrBdr.toString());
		HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
		conn.setDoInput(true);
		//conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
		conn.setChunkedStreamingMode(5);
		conn.setReadTimeout(200000);
		conn.connect();
		if(conn.getResponseCode() != 200)
		{
			//获取失败
			return "";
		}
		InputStreamReader isr = new InputStreamReader(conn.getInputStream(), UTF8);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder result = new StringBuilder();
		String str = null;
		while ((str = br.readLine()) != null)
		{
			result.append(str).append("\n");
		}

		//转化为json对象，注：Json解析的jar包可选其它
		JSONObject resultJson = new JSONObject(result.toString());

		//开发者自行处理错误，本示例失败返回为null
		try
		{
			String error_code = resultJson.getString("error_code");
			if (error_code != null)
			{
				Log.e("出错代码:", error_code);
				Log.e("出错信息:", resultJson.getString("error_msg"));
				return null;
			}
		}
		catch (Exception e) {}

		//获取返回翻译结果
		JSONArray array = (JSONArray) resultJson.get("trans_result");
		JSONObject dst = (JSONObject) array.get(0);
		String text = dst.getString("dst");
		text = URLDecoder.decode(text, UTF8);

		isr.close();
		conn.disconnect();

		return text;
	}
}
