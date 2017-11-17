package com.guozhe.android.myapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.guozhe.android.myapp.Detail.DetailActivity;
import com.guozhe.android.myapp.domain.MusicItem;

import java.util.List;

/**
 * Created by guozhe on 2017. 11. 1..
 */

public class MusicPlayer {
    public static MediaPlayer player = null;
    public static final int play = 1;
    public static final int stop = 2;
    public static final int replay = 3;
    public static int status = play;

    public static void init(List<MusicItem> items, int position, Context context, final Handler handler) {
        Uri musicUri = items.get(position).getMusicUri();
        player = MediaPlayer.create(context, musicUri);
        player.setLooping(false);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(handler != null)
                handler.sendEmptyMessage(DetailActivity.STOP_THREAD);
            }
        });
    }

    public static void player() {
            player.start();
            status = stop;
    }
    public static void pause(){
        if(player != null) {
            player.pause();
            status = replay;
        }
    }
    public static void replay(){
        if(player != null) {
            player.start();
            status = stop;
        }
    }
    public static int getDuration(){
        if(player != null){
            return player.getDuration();
        }else {
            return 0;
        }

    }
    public static int getCurrent(){
        if(player != null){
            return player.getCurrentPosition();
        }else {
            return 0;
        }
    }


    public static void setCurrent(int current) {
        if(player != null) {
            player.seekTo(current);
        }
    }
}
