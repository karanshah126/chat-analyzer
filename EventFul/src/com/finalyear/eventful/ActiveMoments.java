package com.finalyear.eventful;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import com.alchemyapi.api.*;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.*;
import org.xml.sax.SAXException;

public class ActiveMoments {
	private static String dbPath="/data/data/com.finalyear.eventful/databases/TempChatsLog";
    static SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm");
    static ArrayList<String> dateList = new ArrayList<String>();
    static ArrayList<String> timeList = new ArrayList<String>();
    static ArrayList<Integer> msgCountList = new ArrayList<Integer>();
    static ArrayList<Integer> serialNoList = new ArrayList<Integer>();
    static int arrayListLength;
    static ArrayList<Integer> triggerInstanceFlagList=new ArrayList<Integer>();
    static String date,time;
    static String startDateInput,startTimeInput,startTimeStamp,nextTimeStamp,currentTimeStamp;
    static int incrementFlag=0,instanceMessageCountThreshold;
    static int j=0, messageThreshold,currentFlagInArray,selection;
    private ProgressDialog progress;
    Cursor rs;
    ArrayList<String> finalMsgData;
    SQLiteDatabase ogDatabase;

    public ActiveMoments(){}
    
    public ActiveMoments(String sDate, String sTime, int selection){
    	startDateInput = sDate;
    	startTimeInput = sTime;
    	this.selection = selection;
    }
    
public void detectMoments() throws SQLException, ParseException
{

try {
	if(selection==0){
		instanceMessageCountThreshold = 5;
		messageThreshold = 8;
	}
	
	else if(selection==1){
		instanceMessageCountThreshold = 10;
		messageThreshold = 12;
	}
	
	else{
		instanceMessageCountThreshold = 15;
		messageThreshold = 20;
	}
	ogDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
	System.out.println("Opened database successfully");
	
	rs = ogDatabase.rawQuery("Select * from TempMainLog", null);

while (rs.moveToNext()) //Extract DB in arraylists
{
 date = rs.getString(1);
 time = rs.getString(2);
 
 dateList.add(date);
 timeList.add(time);
 msgCountList.add(Integer.parseInt(rs.getString(3)));
 serialNoList.add(Integer.parseInt(rs.getString(0)));
}
rs.close();
}

catch (Exception e)
{
}

System.out.println("Serial No.: \n"+serialNoList);
System.out.println("Instance Message Count: \n"+msgCountList);

//Trigger Conditions
arrayListLength=serialNoList.size();

for(int i=0;i<arrayListLength;i++)
triggerInstanceFlagList.add(0);

for(int i=0;i<arrayListLength-2;i++)
{
currentTimeStamp=dateList.get(i)+" "+timeList.get(i);
 nextTimeStamp=dateList.get(i+1)+" "+timeList.get(i+1);
if(timeMinutesDifference(currentTimeStamp,nextTimeStamp)>=30)
      incrementFlag=0;
if (msgCountList.get(i)>=instanceMessageCountThreshold)
	 triggerInstanceFlagList.set(i, 1);
else if(msgCountList.get(i)<=msgCountList.get(i+1)&& (i+1)<=msgCountList.size())
{

while((i+1+incrementFlag)<=msgCountList.size()&&((msgCountList.get(i+incrementFlag)<=msgCountList.get(i+1+incrementFlag))
&&(timeMinutesDifference(dateList.get(i+incrementFlag)+" "+timeList.get(i+incrementFlag),dateList.get(i+incrementFlag+1)+" "+timeList.get(i+incrementFlag+1))<30)))
{
	if (msgCountList.get(i+1+incrementFlag)>=instanceMessageCountThreshold)
      triggerInstanceFlagList.set(i+incrementFlag+1, 1);
  incrementFlag++;
  if((i+1+incrementFlag)>=msgCountList.size())
	  break;
}
if(incrementFlag>=2&&(i+1+incrementFlag)<=msgCountList.size())
{
  triggerInstanceFlagList.set(i, 2);
  i=i+incrementFlag;     
}
incrementFlag=0;
}    

}//end of for loop increment message

for(int i=0;i<arrayListLength-3;i++)//check this
{
currentTimeStamp=dateList.get(i)+" "+timeList.get(i);

 if((timeMinutesDifference(currentTimeStamp,dateList.get(i+2)+" "+timeList.get(i+2))<=5)&&(totalMessageCount(i,3,msgCountList)>=messageThreshold))
{
    j=i+1;
         triggerInstanceFlagList.set(i, 3);
          i=i+3;
while((i<=arrayListLength)&&(timeMinutesDifference(dateList.get(i-1)+" "+timeList.get(i-1),dateList.get(i)+" "+timeList.get(i))==1))
        i++;
    for(int k=j;k<i;k++)
    {
        currentFlagInArray=(Integer)triggerInstanceFlagList.get(k);
        if(currentFlagInArray==2)
          triggerInstanceFlagList.set(k, 0);
    }
 }

 else if((i<=arrayListLength-4)&&((timeMinutesDifference(currentTimeStamp,dateList.get(i+3)+" "+timeList.get(i+3))<=5)&&(totalMessageCount(i,4,msgCountList)>=messageThreshold)))
{
    j=i+1;
         triggerInstanceFlagList.set(i, 3);
          i=i+4;
  while((i<=arrayListLength)&&(timeMinutesDifference(dateList.get(i-1)+" "+timeList.get(i-1),dateList.get(i)+" "+timeList.get(i))==1))
        i++;
    for(int k=j;k<i;k++)
    {
        currentFlagInArray=(Integer)triggerInstanceFlagList.get(k);
        if(currentFlagInArray==2)
          triggerInstanceFlagList.set(k, 0);
    }
 }

 else if((i<=arrayListLength-5)&&((timeMinutesDifference(currentTimeStamp,dateList.get(i+4)+" "+timeList.get(i+4))<=5)&&(totalMessageCount(i,5,msgCountList)>=messageThreshold)))
{
    j=i+1;
         triggerInstanceFlagList.set(i, 3);
          i=i+5;
  while((i<=arrayListLength)&&(timeMinutesDifference(dateList.get(i-1)+" "+timeList.get(i-1),dateList.get(i)+" "+timeList.get(i))==1))
        i++;
    for(int k=j;k<i;k++)
    {
        currentFlagInArray=(Integer)triggerInstanceFlagList.get(k);
        if(currentFlagInArray==2)
          triggerInstanceFlagList.set(k, 0);
    }
 }
}
System.out.println("Trigger Detection: \n"+triggerInstanceFlagList); //mainTestDisplay 

Trigger.getTriggerInstanceFlagList(triggerInstanceFlagList);
Trigger.getDateList(dateList);
Trigger.getTimeList(timeList);
Trigger.getMesgCountList(msgCountList);
Trigger.getSerialNoList(serialNoList);

ArrayList<Integer> processList = new ArrayList<Integer>();
processList = Trigger.processActiveMoments();
System.out.println("Final Messages to be picked: \n"+processList); //mainTestDisplay 
finalMsgData = new ArrayList<String>(); 
int k=0;
rs = ogDatabase.rawQuery("Select * from TempMainLog", null);
while(rs.moveToNext())
{
	if(processList.get(k)!=0){
	try {
		JSONObject jsonObject = new JSONObject(rs.getString(4));
		JSONArray jsonArray = jsonObject.getJSONArray("msgArray");
		for(int i=0;i<processList.get(k);i++)
		  finalMsgData.add(jsonArray.getString(i));
	} 
	catch (JSONException e) {e.printStackTrace();}
	}
      k++;
} 
System.out.println("finalMsgData: "+finalMsgData);
AlchemyProc ap = new AlchemyProc();
for(int i=0;i<finalMsgData.size();i++){
	ap.keywords = new Vector<String>();
	ap.type = new Vector<String>();
	ap.scor = new Vector<Double>();
	try {
		ap.keywordDetect(finalMsgData.get(i));
		ap.printVec();
	} catch (Exception e) {e.printStackTrace();}
  }
}//end of main

public static long timeMinutesDifference(String timeStamp1Input,String timeStamp2Input) throws ParseException
{
Date d1=timeStampFormat.parse(timeStamp1Input);
Date d2=timeStampFormat.parse(timeStamp2Input);
long result=((d2.getTime()-d1.getTime())/60000);
return result;
}

public static int totalMessageCount(int firstInstance,int noOfInstance,ArrayList<Integer> msgCountList)
{
int totalMesssageCount=0;
for(int i=firstInstance;i<firstInstance+noOfInstance;i++)
{
totalMesssageCount+=msgCountList.get(i);
}
return totalMesssageCount;
}
}