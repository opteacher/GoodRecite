package com.test.opower.goodrecite.word_page.word_main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-6-24.
 */
public class WordMainFragment extends Fragment
{
	private static WordMainFragment instance = new WordMainFragment();
	public static WordMainFragment ins()	{ return instance; }

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View vw = inflater.inflate(R.layout.fragment_word_main, container, false);

		WordMainCtrl.ins().bindMainContent(vw);

		return vw;
	}
}
