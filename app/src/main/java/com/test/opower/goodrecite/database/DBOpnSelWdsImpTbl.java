package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.ImptLst;
import com.test.opower.goodrecite.database.DBMdl.LstItmInf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-6.
 */
public class DBOpnSelWdsImpTbl extends DBMdl.DBOpn
{
	private DBOpnSelWdsImpTbl() {}
	private static DBOpnSelWdsImpTbl instance = null;
	public static DBOpnSelWdsImpTbl ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsImpTbl();
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
		if(rst == null || rst.getClass() != ArrayList.class)
		{
			return R.string.err_not_rgt_cls;
		}

		Cursor csr = rdb.query(
				ImptLst.TABLE_NAME,
				new String[]
						{
								ImptLst.IMPORTANCE,
								ImptLst.TXT_DESC,
								ImptLst.BACKGROUND_CLR
						},
				null, null, null, null, null);
		int impIdx = csr.getColumnIndex(ImptLst.IMPORTANCE);
		int dscIdx = csr.getColumnIndex(ImptLst.TXT_DESC);
		int clrIdx = csr.getColumnIndex(ImptLst.BACKGROUND_CLR);
		if(!csr.moveToFirst())
		{
			return R.string.wng_no_wds_imp_rcd;
		}
		List<LstItmInf> lst = (List<LstItmInf>) rst;
		do
		{
			LstItmInf inf = new LstItmInf();
			inf.data = csr.getInt(impIdx);
			inf.txtDesc = csr.getString(dscIdx);
			inf.bkgdClr = csr.getInt(clrIdx);
			lst.add(inf);
		} while(csr.moveToNext());
		csr.close();

		return 0;
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
