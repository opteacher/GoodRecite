package com.test.opower.goodrecite.word_page.word_query;

import android.view.View;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

/**
 * Created by opower on 16-7-7.
 */
public class WordDetailCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{
	private WordDetailCtrl(BaseActivity act)
	{
		super(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsCtt, WordDetailFragment.ins())
				.commit();
	}

	@Override
	public void bindMainContent(View vw)
	{
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
	}

	@Override
	public void popBackFragment(Object pam)
	{

	}

	@Override
	public void bindBtmBtn(View vw)
	{
		activity.getFragmentManager().popBackStack();
		activity.getFragmentManager().popBackStack();
	}

	private static WordDetailCtrl instance = null;
	public static WordDetailCtrl ini(BaseActivity act)
	{
		instance = new WordDetailCtrl(act);	return instance;
	}
	public static WordDetailCtrl ins()	{ return instance; }
}
