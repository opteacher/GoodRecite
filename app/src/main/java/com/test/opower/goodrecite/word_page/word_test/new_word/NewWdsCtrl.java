package com.test.opower.goodrecite.word_page.word_test.new_word;

import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnSelWdsDifClr;
import com.test.opower.goodrecite.database.DBOpnSelWdsImpClr;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.BtmBtnFragment;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.model.RdoGrpDlgBuilder;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_query.SetWdsInfCtrl;
import com.test.opower.goodrecite.word_page.word_query.SetWdsInfCtrl.DlgCrtePam;
import com.test.opower.goodrecite.word_page.word_test.WordTestCtrl;

/**
 * Created by opower on 16-7-1.
 */
public class NewWdsCtrl extends ViewCtrl implements BtmBtnFragment.BtmBtnVwCtrl
{
	private String word = "";
	private TextView txtNewWord = null;
	private ImageButton btnWdsSd = null;
	private CstmBtn btnSetWdsDif = null;
	private CstmBtn btnSetWdsImp = null;
	private ShapeDrawable dblClrCclOfWdsDif = null;
	private ShapeDrawable dblClrCclOfWdsImp = null;
	private final int clrCircleSz = 50;

	private NewWdsCtrl(BaseActivity act)
	{
		super(act);

		SetWdsInfCtrl.ini(act);
		RdoGrpDlgBuilder.ini(act);

		dblClrCclOfWdsDif = new ShapeDrawable(new OvalShape());
		dblClrCclOfWdsDif.getPaint().setColor(0xFF000000);
		dblClrCclOfWdsDif.setBounds(0, 0, clrCircleSz, clrCircleSz);

		dblClrCclOfWdsImp = new ShapeDrawable(new OvalShape());
		dblClrCclOfWdsImp.getPaint().setColor(0xFF000000);
		dblClrCclOfWdsImp.setBounds(0, 0, clrCircleSz, clrCircleSz);
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
			activity.getFragmentManager().popBackStack();
		}

		//Fragment切换
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsCtt, new NewWdsFragment())
				.commit();

		//底部按钮Fragment切换
		activity.getFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.lytWdsBtm, new BtmBtnFragment()
						.setBtnId(R.id.btnNxtWds)
						.setBtnTxt(R.string.nxt_wds)
						.setViewCtrl(NewWdsCtrl.ins()))
				.commit();
	}

	@Override
	public void bindMainContent(View vw)
	{
		//从页面上收集控件
		collectCtlFromView(vw);

		//点击单词的时候跳转到单词详细画面
		txtNewWord.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});

		//设置发音按钮的点击事件
		btnWdsSd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

			}
		});

		int opnMsg = 0;

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

		//设置单词的难度
		btnSetWdsDif.setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				DlgCrtePam dcp = new DlgCrtePam();
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

		//设置单词的重要度
		btnSetWdsImp.setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				DlgCrtePam dcp = new DlgCrtePam();
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
	}

	@Override
	public void bindBtmBtn(View vw)
	{
		((CstmBtn) vw).setOnClickListener(new CstmBtn.OnClickListener()
		{
			@Override
			public void onClick(View vw)
			{
				//转到下个单词
				WordTestCtrl.ins().toNxtWord();
				//根据单词跳转到正确的Fragment
				WordTestCtrl.ins().toCurFragment();
			}
		});
	}

	@Override
	public void popBackFragment(Object pam) {}

	@Override
	protected void collectCtlFromView(View vw)
	{
		txtNewWord = (TextView) vw.findViewById(R.id.txtNewWord);
		btnWdsSd = (ImageButton) vw.findViewById(R.id.btnWdsSd);
		btnSetWdsDif = (CstmBtn) vw.findViewById(R.id.btnSetWdsDif);
		btnSetWdsImp = (CstmBtn) vw.findViewById(R.id.btnSetWdsImp);
	}

	public String getWord()
	{
		return word;
	}

	private static NewWdsCtrl instance = null;
	public static NewWdsCtrl ini(BaseActivity act)
	{
		instance = new NewWdsCtrl(act);	return instance;
	}
	public static NewWdsCtrl ins()	{ return instance; }
}
