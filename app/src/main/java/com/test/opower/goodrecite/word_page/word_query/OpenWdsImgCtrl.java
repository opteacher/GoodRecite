package com.test.opower.goodrecite.word_page.word_query;

import android.app.Dialog;
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
public class OpenWdsImgCtrl extends ViewCtrl
{
	private String word = "";
	private static final int ADD_WDS_IMG = 0;
	private static final int SEL_WDS_IMG = 1;
	private static final int MNG_WDS_IMG = 2;

	private OpenWdsImgCtrl(BaseActivity act)
	{
		super(act);

		LnkGrpDlgBuilder.ini(act);
	}

	public static class DlgCrtePam
	{
		public String word = "";
		public int x = 0;
		public int y = 0;
		public int width = -2;
		public int height = -2;
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
		String txt = "";
		List<ItmInfo> lst = new ArrayList<>();
		txt = getActivity().getString(R.string.add_wds_img);
		lst.add(new ItmInfo(0xFFffffff, ADD_WDS_IMG, txt));
		txt = getActivity().getString(R.string.sel_wds_img);
		lst.add(new ItmInfo(0xFFffffff, SEL_WDS_IMG, txt));
		txt = getActivity().getString(R.string.mng_wds_img);
		lst.add(new ItmInfo(0xFFffffff, MNG_WDS_IMG, txt));
		inst.setLstInfo(lst);

		//生成并显示对话框
		inst.setVwCtl(this)
			.setLstInfo(lst)
			.setPosType(PosType.LFT_TOP)
			.setDlgPos(dcp.x, dcp.y)
			.setDlgSize(dcp.width, dcp.height)
			.create()
			.show();
	}

	@Override
	public void popBackFragment(Object pam)
	{
		Dialog dlg = LnkGrpDlgBuilder.ins().getDlg();
		if(dlg != null && dlg.isShowing())
		{
			dlg.dismiss();
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
							break;
						case SEL_WDS_IMG:
							break;
						case MNG_WDS_IMG:
							break;
						}
					}
				});
	}

	private static OpenWdsImgCtrl instance = null;
	public static OpenWdsImgCtrl ini(BaseActivity act)
	{
		instance = new OpenWdsImgCtrl(act);	return instance;
	}
	public static OpenWdsImgCtrl ins()	{ return instance; }
}
