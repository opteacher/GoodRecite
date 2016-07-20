package com.test.opower.goodrecite.word_page.word_query;

import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnSelWdsDifClr;
import com.test.opower.goodrecite.database.DBOpnSelWdsDtl;
import com.test.opower.goodrecite.database.DBOpnSelWdsImpClr;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.PopUpTxtBtn;
import com.test.opower.goodrecite.model.WaveClkBtn;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_main.WordMainCtrl;

/**
 * Created by opower on 16-7-7.
 */
public class WordDetailCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{
	private String word = "";
	private WordMainCtrl.ViewType orgVw = WordMainCtrl.ViewType.WORD_MAIN;
	private TextView txtWord = null;
	private TextView txtWdsAmSd = null;
	private ImageButton imgWdsAmSd = null;
	private TextView txtWdsEnSd = null;
	private ImageButton imgWdsEnSd = null;
	private TabLayout lytWdsDtl = null;
	private ViewPager vpgWdsDtl = null;
	private DtlPagerAdapter dpgAdapter = null;
	private PopUpTxtBtn btnSetWdsDif = null;
	private PopUpTxtBtn btnSetWdsImp = null;
	private ShapeDrawable dblClrCclOfWdsDif = null;
	private ShapeDrawable dblClrCclOfWdsImp = null;
	private final int clrCircleSz = 50;

	private WordDetailCtrl(BaseActivity act)
	{
		super(act);

		SetWdsInfCtrl.ini(act);
		OpenWdsImgCtrl.ini(act);

		dblClrCclOfWdsDif = new ShapeDrawable(new OvalShape());
		dblClrCclOfWdsDif.getPaint().setColor(0xFF000000);
		dblClrCclOfWdsDif.setBounds(0, 0, clrCircleSz, clrCircleSz);

		dblClrCclOfWdsImp = new ShapeDrawable(new OvalShape());
		dblClrCclOfWdsImp.getPaint().setColor(0xFF000000);
		dblClrCclOfWdsImp.setBounds(0, 0, clrCircleSz, clrCircleSz);
	}

	public static class FgtTrsInfo
	{
		public String word = "";
		public WordMainCtrl.ViewType org = WordMainCtrl.ViewType.WORD_MAIN;

		public FgtTrsInfo(WordMainCtrl.ViewType org, String word)
		{
			this.org = org;
			this.word = word;
		}
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//转换传进来的参数
		if(pam == null)
		{
			Toast.makeText(
					getActivity(),
					R.string.err_vw_trs_pam,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(pam.getClass() == String.class)
		{
			word = (String) pam;
		}
		else
		if(pam.getClass() == FgtTrsInfo.class)
		{
			FgtTrsInfo fti = (FgtTrsInfo) pam;
			word = fti.word;
			orgVw = fti.org;
		}
		else
		{
			Toast.makeText(
					getActivity(),
					R.string.err_param,
					Toast.LENGTH_SHORT).show();
			return;
		}

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
		//设置单词的难度
		btnSetWdsDif.setOnClickListener(new PopUpTxtBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				SetWdsInfCtrl.DlgCrtePam dcp = new SetWdsInfCtrl.DlgCrtePam();
				dcp.btn = btnSetWdsDif;
				dcp.word = word;
				Point vwPos = CmnUtils.getVwLocOnAct(vw, activity);
				dcp.x = vwPos.x;
				dcp.y = vwPos.y + vw.getHeight();
				dcp.width = vw.getWidth();
				dcp.selDBOpn = DBMdl.SELECT_WORDS_DIFF_TBL;
				dcp.updDBOpn = DBMdl.UPDATE_WORDS_DIFF;
				SetWdsInfCtrl.ins().toCurFragment(dcp);
				dcp.reset();
			}
		});

		//设置单词的重要度
		btnSetWdsImp.setOnClickListener(new PopUpTxtBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				SetWdsInfCtrl.DlgCrtePam dcp = new SetWdsInfCtrl.DlgCrtePam();
				dcp.btn = btnSetWdsImp;
				dcp.word = word;
				Point vwPos = CmnUtils.getVwLocOnAct(vw, activity);
				dcp.x = vwPos.x;
				dcp.y = vwPos.y + vw.getHeight();
				dcp.width = vw.getWidth();
				dcp.selDBOpn = DBMdl.SELECT_WORDS_IMPT_TBL;
				dcp.updDBOpn = DBMdl.UPDATE_WORDS_IMPT;
				SetWdsInfCtrl.ins().toCurFragment(dcp);
				dcp.reset();
			}
		});

		dpgAdapter.bindMainContent(getActivity(), word);
	}

	@Override
	public void setDataToView(View vw)
	{
		int opnMsg = 0;

		//调用数据库，取得相关的数据
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_DETAIL, null, null);
		DBOpnSelWdsDtl.ExeRst er = new DBOpnSelWdsDtl.ExeRst();
		opnMsg = DBMdl.ins().exeOperation(word, er);
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

		//设置单词难度按钮上的小圆颜色
		DBOpnSelWdsDifClr.ExeRst difClr = new DBOpnSelWdsDifClr.ExeRst();
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_DIFF_CLR, null, null);
		opnMsg = DBMdl.ins().exeOperation(word, difClr);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
		}
		else
		{
			dblClrCclOfWdsDif.getPaint().setColor(difClr.difClr);
		}
		btnSetWdsDif.setCompoundDrawables(dblClrCclOfWdsDif, null, null, null);

		//设置单词重要度按钮上的小圆颜色
		DBOpnSelWdsImpClr.ExeRst impClr = new DBOpnSelWdsImpClr.ExeRst();
		DBMdl.ins().begOperation(DBMdl.SELECT_WORD_IMPT_CLR, null, null);
		opnMsg = DBMdl.ins().exeOperation(word, impClr);
		if(opnMsg != 0)
		{
			Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
		}
		else
		{
			dblClrCclOfWdsImp.getPaint().setColor(impClr.impClr);
		}
		btnSetWdsImp.setCompoundDrawables(dblClrCclOfWdsImp, null, null, null);
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
		btnSetWdsDif = (PopUpTxtBtn) vw.findViewById(R.id.btnSetWdsDif);
		btnSetWdsImp = (PopUpTxtBtn) vw.findViewById(R.id.btnSetWdsImp);
	}

	@Override
	public void popBackFragment(Object pam)
	{
		//先将界面类型还原为之前的类型
		WordMainCtrl.ins().setViewType(orgVw);

		//实际后退操作
		if(pam != null && pam.getClass() == Boolean.class && (Boolean) pam)
		{
			activity.getFragmentManager().popBackStack();
		}
		activity.getFragmentManager().popBackStack();
	}

	@Override
	public void bindBtmBtn(View vw)
	{
		((WaveClkBtn) vw).setOnClickListener(new WaveClkBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				popBackFragment(true);
			}
		});
	}

	private static WordDetailCtrl instance = null;
	public static WordDetailCtrl ini(BaseActivity act)
	{
		instance = new WordDetailCtrl(act);	return instance;
	}
	public static WordDetailCtrl ins()	{ return instance; }
}
