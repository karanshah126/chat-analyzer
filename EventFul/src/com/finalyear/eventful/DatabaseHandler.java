package com.finalyear.eventful;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	 // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ChatsLog";
    
    // table name
    private static final String TABLE_MAIN_LOG = "MainLog";
 
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_PHASE = "phase";
    private static final String KEY_NAME = "name";
    private static final String KEY_MSG = "msg";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_LOG_TABLE = "CREATE TABLE " + TABLE_MAIN_LOG + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"  + KEY_TIME + " TEXT," + KEY_PHASE + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_MSG + " TEXT" + ")";
        db.execSQL(CREATE_MAIN_LOG_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_LOG);
 
        // Create tables again
        onCreate(db);
    }
    
    public void addEntry(Schema s) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, s.getDate());
        values.put(KEY_TIME, s.getTime());
        values.put(KEY_PHASE, s.getPhase());
        values.put(KEY_NAME, s.getName());
        values.put(KEY_MSG, s.getMsg());
     
        // Inserting Row
        db.insert(TABLE_MAIN_LOG, null, values);
        db.close(); // Closing database connection
    }
    
    Schema getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_MAIN_LOG, new String[] { KEY_ID,
                KEY_DATE,KEY_TIME,KEY_PHASE,KEY_NAME,KEY_MSG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Schema s = new Schema(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
         
        return s;
    }
    
    public void appendLine(Schema temp, String extra){
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_MSG, temp.getMsg()+extra);
    	
        // updating row
        db.update(TABLE_MAIN_LOG, values, KEY_ID + " = ?",new String[] { String.valueOf(temp.getID()) });
    }
    
    public void deleteAll(){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.execSQL("delete from "+ TABLE_MAIN_LOG);
    }
}