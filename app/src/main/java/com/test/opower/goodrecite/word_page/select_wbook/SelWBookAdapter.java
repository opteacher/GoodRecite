package com.test.opower.goodrecite.word_page.select_wbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;

/**
 * Created by opower on 16-6-27.
 */
public class SelWBookAdapter extends BaseAdapter
{
	private SelWBookAdapter()	{}
	private static SelWBookAdapter instance = new SelWBookAdapter();
	public static SelWBookAdapter ins()	{ return instance; }

	@Override
	public int getCount()
	{
		return SelWBookFragment.ins().getWdBkSet().size();
	}

	@Override
	public Object getItem(int i)
	{
		return SelWBookFragment.ins().getWdBkSet().get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup)
	{
		if(view == null)
		{
			//从资源中加载排版
			view = LayoutInflater.from(SelWBookFragment.ins().getActivity())
					.inflate(R.layout.item_word_book, viewGroup, false);

			//根据索引得到当前项目并判断是不是选定的单词本
			SelWBookFragment.WBInfo di = (SelWBookFragment.WBInfo) getItem(i);
			boolean selWordBook = di.name.equals(SessionData.ins().getWordBook());

			RadioButton rdo = (RadioButton) view.findViewById(R.id.rdoSelDict);
			//设置单选的文本内容
			rdo.setText(di.name);
			//如果是已经选定的单词本，将其单选按钮设置为选定
			if(selWordBook)
			{
				rdo.setChecked(true);
			}
			//设置单选的选择事件监听
			rdo.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					ListView lvwSelBk = SelWBookFragment.ins().getLvwSelBk();
					for(int i = 0; i < lvwSelBk.getChildCount(); ++i)
					{
						View vw = lvwSelBk.getChildAt(i);
						((RadioButton) vw.findViewById(R.id.rdoSelDict))
								.setChecked(false);
					}
					((RadioButton) view).setChecked(true);
				}
			});

			//配置单词本单词数，如果是已经选定的单词本，则表示所有单词已经被下载完毕了
			TextView txtNumWds = (TextView) view.findViewById(R.id.txtNumWds);
			String DloadWdsNum = "0/";
			String allWdsNum = String.valueOf(di.wdLst.size());
			if(selWordBook)
			{
				DloadWdsNum = allWdsNum + "/";
			}
			txtNumWds.setText(DloadWdsNum + allWdsNum);
			txtNumWds.setTag(di.wdLst);

			//配置底部下载进度条长度
			ProgressBar pgsDownLoad = (ProgressBar) view.findViewById(R.id.pgsDownload);
			pgsDownLoad.setMax(di.wdLst.size());
			pgsDownLoad.setProgress(0);
		}
		return view;
	}
}
