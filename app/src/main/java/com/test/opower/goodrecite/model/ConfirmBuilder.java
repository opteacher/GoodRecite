package com.test.opower.goodrecite.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-6-10.
 */
public class ConfirmBuilder extends DialogBuilder
{
	protected View dlgView = null;
	protected Button btnEnsureDlg = null;
	protected Button btnDismissDlg = null;

	public ConfirmBuilder(Context context)
	{
		super(context);
	}

	@Override
	public Dialog create()
	{
		if(dlg == null)
		{
			//创建对话框
			dlg = new Dialog(context);
			dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

			//导入单选对话框并设置标题
			dlgView = loadDlgAndSetTitle(R.layout.dialog_two_btn, "确认退出");

			//从页面上取得控件引用
			collectCtlFromView(dlgView);

			//设置内容视图
			View ctt = getContentView();
			if(ctt != null)
			{
				((FrameLayout) dlgView.findViewById(R.id.lytDlgCtt)).addView(ctt);
			}

			//调整对话框高度
			adjustDlgSize();

			//设置按键事件
			btnDismissDlg.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					dlg.dismiss();
				}
			});
		}
		return dlg;
	}

	protected View getContentView()
	{
		return null;
	}

	protected void collectCtlFromView(View vw)
	{
		btnEnsureDlg = (Button) vw.findViewById(R.id.btnEnsureDlg);
		btnDismissDlg = (Button) vw.findViewById(R.id.btnDismissDlg);
	}
}
