package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;

import java.util.Map;

/**
 * Created by opower on 16-7-1.
 */
public class DBOpnSelWdsInf extends DBMdl.DBOpn
{
	private DBOpnSelWdsInf() {}
	private static DBOpnSelWdsInf instance = null;
	public static DBOpnSelWdsInf ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsInf();
		}
		return instance;
	}

	@Override
	protected int preExe(Object pam, Object rst)
	{
		return 0;
	}

	public static class WordInf
	{
		public int familiarity = 0;
		public int difficulty = 0;
		public int importance = 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		Cursor csr = null;
		Map<String, WordInf> wdsLst = (Map<String, WordInf>) rst;
		try
		{
			csr = rdb.query(DBCtrl.WordBook.TABLE_NAME,
					new String[]
							{
									DBCtrl.WordBook.WORD,
									DBCtrl.WordBook.FAMILIARITY,
									DBCtrl.WordBook.DIFFICULTY,
									DBCtrl.WordBook.IMPORTANCE
							},
					null, null, null, null, null);
			if(!csr.moveToFirst())
			{
				csr.close();
				return R.string.wng_no_wbk_exs;
			}
			do
			{
				int wdsIdx = csr.getColumnIndex(DBCtrl.WordBook.WORD);
				int fmlIdx = csr.getColumnIndex(DBCtrl.WordBook.FAMILIARITY);
				int difIdx = csr.getColumnIndex(DBCtrl.WordBook.DIFFICULTY);
				int iptIdx = csr.getColumnIndex(DBCtrl.WordBook.IMPORTANCE);

				String wd = csr.getString(wdsIdx);
				WordInf wi = new WordInf();
				wi.familiarity = csr.getInt(fmlIdx);
				wi.difficulty = csr.getInt(difIdx);
				wi.importance = csr.getInt(iptIdx);
				wdsLst.put(wd, wi);
			}
			while (csr.moveToNext());
		}
		finally
		{
			if(csr.getCount() > 0)
			{
				csr.close();
			}
		}
		return 0;
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
