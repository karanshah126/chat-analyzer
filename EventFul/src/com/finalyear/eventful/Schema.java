package com.finalyear.eventful;

public final class Schema {
	 //private variables
    int _id;
    String _date;
    String _time;
    String _phase;
    String _name;
    String _msg;
     
    // Empty constructor
    public Schema(){
         
    }
    // constructor
    public Schema(int id, String date, String time, String phase, String name, String msg){
        this._id = id;
        this._date = date;
        this._time = time;
        this._phase = phase;
        this._name = name;
        this._msg = msg;
    }
    public Schema(String date, String time, String phase, String name, String msg){
        this._date = date;
        this._time = time;
        this._phase = phase;
        this._name = name;
        this._msg = msg;
    }
    
     
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
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
    
    // getting phase
    public String getPhase(){
        return this._phase;
    }
     
    // setting phase
    public void setPhase(String phase){
        this._phase = phase;
    }
    
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting msg
    public String getMsg(){
        return this._msg;
    }
     
    // setting msg
    public void setMsg(String msg){
        this._msg = msg;
    }
}
