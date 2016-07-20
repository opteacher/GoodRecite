package com.test.opower.goodrecite.word_page.word_test;

import android.view.View;
import android.widget.Button;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;

/**
 * Created by opower on 16-7-1.
 */
public class ExitWdsTstCtrl extends ViewCtrl
{
	private Button btnEnsureDlg = null;

	private ExitWdsTstCtrl(BaseActivity act)
	{
		super(act);

		ExitWdsTstBuilder.ini(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		ExitWdsTstBuilder.ins()
				.setDlgSizeByScn(0, 0.4f)
				.create()
				.show();
	}

	@Override
	public void bindMainContent(View vw)
	{
		collectCtlFromView(vw);

		btnEnsureDlg.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				popBackFragment(null);
				popBackWdsTstView();
			}
		});
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		btnEnsureDlg = (Button) vw.findViewById(R.id.btnEnsureDlg);
	}

	@Override
	public void popBackFragment(Object pam)
	{
		//关闭对话框
		ExitWdsTstBuilder.ins().getDlg().dismiss();
	}

	private void popBackWdsTstView()
	{
		//修改单词主页类型
		WordMainCtrl.ins().setViewType(WordMainCtrl.ViewType.WORD_MAIN);
		//将单词测试Fragment推出栈（因为是通过对话框退出的，所以不存在强制退出判断）
		activity.getFragmentManager().popBackStack();//用于退出测试底部按钮
		activity.getFragmentManager().popBackStack();//用于退出测试主页面
	}

	private static ExitWdsTstCtrl instance = null;
	public static ExitWdsTstCtrl ini(BaseActivity act)
	{
		instance = new ExitWdsTstCtrl(act);	return instance;
	}
	public static ExitWdsTstCtrl ins()	{ return instance; }
}
