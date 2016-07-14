package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBMdl.LstItmInf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-6.
 */
public class DBOpnSelWdsDifTbl extends DBMdl.DBOpn
{
	private DBOpnSelWdsDifTbl() {}
	private static DBOpnSelWdsDifTbl instance = null;
	public static DBOpnSelWdsDifTbl ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsDifTbl();
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
				DBCtrl.DiffLst.TABLE_NAME,
				new String[]
						{
								DBCtrl.DiffLst.DIFFICULTY,
								DBCtrl.DiffLst.TXT_DESC,
								DBCtrl.DiffLst.BACKGROUND_CLR
						},
				null, null, null, null, null);
		int difIdx = csr.getColumnIndex(DBCtrl.DiffLst.DIFFICULTY);
		int dscIdx = csr.getColumnIndex(DBCtrl.DiffLst.TXT_DESC);
		int clrIdx = csr.getColumnIndex(DBCtrl.DiffLst.BACKGROUND_CLR);
		if(!csr.moveToFirst())
		{
			return R.string.wng_no_wds_dif_rcd;
		}
		List<LstItmInf> lst = (List<LstItmInf>) rst;
		lst.clear();
		do
		{
			LstItmInf inf = new LstItmInf();
			inf.data = csr.getInt(difIdx);
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
