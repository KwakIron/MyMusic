package com.guozhe.android.myapp.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guozhe.android.myapp.MainActivity;
import com.guozhe.android.myapp.PermissionControl;
import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.util.FirebaseAuthUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,PermissionControl.CallBack {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;

    private EditText editEmail;
    private EditText editPassWord;
    private Button btnSiginIn;
    private TextView btnSignUp;

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
        setContentView(R.layout.activity_login);

        PermissionControl.checkVersion(LoginActivity.this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initView() {
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassWord = (EditText) findViewById(R.id.edit_passWord);
        btnSiginIn = (Button) findViewById(R.id.btn_siginIn);
        btnSignUp = (TextView) findViewById(R.id.btn_signUp);
    }
    private void setListener(){
        btnSiginIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signUp:
                goSiginUp();
                break;
            case R.id.btn_siginIn:
                goSiginIn();
                break;
        }
    }
    private void goSiginIn(){
        String email = editEmail.getText().toString().trim();
        String passWord = editPassWord.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"email를 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passWord)){
            Toast.makeText(getApplicationContext(),"passWord를 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(FirebaseAuthUtil.TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(FirebaseAuthUtil.TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "SignIn 되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }else if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("uid",task.getResult().getUser().getUid().toString());
                            startActivity(intent);

                        }
                    }
                });

    }
    private void goSiginUp(){
        Intent intent = new Intent(LoginActivity.this,UserSignUpActivity.class);
        startActivity(intent);
        finish();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.resultGrant(this,requestCode,grantResults);
    }

    @Override
    public void init() {
        initView();
        setListener();
    }
}
