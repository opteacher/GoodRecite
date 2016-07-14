package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;

/**
 * Created by opower on 16-6-29.
 */
public class DBOpnSelStdPln extends DBMdl.DBOpn
{
	private DBOpnSelStdPln() {}
	private static DBOpnSelStdPln instance = null;
	public static DBOpnSelStdPln ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelStdPln();
		}
		return instance;
	}

	@Override
	protected int preExe(Object pam, Object rst)
	{
		return 0;
	}

	public static class ExeRstInfo
	{
		public String wbookName = "";
		public int allWdsNum = -1;
		public int lrnWdsNum = -1;
		public int dayWdsNum = -1;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		//如果会话中没有学习计划，说明用户还没有选择单词本
		String curStdyPln = SessionData.ins().getTestPlan();
		if(curStdyPln.isEmpty())
		{
			return R.string.wng_no_wbk_exs;
		}

		//新建用于返回的学习计划信息类，并填入单词本名
		ExeRstInfo info = (ExeRstInfo) rst;
		info.wbookName = SessionData.ins().getWordBook();

		//调出相关的单词总数和每日学习量
		Cursor csr = rdb.query(
				DBCtrl.TestPlan.TABLE_NAME,
				new String[]
						{
								DBCtrl.TestPlan.WORDS_NUM,
								DBCtrl.TestPlan.LEARNT_NUM,
								DBCtrl.TestPlan.ONE_DAY_NUM
						},
				DBCtrl.TestPlan.PLAN_ID + "=?",
				new String[]{ curStdyPln }, null, null, null);
		//如果存在则填进信息结构，不存在则打出Toast并返回null
		if(csr.moveToFirst())
		{
			int wdsNumIdx = csr.getColumnIndex(DBCtrl.TestPlan.WORDS_NUM);
			info.allWdsNum = csr.getInt(wdsNumIdx);
			int lrnNumIdx = csr.getColumnIndex(DBCtrl.TestPlan.LEARNT_NUM);
			info.lrnWdsNum = csr.getInt(lrnNumIdx);
			int dayNumIdx = csr.getColumnIndex(DBCtrl.TestPlan.ONE_DAY_NUM);
			info.dayWdsNum = csr.getInt(dayNumIdx);
			csr.close();
			return 0;
		}
		else
		{
			csr.close();
			return R.string.wng_no_wbk_exs;
		}
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
