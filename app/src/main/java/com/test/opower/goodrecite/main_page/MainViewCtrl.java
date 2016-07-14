package com.test.opower.goodrecite.main_page;

import android.content.Intent;
import android.view.View;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.word_page.WordActivity;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.model.ViewCtrl;

/**
 * Created by opower on 16-6-24.
 */
public class MainViewCtrl extends ViewCtrl
{
	private CstmBtn btnMuRctWd = null;
	private CstmBtn btnMuRdTrsf = null;
	private CstmBtn btnMuRdyEx = null;
	private CstmBtn btnMuStdSt = null;

	private MainViewCtrl(BaseActivity act)	{ super(act); }

	@Override
	public void toCurFragment(Object pam)
	{
		activity.getFragmentManager()
				.beginTransaction()
				.replace(R.id.lytMnCtt, MainFragment.ins())
				.commit();
	}

	@Override
	public void bindMainContent(View vw)
	{
		//收集页面上的控件
		collectCtlFromView(vw);

		//绑定页面的控件事件
		btnMuRctWd.setOnClickListener(new CstmBtn.OnClickListener()
		{
			//背诵单词按钮被点击
			@Override
			public void onClick(View vw)
			{
				activity.startActivity(new Intent(activity, WordActivity.class));
			}
		});
	}

	@Override
	public void popBackFragment(Object pam)
	{
		activity.finish();
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		btnMuRctWd = (CstmBtn) vw.findViewById(R.id.btnMuRctWd);
		btnMuRdTrsf = (CstmBtn) vw.findViewById(R.id.btnMuRdTrsf);
		btnMuRdyEx = (CstmBtn) vw.findViewById(R.id.btnMuRdyEx);
		btnMuStdSt = (CstmBtn) vw.findViewById(R.id.btnMuStdSt);
	}

	private static MainViewCtrl instance = null;
	public static MainViewCtrl 	ini(BaseActivity act)
	{
		instance = new MainViewCtrl(act);	return instance;
	}
	public static MainViewCtrl ins()	{ return instance; }
}
