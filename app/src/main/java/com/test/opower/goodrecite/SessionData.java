package com.test.opower.goodrecite;

import com.test.opower.goodrecite.database.DBOpnSelWdsInf.WordInf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by opower on 16-6-8.
 */
public class SessionData
{
	private static SessionData instance = new SessionData();
	private SessionData()	{}
	public static SessionData ins()	{ return instance; }

	private String userName = "default";
	private String wordBook = "test";
	private String testPlan = "";
	private Map<String, WordInf> wdsSet = new HashMap<>();

	public Map<String, WordInf> getWdsSet()
	{
		return wdsSet;
	}

	public void setWdsSet(Map<String, WordInf> wdsSet)
	{
		this.wdsSet = wdsSet;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getWordBook()
	{
		return wordBook;
	}

	public void setWordBook(String wordBook)
	{
		this.wordBook = wordBook;
	}

	public String getTestPlan()
	{
		return testPlan;
	}

	public void setTestPlan(String testPlan)
	{
		this.testPlan = testPlan;
	}
}
