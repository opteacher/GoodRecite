package com.test.opower.goodrecite.word_page.set_study_plan;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.WaveClkBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.database.DBOpnSelStdPln;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by opower on 16-6-29.
 */
public class SetStudyPlanCtrl extends ViewCtrl
{
	private Dialog dlg = null;

	private EditText edtWordBook = null;
	private EditText edtWdBkCount = null;
	private EditText edtOneDayMs = null;
	private WaveClkBtn btnEnsureDlg = null;

	private SetStudyPlanCtrl(BaseActivity act)
	{
		super(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		dlg = SetStudyPlanBuilder.ini(activity)
				.setDlgSizeByScn(0, 0.5f)
				.create();
		if (dlg != null)
		{
			dlg.show();
		}
	}

	@Override
	public void bindMainContent(View vw)
	{
		//从页面上取得控件
		collectCtlFromView(vw);

		//从模型中获取页面所需的相关数据
		DBOpnSelStdPln.ExeRstInfo inf = new DBOpnSelStdPln.ExeRstInfo();
		DBMdl.ins().begOperation(DBMdl.SELECT_STUDY_PLAN, null, null);
		int opnMsg = DBMdl.ins().exeOperation(null, inf);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			//将对话框对象设为null以便toCurFragment做判断
			SetStudyPlanBuilder.ins().resetDlg();
			return;
		}

		//将取得的数据填入对话框的控件中
		edtWordBook.setText(inf.wbookName);
		edtWdBkCount.setText(String.valueOf(inf.allWdsNum));
		edtOneDayMs.setText(String.valueOf(inf.dayWdsNum));

		//设定确定按钮
		btnEnsureDlg.setOnClickListener(new WaveClkBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				//取得并判断用户输入的每日练习量是否合理
				int wordsNum = Integer.decode(edtWdBkCount.getText().toString());
				int newDayNum = Integer.decode(edtOneDayMs.getText().toString());
				if (wordsNum < newDayNum)
				{
					Toast.makeText(getActivity(),
							R.string.wng_day_ex_all, Toast.LENGTH_SHORT).show();
					return;
				}

				//将新的学习计划更新到数据库
				DBMdl.ins().begOperation(DBMdl.UPDATE_STUDY_PLAN, null, null);
				int opnMsg = 0;
				opnMsg = DBMdl.ins().exeOperation(newDayNum, null);
				if (opnMsg != 0)
				{
					Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
				}

				//将更新的数据反应到主界面上
				Map<Integer, Object> pam = new HashMap<>();
				pam.put(R.id.pnlWordStatistics, newDayNum);
				WordMainCtrl.ins().upDataOnView(pam);

				//关闭对话框
				popBackFragment(null);
			}
		});
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		edtWordBook = (EditText) vw.findViewById(R.id.edtWordBook);
		edtWdBkCount = (EditText) vw.findViewById(R.id.edtWdBkCount);
		edtOneDayMs = (EditText) vw.findViewById(R.id.edtOneDayMs);
		btnEnsureDlg = (WaveClkBtn) vw.findViewById(R.id.btnEnsureDlg);
	}

	@Override
	public void popBackFragment(Object pam)
	{
		WordMainCtrl.ins().setViewType(WordMainCtrl.ViewType.WORD_MAIN);
		//getActivity().finish();
		if(dlg != null)
		{
			dlg.dismiss();
			dlg = null;
		}
	}

	private static SetStudyPlanCtrl instance = null;
	public static SetStudyPlanCtrl ini(BaseActivity act)
	{
		instance = new SetStudyPlanCtrl(act);
		return instance;
	}
	public static SetStudyPlanCtrl ins()
	{
		return instance;
	}
}
