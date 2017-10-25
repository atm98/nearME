package com.agnt45.nearme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView forgotPass;
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
        forgotPass = findViewById(R.id.FPASS);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d1 = new Dialog(LoginActivity.this);
                d1.setContentView(R.layout.forgot_password_layout);
                d1.show();
                final TextInputLayout email = d1.findViewById(R.id.email_Input);
                Button send = d1.findViewById(R.id.SendMailVerify);
                send.setVisibility(View.INVISIBLE);
                if(email.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please Enter the Email to Continue",Toast.LENGTH_LONG).show();

                }
                else{
                    send.setVisibility(View.VISIBLE);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ProgressDialog progressDialog1 =  new ProgressDialog(LoginActivity.this);
                            progressDialog1.setTitle("Please Wait");
                            progressDialog1.setMessage("Sending Verfication Link to"+email.getEditText().getText().toString());
                            progressDialog1.setCanceledOnTouchOutside(false);
                            progressDialog1.show();
                            mAuth.sendPasswordResetEmail(email.getEditText().getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog1.dismiss();
                                                Toast.makeText(LoginActivity.this,"Verification  Link Sent Please Check your email",Toast.LENGTH_LONG).show();
                                                email.getEditText().setText("");

                                            }
                                            else {
                                                progressDialog1.hide();
                                                Toast.makeText(LoginActivity.this,"Error in Sending Verification  Link Please check the email inputed",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
                }

            }
        });
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

