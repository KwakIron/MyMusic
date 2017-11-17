package com.guozhe.android.myapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by guozhe on 2017. 11. 1..
 */

public class PermissionControl {
    public static final int REQ_FLAG = 100000;
    private static String[] perMissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void checkVersion(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission(activity);
        }else {
            callInit(activity);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private static void checkPermission(Activity activity){
        boolean denied = false;
        for (String perm : perMissions){
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                denied = true;
            break;
        }
        if(denied){
            activity.requestPermissions(perMissions,REQ_FLAG);
        }else {
            callInit(activity);
        }
    }
    public static void resultGrant(Activity activity,int requestCode,@NonNull int[] grantResults){
        boolean granted = true;
        if(requestCode == REQ_FLAG){
            for(int grant : grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED)
                    granted = false;
                break;
            }

            if(granted){
                callInit(activity);
            }else {
                Toast.makeText(activity,"권한을 주셔야 사용할수 있읍니다",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static void callInit(Activity activity){
        if(activity instanceof CallBack)
            ((CallBack)activity).init();
        else
            throw new RuntimeException("must implement CallBack");

    }
    public interface CallBack{
        public void init();
    }
}
