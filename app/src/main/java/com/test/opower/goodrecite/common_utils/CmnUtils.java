package com.test.opower.goodrecite.common_utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by opower on 16-6-8.
 */
public class CmnUtils
{
	/***
	 * 生成MD5码
	 * @param seed 种子
	 * @return MD5码
	 */
	public static String genMD5(String seed)
	{
		MessageDigest md5 = null;
		String sign = "";
		try
		{
			md5 = MessageDigest.getInstance("MD5");
			md5.update(seed.getBytes("utf-8"));
			byte[] bt = md5.digest();
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < bt.length; ++i)
			{
				if (Integer.toHexString(0xff & bt[i]).length() == 1)
				{
					sb.append("0").append(Integer.toHexString(0xff & bt[i]));
				}
				else
				{
					sb.append(Integer.toHexString(0xff & bt[i]));
				}
			}
			sign = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return sign;
	}

	/***
	 * 普通像素转放大像素
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static float pxTosp(Context context, int pxValue)
	{
		final float fontScale = context.getResources()
				.getDisplayMetrics().scaledDensity;
		return (float) pxValue / fontScale + 0.5f;
	}

	/***
	 * 根据屏幕的宽度得到文字的尺寸
	 */
	private static DisplayMetrics dm = new DisplayMetrics();
	public static float getStrTxtSz(String txt, Activity activity)
	{
		activity.getWindowManager()
				.getDefaultDisplay()
				.getMetrics(dm);

		int txtPxSz = dm.widthPixels / txt.length();

		return CmnUtils.pxTosp(activity, txtPxSz);
	}

	public static DisplayMetrics getActivityDM(Activity activity)
	{
		activity.getWindowManager()
				.getDefaultDisplay()
				.getMetrics(dm);
		return dm;
	}

	public static int getScreenWidth(Context ctt)
	{
		WindowManager wm = (WindowManager) ctt.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Context ctt)
	{
		WindowManager wm = (WindowManager) ctt.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	private static int pos[] = new int[2];
	private static Point loc = new Point();
	public static Point getVwLocOnScn(View vw)
	{
		vw.getLocationOnScreen(pos);
		loc.set(pos[0], pos[1]);
		return loc;
	}

	private static Rect frame = new Rect();
	public static Point getVwLocOnAct(View vw, Activity act)
	{
		vw.getLocationInWindow(pos);
		act.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		pos[1] -= frame.top;
		loc.set(pos[0], pos[1]);
		return loc;
	}

	private static Rect rct = new Rect();
	public static Rect getViewRct(View vw)
	{
		vw.getGlobalVisibleRect(rct);
		return rct;
	}

	public static boolean isCls(Object obj, Class cls)
	{
		return obj != null && obj.getClass() == cls;
	}
}
