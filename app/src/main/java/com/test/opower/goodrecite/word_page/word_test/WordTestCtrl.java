package com.test.opower.goodrecite.word_page.word_test;

import com.test.opower.goodrecite.SessionData;
import com.test.opower.goodrecite.database.DBCtrl;
import com.test.opower.goodrecite.database.DBCtrl.WordBook;
import com.test.opower.goodrecite.database.DBOpnSelWdsInf.WordInf;
import com.test.opower.goodrecite.model.BaseActivity;
import com.test.opower.goodrecite.model.ViewCtrl;
import com.test.opower.goodrecite.word_page.word_test.input_word.InputWdsCtrl;
import com.test.opower.goodrecite.word_page.word_test.new_word.NewWdsCtrl;
import com.test.opower.goodrecite.word_page.word_test.select_word.SelectWdsCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by opower on 16-7-1.
 */
public class WordTestCtrl extends ViewCtrl
{
	private List<String> tstLst = new ArrayList<>();
	private int curWdIdx = 0;

	private WordTestCtrl(BaseActivity act)
	{
		super(act);

		NewWdsCtrl.ini(act);
		SelectWdsCtrl.ini(act);
		InputWdsCtrl.ini(act);
		ExitWdsTstCtrl.ini(act);
	}

	@Override
	public void toCurFragment(Object pam)
	{
		//排列单词表（@_@：测使用先乱序排列），并设置当前的单词和单词索引
		for(Map.Entry<String, WordInf> ety : SessionData.ins().getWdsSet().entrySet())
		{
			tstLst.add(ety.getKey());
		}
		//Collections.shuffle(tstLst);

		//根据每个单词的熟悉度、难度、重要度算出显示哪个单词测试界面
		toCurFragment();
	}

	public boolean isFirstWord()	{ return curWdIdx == 0; }

	public void toCurFragment()
	{
		//获得底部按钮的引用
		collectCtlFromView(null);

		Map<String, WordInf> wdsSet = SessionData.ins().getWdsSet();
		String curWd = getCurWord();

		if(!wdsSet.containsKey(curWd))
		{
			return;
		}
		WordInf wi = wdsSet.get(curWd);

		//根据单词的熟悉度，跳转到指定的单词测试页面并改变当前页面的类型
		switch (wi.familiarity)
		{
		case WordBook.UNKNOWN:
			NewWdsCtrl.ins().toCurFragment(curWd);
			break;
		case WordBook.FAMILIAR:
//			switch ((int) (Math.random() * 2))
//			{
//			case 0:
//				NewWdsCtrl.ins().toCurFragment(curWd);
//				break;
//			case 1:
				SelectWdsCtrl.ins().toCurFragment(curWd);
				break;
//			}
//			break;
		case WordBook.TOO_SIMPLE:
			InputWdsCtrl.ins().toCurFragment(curWd);
			break;
		}
	}

	public void toNxtWord()
	{
		//单词索引累加
		++curWdIdx;
		//如果已经是最后一个单词，显示成绩表
		if(curWdIdx >= tstLst.size())
		{
			//@_@
		}
	}

	public String getCurWord()
	{
		if(curWdIdx >= tstLst.size())
		{
			return "";
		}
		else
		{
			return tstLst.get(curWdIdx);
		}
	}

	public DBCtrl.DiffType getCurWdsDif()
	{
		return DBCtrl.DiffType.values()[
				SessionData.ins()
						.getWdsSet()
						.get(getCurWord())
						.difficulty];
	}

	public DBCtrl.ImptType getCurWdsImp()
	{
		return DBCtrl.ImptType.values()[
				SessionData.ins()
						.getWdsSet()
						.get(getCurWord())
						.importance];
	}

	@Override
	public void popBackFragment(Object pam)
	{
		ExitWdsTstCtrl.ins().toCurFragment(null);
	}

	private static WordTestCtrl instance = null;
	public static WordTestCtrl ini(BaseActivity act)
	{
		instance = new WordTestCtrl(act);	return instance;
	}
	public static WordTestCtrl ins()	{ return instance; }
}
