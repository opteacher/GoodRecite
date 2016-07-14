package com.test.opower.goodrecite.word_page.word_query;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBMdl.LstItmInf;
import com.test.opower.goodrecite.database.DBMdl.WdsUpdInf;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.PopUpDlgBuilder;
import com.test.opower.goodrecite.model.RdoGrpDlgBuilder;
import com.test.opower.goodrecite.model.RdoGrpDlgBuilder.RdoInfo;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-5.
 */
public class SetWdsInfCtrl extends ViewCtrl
{
	private RadioGroup rgpDlgLst = null;
	private String word = "";
	private Button btnSetWdsInf = null;

	private SetWdsInfCtrl(BaseActivity act)
	{
		super(act);
	}

	public static class DlgCrtePam
	{
		public String word = "";
		public Button btn = null;
		public int x = 0;
		public int y = 0;
		public int width = -2;
		public int selDBOpn = -1;
		public int updDBOpn = -1;

		public void reset()
		{
			word = "";
			btn = null;
			x = 0;
			y = 0;
			width = -2;
			selDBOpn = -1;
			updDBOpn = -1;
		}
	}

	@Override
	public void toCurFragment(Object pam)
	{
		RdoGrpDlgBuilder inst = RdoGrpDlgBuilder.ins();

		//如果已经显示了，则关闭之
		if(inst.getDlg() != null && inst.getDlg().isShowing())
		{
			inst.getDlg().dismiss();
			return;
		}

		//取得参数并获得当前单词
		DlgCrtePam dcp = (CmnUtils.isCls(pam, DlgCrtePam.class)) ?
				(DlgCrtePam) pam : null;
		word = dcp.word;
		btnSetWdsInf = dcp.btn;

		//从数据库中获取单词信息的对照表
		DBMdl.ins().begOperation(dcp.selDBOpn, null, null);
		List<LstItmInf> DBLst = new ArrayList<>();
		int opnMsg = DBMdl.ins().exeOperation(null, DBLst);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}

		//填入数据库更新ID
		inst.setDBOpnID(dcp.updDBOpn);

		//将全局参数中的单词难度信息转成List表
		List<RdoInfo> vwLst = new ArrayList<>();
		for(int i = 0; i < DBLst.size(); ++i)
		{
			LstItmInf lii = DBLst.get(i);
			RdoInfo ri = new RdoInfo();
			ri.tag = lii.data;
			ri.txt = lii.txtDesc;
			ri.clr = lii.bkgdClr;
			ri.chk = (WordTestCtrl.ins().getCurWdsDif().ordinal() == lii.data);
			vwLst.add(ri);
		}
		inst.setRdoInfoLst(vwLst);

		//绑定控制器
		inst.setViewCtrl(SetWdsInfCtrl.ins());

		//生成并显示对话框
		if(dcp == null)
		{
			//如果没有指定参数，单纯显示对话框
			inst.create().show();
		}
		else
		{
			inst.setPosType(PopUpDlgBuilder.PosType.LFT_TOP)
				.setDlgPos(dcp.x, dcp.y)
				.setDlgSize(dcp.width, 0)
				.create().show();
		}
	}

	@Override
	public void bindMainContent(View vw)
	{
		//绑定控件
		collectCtlFromView(vw);

		//遍历所有单选按钮，设置按钮的按键事件
		for(int i = 0; i < rgpDlgLst.getChildCount(); ++i)
		{
			final RadioButton rdo = (RadioButton) rgpDlgLst.getChildAt(i);
			rdo.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					//配置更新单词本单词信息的数据库操作
					WdsUpdInf pam = new WdsUpdInf();
					pam.data = (int) view.getTag();
					pam.word = word;
					DBMdl.ins().begOperation((int) rgpDlgLst.getTag(), null, null);
					int opnMsg = DBMdl.ins().exeOperation(pam, null);
					if(opnMsg != 0)
					{
						Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					//做完单词信息的更新，同时更新Session中的单词信息
					opnMsg = DBMdl.ins().endOperation(null, null);
					if(opnMsg != 0)
					{
						Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					//改变外部按键前圆形的颜色
					if(btnSetWdsInf != null)
					{
						int clr = ((ColorDrawable) rdo.getBackground()).getColor();
						((ShapeDrawable) btnSetWdsInf.getCompoundDrawables()[0])
								.getPaint().setColor(clr);
						btnSetWdsInf.postInvalidate();
					}

					//操作完成，关闭弹出框
					popBackFragment(null);
				}
			});
		}
	}

	@Override
	public void popBackFragment(Object pam)
	{
		RdoGrpDlgBuilder.ins().getDlg().dismiss();
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		rgpDlgLst = (RadioGroup) vw.findViewById(R.id.rgpDlgLst);
	}

	private static SetWdsInfCtrl instance = null;
	public static SetWdsInfCtrl ini(BaseActivity act)
	{
		instance = new SetWdsInfCtrl(act);	return instance;
	}
	public static SetWdsInfCtrl ins()	{ return instance; }
}
