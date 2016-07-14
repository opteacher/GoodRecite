package com.test.opower.goodrecite.word_page.select_wbook;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.model.CstmBtn;
import com.test.opower.goodrecite.database.DBOpnIstDict;

import java.util.List;

/**
 * Created by opower on 16-6-27.
 */
public class DldWBookListener implements CstmBtn.OnClickListener
{
	private DldWBookListener()	{}
	private static DldWBookListener instance = new DldWBookListener();
	public static DldWBookListener ins()	{ return instance; }

	private Handler hdUpdate = new Handler();
	private String wbookName = "";
	private ProgressBar pgsDownload = null;
	private TextView txtNumWds = null;

	@Override
	public void onClick(View vw)
	{
		//从界面上取得单词表列表控件
		Activity act = SelWBookCtrl.ins().getActivity();
		ListView lvwAvaDicts = (ListView) act.findViewById(R.id.lvwSelBk);
		//遍历每个单词表，检测其选定情况
		for(int i = 0; i < lvwAvaDicts.getChildCount(); ++i)
		{
			View view = lvwAvaDicts.getChildAt(i);
			RadioButton rdo = (RadioButton) view.findViewById(R.id.rdoSelDict);

			//选中
			if(rdo.isChecked())
			{
				//如果选中的单词表跟当前单词表一样，则不需要下载
				if(rdo.getText().equals(SessionData.ins().getWordBook()))
				{
					Toast.makeText(act, R.string.wng_alrdy_seled, Toast.LENGTH_SHORT).show();
					break;
				}

				//收集单词表的信息：名字、进度条和单词数
				wbookName = rdo.getText().toString();
				List<String> allWds = (List<String>) view
						.findViewById(R.id.txtNumWds).getTag();
				pgsDownload = (ProgressBar) view.findViewById(R.id.pgsDownload);
				pgsDownload.setVisibility(View.VISIBLE);
				txtNumWds = (TextView) view.findViewById(R.id.txtNumWds);

				//新启一个线程用来下载
				new Thread(new DloadThread(allWds)).start();
				break;
			}
		}
	}

	public class DloadThread implements Runnable
	{
		private List<String> wdLst = null;

		public DloadThread(List<String> wdLst)
		{
			this.wdLst = wdLst;
		}

		@Override
		public void run()
		{
			//调用DB操作开始方法
			DBMdl.ins().begOperation(DBMdl.INSERT_DICT, null, null);

			//遍历单词列表，把每个单词的相关信息保存到数据库
			for(int i = 0; i < wdLst.size(); ++i)
			{
				//更新已下载单词数
				final int fnlI = i + 1;

				//向服务器发出调用申请获得单词信息，并保存到数据库
				int opnMsg = DBMdl.ins().exeOperation(wdLst.get(i), null);
				if(opnMsg != 0)
				{
					Toast.makeText(SelWBookCtrl.ins().getActivity(),
							opnMsg, Toast.LENGTH_SHORT).show();
				}

				//下载完每个单词后更新界面上的进度条和单词数面板
				hdUpdate.post(new Runnable()
				{
					@Override
					public void run()
					{
						pgsDownload.setProgress(fnlI);
						String numDload = String.valueOf(fnlI);
						numDload += "/" + String.valueOf(wdLst.size());
						txtNumWds.setText(numDload);
					}
				});
			}

			//所有单词下载完成，设定到单词本并初始化一个学习计划
			DBMdl.ins().endOperation(new DBOpnIstDict.WBookInfo(
					wbookName, wdLst.size()), null);

			//最后给用户一个提示，并隐藏进度条
			hdUpdate.post(new Runnable()
			{
				@Override
				public void run()
				{
					Toast.makeText(SelWBookCtrl.ins().getActivity(),
							R.string.info_wbk_dld_over, Toast.LENGTH_SHORT).show();
					pgsDownload.setVisibility(View.GONE);
				}
			});
		}
	}
}
