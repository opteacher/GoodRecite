package com.test.opower.goodrecite.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-5-24.
 */
public class DBCtrl
{
	public final static String DB_NAME = "basic.db";
	public final static String AUTO_ID = "_id";

	public static class Users
	{
		public final static String TABLE_NAME = "users";
		public final static String USER_ID = "user_id";
		public final static String TOKEN = "token";
		public final static String TOKEN_SECRET = "token_secret";
		public final static String USER_NAME = "user_name";
		public final static String USER_ICON = "user_icon";
	}

	//如果有多条数据，使用【|】符号分割
	public static class Dict
	{
		public final static String TABLE_NAME = "dict";
		public final static String WORD = "word";
		public final static String SOUND_AM = "sound_am";
		public final static String SOUND_AM_URL = "sound_am_url";
		public final static String SOUND_EN = "sound_en";
		public final static String SOUND_EN_URL = "sound_en_url";
		public final static String TRANSLATION = "translation";
		public final static String EXAMPLE = "example";
		public final static String PHRASE = "phrase";
		public final static String NEAR_FORM = "near_form";
		public final static String NEAR_SYNO = "near_syno";
		public final static String MULTIPLE = "multiple";
		public final static String THIRD_SGL = "third_sgl";
		public final static String PRESENT = "present";
		public final static String PAST = "past";
		public final static String DONE = "done";
		public final static String COMPARE = "compare";
		public final static String BEST = "best";
	}

	public static class WordImg
	{
		public final static String TABLE_NAME = "word_image";
		public final static String WORD = "word";
		public final static String DESCRIPTION = "description";
		public final static String PICTURE = "picture";
		public final static String PROPOSER = "proposer";
		public final static String COMMENT = "comment";
		public final static String SELECTED = "selected";
	}

	public static class WordBook
	{
		public final static String TABLE_NAME = "word_book";
		public final static String WORD = "word";
		public final static String FAMILIARITY = "familiarity";
		public final static int UNKNOWN = 0;
		public final static int FAMILIAR = 1;
		public final static int KNOWN = 2;
		public final static int TOO_SIMPLE = 3;
		public final static String DIFFICULTY = "difficulty";
		//Record the ratio of appearance in the reading history
		public final static String IMPORTANCE = "importance";
	}

	public static class TestPlan
	{
		public final static String TABLE_NAME = "test_plan";
		public final static String PLAN_ID = "plan_id";
		public final static String USER_ID = "user_id";
		public final static String SELECT_WBOOK = "select_wbook";
		public final static String WORDS_NUM = "word_num";
		public final static String LEARNT_NUM = "learnt_num";
		public final static String ONE_DAY_NUM = "one_day_num";
		public final static String CUR_PROCESS = "cur_process";
	}

	public static class DailyLog
	{
		public final static String TABLE_NAME = "daily_log";
		public final static String PLAN_ID = "plan_id";
		public final static String DATE = "date";
		public final static String TARGET = "target";
		public final static String ACTUAL = "actual";
	}

	public enum DiffType
	{
		HAS_EXIST_IMG, CAN_REM, NO_WAY_REM
	}

	public static class DiffLst
	{
		public final static String TABLE_NAME = "diff_lst";
		public final static String DIFFICULTY = "difficulty";
		public final static String BACKGROUND_CLR = "background_color";
		public final static String TXT_DESC = "txt_description";
	}

	//重要度的评判标准是使用频率和是否可替换，经常使用且不可替换的单词重要度高，反之
	public enum ImptType
	{
		LOW, NORMAL, HIGH
	}

	public static class ImptLst
	{
		public final static String TABLE_NAME = "impt_lst";
		public final static String IMPORTANCE = "importance";
		public final static String BACKGROUND_CLR = "background_color";
		public final static String TXT_DESC = "txt_description";
	}

	private DBConn dbConn = null;
	public static final int DB_VERSION = 1;

