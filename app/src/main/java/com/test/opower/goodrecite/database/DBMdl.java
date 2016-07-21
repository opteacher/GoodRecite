package com.test.opower.goodrecite.database;

import android.database.sqlite.SQLiteDatabase;

import com.test.opower.goodrecite.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by opower on 16-6-27.
 */
public class DBMdl
{
	public static final int INSERT_DICT = 0;
	public static final int SELECT_STUDY_PLAN = 1;
	public static final int UPDATE_STUDY_PLAN = 2;
	public static final int SELECT_CURRENT_PLAN_ID = 3;
	public static final int SELECT_WORDS_INFO = 4;
	public static final int UPDATE_WORDS_DIFF = 5;
	public static final int UPDATE_WORDS_IMPT = 6;
	public static final int SELECT_WORDS_DIFF_TBL = 7;
	public static final int SELECT_WORDS_IMPT_TBL = 8;
	public static final int SELECT_SINGLE_WORD_INFO = 9;
	public static final int SELECT_WORD_DIFF_CLR = 10;
	public static final int SELECT_WORD_IMPT_CLR = 11;
	public static final int SELECT_WORD_EXP_PHS = 12;
	public static final int SELECT_WORD_NEAR_FM_SY = 13;
	public static final int SELECT_WORD_NEAR = 14;
	public static final int SELECT_WORD_DETAIL = 15;
	public static final int INSERT_WORD_IMAGE = 16;

	private DBMdl()
	{
		dbOpnTbl.put(INSERT_DICT, DBOpnIstDict.ins());
		dbOpnTbl.put(SELECT_STUDY_PLAN, DBOpnSelStdPln.ins());
		dbOpnTbl.put(UPDATE_STUDY_PLAN, DBOpnUpdStdPln.ins());
		dbOpnTbl.put(SELECT_CURRENT_PLAN_ID, DBOpnSelCurStdPln.ins());
		dbOpnTbl.put(SELECT_WORDS_INFO, DBOpnSelWdsInf.ins());
		dbOpnTbl.put(UPDATE_WORDS_DIFF, DBOpnUpdWdsDif.ins());
		dbOpnTbl.put(UPDATE_WORDS_IMPT, DBOpnUpdWdsImp.ins());
		dbOpnTbl.put(SELECT_WORDS_DIFF_TBL, DBOpnSelWdsDifTbl.ins());
		dbOpnTbl.put(SELECT_WORDS_IMPT_TBL, DBOpnSelWdsImpTbl.ins());
		dbOpnTbl.put(SELECT_SINGLE_WORD_INFO, DBOpnSelSglWdsInf.ins());
		dbOpnTbl.put(SELECT_WORD_DIFF_CLR, DBOpnSelWdsDifClr.ins());
		dbOpnTbl.put(SELECT_WORD_IMPT_CLR, DBOpnSelWdsImpClr.ins());
		dbOpnTbl.put(SELECT_WORD_EXP_PHS, DBOpnSelWdsExpPhs.ins());
		dbOpnTbl.put(SELECT_WORD_NEAR_FM_SY, DBOpnSelWdsNrFmSy.ins());
		dbOpnTbl.put(SELECT_WORD_NEAR, DBOpnSelWdsNear.ins());
		dbOpnTbl.put(SELECT_WORD_DETAIL, DBOpnSelWdsDtl.ins());
		dbOpnTbl.put(INSERT_WORD_IMAGE, DBOpnIstWdsImg.ins());
	}
	private static DBMdl instance = new DBMdl();
	public static DBMdl ins()	{ return instance; }

	protected Map<Integer, DBOpn> dbOpnTbl = new HashMap<>();
	protected int curOpn = -1;

	/***
	 * 根据给出的信号和参数执行前置操作
	 * @param signal
	 * @param pam
	 */
	public int begOperation(int signal, Object pam, Object rst)
	{
		if(dbOpnTbl.containsKey(signal))
		{
			curOpn = signal;
			return dbOpnTbl.get(signal).preExe(pam, rst);
		}
		return R.string.err_no_such_opn;
	}

	/***
	 * 执行操作
	 * @param pam
	 */
	public int exeOperation(Object pam, Object rst)
	{
		return dbOpnTbl.get(curOpn).exe(pam, rst);
	}

	/***
	 * 操作执行结束，做收尾工作
	 * @param pam
	 */
	public int endOperation(Object pam, Object rst)
	{
		DBOpn opn = dbOpnTbl.get(curOpn);
		curOpn = -1;
		return opn.sufExe(pam, rst);
	}

	//提供给外部模型的模型接口
	public static abstract class DBOpn
	{
		protected SQLiteDatabase rdb = null;
		protected SQLiteDatabase wdb = null;
		protected DBOpn()
		{
			rdb = DBCtrl.getDB().getRdblSQLiteDB();
			wdb = DBCtrl.getDB().getWtblSQLiteDB();
		}
		protected abstract int preExe(Object pam, Object rst);
		protected abstract int exe(Object pam, Object rst);
		protected abstract int sufExe(Object pam, Object rst);
	}

	//公共参数
	public static class LstItmInf
	{
		public int data = -1;
		public String txtDesc = "";
		public int bkgdClr = 0;
	}
	public static class WdsUpdInf
	{
		public String word = "";
		public int data = -1;
	}
}
