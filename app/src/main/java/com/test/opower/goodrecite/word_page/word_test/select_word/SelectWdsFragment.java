package com.test.opower.goodrecite.word_page.word_test.select_word;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.opower.goodrecite.R;

/**
 * Created by opower on 16-7-1.
 */
public class SelectWdsFragment extends Fragment
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View vw = inflater.inflate(R.layout.fragment_select_word, container, false);

		SelectWdsCtrl.ins().bindMainContent(vw);

		return vw;
	}
}
