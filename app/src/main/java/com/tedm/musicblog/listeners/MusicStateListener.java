package com.tedm.musicblog.listeners;

/**
 * Listens for playback changes to send the the fragments bound to this activity
 */
public interface MusicStateListener {


     // Called when {@link com.tedm.musicblog.MusicService#REFRESH} is invoked

    void restartLoader();


    //Called when {@link com.tedm.musicblog.MusicService#PLAYLIST_CHANGED} is invoked

    void onPlaylistChanged();


     // Called when {@link com.tedm.musicblog.MusicService#META_CHANGED} is invoked

    void onMetaChanged();

}
