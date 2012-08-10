package kz.mirinda.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class TimerSQLiteOpenHelper extends SQLiteOpenHelper {
	//table
	public static final String TABLE_TIMERS = "timers";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TIME = "time";
	
	//database
	public static final String DATABASE_NAME = "timers.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE="create table " + TABLE_TIMERS + 
			"(" + COLUMN_ID   + " integer primary key autoincrement, "+
			      COLUMN_NAME + " text not null, "+
			      COLUMN_TIME + " integer);";
	
	public TimerSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("mirinda1", "Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_TIMERS+";");
		onCreate(db);
	}
	
}
