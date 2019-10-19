package com.tedm.musicblog.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ItemHolder(View view) {super(view);}
    }



    public void removeSongAt(int i){}
    public void updateDateSet(){}
}
