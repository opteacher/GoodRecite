package com.test.opower.goodrecite.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by opower on 16-7-13.
 */
public class BtmBtnFragment extends Fragment
{
	protected CstmBtn btnWdsBtm = null;
	private int id = 0;
	private int txt = 0;
	private BtmBtnVwCtrl vc = null;

	public BtmBtnFragment setBtnId(int id)
	{
		this.id = id;	return this;
	}

	public BtmBtnFragment setBtnTxt(int txt)
	{
		this.txt = txt;	return this;
	}

	public BtmBtnFragment setViewCtrl(BtmBtnVwCtrl vc)
	{
		this.vc = vc;	return this;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(btnWdsBtm == null)
		{
			btnWdsBtm = new CstmBtn(getActivity());
			btnWdsBtm.setId(id);
			btnWdsBtm.setText(txt);
			btnWdsBtm.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			btnWdsBtm.setGravity(Gravity.CENTER);

			vc.bindBtmBtn(btnWdsBtm);
		}
		return btnWdsBtm;
	}

	public interface BtmBtnVwCtrl
	{
		void bindBtmBtn(View vw);
	}
}
