package com.guozhe.android.myapp.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.MusicItem;

import java.util.List;

/**
 * Created by guozhe on 2017. 11. 15..
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {
    private List<MusicItem> items;
    private ListFragment.ListFragmentOnclickListenner mListenner;


    public ListAdapter(List<MusicItem> items, ListFragment.ListFragmentOnclickListenner mListenner){
        this.items = items;
        this.mListenner = mListenner;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_fragment,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MusicItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.artist.setText(item.getArtist());
        holder.duration.setText(item.getDuration());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private int position;
        private TextView title;
        private TextView artist;
        private TextView duration;
        private View mView;

        public Holder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            artist = (TextView)itemView.findViewById(R.id.artist);
            duration = (TextView)itemView.findViewById(R.id.duration);
            mView = itemView;

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goDetai(position);
                }
            });
        }
    }
    private void goDetai(int position){
        mListenner.goDetail(position);
    }

}
