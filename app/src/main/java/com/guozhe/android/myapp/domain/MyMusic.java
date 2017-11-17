package com.guozhe.android.myapp.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.guozhe.android.myapp.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guozhe on 2017. 11. 15..
 */

public class MyMusic {
    private String time = "";
    private Set<MusicItem> items = new HashSet<>();;
    private static MyMusic instance = new MyMusic();

    public static MyMusic getInstance(){
        return instance;
    }

    public MyMusic(){

    }

    public List<MusicItem> getItem(){
        return new ArrayList<>(items);
    }

    public void loader(Context context){
        items.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] project = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };
        Cursor cursor = contentResolver.query(uri,project,null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                MusicItem item = new MusicItem();
                item.setId(getValue(cursor,project[0]));
                item.setAlbumId(getValue(cursor,project[1]));
                item.setTitle(getValue(cursor,project[2]).toLowerCase());
                item.setArtist(getValue(cursor,project[3]));

                time =getValue(cursor,project[4]);
                item.setDuration(TimeUtil.musicTime(Integer.parseInt(time)));

                item.setMusicUri(makeMusicUri(item.getId()));
                item.setAlbumUri(makeAlbumUri(item.getAlbumId()));

                items.add(item);
            }
        }
        cursor.close();
    }
    private String getValue(Cursor cursor,String name){
        int index = cursor.getColumnIndex(name);
        return cursor.getString(index);
    }
    private Uri makeMusicUri(String musicId){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(contentUri,musicId);
    }
    private Uri makeAlbumUri(String albumId){
        String albumUri = "content://media/external/audio/albumart/";
        return Uri.parse(albumUri+albumId);

    }

}
