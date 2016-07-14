package com.test.opower.goodrecite.word_page.word_query;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-7-7.
 */
public class WordDetailFragment extends Fragment
{
	private static WordDetailFragment instance = new WordDetailFragment();
	public static WordDetailFragment ins()	{ return instance; }

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View vw = inflater.inflate(R.layout.fragment_word_detail, container, false);
		WordDetailCtrl.ins().bindMainContent(vw);
		return vw;
	}
}