	private static DBCtrl dbCtrl = null;
	private DBCtrl(Context ctt)
	{
		dbConn = new DBConn(ctt, DB_NAME, null, DB_VERSION);
	}
	public static DBCtrl getDB()
	{
		return dbCtrl;
	}
	public static void initDBController(Context ctt)
	{
		if(ctt != null)
		{
			dbCtrl = new DBCtrl(ctt);
			SQLiteDatabase db = dbCtrl.getWtblSQLiteDB();

			//创建表
			//db.execSQL(genDropTblSQL(Dict.TABLE_NAME));
			db.execSQL(genCreateDictTblSQL());
			//db.execSQL(genDropTblSQL(WordImg.TABLE_NAME));
			db.execSQL(genCreateWdImgTblSQL());
			//db.execSQL(genDropTblSQL(WordBook.TABLE_NAME));
			db.execSQL(genCreateWdBookTblSQL());
			//db.execSQL(genDropTblSQL(TestPlan.TABLE_NAME));
			db.execSQL(genCreateTstPlanTblSQL());
			//db.execSQL(genDropTblSQL(DailyLog.TABLE_NAME));
			db.execSQL(genCreateDyLogTblSQL());
			//db.execSQL(genDropTblSQL(DiffLst.TABLE_NAME));
			db.execSQL(genCreateDifLstTblSQL());
			//db.execSQL(genDropTblSQL(ImptLst.TABLE_NAME));
			db.execSQL(genCreateImpLstTblSQL());

			//插入参照表的数据
			insertDiffLstData(db);
			insertImpLstData(db);

			//@_@ for debug
			//dbCtrl.insertDftDatForDebug(true);
			//@_@

			initCommonData();
		}
	}

	private static void insertDiffLstData(SQLiteDatabase db)
	{
		if(!isTblEmpty(DiffLst.TABLE_NAME))	{ return; }

		Map<String, DBPam> pam = new HashMap<>();
		pam.put(DiffLst.DIFFICULTY, new DBPam(
				DiffType.CAN_REM.ordinal(), DBPam.IstType.INTEGER));
		pam.put(DiffLst.BACKGROUND_CLR, new DBPam(
				0xFFf0ad4e, DBPam.IstType.INTEGER));
		pam.put(DiffLst.TXT_DESC, new DBPam("可以记忆"));
		db.execSQL(genInsertTblSQL(DiffLst.TABLE_NAME, pam));

		pam.clear();
		pam.put(DiffLst.DIFFICULTY, new DBPam(
				DiffType.HAS_EXIST_IMG.ordinal(), DBPam.IstType.INTEGER));
		pam.put(DiffLst.BACKGROUND_CLR, new DBPam(
				0xFF5cb85c, DBPam.IstType.INTEGER));
		pam.put(DiffLst.TXT_DESC, new DBPam("有绝妙的记忆方法"));
		db.execSQL(genInsertTblSQL(DiffLst.TABLE_NAME, pam));

		pam.clear();
		pam.put(DiffLst.DIFFICULTY, new DBPam(
				DiffType.NO_WAY_REM.ordinal(), DBPam.IstType.INTEGER));
		pam.put(DiffLst.BACKGROUND_CLR, new DBPam(
				0xFFd9534f, DBPam.IstType.INTEGER));
		pam.put(DiffLst.TXT_DESC, new DBPam("难以记忆"));
		db.execSQL(genInsertTblSQL(DiffLst.TABLE_NAME, pam));
	}

