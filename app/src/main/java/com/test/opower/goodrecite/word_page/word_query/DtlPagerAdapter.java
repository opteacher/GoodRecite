package com.test.opower.goodrecite.word_page.word_query;

import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBOpnSelWdsDtl;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.PopUpImgBtn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-15.
 */
class DtlPagerAdapter extends PagerAdapter
{
	private List<String> title = null;
	private List<View> contents = null;
	private TextView txtWdsTsl = null;
	private LinearLayout lytWdsExp = null;
	private PopUpImgBtn btnWdsImgOpn = null;
	private TextView txtWdsExp = null;
	private LinearLayout lytWdsPhs = null;
	private TextView txtWdsPhs = null;
	private LinearLayout lytWdsNrFm = null;
	private TextView txtWdsNrFm = null;
	private LinearLayout lytWdsNrSy = null;
	private TextView txtWdsNrSy = null;
	private LinearLayout lytWdsMuli = null;
	private TextView txtWdsMuli = null;
	private LinearLayout lytWdsPsnt = null;
	private TextView txtWdsPsnt = null;
	private LinearLayout lytWdsPast = null;
	private TextView txtWdsPast = null;
	private LinearLayout lytWdsDone = null;
	private TextView txtWdsDone = null;

	public DtlPagerAdapter(LayoutInflater inflater, DBOpnSelWdsDtl.ExeRst info)
	{
		title = new ArrayList<>();
		title.add("解释");
		title.add("用例");
		title.add("相关");

		View vw = null;

		contents = new ArrayList<>();

		vw = inflater.inflate(R.layout.item_word_dtl_trsl, null);
		collectCtlFromTrs(vw);
		txtWdsTsl.setText(info.trs.replace("|", "\n\n"));
		contents.add(vw);

		vw = inflater.inflate(R.layout.item_word_dtl_exp, null);
		collectCtlFromExp(vw);
		if(!info.exp.isEmpty())
		{
			lytWdsExp.setVisibility(View.VISIBLE);
			txtWdsExp.setText(info.exp.replace("|", "\n\n"));
		}
		if(!info.phs.isEmpty())
		{
			lytWdsPhs.setVisibility(View.VISIBLE);
			txtWdsPhs.setText(info.phs.replace("|", "\n\n"));
		}
		contents.add(vw);

		vw = inflater.inflate(R.layout.item_word_dtl_rel, null);
		collectCtlFromRel(vw);
		if(!info.nrFm.isEmpty())
		{
			lytWdsNrFm.setVisibility(View.VISIBLE);
			txtWdsNrFm.setText(info.nrFm.replace('|', ','));
		}
		if(!info.nrSy.isEmpty())
		{
			lytWdsNrSy.setVisibility(View.VISIBLE);
			txtWdsNrSy.setText(info.nrSy.replace('|', ','));
		}
		if(!info.muli.isEmpty())
		{
			lytWdsMuli.setVisibility(View.VISIBLE);
			txtWdsMuli.setText(info.muli);
		}
		if(!info.psnt.isEmpty())
		{
			lytWdsPsnt.setVisibility(View.VISIBLE);
			txtWdsPsnt.setText(info.psnt);
		}
		if(!info.past.isEmpty())
		{
			lytWdsPast.setVisibility(View.VISIBLE);
			txtWdsPast.setText(info.past);
		}
		if(!info.done.isEmpty())
		{
			lytWdsDone.setVisibility(View.VISIBLE);
			txtWdsDone.setText(info.done);
		}
		contents.add(vw);
	}

	private void collectCtlFromTrs(View vw)
	{
		txtWdsTsl = (TextView) vw.findViewById(R.id.txtWdsTsl);
		btnWdsImgOpn = (PopUpImgBtn) vw.findViewById(R.id.btnWdsImgOpn);
	}

	private void collectCtlFromExp(View vw)
	{
		lytWdsExp = (LinearLayout) vw.findViewById(R.id.lytWdsExp);
		txtWdsExp = (TextView) vw.findViewById(R.id.txtWdsExp);
		lytWdsPhs = (LinearLayout) vw.findViewById(R.id.lytWdsPhs);
		txtWdsPhs = (TextView) vw.findViewById(R.id.txtWdsPhs);
	}

	private void collectCtlFromRel(View vw)
	{
		lytWdsNrFm = (LinearLayout) vw.findViewById(R.id.lytWdsNrFm);
		txtWdsNrFm = (TextView) vw.findViewById(R.id.txtWdsNrFm);
		lytWdsNrSy = (LinearLayout) vw.findViewById(R.id.lytWdsNrSy);
		txtWdsNrSy = (TextView) vw.findViewById(R.id.txtWdsNrSy);
		lytWdsMuli = (LinearLayout) vw.findViewById(R.id.lytWdsMuli);
		txtWdsMuli = (TextView) vw.findViewById(R.id.txtWdsMuli);
		lytWdsPsnt = (LinearLayout) vw.findViewById(R.id.lytWdsPsnt);
		txtWdsPsnt = (TextView) vw.findViewById(R.id.txtWdsPsnt);
		lytWdsPast = (LinearLayout) vw.findViewById(R.id.lytWdsPast);
		txtWdsPast = (TextView) vw.findViewById(R.id.txtWdsPast);
		lytWdsDone = (LinearLayout) vw.findViewById(R.id.lytWdsDone);
		txtWdsDone = (TextView) vw.findViewById(R.id.txtWdsDone);
	}

	public void bindMainContent(final BaseActivity act, final String word)
	{
		//设置单词联想选项按钮
		btnWdsImgOpn.setOnClickListener(new PopUpImgBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				OpenWdsImgCtrl.DlgCrtePam dcp = new OpenWdsImgCtrl.DlgCrtePam();
				dcp.word = word;
				Point vwPos = CmnUtils.getVwLocOnAct(vw, act);
				dcp.width = 500;
				dcp.x = vwPos.x - dcp.width;
				dcp.y = vwPos.y;
				dcp.ccl = btnWdsImgOpn;
				OpenWdsImgCtrl.ins().toCurFragment(dcp);
			}
		});
	}

	@Override
	public int getCount()
	{
		return contents.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		container.addView(contents.get(position));
		return contents.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView(contents.get(position));
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return title.get(position);
	}
};
