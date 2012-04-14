package de.ring0.hackspace.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StatusDataSource {

	public static final String TABLE_NAME = "status";	
	public static final String CREATE_TABLE_STATUS = 
			"CREATE TABLE status (" +
			"	space INTEGER," +
			"	json TEXT," +
			"	open BLOB," +
			"	closed BLOB," +
			"	FOREIGN KEY(space) REFERENCES(spaceid)" +
			");";
	
	private InternalHelper helper;
	private SQLiteDatabase db;
	
	public StatusDataSource (Context context) {
		
	}
}
