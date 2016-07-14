package com.test.opower.goodrecite.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;

/**
 * Created by opower on 16-6-8.
 */
public abstract class DialogBuilder
{
	protected Context context = null;
	protected LayoutInflater inflater = null;
	protected Dialog dlg = null;
	protected int width = -2;
	protected int height = -2;

	public DialogBuilder(Context context)
	{
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public DialogBuilder setDlgSize(int wid, int hgt)
	{
		if(wid != 0)	{ width = wid; }
		if(hgt != 0)	{ height = hgt; }
		return this;
	}

	public DialogBuilder setDlgSizeByScn(float widScl, float hgtScl)
	{
		DisplayMetrics dm = CmnUtils.getActivityDM((Activity) context);
		if(widScl != 0)	{ this.width = (int) (dm.widthPixels * widScl); }
		if(hgtScl != 0)	{ this.height = (int) (dm.heightPixels * hgtScl); }
		return this;
	}

	protected void adjustDlgSize()
	{
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.width = width;
		lp.height = height;
		w.setAttributes(lp);
	}

	public abstract Dialog create();

	protected View loadDlgAndSetTitle(int lytId, String title)
	{
		//配布布局
		View vw = inflater.inflate(lytId, null);
		View ttl = vw.findViewById(R.id.txtDlgTitle);
		if(ttl != null)
		{
			((TextView) ttl).setText(title);
		}

		//将设置好的视图添加到对话框中
		dlg.setContentView(vw);

		return vw;
	}

	public Dialog getDlg()	{ return dlg; }

	public void resetDlg()	{ dlg = null; }
}
