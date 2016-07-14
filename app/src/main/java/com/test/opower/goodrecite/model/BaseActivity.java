package com.test.opower.goodrecite.model;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-6-20.
 */
public class BaseActivity extends AppCompatActivity
{
	protected int ttlId = 0;
	protected String title = "";
	private Toolbar tbrMain = null;
	private DrawerLayout lytMain = null;
	private ActionBarDrawerToggle abdt = null;

	@Override
	public void setContentView(@LayoutRes int layoutResID)
	{
		super.setContentView(layoutResID);
		//从页面上收集控件
		collectCtlFromView();
		//配置标题
		setupTitle();
	}

	/**
	 * 从页面上收集控件引用
	 */
	private void collectCtlFromView()
	{
		tbrMain = (Toolbar) findViewById(R.id.tbrTop);
		lytMain = (DrawerLayout) findViewById(R.id.lytMain);
	}

	/**
	 * 配置应用程序标题
	 */
	private void setupTitle()
	{
		//设置应用程序标题
		//×有ID先设置ID
		if(ttlId != 0)
		{
			setTitle(ttlId);
		}
		else
		if(!title.isEmpty())
		{
			setTitle(title);
		}
		//将工具栏设置到Activity的工具栏中
		setSupportActionBar(tbrMain);
		//设置HOME键状态为显示
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//如果侧边栏不为null，设置之
		if(lytMain != null)
		{
			//新建一个侧边栏触发器
			abdt = new ActionBarDrawerToggle(
					this,//上下文
					lytMain,//侧边栏布局
					tbrMain,//触发工具栏
					0, 0);//侧边栏打开和关闭所显示的文本
			//默认状态显示三横杠图标
			abdt.syncState();
		}
	}
}
