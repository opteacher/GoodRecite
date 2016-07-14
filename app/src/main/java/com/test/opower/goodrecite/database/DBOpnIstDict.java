package com.test.opower.goodrecite.database;

import android.database.sqlite.SQLiteDatabase;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.common_utils.KingTranslation;

import java.util.HashMap;
import java.util.Map;

/**
 * 插入词典数据库操作
 * Created by opower on 16-6-27.
 */
public class DBOpnIstDict extends DBMdl.DBOpn
{
	private DBOpnIstDict()	{}
	private static DBOpnIstDict instance = null;
	public static DBOpnIstDict ins()
	{
		if(instance == null)
		{
			instance = new DBOpnIstDict();
		}
		return instance;
	}

	/***
	 * 插入前置：清空字典数据库
	 * @param pam
	 */
	@Override
	protected int preExe(Object pam, Object rst)
	{
		//清空单词库
		wdb.execSQL(DBCtrl.genDeleteDatSQL(DBCtrl.Dict.TABLE_NAME, null));
		return 0;
	}

	/***
	 * 执行操作：从金山API接口取得单词数据并保存到数据库中
	 * @param pam
	 */
	@Override
	protected int exe(Object pam, Object rst)
	{
		//将参数转化为单词字符串
		String word = (String) pam;

		//调用金山API取得相关数据
		KingTranslation.WordData wdDat = null;
		try
		{
			wdDat = KingTranslation.translate(word);
		}
		catch (Exception e)
		{
			return R.string.err_fm_service;
		}

		//将取得的单词数据转化成数据库格式
		Map<String, DBCtrl.DBPam> bindPam = new HashMap<>();
		bindPam.put(DBCtrl.Dict.WORD,
				new DBCtrl.DBPam(wdDat.word));
		bindPam.put(DBCtrl.Dict.SOUND_AM,
				new DBCtrl.DBPam(wdDat.phAm.replace("\'", "\'\'")));
		bindPam.put(DBCtrl.Dict.SOUND_AM_URL,
				new DBCtrl.DBPam(wdDat.phAmUrl));
		bindPam.put(DBCtrl.Dict.SOUND_EN,
				new DBCtrl.DBPam(wdDat.phEn.replace("\'", "\'\'")));
		bindPam.put(DBCtrl.Dict.SOUND_EN_URL,
				new DBCtrl.DBPam(wdDat.phEnUrl));
		String translation = "";
		for(Map.Entry<String, String> ety : wdDat.translations.entrySet())
		{
			translation += ety.getKey() + "\t" + ety.getValue() + "|";
		}
		translation = translation.replace("\'", "\'\'");
		bindPam.put(DBCtrl.Dict.TRANSLATION,
				new DBCtrl.DBPam(translation));
		bindPam.put(DBCtrl.Dict.MULTIPLE,
				new DBCtrl.DBPam(wdDat.plTs));
		bindPam.put(DBCtrl.Dict.PAST,
				new DBCtrl.DBPam(wdDat.pastTs));
		bindPam.put(DBCtrl.Dict.DONE,
				new DBCtrl.DBPam(wdDat.doneTs));
		bindPam.put(DBCtrl.Dict.PRESENT,
				new DBCtrl.DBPam(wdDat.ingTs));
		bindPam.put(DBCtrl.Dict.THIRD_SGL,
				new DBCtrl.DBPam(wdDat.thirdTs));
		bindPam.put(DBCtrl.Dict.COMPARE,
				new DBCtrl.DBPam(wdDat.erTs));
		bindPam.put(DBCtrl.Dict.BEST,
				new DBCtrl.DBPam(wdDat.estTs));
		String example = "";
		for(Map.Entry<String, String> ety : wdDat.examples.entrySet())
		{
			String orig = ety.getKey();
			orig = orig.replace("\n", "");
			String trsf = ety.getValue();
			trsf = trsf.replace("\n", "");
			example += orig + "\n" + trsf + "|";
		}
		example = example.replace("\'", "\'\'");
		bindPam.put(DBCtrl.Dict.EXAMPLE,
				new DBCtrl.DBPam(example));

		//将数据库格式的参数表由数据库控制器生成为SQL
		try
		{
			wdb.execSQL(DBCtrl.genInsertTblSQL(DBCtrl.Dict.TABLE_NAME, bindPam));
		}
		catch (Exception e)
		{
			return R.string.err_db_exe;
		}
		return 0;
	}

