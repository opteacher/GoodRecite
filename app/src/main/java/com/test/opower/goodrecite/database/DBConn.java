package com.test.opower.goodrecite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by opower on 16-5-24.
 */
public class DBConn extends SQLiteOpenHelper
{
	public DBConn(Context context, String name,
				  SQLiteDatabase.CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DBCtrl.genCreateUsersTblSQL());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1)
	{
		db.execSQL("DROP TABLE IF EXISTS " + DBCtrl.Users.TABLE_NAME);
		onCreate(db);
	}
}
