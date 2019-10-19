package com.tedm.musicblog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText loginEmailText;
    private EditText loginPassText;

    private Button loginBtn;
    private Button loginRegBtn;

    private FirebaseAuth mAuth;

    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmailText = (EditText) findViewById(R.id.reg_email);
        loginPassText = (EditText) findViewById(R.id.reg_confirm_pass);
        loginBtn = (Button) findViewById(R.id.btn_login_connect);
        loginRegBtn = (Button) findViewById(R.id.btn_login_newAcc);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        loginBtn.setOnClickListener(this);
        loginRegBtn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currenUser = mAuth.getCurrentUser();

        if(currenUser != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToRegister() {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login_connect:

                String loginEmail = loginEmailText.getText().toString();
                String loginPassword = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage,Toast.LENGTH_LONG).show();
                            }

                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                break;

            case R.id.btn_login_newAcc:
                sendToRegister();
                break;

        }
    }
}
