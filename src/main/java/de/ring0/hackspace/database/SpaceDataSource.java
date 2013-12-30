package de.ring0.hackspace.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.ring0.hackspace.datatypes.Space;

public class SpaceDataSource {
	
	private static final String TABLE_NAME = "spaces";
	private static final String[] COLUMNS = { "id", "name", "url" };

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
	
	/**
	 * 
	 * @param space
	 */
	public void insertSpace(Space space) {
		ContentValues cv = new ContentValues();
		cv.put("name", space.name);
		cv.put("url", space.statusapi);
		
		db.insert(TABLE_NAME, null, cv);
	}
	
	/**
	 * 
	 * @param spaces
	 */
	public void populateTable(Space[] spaces) {
		db.beginTransaction();
		for(Space s : spaces) {
			ContentValues cv = new ContentValues();
			cv.put("name", s.name);
			cv.put("url", s.statusapi);
			db.insert(TABLE_NAME, null, cv);
		}
		db.endTransaction();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Space getSpace(int id) {
		Space s = new Space();
		
		Cursor c = db.query(TABLE_NAME, COLUMNS, String.format("id = %d", id), null, null, null, null);
		c.moveToFirst();
		s.id = c.getInt(0);
		s.name = c.getString(1);
		s.statusapi = c.getString(2);
		c.close();
		
		return s;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public int findSpaceByName(String name) {
		Cursor c = db.query(TABLE_NAME, new String[] {"id"}, String.format("name LIKE \"%s\"", name), null, null, null, null);
		if(c.getCount() > 0) {
			c.moveToFirst();
			c.close();
			return c.getInt(0);
		}
		else {
			c.close();
			return -1;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Space[] getAllSpaces() {
		ArrayList<Space> spaces = new ArrayList<Space>();
		Cursor c = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			Space s = new Space();
			s.id = c.getInt(0);
			s.name = c.getString(1);
			s.statusapi = c.getString(2);
			spaces.add(s);
			c.moveToNext();
		}
		c.close();
		return (Space[]) spaces.toArray();
	}
}
