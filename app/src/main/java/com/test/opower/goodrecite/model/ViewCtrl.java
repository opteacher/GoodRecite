package com.test.opower.goodrecite.model;

import android.view.View;

import java.util.Map;

/**
 * Created by opower on 16-6-24.
 */
public abstract class ViewCtrl
{
	protected BaseActivity activity = null;

	public ViewCtrl(final BaseActivity act)	{ activity = act; }

	abstract public void toCurFragment(Object pam);

	public void bindMainContent(View vw)	{}

	protected void collectCtlFromView(View vw)	{}

	public void setDataToView(View vw)	{}

	public void upDataOnView(Map<Integer, Object> pam)	{}

	abstract public void popBackFragment(Object pam);

	public BaseActivity getActivity()	{ return activity; }
}
