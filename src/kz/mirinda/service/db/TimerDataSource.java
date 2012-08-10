package kz.mirinda.service.db;

import java.util.ArrayList;
import java.util.List;

import kz.mirinda.service.model.WTimer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimerDataSource {
	private SQLiteDatabase database;
	private TimerSQLiteOpenHelper helper;
	private String[] allColumns={ helper.COLUMN_ID,
			                      helper.COLUMN_NAME,
			                      helper.COLUMN_TIME };
	public TimerDataSource(Context context) {
		helper = new TimerSQLiteOpenHelper(context);
	}
	public void open(){
		database = helper.getWritableDatabase();
	}
	public void close(){
		database.close();
	}
	
	public WTimer createWTimer(String name,long time){
		WTimer timer = new WTimer();
		//заполн€ю пол€ дл€ вставки
		ContentValues contentValues = new ContentValues();
		contentValues.put(TimerSQLiteOpenHelper.COLUMN_NAME, name);
		contentValues.put(TimerSQLiteOpenHelper.COLUMN_TIME, time);
		//вставл€ю
		long id = database.insert(TimerSQLiteOpenHelper.TABLE_TIMERS, null, contentValues);
		//заполн€ю 
		timer.setId(id);
		timer.setName(name);
		timer.setTime(time);
		return timer;
	}
	
    public List<WTimer> getAllWTimers(){
    	List<WTimer> timers = new ArrayList<WTimer>();
    	
    	Cursor cursor = database.query(TimerSQLiteOpenHelper.TABLE_TIMERS, allColumns, null, null,null , null, TimerSQLiteOpenHelper.COLUMN_TIME);
    	
    	cursor.moveToFirst();
    	while(!cursor.isAfterLast()){
    		timers.add(cursorToWTimer(cursor));
    		cursor.moveToNext();
    	}
    	
    	return timers;
    }
    public WTimer updateWTimer(WTimer timer){
    	
    	ContentValues contentValues = new ContentValues();
		contentValues.put(TimerSQLiteOpenHelper.COLUMN_NAME, timer.getName());
		contentValues.put(TimerSQLiteOpenHelper.COLUMN_TIME, timer.getTime());
    	
    	database.update(TimerSQLiteOpenHelper.TABLE_TIMERS, contentValues,
    					     /*where*/ TimerSQLiteOpenHelper.COLUMN_ID+ "="+ timer.getId(),null);
    	return timer;
    } 
    
    public void deleteWTimer(WTimer timer){
    	deleteWTimer(timer.getId());
    }
    
    public void deleteWTimer(long id){
    	database.delete(TimerSQLiteOpenHelper.TABLE_TIMERS, TimerSQLiteOpenHelper.COLUMN_ID+"= ?", new String[]{""+id});
    }
    private WTimer cursorToWTimer(Cursor cursor){
    	WTimer timer = new WTimer();
    	timer.setId(cursor.getLong(0));
    	timer.setName(cursor.getString(1));
    	timer.setTime(cursor.getLong(2));
    	return timer;
    }
}
