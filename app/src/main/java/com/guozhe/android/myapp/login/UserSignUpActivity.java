package com.guozhe.android.myapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.util.FirebaseAuthUtil;

public class UserSignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editEmail;
    private EditText editPassWord;
    private EditText editCfgPassWord;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(FirebaseAuthUtil.TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(FirebaseAuthUtil.TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        setContentView(R.layout.activity_user_sign_up);

        initView();
        setListtener();
    }

    private void initView() {
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassWord = (EditText) findViewById(R.id.edit_passWord);
        editCfgPassWord = (EditText) findViewById(R.id.edit_cfg_passWord);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
    }
    private void setListtener(){
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signUp:
                next();
                break;
        }
    }
    private void next(){
        String email = editEmail.getText().toString().trim();
        String passWord = editPassWord.getText().toString().trim();
        String config_passWord = editCfgPassWord.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"email를 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            Toast.makeText(getApplicationContext(),"passWord를 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(config_passWord)){
            Toast.makeText(getApplicationContext(),"passWord확인을 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passWord.equals(config_passWord)){
            mAuth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(FirebaseAuthUtil.TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.e("uid","========="+task.getResult().getUser().getUid().toString());
                            Intent intent = new Intent(UserSignUpActivity.this,UserProfileActivity.class);
                            intent.putExtra("uid",task.getResult().getUser().getUid().toString());
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(UserSignUpActivity.this, "사용자가 등록 되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        }


    }
}
