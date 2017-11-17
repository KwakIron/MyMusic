package com.guozhe.android.myapp.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.UserBbs;

public class UserProfileActivity extends AppCompatActivity {

    private EditText edit_name;
    private EditText edit_birthday;
    private ImageView myphoto;
    private Button btnSignUp;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_user_profile);

        initView();
        postData();
        upPhoto();
    }

    private void initView() {
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_birthday = (EditText) findViewById(R.id.edit_birthday);
        myphoto = (ImageView) findViewById(R.id.myPhoto);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
    }
    private String get_intent(){
        Intent intent = getIntent();
        String resuelt="";
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            resuelt = bundle.getString("uid");
        }
        return resuelt;
    }
    private void postData(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_name.getText().toString();
                String birthday = edit_birthday.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"이름을 적어주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(birthday)){
                    Toast.makeText(getApplicationContext(),"생일을 적어주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserBbs bbs = new UserBbs(name,birthday);
                myRef.child(get_intent()).setValue(bbs);
                loadFile();
                Intent intent = new Intent(UserProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadFile(){
        if(imageUri != null) {
            StorageReference riversRef = mStorageRef.child(get_intent());

            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.e("FBstorage", "Upload Success!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Log.e("FBstorage", "Upload fail!");
                        }
                    });
        }
    }
    private void upPhoto(){
        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"앱을 선택하세요"),100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 100:
                    imageUri = data.getData();
                    myphoto.setImageURI(imageUri);
                    break;
            }
        }
    }
}
