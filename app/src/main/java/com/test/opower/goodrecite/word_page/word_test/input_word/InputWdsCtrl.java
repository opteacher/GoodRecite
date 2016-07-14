package com.test.opower.goodrecite.word_page.word_test.input_word;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

/**
 * Created by opower on 16-7-1.
 */
public class InputWdsCtrl extends ViewCtrl
{
	private String word = "";

	private InputWdsCtrl(BaseActivity act)
	{
		super(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//转换传进来的参数
		if(pam.getClass() != String.class)
		{
			return;
		}
		word = (String) pam;

		//在做Fragment切换之前，需要先判断一下栈中有没有其他Test
		//如果有，得先popBack出来
		if(!WordTestCtrl.ins().isFirstWord())
		{
			activity.getFragmentManager().popBackStack();
		}

		//Fragment切换
		activity.getFragmentManager()
				.beginTransaction()
				.replace(R.id.lytWdsCtt, new InputWdsFragment())
				.commit();
	}

	@Override
	public void popBackFragment(Object pam)
	{

	}

	private static InputWdsCtrl instance = null;
	public static InputWdsCtrl ini(BaseActivity act)
	{
		instance = new InputWdsCtrl(act);	return instance;
	}
	public static InputWdsCtrl ins()	{ return instance; }
}
