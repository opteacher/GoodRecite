package com.test.opower.goodrecite.database;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBCtrl.WordImg;

/**
 * Created by opower on 16-7-21.
 */
public class DBOpnSelCurWdsImg extends DBMdl.DBOpn
{
	private DBOpnSelCurWdsImg()	{}
	private static DBOpnSelCurWdsImg instance = null;
	public static DBOpnSelCurWdsImg ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelCurWdsImg();
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
		public String dsc = "";
		public Bitmap pic = null;
	}

	@Override
	protected int exe(Object pam, Object rst)
	{
		//检测参数并将参数转成实际的类型
		if(!CmnUtils.isCls(pam, String.class))
		{
			return R.string.err_not_rgt_cls;
		}
		String word = (String) pam;
		if(!CmnUtils.isCls(rst, ExeRst.class))
		{
			return R.string.err_param;
		}
		ExeRst tmp = (ExeRst) rst;

		Cursor csr = rdb.query(WordImg.TABLE_NAME,
				new String[]
						{
								WordImg.DESCRIPTION,
								WordImg.PICTURE
						},
				WordImg.WORD + "=? AND " + WordImg.SELECTED + "=?",
				new String[]
						{
								word, "1"
						}, null, null, null);
		if(csr.moveToFirst())
		{
			final int DscIdx = csr.getColumnIndex(WordImg.DESCRIPTION);
			final int PicIdx = csr.getColumnIndex(WordImg.PICTURE);

			tmp.dsc = csr.getString(DscIdx);
			if(!csr.isNull(PicIdx))
			{
				String picPh = csr.getString(PicIdx);

			}
		}
		csr.close();
		return 0;
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
