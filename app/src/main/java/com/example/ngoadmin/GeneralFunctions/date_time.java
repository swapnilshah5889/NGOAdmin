package com.example.ngoadmin.GeneralFunctions;

import java.util.Calendar;
import java.util.StringTokenizer;

public class date_time {


    public static String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};


    public static String GetTextViewTimeString(String t) {
        StringTokenizer st = new StringTokenizer(t, ":");
        int hour = Integer.parseInt(st.nextToken());
        int minutes = Integer.parseInt(st.nextToken());
        //Toast.makeText(this, hour+":"+minutes, Toast.LENGTH_SHORT).show();
        String tv_hour;
        String ampm = "";
        if (hour > 12) {
            tv_hour = "" + (hour - 12);
            ampm = "PM";
        } else if (hour == 12) {
            tv_hour = "" + hour;
            ampm = "PM";
        } else {
            tv_hour = "" + hour;
            ampm = "AM";
        }

        String tv_minute;
        if (minutes < 10) {
            tv_minute = "0" + minutes;
        } else
            tv_minute = "" + minutes;

        String l_opentime = tv_hour + ":" + tv_minute+" "+ampm;
        //Toast.makeText(this, ""+l_opentime, Toast.LENGTH_SHORT).show();

        return l_opentime;
    }

    public static String GetDateString(int day, int month, int year){
        String finaltime = year+"-"+month+"-"+day;
        return  finaltime;
    }

    public static int[] GetDateArray(String date){
        int[] finaldate = new int[3];
        StringTokenizer st = new StringTokenizer(date,"-");
        //0:year; 1:month; 2:day
        finaldate[0] = Integer.parseInt(st.nextToken());
        finaldate[1] = Integer.parseInt(st.nextToken());
        finaldate[2] = Integer.parseInt(st.nextToken());
        return finaldate;
    }

    public static String GetTimeString(int hh, int mm){
        String finaltime = hh+":"+mm;
        return finaltime;
    }

    public static int[] GetTimeArray12H(String time){
        int[] finaltime = new int[3];
        StringTokenizer st = new StringTokenizer(time,":");
        finaltime[0] = Integer.parseInt(st.nextToken());
        finaltime[1] = Integer.parseInt(st.nextToken());
        if(finaltime[0]>=12){

            // 1 for PM
            finaltime[2] = 1;

            // Convert to 12 hour format
            if(finaltime[0]>12){
                finaltime[0]-=12;
            }
        }
        else{
            // 0 for AM
            finaltime[2] = 0;
        }

        return finaltime;
    }

    public static int[] GetTimeArray24H(String time){
        int[] finaltime = new int[2];
        StringTokenizer st = new StringTokenizer(time,":");
        finaltime[0] = Integer.parseInt(st.nextToken());
        finaltime[1] = Integer.parseInt(st.nextToken());

        return finaltime;
    }

    public static int[] GetCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int[] currDate = new int[3];
        currDate[0] = c.get(Calendar.YEAR);
        currDate[1] = c.get(Calendar.MONTH);
        currDate[2] = c.get(Calendar.DAY_OF_MONTH);
        return currDate;
    }

    public static int[] GetCurrentTime(){
        final Calendar c = Calendar.getInstance();
        int[] currTime = new int[4];

        currTime[0] = c.get(Calendar.HOUR_OF_DAY);
        currTime[1] = c.get(Calendar.MINUTE);
        currTime[2] = c.get(Calendar.SECOND);
        currTime[3] = c.get(Calendar.MILLISECOND);

        return currTime;
    }

    public static String GetImageName(){
        String img = "";

        int date[] = GetCurrentDate();
        img = ""+date[0];

        //Month
        if(date[1] < 10)
           img+="0"+date[1];
        else
            img+=""+date[1];

        //Day
        if(date[2] < 10){
            img+="0"+date[2];
        }
        else
            img+=""+date[2];

        date = GetCurrentTime();

        //Hour
        if(date[0] < 10){
            img+="0"+date[0];
        }
        else
            img+=""+date[0];

        //Minute
        if(date[1] < 10){
            img+="0"+date[1];
        }
        else
            img+=""+date[1];

        //Seconds
        if(date[2] < 10){
            img+="0"+date[2];
        }
        else
            img+=""+date[2];


        //Miliseconds
        if(date[3] < 10){
            img+="00"+date[3];
        }
        else if(date[3] > 9 && date[3] < 100)
            img+="0"+date[3];
        else
            img+=""+date[3];

        return img;
    }

}
