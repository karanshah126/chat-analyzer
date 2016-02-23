package com.finalyear.eventful;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TempDatabaseHandler extends SQLiteOpenHelper{
	 // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "TempChatsLog";
    
    // table name
    private static final String TABLE_MAIN_LOG = "TempMainLog";
 
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_MSG = "msgCount";
    private static final String KEY_MSGLIST = "msgList";
 
    public TempDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_LOG_TABLE = "CREATE TABLE " + TABLE_MAIN_LOG + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"  + KEY_TIME + " TEXT," + KEY_MSG + " INTEGER, " + KEY_MSGLIST + " TEXT" + ")";
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
    
    public void addEntry(TempSchema s) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, s.getDate());
        values.put(KEY_TIME, s.getTime());
        values.put(KEY_MSG, s.getMsgCount());
        values.put(KEY_MSGLIST, s.getMsgList());
     
        // Inserting Row
        db.insert(TABLE_MAIN_LOG, null, values);
        db.close(); // Closing database connection
    }
    
    TempSchema getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_MAIN_LOG, new String[] { KEY_ID,
                KEY_DATE,KEY_TIME,KEY_MSG,KEY_MSGLIST }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        TempSchema s = new TempSchema(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
         
        return s;
    }
    
    public void deleteAll(){
    	SQLiteDatabase db = this.getReadableDatabase();
    	db.execSQL("delete from "+ TABLE_MAIN_LOG);
    }
}