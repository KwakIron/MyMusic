package com.guozhe.android.myapp.Detail;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.guozhe.android.myapp.MusicPlayer;

/**
 * Created by guozhe on 2017. 11. 2..
 */

public class SeekBarThread extends Thread {
    private Handler handler;
    private boolean runFlag = true;


    public SeekBarThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        while (runFlag) {
            int current = MusicPlayer.getCurrent();
            Message msg = new Message();
            msg.what = DetailActivity.CHANGESEEKBAR;
            msg.arg1 = current;
            handler.sendMessage(msg);

            Log.d("SeekBarThread","current===="+current+"duration"+MusicPlayer.getDuration());
            if(current >= MusicPlayer.getDuration())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }
}
