package com.agnt45.nearme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private TextInputLayout email,passwd;
    private String Email,Passwd;
    private FirebaseAuth mAuth;
    private Button Login;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email  = findViewById(R.id.emailInput);
        passwd =   findViewById(R.id.passwordInput);
        Login = findViewById(R.id.LoginButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getEditText().getText().toString();
                Passwd = passwd.getEditText().getText().toString();
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(Email, Passwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("hello:","working");
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Intent Home  = new Intent(LoginActivity.this,HomeActivity.class);
                                    Home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(Home);
                                    finish();
                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(LoginActivity.this,"Error While Logging In Plase Check Your Email or Password", Toast.LENGTH_LONG).show();

                                }


                            }
                        });
            }
        });
    }
}

