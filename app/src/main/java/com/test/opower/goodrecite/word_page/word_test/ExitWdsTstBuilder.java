package com.test.opower.goodrecite.word_page.word_test;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.opower.goodrecite.model.ConfirmBuilder;

/**
 * Created by opower on 16-7-1.
 */
public class ExitWdsTstBuilder extends ConfirmBuilder
{
	private final static String txt = "确认退出单词测试？";
	private TextView txtExitTst = null;

	private ExitWdsTstBuilder(Context context)
	{
		super(context);

		txtExitTst = new TextView(context);
		txtExitTst.setBackgroundResource(android.R.color.white);
		txtExitTst.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		txtExitTst.setGravity(Gravity.CENTER);
		txtExitTst.setTextSize(30);
		txtExitTst.setText(txt);
	}

	@Override
	public Dialog create()
	{
		super.create();

		ExitWdsTstCtrl.ins().bindMainContent(dlg.getWindow().getDecorView());

		return dlg;
	}

	@Override
	protected View getContentView()
	{
		return txtExitTst;
	}

	private static ExitWdsTstBuilder instance = null;
	public static ExitWdsTstBuilder ini(Context ctt)
	{
		instance = new ExitWdsTstBuilder(ctt);	return instance;
	}
	public static ExitWdsTstBuilder ins()	{ return instance; }
}
