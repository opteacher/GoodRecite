package com.test.opower.goodrecite.word_page.word_query;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.common_utils.CmnUtils;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.LnkGrpDlgBuilder;
import com.test.opower.goodrecite.model.LnkGrpDlgBuilder.ItmInfo;
import com.test.opower.goodrecite.model.PopUpDlgBuilder.PosType;
import com.test.opower.goodrecite.model.ViewCtrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by opower on 16-7-19.
 */
public class OpnWdsImgCtrl extends ViewCtrl
{
	private String word = "";
	private static final int ADD_WDS_IMG = 0;
	private static final int SEL_WDS_IMG = 1;
	private static final int MNG_WDS_IMG = 2;

	private OpnWdsImgCtrl(BaseActivity act)
	{
		super(act);

		LnkGrpDlgBuilder.ini(act);
		EdtWdsImgCtrl.ini(act);
		SelWdsImgCtrl.ini(act);
	}

	public static class DlgCrtePam
	{
		public String word = "";
		public int x = 0;
		public int y = 0;
		public int width = -2;
		public int height = -2;
		public DialogInterface.OnCancelListener ccl = null;
	}

	@Override
	public void toCurFragment(Object pam)
	{
		LnkGrpDlgBuilder inst = LnkGrpDlgBuilder.ins();

		//如果已经显示了，则关闭之
		if(inst.getDlg() != null && inst.getDlg().isShowing())
		{
			inst.getDlg().dismiss();
			return;
		}

		//取得参数并获得当前单词
		DlgCrtePam dcp = null;
		if(CmnUtils.isCls(pam, DlgCrtePam.class))
		{
			dcp = (DlgCrtePam) pam;
		}
		else
		{
			return;
		}
		word = dcp.word;

		//填入选项
		List<ItmInfo> lst = new ArrayList<>();
		lst.add(new ItmInfo(0xFFffffff, ADD_WDS_IMG,
				R.string.add_wds_img, R.drawable.plus_red));
		lst.add(new ItmInfo(0xFFffffff, SEL_WDS_IMG,
				R.string.sel_wds_img, R.drawable.sel_lst));
		lst.add(new ItmInfo(0xFFffffff, MNG_WDS_IMG,
				R.string.mng_wds_img, R.drawable.edit));
		inst.setLstInfo(lst);

		//生成并显示对话框
		inst.setVwCtl(this)
			.setLstInfo(lst)
			.setPosType(PosType.LFT_TOP)
			.setDlgPos(dcp.x, dcp.y)
			.setDlgSize(dcp.width, dcp.height)
			.create()
			.show();

		//设置对话框关闭时所执行的触发按钮状态变化
		if(dcp.ccl != null)
		{
			inst.getDlg().setOnCancelListener(dcp.ccl);
		}
	}

	@Override
	public void popBackFragment(Object pam)
	{
		Dialog dlg = LnkGrpDlgBuilder.ins().getDlg();
		if(dlg != null && dlg.isShowing())
		{
			dlg.cancel();
		}
	}

	@Override
	public void bindMainContent(View vw)
	{
		LnkGrpDlgBuilder.ins().getLvwDlgLst().setOnItemClickListener(
				new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick(
							AdapterView<?> adapterView,
							View view,
							int i,
							long l)
					{
						switch ((Integer) view.getTag())
						{
						case ADD_WDS_IMG:
							EdtWdsImgCtrl.ins().toCurFragment(word);
							break;
						case SEL_WDS_IMG:
							SelWdsImgCtrl.ins().toCurFragment(word);
							break;
						case MNG_WDS_IMG:
							break;
						}
					}
				});
	}

	private static OpnWdsImgCtrl instance = null;
	public static OpnWdsImgCtrl ini(BaseActivity act)
	{
		instance = new OpnWdsImgCtrl(act);	return instance;
	}
	public static OpnWdsImgCtrl ins()	{ return instance; }
}
