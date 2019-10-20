package com.tedm.musicblog.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tedm.musicblog.models.Song;

public class BaseSongAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    @Override
    public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull V v, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // Missing getItemViewType. Will Add later

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ItemHolder(View view) {super(view);}
    }

    // Need to declare sourceType TimberUtils.IdType sourceType
    public void playAll(final Activity context, final long[] list, int position,
                        final long sourceId, final boolean forceShuffle, final Song currentSong, boolean navigateNowPlaying){

        // Waiting for code.
    }



    public void removeSongAt(int i){}
    public void updateDateSet(){}
}
