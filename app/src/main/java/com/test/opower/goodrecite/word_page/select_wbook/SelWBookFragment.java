package com.test.opower.goodrecite.word_page.select_wbook;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.test.opower.goodrecite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-6-24.
 */
public class SelWBookFragment extends Fragment
{
	private static SelWBookFragment instance = new SelWBookFragment();
	public static SelWBookFragment ins()	{ return instance; }

	private ListView lvwSelBk = null;
	public class WBInfo
	{
		public String name = "";
		public List<String> wdLst = null;
	}
	private List<WBInfo> wdBkSet = null;

	public List<WBInfo> getWdBkSet()
	{
		return wdBkSet;
	}

	public ListView getLvwSelBk()
	{
		return lvwSelBk;
	}

	public SelWBookFragment()
	{
		//@_@ 测试用
		wdBkSet = new ArrayList<>();
		WBInfo wbi = new WBInfo();
		wbi.name = "测试词典";
		wbi.wdLst = new ArrayList<>();
		wbi.wdLst.add("abandon");
		wbi.wdLst.add("aboard");
		wbi.wdLst.add("absolute");
		wbi.wdLst.add("absolutely");
		wbi.wdLst.add("absorb");
		wbi.wdLst.add("abstract");
		wbi.wdLst.add("abundant");
		wbi.wdLst.add("abuse");
		wbi.wdLst.add("academic");
		wdBkSet.add(wbi);
		//@_@
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//获取Fragment视图
		View vw = inflater.inflate(R.layout.fragment_select_dict, container, false);

		//将列表适配器配置到列表控件中
		lvwSelBk = (ListView) vw.findViewById(R.id.lvwSelBk);
		lvwSelBk.setAdapter(SelWBookAdapter.ins());

		//绑定底部Button的按键事件
		SelWBookCtrl.ins().bindBtmButton(null);

		return vw;
	}
}
