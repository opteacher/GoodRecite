package com.test.opower.goodrecite.main_page;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-6-22.
 */
public class MainFragment extends Fragment
{
	private static MainFragment instance = new MainFragment();
	public static MainFragment ins()	{ return instance; }

	//页面控件
	private ViewFlipper vfpAdv = null;

	//页面资源
	private int[] advImg = new int[]
			{
					R.drawable.longman_logo,
					R.drawable.oxford_logo
			};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//获得指定View
		View vw = inflater.inflate(R.layout.fragment_main, container, false);

		//从页面上获取控件引用
		collectCtlFromView(vw);

		//初始化广告栏
		initAdvVwPager();

		//绑定控件触发事件
		MainViewCtrl.ins().bindMainContent(vw);

		return vw;
	}

	private void collectCtlFromView(View vw)
	{
		//广告栏
		vfpAdv = (ViewFlipper) vw.findViewById(R.id.vfpAdv);
	}

	private void initAdvVwPager()
	{
		//遍历广告资源，把资源填进广告栏中（@_@：实际应该是从外部网站或者数据库中获取图片）
		for(int id: advImg)
		{
			ImageView imgVw = new ImageView(getActivity());
			imgVw.setImageResource(id);
			imgVw.setScaleType(ImageView.ScaleType.FIT_XY);
			vfpAdv.addView(imgVw);
		}

		//设置广告栏的动画效果
		vfpAdv.setInAnimation(getActivity(), R.anim.adv_in);
		vfpAdv.setOutAnimation(getActivity(), R.anim.adv_out);

		//开始广告栏动画
		vfpAdv.startFlipping();
	}
}
