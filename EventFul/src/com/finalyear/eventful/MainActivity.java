package com.finalyear.eventful;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.ProgressDialog;  
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity  extends FragmentActivity {
	private String header="";
	private String date="";
	private String time="";
	private String phase="";
	private String name="";
	private String msg="";
	private String currDate;
	private String startDate;
	private String startTime;
	private String dbPath="/data/data/com.finalyear.eventful/databases/ChatsLog";
	private String re1="((?:(?:[0-2]?\\d{1})|(?:[3][01]{1}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3})))(?![\\d])";	// DDMMYYYY 1
	private String re2="(\\s+)";	// White Space 1
	private String re3="((?:(?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):(?:[0-5][0-9])(?::[0-5][0-9])?(?:\\s?(?:am|AM|pm|PM))?)";	// HourMinuteSec 1
	private String re4="(:)";	// Any Single Character 1
	private String re5="((?:[a-z][a-z]+))";	// Word 1
	private String re6="(\\s+)";	// White Space 2
	private String re7="((?:[a-z][a-z]+))";	// Word 2
	private String re8="(-)";	// Any Single Character 2
	private String re9="(\\s+)";	// White Space 3
	private String finalReg1 = re1+re2+re3+re4+re5+re8+re9;
	private String finalReg2 = re1+re2+re3+re4+re5+re6+re7+re8+re9;
	private ProgressDialog progress;
	private int semaphore = 0;
	static final int dialog_id_1=1;
	static final int dialog_id_2=2;
	static final int dialog_id_3=3;
	Calendar today = Calendar.getInstance();
	private	Intent receivedIntent;
	int hour, minute, day, month, yr;
	static SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	static SimpleDateFormat timeParseFormat = new SimpleDateFormat("hh:mm:ss a");
	static SimpleDateFormat timeDispFormat = new SimpleDateFormat("HH:mm");
	private RadioButtonAlert rb;
	ArrayList <String> tempMsgStore = new ArrayList<String>();
	JSONObject jsonObj = new JSONObject();
	TempDatabaseHandler tdb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		day = today.get(Calendar.DAY_OF_MONTH);
		month = today.get(Calendar.MONTH);
		yr = today.get(Calendar.YEAR);
		currDate = getCurrentDate();
		
		receivedIntent = getIntent();
		String receivedAction = receivedIntent.getAction();
		/*for (String key: bundle.keySet())
		{
		  Log.d ("myApplication", key + " is a key in the bundle");
		}*/
		if(receivedAction.equals(Intent.ACTION_SEND_MULTIPLE)){
			//via Share
			new AsyncTaskEx().execute();
		}
			

		else if(receivedAction.equals(Intent.ACTION_MAIN)){
			//via Main Launcher
			new AlertDialog.Builder(this)
		    .setTitle("Invalid Access!")
		    .setMessage("Go to Hike Messenger and perform 'Email Chat' to use this app.")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            System.exit(1);
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}
	}
	
	private class AsyncTaskEx extends AsyncTask<Void, Void, Void> {
		 @Override
		    protected void onPreExecute() {
		    	progress = new ProgressDialog(MainActivity.this);
		    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    	progress.setIndeterminate(true);
		    	progress.setCancelable(false);
		    	progress.setMessage("Reading chats and inserting into database...");
		    	progress.show();
		        return ;
		    }
		 
	    @Override
	    protected Void doInBackground(Void... arg0) {
	    	chatRead(receivedIntent);
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	    	progress.dismiss();
	    	rb = new RadioButtonAlert();
			rb.show(getSupportFragmentManager(), "radio_dialog");
			showDialog(dialog_id_1); //datepicker
	        return ;
	    }
	}

	protected void chatRead(Intent receivedIntent){
		
		ArrayList<Uri> fileUris = receivedIntent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		 Uri filePath = fileUris.get(0);
		 File file = new File(filePath.getPath());
		 int i;
		 try {
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    DatabaseHandler db = new DatabaseHandler(this);
			    Log.d("Insert: ", "Inserting..."); 
			    String line;
			    header=br.readLine();
			    int id=0, flag=0;
			    while ((line = br.readLine()) != null) {
			    	if(!regex(line)){
			    		Schema temp = db.getEntry(id);
			    		db.appendLine(temp, line);
			    		flag=1;
			    	}
			    	else{
			    		flag=0;
			    	}
			    	if(flag==0){
			    	date = line.substring(0,10);
			    	time = line.substring(11,19);
			    	phase = line.substring(20,22);
			    	i = line.indexOf("-");
			    	name = line.substring(23,i);
			    	msg=line.substring(i+2);
			    	Schema s=new Schema(date, time, phase, name, msg);
			    	db.addEntry(s);
			    	id++;
			    	flag=0;
			    	}
			    }
			    br.close();
			    db.close();
			}
			catch (IOException e) {}
		        
		        // Reading all Schemas
		       /* Log.d("Reading: ", "Reading schema..."); 
		        Schema s = db.getEntry(1);
		        String log = "ID: "+s.getID()+" ,Name: " + s.getName() + " ,Date: " + s.getDate();
		        Log.d("Entry: ", log);
		        db.deleteAll(); //TO DO prevent double entries next time*/
		    }

	protected Dialog onCreateDialog(int id){
		
		switch(id){
		
		case dialog_id_1:
				DatePickerDialog dDialog = new DatePickerDialog(this, mDateSetListener, yr, month, day);
				dDialog.getDatePicker().setMaxDate(new Date().getTime());
				dDialog.setTitle("Select start date for analysis:");
				return dDialog;
		case dialog_id_2:
				RangeTimePickerDialog rtDialog = new RangeTimePickerDialog(this, mTimeSetListener, today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), false);
				rtDialog.setTitle("Select start time for analysis:");
				return rtDialog;
		case dialog_id_3:
				TimePickerDialog tDialog= new TimePickerDialog(this, mTimeSetListener, today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), false);
				tDialog.setTitle("Select start time for analysis:");
				return tDialog;
		}

		return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			day = dayOfMonth;
			month = monthOfYear;
			++month;
			yr = year;
			String strDay = Integer.toString(day);
			String strMon = Integer.toString(month);
			if((day<=9&&day>=1)&&(month<=9&&month>=1)){
				strDay = "0" + Integer.toString(day);
				strMon = "0" + Integer.toString(month);
				}
			
			else if(day<=9&&day>=1){
				strDay = "0" + Integer.toString(day);
			}
			
			else if(month<=9&&month>=1){
				strMon = "0" + Integer.toString(month);
				}
			
			else {
			}
			date = strDay + "/" + strMon + "/" + yr;
			Toast.makeText(getBaseContext(), "Set Date - "+ date, Toast.LENGTH_LONG).show();
			if(date.equals(currDate))
				showDialog(dialog_id_2); //rangetimepicker
			else
				showDialog(dialog_id_3); //timepicker
		}
	};
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int hour_minute) {
			hour = hourOfDay;
			minute = hour_minute;
			String strHour = Integer.toString(hour);
			String strMin = Integer.toString(minute);
			if((minute<=9&&minute>=0)&&(hour<=9&&hour>=0)){
				strHour = "0" + Integer.toString(hour);
				strMin = "0" + Integer.toString(minute);
				}
			
			else if(hour<=9&&hour>=0){
				strHour = "0" + Integer.toString(hour);
			}
			
			else if(minute<=9&&minute>=0){
				strMin = "0" + Integer.toString(minute);
				}
			
			else {
			}
			time = strHour + ":" + strMin;
			Toast.makeText(getBaseContext(), "Set Time - " + time, Toast.LENGTH_LONG).show();
			if(semaphore == 0){
				try {
					dbConvert(date, time);
					semaphore = 1;
				} 
				catch (JSONException e) {}
			}
		}
	};
	
	public String getCurrentDate(){
	   ++month;
       String cDate = "";
       String strDay = Integer.toString(day);
		String strMon = Integer.toString(month);
		if((day<=9&&day>=1)&&(month<=9&&month>=1)){
			strDay = "0" + Integer.toString(day);
			strMon = "0" + Integer.toString(month);
			}
		
		else if(day<=9&&day>=1){
			strDay = "0" + Integer.toString(day);
		}
		
		else if(month<=9&&month>=1){
			strMon = "0" + Integer.toString(month);
			}
		
		else {
		}
		cDate = strDay + "/" + strMon + "/" + yr;
       return cDate;
	}

	protected boolean regex(String txt){
	    Pattern p1 = Pattern.compile(finalReg1,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m1 = p1.matcher(txt);
	    Pattern p2 = Pattern.compile(finalReg2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    Matcher m2 = p2.matcher(txt);
	    
	    if (m1.find()||m2.find())
	        return true;
	    else 
	    	return false;
	}
	
	private void dbConvert(String ipDate, String ipTime) throws JSONException {
		Log.d("Convert:", "Converting...");
		SQLiteDatabase ogDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		String ipTimeStamp = ipDate + " " + ipTime;
		String timeConvert = "";
		String currTimeStamp = "";
		int id = 0, startFlag = 0, tempid = 1, msgCount = 0;
		//get start timestamp
		while(startFlag!=1){
		++id;
		String[] args = {String.valueOf(id)};
		Cursor resultSet = ogDatabase.rawQuery("Select date,time,phase from MainLog Where id = ?", args);
		try{
		resultSet.moveToNext();
		timeConvert = timeDispFormat.format(timeParseFormat.parse(resultSet.getString(1) + " " + resultSet.getString(2)));
		currTimeStamp = resultSet.getString(0) + " " +timeConvert;
			if(timeMinutesDifference(currTimeStamp, ipTimeStamp)<=30)
				startFlag = 1;
		}
		
		catch(Exception e){
			break;
		}
		}
		
		if(startFlag==1){
		tdb = new TempDatabaseHandler(this);
		String oldDate = "";
		String oldTimeConvert = "";
		String msgList = "";
		try{
		oldDate = currTimeStamp.substring(0, 10);
		oldTimeConvert = currTimeStamp.substring(11, 16);
		startDate = oldDate;
		startTime = oldTimeConvert;		
		String newDate = oldDate;
		String newTimeConvert = oldTimeConvert;
		Cursor resultSet = null;
		resultSet = ogDatabase.rawQuery("Select date,time,phase,msg from MainLog Where id = ?", new String[]{String.valueOf(id)});
		resultSet.moveToNext();
		while(resultSet.isAfterLast()==false){
		while(oldDate.equals(newDate)&& oldTimeConvert.equals(newTimeConvert)){
			tempMsgStore.add(resultSet.getString(3));
			msgCount++;
			id++;
			String[] args = {String.valueOf(id)};
			resultSet = ogDatabase.rawQuery("Select date,time,phase,msg from MainLog Where id = ?", args);
			resultSet.moveToNext();
			newTimeConvert = timeDispFormat.format(timeParseFormat.parse(resultSet.getString(1) + " " + resultSet.getString(2)));
			newDate = resultSet.getString(0);
		}
		jsonObj.put("msgArray", new JSONArray(tempMsgStore));
		msgList = jsonObj.toString();
		TempSchema s = new TempSchema(tempid, oldDate, oldTimeConvert, msgCount, msgList);
		tdb.addEntry(s);
		tempid++;
		msgCount = 0;
		tempMsgStore.clear();
		msgList = "";
		oldDate = newDate;
		oldTimeConvert = newTimeConvert;
		}
		resultSet.close();
		}
		catch(Exception e){}
		jsonObj.put("msgArray", new JSONArray(tempMsgStore));
		msgList = jsonObj.toString();
		TempSchema s = new TempSchema(tempid, oldDate, oldTimeConvert, msgCount, msgList);
		tdb.addEntry(s);
		ogDatabase.execSQL("DELETE FROM MainLog");
		ogDatabase.close();
		new AsyncTaskFinal().execute();
		}
		else
		{
			new AlertDialog.Builder(this)
		    .setTitle("No Data Error!")
		    .setMessage("No chat messages to analyze since input date & time.")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            System.exit(1);
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}
	}
	
	private class AsyncTaskFinal extends AsyncTask<Void, Void, Void> {
		 @Override
		    protected void onPreExecute() {
		    	progress = new ProgressDialog(MainActivity.this);
		    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    	progress.setIndeterminate(true);
		    	progress.setCancelable(false);
		    	progress.setMessage("Analyzing...");
		    	progress.show();
		    	System.out.println("progress");
		        return ;
		    }
		 
	    @Override
	    protected Void doInBackground(Void... arg0) {
	    	ActiveMoments AMObj = new ActiveMoments(startDate, startTime, rb.getSelection());
			try{
			AMObj.detectMoments();
			}
			catch(Exception e){e.printStackTrace();}
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	    	tdb.deleteAll();
			tdb.close();
	    	progress.dismiss();
	        return ;
	    }
	}

	
	public static long timeMinutesDifference(String timeStamp1Input, String timeStamp2Input)
	    {
		long result=0;
		try{
	        Date d1=timeStampFormat.parse(timeStamp1Input);
	        Date d2=timeStampFormat.parse(timeStamp2Input);
	        result=((d2.getTime()-d1.getTime())/60000);
		}
		catch(Exception e){
			
		}
	        return result;
	    }
}