package com.test.opower.goodrecite.main_page;

import android.os.Bundle;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBCtrl;
import com.test.opower.goodrecite.model.BaseActivity;

public class MainActivity extends BaseActivity
{
	public MainActivity()
	{
		title = "主界面";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//数据库初始化
		DBCtrl.initDBController(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainViewCtrl.ini(this).toCurFragment(null);
	}
}