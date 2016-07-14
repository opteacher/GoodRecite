package com.test.opower.goodrecite.word_page.select_wbook;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.CstmBtn;

/**
 * Created by opower on 16-7-13.
 */
public class SelWbkBtmFragment extends Fragment
{
	private static SelWbkBtmFragment instance = new SelWbkBtmFragment();
	public static SelWbkBtmFragment ins()	{ return instance; }

	private CstmBtn btnSelWbkBtm = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(btnSelWbkBtm == null)
		{
			btnSelWbkBtm = new CstmBtn(getActivity());
			btnSelWbkBtm.setId(R.id.btnSelWbkBtm);
			btnSelWbkBtm.setText(R.string.start_dld_wb);
			btnSelWbkBtm.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			btnSelWbkBtm.setGravity(Gravity.CENTER);
		}
		return btnSelWbkBtm;
	}
}
