package com.test.opower.goodrecite.word_page.word_query;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.test.opower.goodrecite.R;
import com.test.opower.goodrecite.database.DBMdl;
import com.test.opower.goodrecite.database.DBOpnIstWdsImg;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.model.WaveClkBtn;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by opower on 16-7-21.
 */
public class EdtWdsImgCtrl extends ViewCtrl
{
	private String word = "";
	private Button btnSelWdImgPic = null;
	private RadioButton rdoSelLclPic = null;
	private RadioButton rdoSelNetPic = null;
	private EditText edtWdImgPicURL = null;
	private EditText edtWdImgDsc = null;
	private WaveClkBtn btnEnsureDlg = null;

	private EdtWdsImgCtrl(BaseActivity act)
	{
		super(act);

		EdtWdsImgBuilder.ini(act);
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
		{
			Toast.makeText(
					getActivity(),
					R.string.err_param,
					Toast.LENGTH_SHORT).show();
			return;
		}

		EdtWdsImgBuilder.ins().create().show();
	}

	@Override
	public void popBackFragment(Object pam)
	{
		EdtWdsImgBuilder.ins().getDlg().dismiss();
	}

	@Override
	protected void collectCtlFromView(View vw)
	{
		btnSelWdImgPic = (Button) vw.findViewById(R.id.btnSelWdImgPic);
		rdoSelLclPic = (RadioButton) vw.findViewById(R.id.rdoSelLclPic);
		rdoSelNetPic = (RadioButton) vw.findViewById(R.id.rdoSelNetPic);
		edtWdImgPicURL = (EditText) vw.findViewById(R.id.edtWdImgPicURL);
		edtWdImgDsc = (EditText) vw.findViewById(R.id.edtWdImgDsc);
		btnEnsureDlg = (WaveClkBtn) vw.findViewById(R.id.btnEnsureDlg);
	}

	@Override
	public void bindMainContent(View vw)
	{
		final View view = vw;
		//选择图片作为单词联想的按钮事件
		btnSelWdImgPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent itt = new Intent(Intent.ACTION_GET_CONTENT);
				itt.setType("image/*");
				getActivity().startActivity(itt);
				//@_@ 将选择的图片路径保存在Button的tag里面
			}
		});

		//两个单选点击之后的Enable/Disable事件
		rdoSelLclPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				btnSelWdImgPic.setEnabled(true);
				rdoSelNetPic.setChecked(false);
				edtWdImgPicURL.setEnabled(false);
			}
		});
		rdoSelNetPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				edtWdImgPicURL.setEnabled(true);
				rdoSelLclPic.setChecked(false);
				btnSelWdImgPic.setEnabled(false);
			}
		});
		rdoSelLclPic.setChecked(true);
		rdoSelLclPic.callOnClick();


		//添加新的单词联想
		final Dialog dlg = EdtWdsImgBuilder.ins().getDlg();
		btnEnsureDlg.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//从画面上取得单词联想文字描述
				String txtDsc = edtWdImgDsc.getText().toString();
				if(txtDsc.isEmpty())
				{
					Toast.makeText(
							getActivity(),
							R.string.wng_no_wds_txt_img_dsc,
							Toast.LENGTH_SHORT).show();
					dlg.dismiss();
					return;
				}

				//填入数据库
				DBMdl.ins().begOperation(DBMdl.INSERT_WORD_IMAGE, word, null);
				DBOpnIstWdsImg.ExePam ep = new DBOpnIstWdsImg.ExePam();
				ep.dsc = txtDsc;
				if(rdoSelLclPic.isChecked())
				{
					Object tmp = btnSelWdImgPic.getTag();
					if(tmp != null)
					{
						ep.pic = tmp.toString();
					}
				}
				else
				if(rdoSelNetPic.isChecked())
				{
					ep.pic = edtWdImgPicURL.getText().toString();
				}
				int opnMsg = DBMdl.ins().exeOperation(ep, null);
				if(opnMsg != 0)
				{
					Toast.makeText(getActivity(), opnMsg, Toast.LENGTH_SHORT).show();
				}

				//更新单词详细界面的相关内容
				Map<Integer, Object> pam = new HashMap<>();
				pam.put(R.id.txtWdsImgDsc, txtDsc);
				if(!ep.pic.isEmpty())
				{
					pam.put(R.id.imgWdsImgPic, ep.pic);
				}
				WordDetailCtrl.ins().upDataOnView(pam);

				//关闭单词联想输入对话框
				dlg.dismiss();

				//关闭单词联想选项列表对话框
				OpnWdsImgCtrl.ins().popBackFragment(null);
			}
		});
	}

	private static EdtWdsImgCtrl instance = null;
	public static EdtWdsImgCtrl ini(BaseActivity act)
	{
		instance = new EdtWdsImgCtrl(act);	return instance;
	}
	public static EdtWdsImgCtrl ins()	{ return instance; }
}
