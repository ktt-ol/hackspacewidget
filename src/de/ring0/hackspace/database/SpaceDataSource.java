package de.ring0.hackspace.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SpaceDataSource {
	
	public static final String TABLE_NAME = "spaces";

	private InternalHelper helper;
	private SQLiteDatabase db;
	
	public SpaceDataSource(Context context) {
		helper = new InternalHelper(context, TABLE_NAME);
	}
	
	public void open() throws SQLException {
		db = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}
	
	public void insertSpace(String name, String url) {
		
	}
}
