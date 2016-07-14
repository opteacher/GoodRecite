package com.test.opower.goodrecite.word_page.word_main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by opower on 16-6-4.
 */
public class WordMainPanel extends View
{
	private Paint pit = new Paint();
	private int edgeColor = 0xFF4B515F;
	private int bkgdColor = Color.GRAY;
	private int hasLearntColor = 0xff5cb85c;
	private int dailyMissColor = 0xfff0ad4e;

	private int norRad = 0;
	private int selRad = 0;
	private RectF rctNorF = new RectF();
	private RectF rctSelF = new RectF();
	private int totalNum = 100;
	private Region unLrnRgn = new Region();
	private int hasLearnt = 0;
	private Region lrntRgn = new Region();
	private int dailyMiss = 0;
	private Region dailyRgn = new Region();

	private enum CurSelArea
	{
		NO_SEL, UNLRN_RGN, LRNT_RGN, DAILY_RGN
	}
	private CurSelArea csa = CurSelArea.NO_SEL;
	private Path tmpPh = new Path();

	public WordMainPanel(Context context)
	{
		this(context, null, 0);
	}

	public WordMainPanel(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public WordMainPanel(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public WordMainPanel setTotalNum(int num)
	{
		totalNum = num;
		postInvalidate();
		return this;
	}

	public WordMainPanel setHasLearnt(int hasLearnt)
	{
		this.hasLearnt = hasLearnt;
		postInvalidate();
		return this;
	}

	public WordMainPanel setDayMission(int dailyMiss)
	{
		this.dailyMiss = dailyMiss;
		postInvalidate();
		return this;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		Rect rctCvs = canvas.getClipBounds();
		norRad = (int) ((Math.min(canvas.getWidth(), canvas.getHeight())>>1)*0.8);
		selRad = (int) ((Math.min(canvas.getWidth(), canvas.getHeight())>>1)*0.9);

		rctNorF.set(
				rctCvs.centerX() - norRad, rctCvs.centerY() - norRad,
				rctCvs.centerX() + norRad, rctCvs.centerY() + norRad);
		rctSelF.set(
				rctCvs.centerX() - selRad, rctCvs.centerY() - selRad,
				rctCvs.centerX() + selRad, rctCvs.centerY() + selRad);

		pit.setStyle(Paint.Style.FILL);

		int hasLrntAgl = (int) ((float)hasLearnt/(float)totalNum*360);
		drawRateArc(canvas, hasLearntColor, 0, hasLrntAgl,
				lrntRgn, CurSelArea.LRNT_RGN);

		int dailyMisAgl = (int) ((float)dailyMiss/(float)totalNum*360);
		drawRateArc(canvas, dailyMissColor, hasLrntAgl, dailyMisAgl,
				dailyRgn, CurSelArea.DAILY_RGN);

		int startAgl = hasLrntAgl + dailyMisAgl;
		drawRateArc(canvas, bkgdColor, startAgl, 360 - startAgl,
				unLrnRgn, CurSelArea.UNLRN_RGN);
	}

	private void drawRateArc(Canvas cvs, int color, int startAgl,
							 int endAgl, Region rgn, CurSelArea rgnType)
	{
		RectF rctTmp;
		if(csa == rgnType)
		{
			rctTmp = rctSelF;
		}
		else
		{
			rctTmp = rctNorF;
		}
		pit.setColor(color);
		cvs.drawArc(rctTmp, startAgl, endAgl, true, pit);
		tmpPh.reset();
		tmpPh.addArc(rctTmp, startAgl, endAgl);
		rgn.setPath(tmpPh, new Region(cvs.getClipBounds()));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(rctNorF.contains(event.getX(), event.getY()))
		{
			if(unLrnRgn.contains((int) event.getX(), (int) event.getY()))
			{
				if(csa == CurSelArea.UNLRN_RGN)
				{
					csa = CurSelArea.NO_SEL;
				}
				else
				{
					csa = CurSelArea.UNLRN_RGN;
				}
			}
			else
			if(dailyRgn.contains((int) event.getX(), (int) event.getY()))
			{
				if(csa == CurSelArea.DAILY_RGN)
				{
					csa = CurSelArea.NO_SEL;
				}
				else
				{
					csa = CurSelArea.DAILY_RGN;
				}
			}
			else
			if(lrntRgn.contains((int) event.getX(), (int) event.getY()))
			{
				if(csa == CurSelArea.LRNT_RGN)
				{
					csa = CurSelArea.NO_SEL;
				}
				else
				{
					csa = CurSelArea.LRNT_RGN;
				}
			}
			else
			{
				csa = CurSelArea.NO_SEL;
			}
			postInvalidate();
		}
		return super.onTouchEvent(event);
	}

	public interface RefreshWdsMnPnlCallback
	{
		void resetDailyNum(int dayNum);
	}
}
