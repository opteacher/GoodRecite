package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-8.
 */
public class DBOpnSelWdsExpPhs extends DBMdl.DBOpn
{
	private DBOpnSelWdsExpPhs() {}
	private static DBOpnSelWdsExpPhs instance = null;
	public static DBOpnSelWdsExpPhs ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsExpPhs();
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
								DBCtrl.Dict.EXAMPLE,
								DBCtrl.Dict.PHRASE
						},
				DBCtrl.Dict.WORD + "=?", new String[] { word },
				null, null, null);
		int expIdx = csr.getColumnIndex(DBCtrl.Dict.EXAMPLE);
		int phsIdx = csr.getColumnIndex(DBCtrl.Dict.PHRASE);
		if(csr.moveToFirst())
		{
			er.lst = new ArrayList<>();

			String[] example = {};
			if(!csr.isNull(expIdx))
			{
				example = csr.getString(expIdx).split("\\|");
			}
			String[] phrase = {};
			if(!csr.isNull(phsIdx))
			{
				phrase = csr.getString(phsIdx).split("\\|");
			}
			for(String str : example)
			{
				er.lst.add(str);
			}
			for(String str : phrase)
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
