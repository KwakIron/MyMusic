package com.guozhe.android.myapp.util;

/**
 * Created by guozhe on 2017. 11. 15..
 */

public class TimeUtil {
    public static String musicTime(int time){
        long min = time/1000/60;
        long sec = time/1000%60;
        return String.format("%02d",min)+":"+String.format("%02d",sec);
    }
}
