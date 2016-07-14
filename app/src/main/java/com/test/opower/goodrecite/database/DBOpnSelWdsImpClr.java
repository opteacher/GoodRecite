package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.ImptLst;
import com.test.opower.goodrecite.database.DBCtrl.WordBook;

/**
 * Created by opower on 16-7-6.
 */
public class DBOpnSelWdsImpClr extends DBMdl.DBOpn
{
	private DBOpnSelWdsImpClr() {}
	private static DBOpnSelWdsImpClr instance = null;
	public static DBOpnSelWdsImpClr ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsImpClr();
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
		public int impClr = 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		String word = (String) pam;
		if(word == null || word.isEmpty())
		{
			return R.string.err_param;
		}
		if(rst == null || rst.getClass() != ExeRst.class)
		{
			return R.string.err_not_rgt_cls;
		}
		ExeRst clr = (ExeRst) rst;

		String sql =
				"SELECT " + ImptLst.BACKGROUND_CLR +
				" FROM " + ImptLst.TABLE_NAME +
				" WHERE " + ImptLst.IMPORTANCE + "=" +
						"(SELECT " + WordBook.IMPORTANCE +
						" FROM " + WordBook.TABLE_NAME +
						" WHERE " + WordBook.WORD + "=?)";
		Cursor csr = rdb.rawQuery(sql, new String[]	{ word });
		int clrIdx = csr.getColumnIndex(ImptLst.BACKGROUND_CLR);
		if(csr.moveToFirst())
		{
			clr.impClr = csr.getInt(clrIdx);
			csr.close();
			return 0;
		}
		else
		{
			csr.close();
			return R.string.err_db_exe;
		}
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
