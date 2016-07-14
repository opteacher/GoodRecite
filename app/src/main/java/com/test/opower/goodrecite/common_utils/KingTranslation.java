package com.test.opower.goodrecite.common_utils;

import android.util.Log;

import com.test.opower.goodrecite.net.NetConn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by opower on 16-6-4.
 */
public class KingTranslation
{
	private static final String UTF8 = "utf-8";
	private static final String BASE_ADDR = "http://dict-co.iciba.com/api/dictionary.php";
	private static final String KEY = "AC6AD4D3834045AB4C8F3DC496A14007";
	public static final String JSON = "json";
	public static final String XML = "xml";

	private static HttpURLConnection conn = null;

	public static class WordData
	{
		public String word = "";//单词
		public String plTs = "";//复数
		public String pastTs = "";//过去式
		public String doneTs = "";//过去分词
		public String ingTs = "";//现在分词
		public String thirdTs = "";//第三人称单数
		public String erTs = "";//比较式
		public String estTs = "";//最大式
		public String phAm = "";//美式发音
		public String phEn = "";//英式发音
		public String phAmUrl = "";//美式发音url
		public String phEnUrl = "";//英式发音url
		public Map<String, String> translations = new HashMap<>();//翻译
		public Map<String, String> examples = new HashMap<>();//例句
	}

	public static WordData translate(String q) throws Exception
	{
		WordData wdDat = new WordData();

		StringBuilder sb = new StringBuilder();
		sb.append(BASE_ADDR)
				.append("?w=").append(q)
				.append("&type=").append(JSON)
				.append("&key=").append(KEY);
		InputStream is = NetConn.getNet().getInputStream(sb.toString(), "GET");
		InputStreamReader isr = new InputStreamReader(is, UTF8);
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
		wdDat.word = resultJson.getString("word_name");
		JSONObject exChg = (JSONObject) resultJson.get("exchange");
		wdDat.plTs = getAryFirstItm(String.class, exChg.get("word_pl"));
		wdDat.pastTs = getAryFirstItm(String.class, exChg.get("word_past"));
		wdDat.doneTs = getAryFirstItm(String.class, exChg.get("word_done"));
		wdDat.ingTs = getAryFirstItm(String.class, exChg.get("word_ing"));
		wdDat.thirdTs = getAryFirstItm(String.class, exChg.get("word_third"));
		wdDat.erTs = exChg.getString("word_er");
		wdDat.estTs = exChg.getString("word_est");
		JSONObject sym = getAryFirstItm(JSONObject.class, resultJson.get("symbols"));
		wdDat.phAm = URLDecoder.decode(sym.getString("ph_am"), UTF8);
		wdDat.phEn = URLDecoder.decode(sym.getString("ph_en"), UTF8);
		wdDat.phAmUrl = URLDecoder.decode(sym.getString("ph_am_mp3"), UTF8);
		wdDat.phEnUrl = URLDecoder.decode(sym.getString("ph_en_mp3"), UTF8);
		Object tmp = sym.get("parts");
		if(tmp.getClass() == JSONArray.class)
		{
			JSONArray parts = (JSONArray) tmp;
			for(int i = 0; i < parts.length(); ++i)
			{
				JSONObject part = (JSONObject) parts.get(i);
				String pt = part.getString("part");
				JSONArray means = (JSONArray) part.get("means");
				String ms = "";
				for(int j = 0; j < means.length(); ++j)
				{
					ms += URLDecoder.decode(means.get(j).toString(), UTF8) + ";";
				}
				wdDat.translations.put(pt, ms);
			}
		}
		is.close();
		NetConn.getNet().disconnect();

		XmlPullParserFactory fcty = null;
		fcty = XmlPullParserFactory.newInstance();
		fcty.setNamespaceAware(true);

		sb = new StringBuilder();
		sb.append(BASE_ADDR)
				.append("?w=").append(q)
				.append("&type=").append(XML)
				.append("&key=").append(KEY);
		is = NetConn.getNet().getInputStream(sb.toString(), "GET");

		XmlPullParser xpp = fcty.newPullParser();
		xpp.setInput(is, null);

		int eveTyp = xpp.getEventType();
		while(eveTyp != XmlPullParser.END_DOCUMENT)
		{
			if(eveTyp == XmlPullParser.START_TAG
			&& xpp.getName().equals("sent"))
			{
				eveTyp = xpp.next();

				String orig = "";
				String trsl = "";
				while(!(eveTyp == XmlPullParser.END_TAG
						&& xpp.getName().equals("sent")))
				{
					if(eveTyp == XmlPullParser.START_TAG
					&& xpp.getName().equals("orig"))
					{
						orig = xpp.nextText();
					}
					else
					if(eveTyp == XmlPullParser.START_TAG
					&& xpp.getName().equals("trans"))
					{
						trsl = xpp.nextText();
					}
					eveTyp = xpp.next();
				}
				wdDat.examples.put(orig, trsl);
			}
			eveTyp = xpp.next();
		}
		NetConn.getNet().disconnect();

		return wdDat;
	}

	private static <T> T getAryFirstItm(Class<T> cls, Object obj)
			throws JSONException, IllegalAccessException, InstantiationException
	{
		if(obj.getClass() == JSONArray.class)
		{
			JSONArray ary = (JSONArray) obj;
			if(ary.length() > 0)
			{
				return (T) ary.get(0);
			}
		}
		return cls.newInstance();
	}
}
