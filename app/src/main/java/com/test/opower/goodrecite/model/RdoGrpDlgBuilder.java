package com.test.opower.goodrecite.model;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.test.opower.goodrecite.R;

import java.util.List;

/**
 * Created by opower on 16-7-5.
 */
public class RdoGrpDlgBuilder extends PopUpDlgBuilder
{
	private static RdoGrpDlgBuilder instance = null;
	public static RdoGrpDlgBuilder ini(Context context)
	{
		instance = new RdoGrpDlgBuilder(context);
		return instance;
	}
	public static RdoGrpDlgBuilder ins()	{ return instance; }

	private ViewCtrl ctrl = null;
	private RadioGroup rgpDlgLst = null;

	private RdoGrpDlgBuilder(Context context)
	{
		super(context);

		rgpDlgLst = new RadioGroup(context);
		rgpDlgLst.setId(R.id.rgpDlgLst);
		rgpDlgLst.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		rgpDlgLst.setPadding(5, 5, 5, 5);
	}

	public static class RdoInfo
	{
		public Object tag = null;
		public String txt = "";
		public int clr = 0;
		public boolean chk = false;
	}

	public RdoGrpDlgBuilder setRdoInfoLst(List<RdoInfo> lst)
	{
		rgpDlgLst.removeAllViews();
		for(RdoInfo inf : lst)
		{
			rgpDlgLst.addView(geneRdoBtn(inf));
		}
		return this;
	}

	public RdoGrpDlgBuilder setViewCtrl(ViewCtrl vc)
	{
		ctrl = vc;	return this;
	}

	private RadioButton geneRdoBtn(RdoInfo inf)
	{
		RadioButton rdo = new RadioButton(context);
		rdo.setText(inf.txt);
		rdo.setBackgroundColor(inf.clr);
		rdo.setLayoutParams(new RadioGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		rdo.setTag(inf.tag);
		rdo.setChecked(inf.chk);
		return rdo;
	}

	public RdoGrpDlgBuilder setDBOpnID(int id)
	{
		rgpDlgLst.setTag(id);	return this;
	}

	@Override
	public Dialog create()
	{
		//如果没有对话框，再创建
		if(dlg == null)
		{
			dlg = new Dialog(context, R.style.PopupDlgTheme);
		}
		//设置对话框内容
		dlg.setContentView(rgpDlgLst);
		//调整对话框
		adjustDlgSize();
		//绑定控制器
		if(ctrl != null)
		{
			ctrl.bindMainContent(dlg.getWindow().getDecorView());
		}

		return dlg;
	}
}
