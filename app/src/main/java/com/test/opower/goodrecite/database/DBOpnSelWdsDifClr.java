package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.DiffLst;
import com.test.opower.goodrecite.database.DBCtrl.WordBook;

/**
 * Created by opower on 16-7-6.
 */
public class DBOpnSelWdsDifClr extends DBMdl.DBOpn
{
	private DBOpnSelWdsDifClr() {}
	private static DBOpnSelWdsDifClr instance = null;
	public static DBOpnSelWdsDifClr ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsDifClr();
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
		public int difClr = 0;
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
				"SELECT " + DiffLst.BACKGROUND_CLR +
				" FROM " + DiffLst.TABLE_NAME +
				" WHERE " + DiffLst.DIFFICULTY + "=" +
						"(SELECT " + WordBook.DIFFICULTY +
						" FROM " + WordBook.TABLE_NAME +
						" WHERE " + WordBook.WORD + "=?)";
		Cursor csr = rdb.rawQuery(sql, new String[]	{ word });
		int clrIdx = csr.getColumnIndex(DiffLst.BACKGROUND_CLR);
		if(csr.moveToFirst())
		{
			clr.difClr = csr.getInt(clrIdx);
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
