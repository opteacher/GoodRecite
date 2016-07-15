package com.test.opower.goodrecite.database;

import android.database.Cursor;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl.Dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-7-15.
 */
public class DBOpnSelWdsDtl extends DBMdl.DBOpn
{
	private DBOpnSelWdsDtl() {}
	private static DBOpnSelWdsDtl instance = null;
	public static DBOpnSelWdsDtl ins()
	{
		if(instance == null)
		{
			instance = new DBOpnSelWdsDtl();
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
		public String sdAm = "";
		public String sdAmUrl = "";
		public String sdEn = "";
		public String sdEnUrl = "";
		public String trs = "";
		public String exp = "";
		public String phs = "";
		public String nrFm = "";
		public String nrSy = "";
		public String muli = "";
		public String psnt = "";
		public String past = "";
		public String done = "";
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
		ExeRst info = (ExeRst) rst;

		Cursor csr = rdb.query(Dict.TABLE_NAME,
				new String[]
						{
								Dict.SOUND_AM,
								Dict.SOUND_AM_URL,
								Dict.SOUND_EN,
								Dict.SOUND_EN_URL,
								Dict.TRANSLATION,
								Dict.EXAMPLE,
								Dict.PHRASE,
								Dict.NEAR_FORM,
								Dict.NEAR_SYNO,
								Dict.MULTIPLE,
								Dict.PRESENT,
								Dict.PAST,
								Dict.DONE
						},
				Dict.WORD + "=?",
				new String[]{word},
				null, null, null);

		if (csr.moveToFirst())
		{
			final int SdAmIdx = csr.getColumnIndex(Dict.SOUND_AM);
			final int SdAmUrlIdx = csr.getColumnIndex(Dict.SOUND_AM_URL);
			final int SdEnIdx = csr.getColumnIndex(Dict.SOUND_EN);
			final int SdEnUrlIdx = csr.getColumnIndex(Dict.SOUND_EN_URL);
			final int TrsIdx = csr.getColumnIndex(Dict.TRANSLATION);
			final int ExpIdx = csr.getColumnIndex(Dict.EXAMPLE);
			final int PhsIdx = csr.getColumnIndex(Dict.PHRASE);
			final int NrFmIdx = csr.getColumnIndex(Dict.NEAR_FORM);
			final int NrSyIdx = csr.getColumnIndex(Dict.NEAR_SYNO);
			final int MuliIdx = csr.getColumnIndex(Dict.MULTIPLE);
			final int PsntIdx = csr.getColumnIndex(Dict.PRESENT);
			final int PastIdx = csr.getColumnIndex(Dict.PAST);
			final int DoneIdx = csr.getColumnIndex(Dict.DONE);

			info.trs = csr.getString(TrsIdx);

			info.sdAm = csr.getString(SdAmIdx);
			if(!csr.isNull(SdAmUrlIdx))
			{
				info.sdAmUrl = csr.getString(SdAmUrlIdx);
			}
			info.sdEn = csr.getString(SdEnIdx);
			if(!csr.isNull(SdEnUrlIdx))
			{
				info.sdEnUrl = csr.getString(SdEnUrlIdx);
			}
			if (!csr.isNull(ExpIdx))
			{
				info.exp = csr.getString(ExpIdx);
			}
			if (!csr.isNull(PhsIdx))
			{
				info.phs = csr.getString(PhsIdx);
			}
			if (!csr.isNull(NrFmIdx))
			{
				info.nrFm = csr.getString(NrFmIdx);
			}
			if (!csr.isNull(NrSyIdx))
			{
				info.nrSy = csr.getString(NrSyIdx);
			}
			if (!csr.isNull(MuliIdx))
			{
				info.muli = csr.getString(MuliIdx);
			}
			if (!csr.isNull(PsntIdx))
			{
				info.psnt = csr.getString(PsntIdx);
			}
			if (!csr.isNull(PastIdx))
			{
				info.past = csr.getString(PastIdx);
			}
			if (!csr.isNull(DoneIdx))
			{
				info.done = csr.getString(DoneIdx);
			}
			return 0;
		}
		else
		{
			return R.string.wng_no_wds_rcd;
		}
	}

	@Override
	protected int sufExe(Object pam, Object rst)
	{
		return 0;
	}
}
