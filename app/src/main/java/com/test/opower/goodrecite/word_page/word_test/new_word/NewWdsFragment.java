package com.test.opower.goodrecite.word_page.word_test.new_word;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;

/**
 * Created by opower on 16-7-1.
 */
public class NewWdsFragment extends Fragment
{
	private TextView txtNewWord = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View vw = inflater.inflate(R.layout.fragment_new_word, container, false);

		//调用控制器的绑定
		NewWdsCtrl.ins().bindMainContent(vw);

		//利用屏幕宽度算出字体的大小（尽量占满屏幕宽度）
		String wd = NewWdsCtrl.ins().getWord();
		txtNewWord = (TextView) vw.findViewById(R.id.txtNewWord);
		txtNewWord.setTextSize(CmnUtils.getStrTxtSz(wd, getActivity()));
		txtNewWord.setText(wd);

		return vw;
	}
}
