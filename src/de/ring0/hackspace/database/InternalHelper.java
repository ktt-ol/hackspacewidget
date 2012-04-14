package de.ring0.hackspace.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class InternalHelper extends SQLiteOpenHelper {

	public static final String TABLE_SPACES = "spaces";
	public static final String TABLE_STATUS = "status";
	
	public static final int TABLE_VERSION = 0;
	
	
	public static final String CREATE_TABLE_SPACES = 
			"CREATE TABLE spaces (" +
			"	spaceid INTEGER AUTO_INCREMENT," +
			"	name TEXT," +
			"	url TEXT," +
			"	PRIMARY KEY(`id`)" +
			");";
	
	public static final String CREATE_TABLE_STATUS = 
			"CREATE TABLE status (" +
			"	space INTEGER," +
			"	json TEXT," +
			"	open BLOB," +
			"	closed BLOB," +
			"	FOREIGN KEY(space) REFERENCES(spaceid)" +
			");";

	public InternalHelper(Context context, String name) {
		super(context, name, null, TABLE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SPACES);
		db.execSQL(CREATE_TABLE_STATUS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(InternalHelper.class.getSimpleName(), 
			String.format("Upgrading from version %d to new version %d", oldVersion, newVersion));
		db.execSQL("DROP TABLE " + TABLE_SPACES);
		db.execSQL("DROP TABLE " + TABLE_STATUS);
		onCreate(db);	
	}
}
