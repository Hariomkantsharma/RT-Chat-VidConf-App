package com.haridroid.realtimechatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.haridroid.realtimechatapplication.MainActivity;
import com.haridroid.realtimechatapplication.R;


public class registrationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    boolean loginPossible = false;
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(registrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(registrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);


        mAuth= FirebaseAuth.getInstance();
        EditText userName = findViewById(R.id.userName);
        EditText passWord = findViewById(R.id.passWord);
        Button RegisterBtn = findViewById(R.id.registerBtn);
        TextView whatNext = findViewById(R.id.backToLogin);
        progressBar= findViewById(R.id.progressBar);

        whatNext.setVisibility(View.GONE);

        whatNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatNext.setVisibility(View.GONE);
                if(loginPossible){
                    Intent intent = new Intent(registrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    userName.setText("");
                    passWord.setText("");
                }

            }
        });
        userName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                whatNext.setVisibility(View.GONE);
                return false;
            }
        });
        passWord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                whatNext.setVisibility(View.GONE);
                return false;
            }
        });
//        userName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                whatNext.setVisibility(View.GONE);
//            }
//        });
//        passWord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                whatNext.setVisibility(View.GONE);
//            }
//        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email = userName.getText().toString();
                password = passWord.getText().toString();

                if (email.isEmpty()) {
                    userName.setError("Email is required!");
                    userName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    progressBar.setVisibility(View.GONE);
                    userName.setError("Please provide valid email!");
                    userName.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    passWord.setError("Email is required!");
                    passWord.requestFocus();
                    return;
                }
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(registrationActivity.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        /// add pop up of success
                                        loginPossible = true;
                                        whatNext.setTextColor(Color.WHITE);
                                        whatNext.setText("Login now");
                                        whatNext.setVisibility(View.VISIBLE);
                                    } else {
                                        // Check for email already in use error
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            whatNext.setTextColor(Color.RED);
                                            whatNext.setText("Email already in use, Click here to clear fields");
                                            whatNext.setVisibility(View.VISIBLE);
                                        } else {
                                            whatNext.setTextColor(Color.RED);
                                            whatNext.setText("Enter valid details, Click here to clear fields");
                                            whatNext.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            });

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    whatNext.setTextColor(Color.RED);
                    whatNext.setText("No Internet Connection");
                    whatNext.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}