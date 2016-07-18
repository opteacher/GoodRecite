package com.test.opower.goodrecite.word_page.word_test.select_word;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;
import com.test.opower.goodrecite.word_page.word_query.WordDetailCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

import java.util.List;

/**
 * Created by opower on 16-7-8.
 */
public class AnswerListAdapter extends BaseAdapter
{
	private List<String> lstAnswers = null;

	public AnswerListAdapter setAsrLst(List<String> lst)
	{
		lstAnswers = lst;	return this;
	}

	private class ViewHolder
	{
		public ViewHolder(Button btn, RadioButton rdo)
		{
			this.btn = btn;
			this.rdo = rdo;
		}

		public RadioButton rdo;
		public Button btn;
	}

	@Override
	public int getCount()
	{
		return lstAnswers.size();
	}

	@Override
	public Object getItem(int i)
	{
		return lstAnswers.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		ViewHolder vwHdr = null;
		String word = "";
		if (view == null)
		{
			view = LayoutInflater.from(SelectWdsCtrl.ins().getActivity())
					.inflate(R.layout.item_select_asr, null);
			vwHdr = new ViewHolder(
					(Button) view.findViewById(R.id.btnWdsDtl),
					(RadioButton) view.findViewById(R.id.rdoAnswer));
			word = lstAnswers.get(i);
			vwHdr.rdo.setText(word);
			vwHdr.btn.setTag(word);
			view.setTag(vwHdr);
		} else
		{
			vwHdr = (ViewHolder) view.getTag();
		}

		if (SelectWdsCtrl.ins().isShowAnswer())
		{
			if (i == viewGroup.getChildCount() - 1)
			{
				SelectWdsCtrl.ins().hideAnswer();
			}
			vwHdr.btn.setVisibility(View.VISIBLE);
			view.findViewById(R.id.vwAsrSpln).setVisibility(View.VISIBLE);
			if (word == WordTestCtrl.ins().getCurWord())
			{
				vwHdr.rdo.setChecked(true);
			}
		}

		final ViewGroup vwGp = viewGroup;
		vwHdr.rdo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				for (int i = 0; i < vwGp.getChildCount(); ++i)
				{
					((RadioButton) vwGp.getChildAt(i)
							.findViewById(R.id.rdoAnswer)).setChecked(false);
				}
				((RadioButton) view).setChecked(true);
			}
		});

		final String wd = word;
		vwHdr.btn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				WordMainCtrl.FgtTrsInfo fti = new WordMainCtrl.FgtTrsInfo();
				fti.vt = WordMainCtrl.ViewType.WORD_DETAIL;
				fti.subPam = new WordDetailCtrl.FgtTrsInfo(
						WordMainCtrl.ViewType.WORD_TEST, wd);
				WordMainCtrl.ins().toCurFragment(fti);
			}
		});
		return view;
	}
}
