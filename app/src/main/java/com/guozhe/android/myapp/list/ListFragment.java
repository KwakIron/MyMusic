package com.guozhe.android.myapp.list;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.MyMusic;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private ListFragmentOnclickListenner mListenner;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        MyMusic myMusic = MyMusic.getInstance();
        myMusic.loader(container.getContext());

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        Log.e("Music","================"+myMusic.getItem().toString());

        com.guozhe.android.myapp.list.ListAdapter adapter = new com.guozhe.android.myapp.list.ListAdapter(myMusic.getItem(),mListenner);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ListFragmentOnclickListenner){
            mListenner = (ListFragmentOnclickListenner)context;
        }else {
            throw new RuntimeException("must implement lstFragmentOnclickListenner");
        }

    }
    public interface ListFragmentOnclickListenner{
        public void goDetail(int position);
    }

}