	public static class WBookInfo
	{
		public String wbkName = "";
		public int wdsNum = -1;

		public WBookInfo(String wbkName, int wdsNum)
		{
			this.wbkName = wbkName;
			this.wdsNum = wdsNum;
		}
	}

	/***
	 * 后置操作：生成单词学习计划
	 * @param pam
	 */
	@Override
	protected int sufExe(Object pam, Object rst)
	{
		//从参数中取出数据
		if(pam.getClass() != WBookInfo.class)
		{
			return R.string.err_not_rgt_cls;
		}
		WBookInfo info = (WBookInfo) pam;

		//销毁单词本表相关数据
		wdb.execSQL(DBCtrl.genDeleteDatSQL(
				DBCtrl.WordBook.TABLE_NAME, null));//单词本

		//将词典的数据导进用户词汇表
		String sql = "INSERT INTO "
				+ DBCtrl.WordBook.TABLE_NAME + " ("
				+ DBCtrl.WordBook.WORD + ","
				+ DBCtrl.WordBook.FAMILIARITY + ","
				+ DBCtrl.WordBook.DIFFICULTY + ","
				+ DBCtrl.WordBook.IMPORTANCE + ")"
				+ " SELECT "
				+ DBCtrl.Dict.WORD + ","
				+ DBCtrl.WordBook.UNKNOWN + ","
				+ DBCtrl.DiffType.CAN_REM.ordinal() + ","
				+ "0"
				+ " FROM " + DBCtrl.Dict.TABLE_NAME;
		wdb.execSQL(sql);

		//设置新的学习计划
		setNewStudyPlan(info.wbkName, info.wdsNum);

		return 0;
	}

	public static String setNewStudyPlan(String wordBook, int wdsNum)
	{
		SQLiteDatabase db = DBCtrl.getDB().getWtblSQLiteDB();

		//销毁数据库相关数据
		db.execSQL(DBCtrl.genDeleteDatSQL(
				DBCtrl.TestPlan.TABLE_NAME, null));//计划
		db.execSQL(DBCtrl.genDeleteDatSQL(
				DBCtrl.DailyLog.TABLE_NAME, null));//每日记录

		//新建计划
		Map<String, DBCtrl.DBPam> pam = new HashMap<>();
		String planId = CmnUtils.genMD5(SessionData.ins().getUserName() + wordBook);
		pam.put(DBCtrl.TestPlan.PLAN_ID, new DBCtrl.DBPam(planId));
		pam.put(DBCtrl.TestPlan.USER_ID, new DBCtrl.DBPam(
				SessionData.ins().getUserName()));
		pam.put(DBCtrl.TestPlan.SELECT_WBOOK, new DBCtrl.DBPam(wordBook));
		pam.put(DBCtrl.TestPlan.WORDS_NUM, new DBCtrl.DBPam(
				wdsNum, DBCtrl.DBPam.IstType.INTEGER));
		pam.put(DBCtrl.TestPlan.LEARNT_NUM, new DBCtrl.DBPam(
				0, DBCtrl.DBPam.IstType.INTEGER));
		int dailyNum = wdsNum/10;
		if(dailyNum == 0)	{ dailyNum = 1; }
		pam.put(DBCtrl.TestPlan.ONE_DAY_NUM, new DBCtrl.DBPam(
				dailyNum, DBCtrl.DBPam.IstType.INTEGER));
		pam.put(DBCtrl.TestPlan.CUR_PROCESS, new DBCtrl.DBPam(
				1, DBCtrl.DBPam.IstType.INTEGER));
		db.execSQL(DBCtrl.genInsertTblSQL(DBCtrl.TestPlan.TABLE_NAME, pam));

		//将当前单词本的名字设置到Session中
		SessionData.ins().setTestPlan(planId);
		SessionData.ins().setWordBook(wordBook);

		//构建日期监听服务，在日期更替的时候设定每天的学习目标
//		WordsActivity act = SelWBookCtrl.ins().getActivity();
//		Intent itt = CmnData.getListenDateChgItt(act);
//		itt.putExtra(IstFstRcd, true);
//		itt.putExtra(DailyMiss, dailyNum);
//		act.bindService(itt, act, Context.BIND_AUTO_CREATE);

		return planId;
	}
}
