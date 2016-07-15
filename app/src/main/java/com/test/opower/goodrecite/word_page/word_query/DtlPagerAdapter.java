package com.test.opower.goodrecite.word_page.word_query;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBOpnSelWdsDtl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-15.
 */
class DtlPagerAdapter extends PagerAdapter
{
	private List<String> title = null;
	private List<View> contents = null;

	public DtlPagerAdapter(LayoutInflater inflater, DBOpnSelWdsDtl.ExeRst info)
	{
		title = new ArrayList<>();
		title.add("解释");
		title.add("用例");
		title.add("相关");

		View vw = null;
		TextView txt = null;

		contents = new ArrayList<>();
		vw = inflater.inflate(R.layout.item_word_dtl_trsl, null);
		((TextView) vw.findViewById(R.id.txtWdsTrsf))
				.setText(info.trs.replace("|", "\n\n"));
		contents.add(vw);

		vw = inflater.inflate(R.layout.item_word_dtl_exp, null);
		if(!info.exp.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsExp);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.exp.replace("|", "\n\n"));
		}
		if(!info.phs.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsPhs);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.phs.replace("|", "\n\n"));
		}
		contents.add(vw);

		vw = inflater.inflate(R.layout.item_word_dtl_rel, null);
		if(!info.nrFm.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsNrFm);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.nrFm.replace('|', ','));
		}
		if(!info.nrSy.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsNrSy);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.nrSy.replace('|', ','));
		}
		if(!info.muli.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsMuli);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.muli);
		}
		if(!info.psnt.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsPsnt);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.psnt);
		}
		if(!info.past.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsPast);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.past);
		}
		if(!info.done.isEmpty())
		{
			txt = (TextView) vw.findViewById(R.id.txtWdsDone);
			txt.setVisibility(View.VISIBLE);
			txt.setText(info.done);
		}
		contents.add(vw);
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
