package com.test.opower.goodrecite.word_page.set_study_plan;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.DialogBuilder;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;

/**
 * Created by opower on 16-6-7.
 */
public class SetStudyPlanBuilder extends DialogBuilder
{
	private SetStudyPlanBuilder(Context ctt)
	{
		super(ctt);
	}
	private static SetStudyPlanBuilder instance = null;
	public static SetStudyPlanBuilder ini(Context ctt)
	{
		if(!(instance != null && instance.context == ctt))
		{
			instance = new SetStudyPlanBuilder(ctt);
		}
		return instance;
	}
	public static SetStudyPlanBuilder ins()	{ return instance; }

	@Override
	public Dialog create()
	{
		//创建对话框
		dlg = new Dialog(context);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//导入单选对话框并设置标题
		View vw = loadDlgAndSetTitle(R.layout.dialog_sgl_btn, "设置学习计划");

		final View ctt = inflater.inflate(R.layout.content_set_study_pln, null);
		((FrameLayout) vw.findViewById(R.id.lytDlgCtt)).addView(ctt);

		//设置对话框高度
		adjustDlgSize();

		//绑定对话框的控件事件
		// （因为对话框是独立于Fragment之外的，所以需要在生成的时候单独执行）
		WordMainCtrl.ins().bindMainContent(vw);

		return dlg;
	}
}
