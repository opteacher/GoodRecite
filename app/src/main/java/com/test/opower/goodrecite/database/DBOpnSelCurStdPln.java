package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;

/**
 * Created by opower on 16-7-1.
 */
public class DBOpnSelCurStdPln extends DBMdl.DBOpn
{
	private DBOpnSelCurStdPln() {}
	private static DBOpnSelCurStdPln instance = null;
	public static DBOpnSelCurStdPln ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelCurStdPln();
		}
		return instance;
	}

	@Override
	protected int preExe(Object pam, Object rst)
	{
		return 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		Cursor csr = rdb.query(
				DBCtrl.TestPlan.TABLE_NAME,
				new String[]
						{
								DBCtrl.TestPlan.PLAN_ID,
								DBCtrl.TestPlan.SELECT_WBOOK
						},
				DBCtrl.TestPlan.CUR_PROCESS + "=1" +
						" AND " +
				DBCtrl.TestPlan.USER_ID + "=?",
				new String[]
						{
								SessionData.ins().getUserName()
						}, null, null, null);
		if(csr.moveToFirst())
		{
			int idx = -1;
			idx = csr.getColumnIndex(DBCtrl.TestPlan.PLAN_ID);
			SessionData.ins().setTestPlan(csr.getString(idx));
			idx = csr.getColumnIndex(DBCtrl.TestPlan.SELECT_WBOOK);
			SessionData.ins().setWordBook(csr.getString(idx));
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