	private static void insertImpLstData(SQLiteDatabase db)
	{
		if(!isTblEmpty(ImptLst.TABLE_NAME))	{ return; }

		Map<String, DBPam> pam = new HashMap<>();
		pam.put(ImptLst.IMPORTANCE, new DBPam(
				ImptType.LOW.ordinal(), DBPam.IstType.INTEGER));
		pam.put(ImptLst.BACKGROUND_CLR, new DBPam(
				0xFF0000ff, DBPam.IstType.INTEGER));
		pam.put(ImptLst.TXT_DESC, new DBPam("次要"));
		db.execSQL(genInsertTblSQL(ImptLst.TABLE_NAME, pam));

		pam.clear();
		pam.put(ImptLst.IMPORTANCE, new DBPam(
				ImptType.NORMAL.ordinal(), DBPam.IstType.INTEGER));
		pam.put(ImptLst.BACKGROUND_CLR, new DBPam(
				0xFF00ff00, DBPam.IstType.INTEGER));
		pam.put(ImptLst.TXT_DESC, new DBPam("普通"));
		db.execSQL(genInsertTblSQL(ImptLst.TABLE_NAME, pam));

		pam.clear();
		pam.put(ImptLst.IMPORTANCE, new DBPam(
				ImptType.HIGH.ordinal(), DBPam.IstType.INTEGER));
		pam.put(ImptLst.BACKGROUND_CLR, new DBPam(
				0xFFff0000, DBPam.IstType.INTEGER));
		pam.put(ImptLst.TXT_DESC, new DBPam("重要"));
		db.execSQL(genInsertTblSQL(ImptLst.TABLE_NAME, pam));
	}

	private static void initCommonData()
	{
		SQLiteDatabase db = DBCtrl.getDB().getRdblSQLiteDB();
	}

	public SQLiteDatabase getRdblSQLiteDB()
	{
		return dbConn.getReadableDatabase();
	}

	public SQLiteDatabase getWtblSQLiteDB()
	{
		return dbConn.getWritableDatabase();
	}

