package com.test.opower.goodrecite.word_page.word_query;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.model.DialogBuilder;

/**
 * Created by opower on 16-7-22.
 */
public class SelWdsImgBuilder extends DialogBuilder
{
	private ListView lvwWdsImg = null;

	private SelWdsImgBuilder(Context context)
	{
		super(context);

		lvwWdsImg = new ListView(context);
		lvwWdsImg.setId(R.id.lvwWdsImg);
		lvwWdsImg.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	@Override
	public Dialog create()
	{
		View vw = loadDlgAndSetTitle(R.layout.dialog_sgl_btn, "选择单词联想");

		((FrameLayout) vw.findViewById(R.id.lytDlgCtt)).addView(lvwWdsImg);

		setDlgSizeByScn(0, 0.6f);

		return dlg;
	}

	private static SelWdsImgBuilder instance = null;
	public static SelWdsImgBuilder ini(Context ctt)
	{
		instance = new SelWdsImgBuilder(ctt);	return instance;
	}
	public static SelWdsImgBuilder ins()	{ return instance; }
}
