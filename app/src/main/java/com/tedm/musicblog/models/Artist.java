package com.tedm.musicblog.models;

public class Artist {

    public final int albumCount;
    public final long id;
    public final String name;
    public final int songCount;

    public Artist() {
        this.albumCount = -1;
        this.id = -1;
        this.name = "";
        this.songCount = -1;
    }

    public Artist(int albumCount, long id, String name, int songCount) {
        this.albumCount = albumCount;
        this.id = id;
        this.name = name;
        this.songCount = songCount;
    }
}
