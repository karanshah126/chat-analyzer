package com.finalyear.eventful;

import java.text.ParseException;
import java.util.ArrayList;

public class Trigger {
    
	 
    static ArrayList triggerInstanceFlagListAM=new ArrayList();
    static ArrayList dateListAM = new ArrayList();
    static ArrayList timeListAM = new ArrayList();
    static ArrayList<Integer> mesgCountListAM = new ArrayList<Integer>();
    static ArrayList serialNoListAM = new ArrayList();
    
    static ArrayList<Integer> noOfMesgsToExtractListAM= new ArrayList<Integer>();
    static int thresholdForActiveMoment1=3;
   
    
    public static ArrayList<Integer> processActiveMoments() throws ParseException//ArrayList flagList, ArrayList countList, ArrayList )
    {
        ArrayList<Integer> processList= new ArrayList<Integer>();
        ActiveMoments activeMObj = new ActiveMoments();
        int w=0,v=0;
        
        for(int i=0;i<mesgCountListAM.size();i++)
            processList.add(0);
     
        
        for(int i=0;i<triggerInstanceFlagListAM.size();i++)
        {
            if((Integer)(triggerInstanceFlagListAM.get(i))==1)
            {
              
                //what about condition of consecutive threshold eg:8,9?
                if(i==0)
                {
                    //add % of messages here
                    processList.set(i, thresholdForActiveMoment1); 
                }
                else if(activeMObj.timeMinutesDifference(dateListAM.get(i-1)+" "+timeListAM.get(i-1),dateListAM.get(i)+" "+timeListAM.get(i))<30)
                {
                  if(mesgCountListAM.get(i-1)<=thresholdForActiveMoment1)
                  {
                     processList.set(i-1,mesgCountListAM.get(i-1)); 
                      processList.set(i,thresholdForActiveMoment1-mesgCountListAM.get(i-1)); 
                  }
                  else processList.set(i-1, thresholdForActiveMoment1); 
                }
                else processList.set(i, thresholdForActiveMoment1);  
            }
            
            else if((Integer)(triggerInstanceFlagListAM.get(i))==2)
            {
                
                if(mesgCountListAM.get(i)<thresholdForActiveMoment1)
                {
                    w=thresholdForActiveMoment1;v=0;//thresholdForActiveMoment1-mesgCountListAM.get(i);
                    while(w>0)
                    {
                       if(mesgCountListAM.get(i+v)<=w)
                     processList.set(i+v,mesgCountListAM.get(i+v));
                       else
                      processList.set(i+v,w);    
                     w=w-mesgCountListAM.get(i+v);v++;
                    }
                }
                else
                {
                  processList.set(i,thresholdForActiveMoment1);
                }
                    
            }
              
            else if((Integer)(triggerInstanceFlagListAM.get(i))==3)
            {
            
             if(mesgCountListAM.get(i)<thresholdForActiveMoment1)
                {
                    w=thresholdForActiveMoment1;v=0;//thresholdForActiveMoment1-mesgCountListAM.get(i);
                    while(w>0)
                    {
                       if(mesgCountListAM.get(i+v)<=w)
                     processList.set(i+v,mesgCountListAM.get(i+v));
                       else
                      processList.set(i+v,w);    
                     w=w-mesgCountListAM.get(i+v);v++;
                    }
                }
                else  processList.set(i,thresholdForActiveMoment1);
             //
            }
            
            else
            {}
            
        }
        return(processList);
    }
    
    public static void getTriggerInstanceFlagList(ArrayList passedTriggerInstanceFlagList)
    {   
        triggerInstanceFlagListAM=passedTriggerInstanceFlagList;
    }
    
     public static void getDateList(ArrayList passedDateList)
    {
        dateListAM=passedDateList;
    }
     
      public static void getTimeList(ArrayList passedTimeList)
    { 
        timeListAM=passedTimeList;
    }
      
       public static void getMesgCountList(ArrayList passedMesgCountList)
    {  
        mesgCountListAM=passedMesgCountList;
    }
       
        public static void getSerialNoList(ArrayList passedSerialNoList)
    {
        serialNoListAM=passedSerialNoList;
    }
}
