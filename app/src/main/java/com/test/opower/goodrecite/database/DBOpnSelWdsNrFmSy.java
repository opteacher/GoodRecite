package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-8.
 */
public class DBOpnSelWdsNrFmSy extends DBMdl.DBOpn
{
	private DBOpnSelWdsNrFmSy() {}
	private static DBOpnSelWdsNrFmSy instance = null;
	public static DBOpnSelWdsNrFmSy ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsNrFmSy();
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
		public List<String> lst = null;
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
		ExeRst er = (ExeRst) rst;

		Cursor csr = rdb.query(DBCtrl.Dict.TABLE_NAME,
				new String[]
						{
								DBCtrl.Dict.NEAR_FORM,
								DBCtrl.Dict.NEAR_SYNO
						},
				DBCtrl.Dict.WORD + "=?", new String[] { word },
				null, null, null);
		int nrFmIdx = csr.getColumnIndex(DBCtrl.Dict.NEAR_FORM);
		int nrSyIdx = csr.getColumnIndex(DBCtrl.Dict.NEAR_SYNO);
		if(csr.moveToFirst())
		{
			er.lst = new ArrayList<>();

			String[] nearFm = {};
			if(!csr.isNull(nrFmIdx))
			{
				nearFm = csr.getString(nrFmIdx).split("\\|");
			}
			String[] nearSy = {};
			if(!csr.isNull(nrSyIdx))
			{
				nearSy = csr.getString(nrSyIdx).split("\\|");
			}
			for(String str : nearFm)
			{
				er.lst.add(str);
			}
			for(String str : nearSy)
			{
				er.lst.add(str);
			}
			return 0;
		}
		else
		{
			return R.string.wng_no_wds_example;
		}
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
