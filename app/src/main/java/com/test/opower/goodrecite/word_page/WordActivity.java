package com.test.opower.goodrecite.word_page;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;

public class WordActivity extends BaseActivity
{
	public WordActivity()
	{
		title = "单词界面";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);

		WordMainCtrl.ini(this).toCurFragment();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == android.R.id.home)
		{
			WordMainCtrl.ins().popBackFragment(true);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			WordMainCtrl.ins().popBackFragment(null);
		}
		return super.onKeyDown(keyCode, event);
	}
}
