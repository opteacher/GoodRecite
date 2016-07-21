package com.test.opower.goodrecite.database;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBCtrl.DBPam.IstType;
import com.test.opower.goodrecite.database.DBCtrl.WordImg;
import com.test.opower.goodrecite.database.DBCtrl.DBPam;
import com.test.opower.goodrecite.database.DBCtrl.DBCdn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-7-21.
 */
public class DBOpnIstWdsImg extends DBMdl.DBOpn
{
	private String word = "";

	private DBOpnIstWdsImg()	{}
	private static DBOpnIstWdsImg instance = null;
	public static DBOpnIstWdsImg ins()
	{
		if(instance == null)
		{
			instance = new DBOpnIstWdsImg();
		}
		return instance;
	}

	@Override
	protected int preExe(Object pam, Object rst)
	{
		//参数检测
		if(!CmnUtils.isCls(pam, String.class))
		{
			return R.string.err_not_rgt_cls;
		}
		word = (String) pam;

		//将新的单词联想插入数据库之前，先将数据库的关于此单词的选定取消
		Map<String, DBPam> p = new HashMap<>();
		p.put(WordImg.SELECTED, new DBCtrl.DBPam(0, IstType.INTEGER));
		List<DBCdn> c = new ArrayList<>();
		c.add(new DBCdn(word).setColNam(WordImg.WORD));
		c.add(new DBCdn(1, IstType.INTEGER).setColNam(WordImg.SELECTED));
		wdb.execSQL(DBCtrl.genUpdateTblSQL(WordImg.TABLE_NAME, p, c));

		return 0;
	}

	public static class ExePam
	{
		public String dsc = "";
		public String lclPic = "";
		public String netPic = "";
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		//参数检测
		if(!CmnUtils.isCls(pam, ExePam.class))
		{
			return R.string.err_not_rgt_cls;
		}
		ExePam ep = (ExePam) pam;

		//再插入新的单词联想
		Map<String, DBPam> p = new HashMap<>();
		//单词
		p.put(WordImg.WORD, new DBPam(word));
		//文字描述
		p.put(WordImg.DESCRIPTION, new DBPam(ep.dsc));
		//图片联想
		if(!ep.lclPic.isEmpty())
		{
			p.put(WordImg.PICTURE, new DBPam(ep.lclPic));
		}
		else
		if(!ep.netPic.isEmpty())
		{
			p.put(WordImg.PICTURE, new DBPam(ep.netPic));
		}
		//写入当前用户名
		p.put(WordImg.PROPOSER, new DBPam(SessionData.ins().getUserName()));
		//默认选定
		p.put(WordImg.SELECTED, new DBPam(1, IstType.INTEGER));
		wdb.execSQL(DBCtrl.genInsertTblSQL(WordImg.TABLE_NAME, p));

		return 0;
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
