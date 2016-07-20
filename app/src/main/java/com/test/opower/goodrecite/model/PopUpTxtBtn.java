package com.test.opower.goodrecite.model;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-7-21.
 */
public class PopUpTxtBtn extends Button implements DialogInterface.OnCancelListener
{
	private boolean isSelect = false;

	public PopUpTxtBtn(Context context)
	{
		this(context, null, 0);
	}

	public PopUpTxtBtn(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public PopUpTxtBtn(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onCancel(DialogInterface dialogInterface)
	{
		chgSelect();
		postInvalidate();
	}

	public PopUpTxtBtn setOnClickListener(final OnClickListener l)
	{
		this.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//改变按钮的背景颜色
				chgSelect();
				//执行用户定义的按键事件
				l.onClick(view);
			}
		});
		return this;
	}

	private void chgSelect()
	{
		setBackgroundResource((isSelect) ?
				android.R.color.transparent : R.color.colorSelected);
		isSelect = !isSelect;
	}

	public interface OnClickListener
	{
		void onClick(View vw);
	}
}
