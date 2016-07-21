package com.test.opower.goodrecite.model;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
		public int img = 0;
		public int txt = 0;
		public int clr = 0xFFffffff;

		public ItmInfo(int clr, Object tag, int txt, int img)
		{
			this.clr = clr;
			this.tag = tag;
			this.txt = txt;
			this.img = img;
		}
	}

	public LnkGrpDlgBuilder setLstInfo(List<ItmInfo> lst)
	{
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

		//调用控制器绑定控件触发事件
		ctrl.bindMainContent(dlg.getWindow().getDecorView());

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
				view = inflater.inflate(R.layout.item_hor_img_txt, viewGroup, false);
				ImageView imgHorItm = (ImageView) view.findViewById(R.id.imgHorItm);
				TextView txtHorItm = (TextView) view.findViewById(R.id.txtHorItm);
				if(ii.img == 0)
				{
					imgHorItm.setVisibility(View.GONE);
				}
				else
				{
					imgHorItm.setImageResource(ii.img);
					imgHorItm.setBackgroundResource(R.drawable.btn_color);
				}
				if(ii.txt == 0)
				{
					txtHorItm.setVisibility(View.GONE);
				}
				else
				{
					txtHorItm.setText(ii.txt);
					txtHorItm.setPadding(2, 2, 2, 2);
				}
				if(ii.tag != null)
				{
					view.setTag(ii.tag);
				}
				view.setBackgroundColor(ii.clr);
				view.setLayoutParams(new ListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
			}
			return view;
		}
	}
}
