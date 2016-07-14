package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.WordBook;

/**
 * Created by opower on 16-7-6.
 */
public class DBOpnSelSglWdsInf extends DBMdl.DBOpn
{
	private DBOpnSelSglWdsInf() {}
	private static DBOpnSelSglWdsInf instance = null;
	public static DBOpnSelSglWdsInf ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelSglWdsInf();
		}
		return instance;
	}

	@Override
	protected int preExe(Object pam, Object rst)
	{
		return 0;
	}

	public static class ExeRst
	{
		public int familiarity = 0;
		public int difficulty = 0;
		public int importance = 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		String word = (String) pam;
		if(word == null || word.isEmpty())
		{
			return R.string.err_not_rgt_cls;
		}
		ExeRst tmp = null;
		if(rst != null && rst.getClass() == ExeRst.class)
		{
			tmp = (ExeRst) rst;
		}

		Cursor csr = rdb.query(WordBook.TABLE_NAME,
				new String[]
						{
								WordBook.FAMILIARITY,
								WordBook.DIFFICULTY,
								WordBook.IMPORTANCE
						},
				WordBook.WORD + "=?", new String[]	{ word }, null, null, null);
		if(!csr.moveToFirst())
		{
			csr.close();
			return R.string.wng_no_wbk_exs;
		}
		else
		{
			int fmlIdx = csr.getColumnIndex(WordBook.FAMILIARITY);
			int difIdx = csr.getColumnIndex(WordBook.DIFFICULTY);
			int iptIdx = csr.getColumnIndex(WordBook.IMPORTANCE);

			if(tmp != null)
			{
				tmp.familiarity = csr.getInt(fmlIdx);
				tmp.difficulty = csr.getInt(difIdx);
				tmp.importance = csr.getInt(iptIdx);
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
