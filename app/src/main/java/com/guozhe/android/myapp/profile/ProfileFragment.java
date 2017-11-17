package com.guozhe.android.myapp.profile;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.UserBbs;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final ProfileFragment instance = new ProfileFragment();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorage;

    public static ProfileFragment getInstance(String uid) {
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        mStorage = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = (TextView)view.findViewById(R.id.name) ;
        TextView birthday = (TextView)view.findViewById(R.id.birthday);
        ImageView myPhoto = (ImageView)view.findViewById(R.id.myPhoto1);

        Bundle bundle = getArguments();
        String uid =bundle.getString("uid");
        loadData(uid,name,birthday);
        loadFile(uid,myPhoto);

        return view;
    }
    private void loadData(final String uid, final TextView name, final TextView birthday){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.getKey().equals(uid)){
                        UserBbs bbs = item.getValue(UserBbs.class);
                        name.setText(bbs.name);
                        birthday.setText(bbs.birthday);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadFile(String uid, final ImageView myPhoto){
        StorageReference reference = mStorage.child(uid);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext())
                        .load(uri)
                        .into(myPhoto);
            }
        });



    }

}
