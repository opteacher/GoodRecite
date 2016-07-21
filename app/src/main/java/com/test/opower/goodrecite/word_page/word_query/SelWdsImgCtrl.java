package com.test.opower.goodrecite.word_page.word_query;

import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.ViewCtrl;

/**
 * Created by opower on 16-7-22.
 */
public class SelWdsImgCtrl extends ViewCtrl
{
	private String word = "";

	private SelWdsImgCtrl(BaseActivity act)
	{
		super(act);

		SelWdsImgBuilder.ini(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//转换传进来的参数
		if(pam == null)
		{
			Toast.makeText(
					getActivity(),
					R.string.err_vw_trs_pam,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(pam.getClass() == String.class)
		{
			word = (String) pam;
		}
		else
		{
			Toast.makeText(
					getActivity(),
					R.string.err_param,
					Toast.LENGTH_SHORT).show();
			return;
		}

		SelWdsImgBuilder.ins().create().show();
	}

	@Override
	public void popBackFragment(Object pam)
	{

	}

	private static SelWdsImgCtrl instance = null;
	public static SelWdsImgCtrl ini(BaseActivity act)
	{
		instance = new SelWdsImgCtrl(act);	return instance;
	}
	public static SelWdsImgCtrl ins()	{ return instance; }
}
