package com.guozhe.android.myapp.Detail;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.MusicItem;

import java.util.List;

/**
 * Created by guozhe on 2017. 11. 1..
 */

public class DetailAdapter extends PagerAdapter {
    List<MusicItem> items;

    public DetailAdapter(List<MusicItem> items){
        this.items = items;

    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_detail,null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView textView = (TextView)view.findViewById(R.id.textTitle);

        textView.setText(items.get(position).getTitle());
        imageView.setImageURI(items.get(position).getAlbumUri());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