	public static String genCreateUsersTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ Users.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Users.USER_ID + " VARCHAR UNIQUE NOT NULL,"
				+ Users.USER_NAME + " VARCHAR NOT NULL,"
				+ Users.USER_ICON + " VARCHAR,"
				+ Users.TOKEN + " VARCHAR,"
				+ Users.TOKEN_SECRET + " VARCHAR)";
	}

	public static String genCreateDictTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ Dict.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Dict.WORD + " VARCHAR UNIQUE NOT NULL,"
				+ Dict.SOUND_AM + " VARCHAR NOT NULL,"
				+ Dict.SOUND_AM_URL + " VARCHAR,"
				+ Dict.SOUND_EN + " VARCHAR NOT NULL,"
				+ Dict.SOUND_EN_URL + " VARCHAR,"
				+ Dict.TRANSLATION + " VARCHAR NOT NULL,"
				+ Dict.EXAMPLE + " VARCHAR,"
				+ Dict.PHRASE + " VARCHAR,"
				+ Dict.NEAR_FORM + " VARCHAR,"
				+ Dict.NEAR_SYNO + " VARCHAR,"
				+ Dict.MULTIPLE + " VARCHAR,"
				+ Dict.THIRD_SGL + " VARCHAR,"
				+ Dict.PRESENT + " VARCHAR,"
				+ Dict.PAST + " VARCHAR,"
				+ Dict.DONE + " VARCHAR,"
				+ Dict.COMPARE + " VARCHAR,"
				+ Dict.BEST + " VARCHAR)";
	}

	public static String genCreateWdImgTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ WordImg.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ WordImg.WORD + " VARCHAR NOT NULL,"
				+ WordImg.DESCRIPTION + " VARCHAR UNIQUE NOT NULL,"
				+ WordImg.PICTURE + " VARCHAR,"
				+ WordImg.PROPOSER + " VARCHAR NOT NULL,"
				+ WordImg.COMMENT + " INTEGER,"
				+ WordImg.SELECTED + " INTEGER NOT NULL)";
	}

	public static String genCreateWdBookTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ WordBook.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ WordBook.WORD + " VARCHAR NOT NULL,"
				+ WordBook.DIFFICULTY + " INTEGER NOT NULL,"
				+ WordBook.FAMILIARITY + " INTEGER NOT NULL,"
				+ WordBook.IMPORTANCE + " INTEGER NOT NULL)";
	}

	public static String genCreateTstPlanTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ TestPlan.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TestPlan.PLAN_ID + " VARCHAR UNIQUE NOT NULL,"
				+ TestPlan.USER_ID + " VARCHAR NOT NULL,"
				+ TestPlan.SELECT_WBOOK + " VARCHAR NOT NULL,"
				+ TestPlan.WORDS_NUM + " INTEGER NOT NULL,"
				+ TestPlan.LEARNT_NUM + " INTEGER NOT NULL,"
				+ TestPlan.ONE_DAY_NUM + " INTEGER NOT NULL,"
				+ TestPlan.CUR_PROCESS + " INTEGER NOT NULL)";
	}

	public static String genCreateDyLogTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ DailyLog.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DailyLog.PLAN_ID + " VARCHAR NOT NULL,"
				+ DailyLog.DATE + " DATE UNIQUE NOT NULL,"
				+ DailyLog.TARGET + " INTEGER NOT NULL,"
				+ DailyLog.ACTUAL + " INTEGER NOT NULL)";
	}

	public static String genCreateDifLstTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ DiffLst.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DiffLst.DIFFICULTY + " INTEGER UNIQUE NOT NULL,"
				+ DiffLst.BACKGROUND_CLR + " INTEGER NOT NULL,"
				+ DiffLst.TXT_DESC + " VARCHAR NOT NULL)";
	}

	public static String genCreateImpLstTblSQL()
	{
		return "CREATE TABLE IF NOT EXISTS "
				+ ImptLst.TABLE_NAME + "("
				+ AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ImptLst.IMPORTANCE + " INTEGER UNIQUE NOT NULL,"
				+ ImptLst.BACKGROUND_CLR + " INTEGER NOT NULL,"
				+ ImptLst.TXT_DESC + " VARCHAR NOT NULL)";
	}

	public static String genDropTblSQL(String tblNam)
	{
		return "DROP TABLE IF EXISTS " + tblNam;
	}

	public static class DBPam
	{
		public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
		public String data = null;
		public enum IstType
		{
			STRING, INTEGER, DATE
		}
		public IstType istType = IstType.STRING;

		public DBPam(Object data, IstType istType)
		{
			this.istType = istType;
			cvtDatByTyp(data);
		}

		public DBPam(Object data)
		{
			cvtDatByTyp(data);
		}

		public DBPam(DBPam pam)
		{
			data = pam.data;
			istType = pam.istType;
		}

		protected void cvtDatByTyp(Object data)
		{
			switch (istType)
			{
			case STRING:
				this.data = "\'" + data.toString() + "\'";
				break;
			case INTEGER:
				this.data = data.toString();
				break;
			case DATE:
				this.data = "date(\'" + SDF.format(data) + "\')";
				break;
			default:
				this.data = data.toString();
				break;
			}
		}
	}

	public static class DBCdn extends DBPam
	{
		public String colNam = "";
		public enum Opn
		{
			EQL, UEQL, BGR, SMR, BGR_EQL, SMR_EQL
		}
		public Opn opn = Opn.EQL;
		public enum Ops
		{
			NO, AND, OR
		}
		public Ops ops = Ops.AND;

		public DBCdn(Object data, IstType istType)
		{
			super(data, istType);
		}

		public DBCdn(Object data)
		{
			super(data);
		}

		public DBCdn(DBPam pam)
		{
			super(pam);
		}

		public DBCdn setData(Object data)
		{
			cvtDatByTyp(data);
			return this;
		}

		public DBCdn setColNam(String col)
		{
			this.colNam = col;
			return this;
		}

		public DBCdn setOpn(Opn opn)
		{
			this.opn = opn;
			return this;
		}

		public DBCdn setOps(Ops ops)
		{
			this.ops = ops;
			return this;
		}

		public static String cpsCdns(List<DBCdn> lst)
		{
			String ret = "";
			for(int i = 0; i < lst.size(); ++i)
			{
				DBCdn cdn = lst.get(i);
				ret += cdn.colNam;
				switch (cdn.opn)
				{
				case EQL:
					ret += "=";	break;
				case BGR:
					ret += ">";	break;
				case SMR:
					ret += "<";	break;
				case UEQL:
					ret += "<>";	break;
				case BGR_EQL:
					ret += ">=";	break;
				case SMR_EQL:
					ret += "<=";	break;
				}
				ret += cdn.data;
				if(i == lst.size() - 1)
				{
					break;
				}
				switch (cdn.ops)
				{
				case AND:
					ret += " AND ";	break;
				case OR:
					ret += " OR ";	break;
				}
			}
			return ret;
		}
	}

	public static String genDeleteDatSQL(String tblNam, List<DBCdn> cdn)
	{
		String sql = "DELETE FROM " + tblNam;
		if(cdn == null)	{ return sql; }
		sql += " WHERE " + DBCdn.cpsCdns(cdn);
		return sql;
	}

	public static String genInsertTblSQL(String tblNam, Map<String, DBPam> bindParam)
	{
		String sql = "INSERT INTO " + tblNam + " (";
		String val = " VALUES (";
		for(Map.Entry<String, DBPam> ety : bindParam.entrySet())
		{
			sql += ety.getKey() + ",";
			val += ety.getValue().data + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		val = val.substring(0, val.length() - 1);
		sql += ")" + val + ")";
		return sql;
	}

	public static String genUpdateTblSQL(String tblNam, Map<String, DBPam> pam, List<DBCdn> cdn)
	{
		String sql = "UPDATE " + tblNam + " SET ";
		for(Map.Entry<String, DBPam> ety : pam.entrySet())
		{
			sql += ety.getKey() + "=";
			sql += ety.getValue().data + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		if(cdn != null && !cdn.isEmpty())
		{
			sql += " WHERE " + DBCdn.cpsCdns(cdn);
		}
		return sql;
	}

//	public void insertDftDatForDebug(boolean chkEmpty)
//	{
//		SQLiteDatabase wdb = getDB().getWtblSQLiteDB();
//		if((chkEmpty) ? isTblEmpty(Dict.TABLE_NAME) : true)
//		{
//			Map<String, DBPam> param = new HashMap<>();
//			param.put(Dict.WORD, new DBPam("apple"));
//			param.put(Dict.SOUND_AM, new DBPam("ˈæpəl"));
//			param.put(Dict.SOUND_EN, new DBPam("ˈæpl"));
//			param.put(Dict.TRANSLATION, new DBPam("n.  苹果;  苹果公司;  苹果树"));
//			param.put(Dict.EXAMPLE, new DBPam(
//					"I want an apple.\n我想要一个苹果。|" +
//					"Penny\'\'s only son was the apple of her eye.\n彭妮的独子是她的心肝宝贝。"));
//			param.put(Dict.MULTIPLE, new DBPam("apples"));
//			param.put(Dict.NEAR_FORM, new DBPam("emple|copple"));
//			wdb.execSQL(genInsertTblSQL(Dict.TABLE_NAME, param));
//		}
//		if((chkEmpty) ? isTblEmpty(WordImg.TABLE_NAME) : true)
//		{
//			Map<String, DBPam> param = new HashMap<>();
//			param.put(WordImg.WORD, new DBPam("apple"));
//			param.put(WordImg.DESCRIPTION, new DBPam("app（add），len（length），每天吃可以长高高～"));
//			param.put(WordImg.PICTURE, new DBPam("http://img38.ddimg.cn/64/24/23607208-1_u.jpg"));
//			param.put(WordImg.PROPOSER, new DBPam(SessionData.ins().getUserName()));
//			param.put(WordImg.COMMENT, new DBPam(5, DBPam.IstType.INTEGER));
//			param.put(WordImg.SELECTED, new DBPam(1, DBPam.IstType.INTEGER));
//			wdb.execSQL(genInsertTblSQL(WordImg.TABLE_NAME, param));
//
//			param.clear();
//			param.put(WordImg.WORD, new DBPam("apple"));
//			param.put(WordImg.DESCRIPTION, new DBPam("appl（application），e（出），可以用来榨汁"));
//			param.put(WordImg.PROPOSER, new DBPam("opower"));
//			param.put(WordImg.COMMENT, new DBPam(2, DBPam.IstType.INTEGER));
//			param.put(WordImg.SELECTED, new DBPam(0, DBPam.IstType.INTEGER));
//			wdb.execSQL(genInsertTblSQL(WordImg.TABLE_NAME, param));
//		}
//		if(isTblEmpty(WordBook.TABLE_NAME))
//		{
//			String sql = "INSERT INTO "
//					+ WordBook.TABLE_NAME + " ("
//					+ WordBook.WORD + ","
//					+ WordBook.FAMILIARITY + ","
//					+ WordBook.DIFFICULTY + ","
//					+ WordBook.IMPORTANCE + ")"
//					+ " SELECT "
//					+ Dict.WORD + ","
//					+ WordBook.UNKNOWN + ","
//					+ WordBook.CAN_REM + ","
//					+ "0"
//					+ " FROM " + Dict.TABLE_NAME;
//			wdb.execSQL(sql);
//		}
//	}

	public static boolean isTblEmpty(String tblName)
	{
		SQLiteDatabase rdb = getDB().getRdblSQLiteDB();
		Cursor csr = rdb.query(tblName, new String[] { AUTO_ID }, null, null, null, null, null);
		boolean ret = (csr.getCount() == 0);
		csr.close();
		return ret;
	}

	public static int getRecordCount(String tblName, String col, List<DBCdn> cdn)
	{
		String sql = "SELECT COUNT(" + col + ") FROM " + tblName;
		if(cdn != null && !cdn.isEmpty())
		{
			sql += " WHERE " + DBCdn.cpsCdns(cdn);
		}
		Cursor csr = getDB().getRdblSQLiteDB().rawQuery(sql, null);
		int rcdCt = csr.getInt(0);
		csr.close();
		return rcdCt;
	}

	public static boolean chkIdentifiedRcdExs(String tblName, String col, List<DBCdn> cdn)
	{
		String sql = "SELECT ";
		if(col == null || col.isEmpty())
		{
			sql += "* FROM " + tblName;
		}
		else
		{
			sql += col + " FROM " + tblName;
		}
		if(cdn != null && !cdn.isEmpty())
		{
			sql += " WHERE " + DBCdn.cpsCdns(cdn);
		}
		Cursor csr = getDB().getRdblSQLiteDB().rawQuery(sql, null);
		boolean ret = (csr.getCount() != 0);
		csr.close();
		return ret;
	}

	public static int getSglInteger(String tblName, String col, List<DBCdn> cdn, int defRet)
	{
		Cursor csr = getDB().getRdblSQLiteDB().query(
				tblName, new String[]{col}, DBCdn.cpsCdns(cdn), null, null, null, null);
		if(csr.moveToFirst())
		{
			int idx = csr.getColumnIndex(col);
			if(idx != -1 && !csr.isNull(idx))
			{
				int ret = csr.getInt(idx);
				csr.close();
				return ret;
			}
		}
		csr.close();
		return defRet;
	}

	public static String getSglString(String tblName, String col, List<DBCdn> cdn, String defRet)
	{
		Cursor csr = getDB().getRdblSQLiteDB().query(
				tblName, new String[]{col}, DBCdn.cpsCdns(cdn), null, null, null, null);
		if(csr.moveToFirst())
		{
			int idx = csr.getColumnIndex(col);
			if(idx != -1 && !csr.isNull(idx))
			{
				String ret = csr.getString(idx);
				csr.close();
				return ret;
			}
		}
		csr.close();
		return defRet;
	}
}
