package com.test.opower.goodrecite.word_page.word_main;

import android.view.View;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnSelStdPln;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.select_wbook.SelWBookCtrl;
import com.test.opower.goodrecite.word_page.set_study_plan.SetStudyPlanCtrl;
import com.test.opower.goodrecite.word_page.word_test.ExitWdsTstCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

import java.util.Map;

/**
 * Created by opower on 16-6-27.
 */
public class WordMainCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{


	public enum ViewType
	{
		WORD_MAIN, WORD_TEST, SELECT_WBOOK, SET_PLAN
	}
	private ViewType vwType = ViewType.WORD_MAIN;
	private CstmBtn btnSelWBook = null;
	private CstmBtn btnSetPlan = null;
	private WordMainPanel wmp = null;

	private WordMainCtrl(BaseActivity act)
	{
		super(act);

		SelWBookCtrl.ini(act);
		WordTestCtrl.ini(act);
		SetStudyPlanCtrl.ini(act);
		ExitWdsTstCtrl.ini(act);
	}

	/***
	 * +_+：仅供其他控制器的popBackFragment函数调用！！！！
	 * @param vt
	 */
	public void setViewType(ViewType vt)
	{
		vwType = vt;
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//将填入的参数转换成ViewType
		vwType = (ViewType) pam;

		//根据给出的ViewType跳转至指定的Fragment
		switch (vwType)
		{
		case WORD_MAIN:
			WordMainCtrl.ins().toCurFragment();	break;
		case SELECT_WBOOK:
			SelWBookCtrl.ins().toCurFragment(null);	break;
		case SET_PLAN:
			SetStudyPlanCtrl.ins().toCurFragment(null);	break;
		case WORD_TEST:
			WordTestCtrl.ins().toCurFragment(null);	break;
		default:
			return;
		}
	}

	public void toCurFragment()
	{
		//将主界面替换成单词主界面
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsCtt, WordMainFragment.ins())
				.commit();

		//替换底部按钮
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsBtm, new BtmBtnFragment()
						.setBtnId(R.id.btnWdsMnBtm)
						.setBtnTxt(R.string.start_study)
						.setViewCtrl(WordMainCtrl.ins()))
				.commit();
	}

	@Override
	public void bindMainContent(View vw)
	{
		switch (vwType)
		{
		case WORD_MAIN:
			WordMainCtrl.ins().bindWmnContent(vw);	break;
		case SELECT_WBOOK:
			SelWBookCtrl.ins().bindBtmButton(vw);	break;
		case SET_PLAN:
			SetStudyPlanCtrl.ins().bindMainContent(vw);	break;
		}
	}

	private void bindWmnContent(View vw)
	{
		//从页面上收集控件
		collectCtlFromView(vw);

		//将数据库的数据填入到界面中
		setDataToView(vw);

		//设置选择单词表按钮的按键事件
		btnSelWBook.setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				//将页面类型切换到选择单词本模式
				toCurFragment(ViewType.SELECT_WBOOK);
			}
		});

		//设置设定学习计划按钮的按键事件
		btnSetPlan.setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				toCurFragment(ViewType.SET_PLAN);
			}
		});
	}

	@Override
	public void bindBtmBtn(View vw)
	{
		((CstmBtn) vw).setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				toCurFragment(ViewType.WORD_TEST);
			}
		});
	}

	@Override
	public void setDataToView(View vw)
	{
		int opnMsg = 0;

		//从数据库中得到当前选定的学习计划的ID并设定到Session中去
		DBMdl.ins().begOperation(DBMdl.SELECT_CURRENT_PLAN_ID, null, null);
		opnMsg = DBMdl.ins().exeOperation(null, null);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}

		//从数据库中调出当前选定的单词学习计划
		DBOpnSelStdPln.ExeRstInfo inf = new DBOpnSelStdPln.ExeRstInfo();
		DBMdl.ins().begOperation(DBMdl.SELECT_STUDY_PLAN, null, null);
		opnMsg = DBMdl.ins().exeOperation(null, inf);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}

		//将取得的数据设定到画面的面板属性中去
		wmp.setTotalNum(inf.allWdsNum);
		wmp.setHasLearnt(inf.lrnWdsNum);
		wmp.setDayMission(inf.dayWdsNum);

		//从数据库抽出单词表
		DBMdl.ins().begOperation(DBMdl.SELECT_WORDS_INFO, null, null);
		opnMsg = DBMdl.ins().exeOperation(null, SessionData.ins().getWdsSet());
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public void upDataOnView(Map<Integer, Object> pam)
	{
		if(pam.containsKey(R.id.pnlWordStatistics))
		{
			wmp.setDayMission((Integer) pam.get(R.id.pnlWordStatistics));
		}
	}

	/***
	 * 被Activity的HOME键和后退键按键事件所调用
	 * @param pam 因为后退键会自动执行popBackStack和activity.finish，
	 *            所以给出这个参数给HOME键做判断，是否强制执行后退逻辑
	 */
	@Override
	public void popBackFragment(Object pam)
	{
		switch (vwType)
		{
		case WORD_MAIN:
			WordMainCtrl.ins().popBackFragment();
			break;
		case WORD_TEST:
			WordTestCtrl.ins().popBackFragment(pam);
			break;
		case SELECT_WBOOK:
			SelWBookCtrl.ins().popBackFragment(pam);
			break;
		case SET_PLAN:
			SetStudyPlanCtrl.ins().popBackFragment(pam);
			break;
		}
	}

	private void popBackFragment()
	{
		activity.finish();
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		btnSelWBook = (CstmBtn) vw.findViewById(R.id.btnSelWBook);
		btnSetPlan = (CstmBtn) vw.findViewById(R.id.btnSetPlan);
		wmp = (WordMainPanel) vw.findViewById(R.id.pnlWordStatistics);
	}

	private static WordMainCtrl instance = null;
	public static WordMainCtrl ini(BaseActivity act)
	{
		instance = new WordMainCtrl(act);	return instance;
	}
	public static WordMainCtrl ins()	{ return instance; }
}