package com.test.opower.goodrecite.database;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-6-29.
 */
public class DBOpnUpdStdPln extends DBMdl.DBOpn
{
	private DBOpnUpdStdPln() {}
	private static DBOpnUpdStdPln instance = null;
	public static DBOpnUpdStdPln ins()
	{
		if(instance == null)
		{
			instance = new DBOpnUpdStdPln();
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
		//对填入到数据库的数据进行检测
		if(pam.getClass() != Integer.class)
		{
			return R.string.err_not_rgt_cls;
		}
		int newDayNum = (int) pam;
		String plnId = SessionData.ins().getTestPlan();
		if(plnId.isEmpty())
		{
			return R.string.err_sm_not_rdy;
		}

		//装配SQL的参数和条件
		Map<String, DBCtrl.DBPam> param = new HashMap<>();
		param.put(DBCtrl.TestPlan.ONE_DAY_NUM,
				new DBCtrl.DBPam(newDayNum, DBCtrl.DBPam.IstType.INTEGER));
		List<DBCtrl.DBCdn> cdn = new ArrayList<>();
		cdn.add(new DBCtrl.DBCdn(plnId).setColNam(DBCtrl.TestPlan.PLAN_ID));

		//执行SQL
		wdb.execSQL(DBCtrl.genUpdateTblSQL(DBCtrl.TestPlan.TABLE_NAME, param, cdn));

		return 0;
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
