package com.test.opower.goodrecite.word_page.word_query;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnSelWdsDtl;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

/**
 * Created by opower on 16-7-7.
 */
public class WordDetailCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{
	private String word = "";
	private TextView txtWord = null;
	private TextView txtWdsAmSd = null;
	private ImageButton imgWdsAmSd = null;
	private TextView txtWdsEnSd = null;
	private ImageButton imgWdsEnSd = null;
	private TabLayout lytWdsDtl = null;
	private ViewPager vpgWdsDtl = null;
	private DtlPagerAdapter dpgAdapter = null;

	private WordDetailCtrl(BaseActivity act)
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

		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsCtt, WordDetailFragment.ins())
				.commit();

		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsBtm, new BtmBtnFragment()
						.setBtnId(R.id.btnClsDtl)
						.setBtnTxt(R.string.close)
						.setViewCtrl(WordDetailCtrl.ins()))
				.commit();
	}

	@Override
	public void bindMainContent(View vw)
	{

	}

	@Override
	public void setDataToView(View vw)
	{
		//调用数据库，取得相关的数据
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_DETAIL, null, null);
		DBOpnSelWdsDtl.ExeRst er = new DBOpnSelWdsDtl.ExeRst();
		int opnMsg = DBMdl.ins().exeOperation(word, er);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
			return;
		}

		//将取得的数据填入到画面的控件中去
		txtWord.setText(word);
		final String SD = getActivity().getResources().getString(R.string.wds_sd);
		txtWdsAmSd.setText(txtWdsAmSd.getText().toString().replace(SD, er.sdAm));
		if(!er.sdAmUrl.isEmpty())
		{
			imgWdsAmSd.setTag(er.sdAmUrl);
		}
		txtWdsEnSd.setText(txtWdsEnSd.getText().toString().replace(SD, er.sdEn));
		if(!er.sdEnUrl.isEmpty())
		{
			imgWdsEnSd.setTag(er.sdEnUrl);
		}
		dpgAdapter = new DtlPagerAdapter(getActivity().getLayoutInflater(), er);
		vpgWdsDtl.setAdapter(dpgAdapter);
		lytWdsDtl.setupWithViewPager(vpgWdsDtl);
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		txtWord = (TextView) vw.findViewById(R.id.txtWord);
		txtWdsAmSd = (TextView) vw.findViewById(R.id.txtWdsAmSd);
		imgWdsAmSd = (ImageButton) vw.findViewById(R.id.imgWdsAmSd);
		txtWdsEnSd = (TextView) vw.findViewById(R.id.txtWdsEnSd);
		imgWdsEnSd = (ImageButton) vw.findViewById(R.id.imgWdsEnSd);
		lytWdsDtl = (TabLayout) vw.findViewById(R.id.lytWdsDtl);
		vpgWdsDtl = (ViewPager) vw.findViewById(R.id.vpgWdsDtl);
	}

	@Override
	public void popBackFragment(Object pam)
	{

	}

	@Override
	public void bindBtmBtn(View vw)
	{
		activity.getFragmentManager().popBackStack();
		activity.getFragmentManager().popBackStack();
	}

	private static WordDetailCtrl instance = null;
	public static WordDetailCtrl ini(BaseActivity act)
	{
		instance = new WordDetailCtrl(act);	return instance;
	}
	public static WordDetailCtrl ins()	{ return instance; }
}
