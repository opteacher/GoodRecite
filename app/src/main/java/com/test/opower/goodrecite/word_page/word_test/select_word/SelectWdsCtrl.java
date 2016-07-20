package com.test.opower.goodrecite.word_page.word_test.select_word;

import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnSelWdsExpPhs;
import com.test.opower.goodrecite.database.DBOpnSelWdsNear;
import com.test.opower.goodrecite.database.DBOpnSelWdsNrFmSy;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.WaveClkBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

import java.util.ArrayList;

/**
 * Created by opower on 16-7-1.
 */
public class SelectWdsCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{
	private String word = "";
	private boolean showAnswer = false;
	private ListView lstAnswers = null;
	private TextView txtWdsQuest = null;
	private TextView txtQuestTrsl = null;
	private View vwSplitSelWdsTsf = null;

	private SelectWdsCtrl(BaseActivity act)
	{
		super(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//转换传进来的参数
		if(pam.getClass() != String.class)
		{
			return;
		}
		word = (String) pam;

		//Fragment切换
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsCtt, new SelectWdsFragment())
				.commit();

		//底部按钮
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsBtm, new BtmBtnFragment()
						.setBtnId(R.id.btnSubAsr)
						.setBtnTxt(R.string.sub_asr)
						.setViewCtrl(SelectWdsCtrl.ins()))
				.commit();
	}

	@Override
	public void setDataToView(View vw)
	{
		//收集控件
		collectCtlFromView(vw);

		//从数据库中取得例句和短语作为问题
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_EXP_PHS, null, null);
		DBOpnSelWdsExpPhs.ExeRst rstEP = new DBOpnSelWdsExpPhs.ExeRst();
		int opnMsg = DBMdl.ins().exeOperation(word, rstEP);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}
		//随意挑选一个作为问题
		String quest = rstEP.lst.get((int)(Math.random() * rstEP.lst.size()));
		//将问题和翻译分离分别填入到相应的TextView中
		String[] qstAndTsf = quest.split("\n");
		if(qstAndTsf.length != 2)
		{
			Toast.makeText(getActivity(), R.string.err_db_data, Toast.LENGTH_SHORT).show();
			return;
		}
		txtWdsQuest.setText(qstAndTsf[0].replace(word, "_____"));
		txtQuestTrsl.setText(qstAndTsf[1]);

		//从数据库中取得近形词和前后词作为选择的答案
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_NEAR_FM_SY, null, null);
		DBOpnSelWdsNrFmSy.ExeRst rstNFS = new DBOpnSelWdsNrFmSy.ExeRst();
		rstNFS.lst = new ArrayList<>();
		opnMsg = DBMdl.ins().exeOperation(word, rstNFS);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}
		//如果改单词没有近形词和近义词，取该单词前后的临近的单词作为选项
		if(rstNFS.lst.isEmpty())
		{
			DBMdl.ins().begOperation(DBMdl.SELECT_WORD_NEAR, null, null);
			DBOpnSelWdsNear.ExePam pamNr = new DBOpnSelWdsNear.ExePam();
			pamNr.word = word;
			pamNr.num = 3;
			DBOpnSelWdsNear.ExeRst rstNr = new DBOpnSelWdsNear.ExeRst();
			rstNr.lst = new ArrayList<>();
			opnMsg = DBMdl.ins().exeOperation(pamNr, rstNr);
			if(opnMsg != 0)
			{
				Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
				return;
			}
			rstNFS.lst = rstNr.lst;
			rstNFS.lst.add(word);
		}

		//绑定答案列表的适配器
		lstAnswers.setAdapter(new AnswerListAdapter().setAsrLst(rstNFS.lst));
	}

	@Override
	public void bindBtmBtn(View vw)
	{
		((WaveClkBtn) vw).setOnClickListener(new WaveClkBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				if(!showAnswer)
				{
					//将显示答案标志设为true
					showAnswer = true;
					//将问题原题和翻译之间的分割线显示出来
					vwSplitSelWdsTsf.setVisibility(View.VISIBLE);
					//将问题的翻译显示出来
					txtQuestTrsl.setVisibility(View.VISIBLE);
					//遍历答案列表，显示选择是否正确，并显示各个选项对应单词的详细链接
					for (int i = 0; i < lstAnswers.getCount(); ++i)
					{
						View itm = lstAnswers.getChildAt(i);
						//将选项中单词的详细链接按钮和分隔符显示出来
						itm.findViewById(R.id.vwAsrSpln).setVisibility(View.VISIBLE);
						itm.findViewById(R.id.btnWdsDtl).setVisibility(View.VISIBLE);

						RadioButton rdoBtn = (RadioButton) itm.findViewById(R.id.rdoAnswer);
						//如果遍历到的单选是选定状态
						if (rdoBtn.isChecked())
						{
							//判断是否选择到了正确的答案
							if (rdoBtn.getText() == word)
							{
								//选择了正确的答案
								rdoBtn.setBackgroundResource(R.color.colorPass);
							} else
							{
								//选择了错误的答案
								rdoBtn.setBackgroundResource(R.color.colorError);
								rdoBtn.setChecked(false);
							}
						}
						//最后将单选定位在正确的选项上
						if (rdoBtn.getText() == word)
						{
							rdoBtn.setChecked(true);
						}
					}
					//推出底部按钮的Fragment（@_@： 是否一定确定是按钮Fragment？）
					activity.getFragmentManager().popBackStack();
					//装填下个单词的底部按钮Fragment
					//底部按钮Fragment切换
					activity.getFragmentManager()
							.beginTransaction()
							.addToBackStack(null)
							.replace(R.id.lytWdsBtm, new BtmBtnFragment()
									.setBtnId(R.id.btnNxtWds)
									.setBtnTxt(R.string.nxt_wds)
									.setViewCtrl(SelectWdsCtrl.ins()))
							.commit();
				}
				else
				{
					//如果已经是显示答案的状态了
					showAnswer = false;
					//转到下个单词
					WordTestCtrl.ins().toNxtWord();
					//根据单词跳转到正确的Fragment
					WordTestCtrl.ins().toCurFragment();
				}
			}
		});
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		lstAnswers = (ListView) vw.findViewById(R.id.lstAnswers);
		txtWdsQuest = (TextView) vw.findViewById(R.id.txtWdsQuest);
		txtQuestTrsl = (TextView) vw.findViewById(R.id.txtQuestTrsl);
		vwSplitSelWdsTsf = vw.findViewById(R.id.vwSplitSelWdsTsf);
	}

	@Override
	public void popBackFragment(Object pam)
	{

	}

	public boolean isShowAnswer()
	{
		return showAnswer;
	}

	public void hideAnswer()
	{
		showAnswer = false;
	}

	private static SelectWdsCtrl instance = null;
	public static SelectWdsCtrl ini(BaseActivity act)
	{
		instance = new SelectWdsCtrl(act);	return instance;
	}
	public static SelectWdsCtrl ins()	{ return instance; }
}
