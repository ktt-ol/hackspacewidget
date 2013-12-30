package de.ring0.hackspace.database;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import de.ring0.hackspace.datatypes.Space;
import de.ring0.hackspace.datatypes.field.SpaceIcon;

public class StatusDataSource {

	public static final String TABLE_NAME = "status";
	public static final String[] ALL_COLUMNS= {"space", "json", "open", "closed"};
	
	private InternalHelper helper;
	private SQLiteDatabase db;
	
	public StatusDataSource(Context context) {
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
	public void updateStatus(Space space) {
		ContentValues cv = new ContentValues();
		Gson g = new Gson();
		
		cv.put("space", space.id);
		cv.put("json", g.toJson(space));
		
		Cursor c = db.query(TABLE_NAME, new String[] {"id"} , "space = "+space.id, null, null, null, null);
		if(c.getCount() == 1) {
			db.replace(TABLE_NAME, null, cv);
		} else {
			try {
				URL open = new URL(space.state.icon.open);
				URL closed = new URL(space.state.icon.closed);
				URLConnection urlOpen = open.openConnection();
				URLConnection urlClosed = closed.openConnection();
				
				cv.put("open", urlOpen.getContent().toString().getBytes());
				cv.put("closed", urlClosed.getContent().toString().getBytes());
				
				db.insert(TABLE_NAME, null, cv);
				
			} catch (ClientProtocolException e) {
				Log.e(StatusDataSource.class.getSimpleName(), e.getMessage());
			} catch (IOException e) {
				Log.e(StatusDataSource.class.getSimpleName(), e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Space getSpace(int id) {
		Gson g = new Gson();
		Space s = null;
		
		Cursor c = db.query(TABLE_NAME, new String[] {"json"}, "space = "+id, null, null, null, null);
		c.moveToFirst();
		if(c.getCount() > 0)
			s = g.fromJson(c.getString(0), Space.class);
		
		return s;
	}

	public SpaceIcon getIcons(int id) {
		SpaceIcon si = new SpaceIcon();
		
		Cursor c = db.query(TABLE_NAME, new String[] {"open", "closed"}, "space = "+id, null, null, null, null);
		if(c.getCount() > 0) {
			byte[] open, closed;
			open = c.getBlob(0);
			closed = c.getBlob(1);
			si.openIcon = BitmapFactory.decodeByteArray(open, 0, open.length);
			si.closedIcon = BitmapFactory.decodeByteArray(closed, 0, closed.length);
			
			return si;
		}
		return null;
	}
}
