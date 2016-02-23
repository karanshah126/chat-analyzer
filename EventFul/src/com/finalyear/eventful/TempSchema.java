package com.finalyear.eventful;

public class TempSchema {
		 //private variables
	    int _id;
	    String _date;
	    String _time;
	    int _msgCount;
	    String _msgList;
	     
	    // Empty constructor
	    public TempSchema(){
	    }
	    // constructor
	    public TempSchema(int id, String date, String time, int msgCount, String msgList){
	        this._id = id;
	        this._date = date;
	        this._time = time;
	        this._msgCount = msgCount;
	        this._msgList = msgList;
	    }
	     
	    // getting ID
	    public int getID(){
	        return this._id;
	    }
	     
	    // setting ID
	    public void setID(int id){
	        this._id = id;
	    }
	     
	    // getting date
	    public String getDate(){
	        return this._date;
	    }
	     
	    // setting date
	    public void setDate(String date){
	        this._date = date;
	    }
	    
	    // getting time
	    public String getTime(){
	        return this._time;
	    }
	     
	    // setting time
	    public void setTime(String time){
	        this._time = time;
	    }
	    
	    // getting msgCount
	    public int getMsgCount(){
	        return this._msgCount;
	    }
	     
	    // setting msgCount
	    public void setMsg(int msgCount){
	        this._msgCount = msgCount;
	    }
	    
	    // getting MsgList
	    public String getMsgList(){
	        return this._msgList;
	    }
	     
	    // setting MsgList
	    public void setMsgList(String msgList){
	        this._msgList = msgList;
	    }
	}
