package com.test.opower.goodrecite.database;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-8.
 */
public class DBOpnSelWdsNear extends DBMdl.DBOpn
{
	private DBOpnSelWdsNear() {}
	private static DBOpnSelWdsNear instance = null;
	public static DBOpnSelWdsNear ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsNear();
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

	public static class ExePam
	{
		public String word = "";
		public int num = 0;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		if(pam == null || pam.getClass() != ExePam.class)
		{
			return R.string.err_param;
		}
		ExePam ep = (ExePam) pam;
		if(rst == null || rst.getClass() != ExeRst.class)
		{
			return R.string.err_not_rgt_cls;
		}
		ExeRst er = (ExeRst) rst;

		List<DBCdn> cdn = new ArrayList<>();
		cdn.add(new DBCtrl.DBCdn(ep.word).setColNam(Dict.WORD));
		int autoId = DBCtrl.getSglInteger(Dict.TABLE_NAME, DBCtrl.AUTO_ID, cdn, -1);
		DBCdn idCdn = new DBCdn(autoId, DBPam.IstType.INTEGER)
				.setColNam(DBCtrl.AUTO_ID);
		cdn.clear();
		cdn.add(idCdn);
		String str = "";
		for(int i = 1; i < ep.num; ++i)
		{
			idCdn.setData(autoId + i);
			str = DBCtrl.getSglString(Dict.TABLE_NAME, Dict.WORD, cdn, "");
			if(!str.isEmpty())
			{
				er.lst.add(str);
			}

			if(er.lst.size() >= ep.num - 1)
			{
				break;
			}

			idCdn.setData(autoId - i);
			str = DBCtrl.getSglString(Dict.TABLE_NAME, Dict.WORD, cdn, "");
			if(!str.isEmpty())
			{
				er.lst.add(str);
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
