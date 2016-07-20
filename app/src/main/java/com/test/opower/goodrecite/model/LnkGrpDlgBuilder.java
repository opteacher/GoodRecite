package com.test.opower.goodrecite.model;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.opower.goodrecite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-19.
 */
public class LnkGrpDlgBuilder extends PopUpDlgBuilder
{
	private static LnkGrpDlgBuilder instance = null;
	public static LnkGrpDlgBuilder ini(Context context)
	{
		instance = new LnkGrpDlgBuilder(context);
		return instance;
	}
	public static LnkGrpDlgBuilder ins()	{ return instance; }

	private ViewCtrl ctrl = null;
	private ListView lvwDlgLst = null;
	private List<ItmInfo> lstDat = new ArrayList<>();
	private LstInfoAdapter lia = new LstInfoAdapter();

	public LnkGrpDlgBuilder(Context context)
	{
		super(context);

		lvwDlgLst = new ListView(context);
		lvwDlgLst.setId(R.id.lvwDlgLst);
		lvwDlgLst.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		lvwDlgLst.setPadding(5, 5, 5, 5);
		lvwDlgLst.setAdapter(lia);
	}

	public static class ItmInfo
	{
		public Object tag = null;
		public String txt = "";
		public int clr = 0xFFffffff;

		public ItmInfo(int clr, Object tag, String txt)
		{
			this.clr = clr;
			this.tag = tag;
			this.txt = txt;
		}
	}

	public LnkGrpDlgBuilder setLstInfo(List<ItmInfo> lst)
	{
		//lvwDlgLst.removeAllViews();
		lstDat = lst;
		lia.notifyDataSetChanged();
		return this;
	}

	public LnkGrpDlgBuilder setVwCtl(ViewCtrl vc)
	{
		ctrl = vc;	return this;
	}

	public ListView getLvwDlgLst()
	{
		return lvwDlgLst;
	}

	@Override
	public Dialog create()
	{
		//如果没有对话框，再创建
		if(dlg == null)
		{
			dlg = new Dialog(context, R.style.PopupDlgTheme);
		}

		//设置对话框内容
		dlg.setContentView(lvwDlgLst);

		//调整对话框
		adjustDlgSize();

		return dlg;
	}

	private class LstInfoAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return lstDat.size();
		}

		@Override
		public Object getItem(int i)
		{
			return lstDat.get(i);
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup)
		{
			ItmInfo ii = lstDat.get(i);
			if(view == null)
			{
				TextView tmp = new TextView(context);
				tmp.setText(ii.txt);
				tmp.setTag(ii.tag);
				tmp.setBackgroundColor(ii.clr);
				tmp.setLayoutParams(new ListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				tmp.setGravity(Gravity.CENTER);
				tmp.setPadding(2, 2, 2, 2);
				view = tmp;
			}
			return view;
		}
	}
}
