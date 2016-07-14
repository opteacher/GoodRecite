package com.test.opower.goodrecite.database;

import android.util.Log;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.database.DBMdl.WdsUpdInf;
import com.test.opower.goodrecite.database.DBOpnSelSglWdsInf.ExeRst;
import com.test.opower.goodrecite.database.DBOpnSelWdsInf.WordInf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-7-5.
 */
public class DBOpnUpdWdsDif extends DBMdl.DBOpn
{
	private DBOpnUpdWdsDif() {}
	private static DBOpnUpdWdsDif instance = null;
	public static DBOpnUpdWdsDif ins()
	{
		if(instance == null)
		{
			instance = new DBOpnUpdWdsDif();
		}
		return instance;
	}

	private String word = "";

	@Override
	protected int preExe(Object pam, Object rst)
	{
		return 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		if(pam == null || pam.getClass() != WdsUpdInf.class)
		{
			return R.string.err_not_rgt_cls;
		}
		WdsUpdInf tmp = (WdsUpdInf) pam;
		word = tmp.word;

		Map<String, DBCtrl.DBPam> param = new HashMap<>();
		param.put(DBCtrl.WordBook.DIFFICULTY, new DBCtrl.DBPam(
				tmp.data, DBCtrl.DBPam.IstType.INTEGER));
		List<DBCtrl.DBCdn> cdn = new ArrayList<>();
		cdn.add(new DBCtrl.DBCdn(tmp.word).setColNam(DBCtrl.WordBook.WORD));
		wdb.execSQL(DBCtrl.genUpdateTblSQL(DBCtrl.WordBook.TABLE_NAME, param, cdn));
		return 0;
	}

	/***
	 * 单词信息发生变动，更新到单词信息列表
	 * @param pam
	 * @param rst
	 * @return
	 */
	@Override
	protected int sufExe(Object pam, Object rst)
	{
		if(!SessionData.ins()
				.getWdsSet()
				.containsKey(word))
		{
			return R.string.err_inside;
		}
		DBMdl.ins().begOperation(DBMdl.SELECT_SINGLE_WORD_INFO, null, null);
		ExeRst inf = new ExeRst();
		int opnMsg = DBMdl.ins().exeOperation(word, inf);
		if(opnMsg != 0)
		{
			return opnMsg;
		}
		WordInf wi = SessionData.ins().getWdsSet().get(word);
		wi.difficulty = inf.difficulty;
		return 0;
	}
}
