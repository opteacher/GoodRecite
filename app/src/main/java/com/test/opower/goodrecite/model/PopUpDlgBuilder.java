package com.test.opower.goodrecite.model;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by opower on 16-7-5.
 */
public abstract class PopUpDlgBuilder extends DialogBuilder
{
	public enum PosType
	{
		LFT_TOP, RGT_TOP, LFT_BTM, RGT_BTM
	}
	protected int x = 0;
	protected int y = 0;
	protected PosType pt = PosType.LFT_TOP;

	public PopUpDlgBuilder(Context context)
	{
		super(context);
	}

	public PopUpDlgBuilder setDlgPos(int x, int y)
	{
		this.x = x;	this.y = y;	return this;
	}

	public PopUpDlgBuilder setPosType(PosType pt)
	{
		this.pt = pt;	return this;
	}

	@Override
	public void adjustDlgSize()
	{
		//设置对话框的重力朝向
		Window w = dlg.getWindow();
		switch(pt)
		{
		case LFT_TOP:
			w.setGravity(Gravity.LEFT | Gravity.TOP);	break;
		case RGT_TOP:
			w.setGravity(Gravity.RIGHT | Gravity.TOP);	break;
		case LFT_BTM:
			w.setGravity(Gravity.LEFT | Gravity.BOTTOM);	break;
		case RGT_BTM:
			w.setGravity(Gravity.RIGHT | Gravity.BOTTOM);	break;
		}

		//再设置对话框的位置和尺寸
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = x;
		lp.y = y;
		lp.width = width;
		lp.height = height;
		w.setAttributes(lp);
	}
}
