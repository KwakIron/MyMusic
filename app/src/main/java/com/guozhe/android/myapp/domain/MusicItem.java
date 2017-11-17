package com.guozhe.android.myapp.domain;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by guozhe on 2017. 11. 15..
 */

public class MusicItem implements Serializable {
    private String id;
    private String albumId;
    private String title;
    private String artist;
    private String duration;

    private Uri musicUri;
    private Uri albumUri;

    // Set 이 중복값을 허용하지 않도록 equals 와 hashCode를 활용한다

    @Override
    public boolean equals(Object item) {
        if(item == null) return false;
        if(item instanceof MusicItem) return false;

        return id.hashCode() == item.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Uri getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
        this.albumUri = albumUri;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
