package edu.miami.masonandluke.phlogging;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhloggingDB {

	// -----------------------------------------------------------------------------
	public static final String DATABASE_NAME = "Phlogging.db";
	private static final int DATABASE_VERSION = 1;

	private static final String PHLOGGING_TABLE_NAME = "Phlogging";
	private static final String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ PHLOGGING_TABLE_NAME
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "image_id INTEGER NOT NULL UNIQUE, "
			+ "image_data STRING NOT NULL UNIQUE, "
			+ "title TEXT,"
			+ "description TEXT,"
			+ "time LONG NOT NULL UNIQUE,"
			+ "lat DOUBLE,"
			+ "long DOUBLE,"
			+ "orientation FLOAT,"
			+ "recording BLOB);";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase theDB;

	// -----------------------------------------------------------------------------
	public PhloggingDB(Context theContext) {

		dbHelper = new DatabaseHelper(theContext);
		theDB = dbHelper.getWritableDatabase();
	}

	// -----------------------------------------------------------------------------
	public void close() {

		dbHelper.close();
		theDB.close();
	}

	// -----------------------------------------------------------------------------
	public boolean addPhlog(ContentValues phlog) {

		return (theDB.insert(PHLOGGING_TABLE_NAME, null, phlog) >= 0);
	}

	// -----------------------------------------------------------------------------
	public boolean updatePhlog(long imageId, ContentValues imageData) {

		return (theDB.update(PHLOGGING_TABLE_NAME, imageData, "_id =" + imageId,
				null) > 0);
	}

	// -----------------------------------------------------------------------------
	public boolean deletePhlog(long imageId) {

		return (theDB.delete(PHLOGGING_TABLE_NAME, "_id =" + imageId, null) > 0);
	}

	// -----------------------------------------------------------------------------
	public Cursor fetchAllPhlogs() {

		String[] fieldNames = { "_id", "image_id", "image_data", "title", "recording",
				"image_data" };

		return (theDB.query(PHLOGGING_TABLE_NAME, fieldNames, null, null, null,
				null, "image_id"));
	}

	// -----------------------------------------------------------------------------
	public ContentValues getImageById(long imageId) {

		Cursor cursor;
		ContentValues imageData;

		cursor = theDB.query(PHLOGGING_TABLE_NAME, null, "_id = \"" + imageId
				+ "\"", null, null, null, null);
		imageData = imageDataFromCursor(cursor);
		cursor.close();
		return (imageData);
	}

	// Because the file will still be in the database after the image is
	// removed. This function will loop through and make delete any file that
	// does not exist on the sdcard
	public void removeEmptyFiles() {
		Cursor cursor;
		String[] fieldNames = { "_id", "image_data" };
		cursor = theDB.query(PHLOGGING_TABLE_NAME, fieldNames, null, null, null,
				null, "image_id");
		if (cursor.moveToFirst()) {
			do {
				int imageMediaId = cursor.getInt(cursor.getColumnIndex("_id"));
				String data = cursor.getString(cursor
						.getColumnIndex("image_data"));
				Log.i("remove", data);
				if (!(new File(data)).exists()) {
					Log.i("is there a picture?", "no");
					deletePhlog(imageMediaId);
				}
			} while (cursor.moveToNext());
		}

		cursor.close();

	}

	// -----------------------------------------------------------------------------
	public ContentValues getImageByMediaId(long imageId) {

		Cursor cursor;
		ContentValues imageData;

		cursor = theDB.query(PHLOGGING_TABLE_NAME, null, "image_id = " + imageId,
				null, null, null, null);
		imageData = imageDataFromCursor(cursor);
		cursor.close();
		return (imageData);
	}

	// -----------------------------------------------------------------------------
	private ContentValues imageDataFromCursor(Cursor cursor) {

		String[] fieldNames;
		int index;
		ContentValues songData;

		if (cursor != null && cursor.moveToFirst()) {
			fieldNames = cursor.getColumnNames();
			songData = new ContentValues();
			for (index = 0; index < fieldNames.length; index++) {
				if (fieldNames[index].equals("_id")) {
					songData.put("_id", cursor.getInt(index));
				} else if (fieldNames[index].equals("image_id")) {
					songData.put("image_id", cursor.getInt(index));
				} else if (fieldNames[index].equals("title")) {
					songData.put("title", cursor.getString(index));
				} else if (fieldNames[index].equals("image_data")) {
					songData.put("image_data", cursor.getString(index));
				} else if (fieldNames[index].equals("description")) {
					songData.put("description", cursor.getString(index));
				} else if (fieldNames[index].equals("time")) {
					songData.put("time", cursor.getInt(index));
				} else if (fieldNames[index].equals("lat")) {
					songData.put("lat", cursor.getDouble(index));
				} else if (fieldNames[index].equals("long")) {
					songData.put("long", cursor.getDouble(index));
				} else if (fieldNames[index].equals("orientaton")) {
					songData.put("orientation", cursor.getFloat(index));
				} else if (fieldNames[index].equals("recording")) {
					songData.put("recording", cursor.getBlob(index));
				}
			}
			return (songData);
		} else {
			return (null);
		}
	}

	// =============================================================================
	private static class DatabaseHelper extends SQLiteOpenHelper {
		// -------------------------------------------------------------------------
		public DatabaseHelper(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// -------------------------------------------------------------------------
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CREATE_IMAGE_TABLE);
		}

		// -------------------------------------------------------------------------
		@Override
		public void onOpen(SQLiteDatabase db) {

			super.onOpen(db);
		}

		// -------------------------------------------------------------------------
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + PHLOGGING_TABLE_NAME);
			onCreate(db);
		}
		// -------------------------------------------------------------------------
	}
	// =============================================================================
}
// =============================================================================
