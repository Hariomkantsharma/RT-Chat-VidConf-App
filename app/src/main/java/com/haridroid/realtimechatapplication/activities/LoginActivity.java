package com.haridroid.realtimechatapplication.activities;

import android.content.Context;
import android.content.Intent;import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.haridroid.realtimechatapplication.utils.GoogleSignInHelper;
import com.haridroid.realtimechatapplication.MainActivity;
import com.haridroid.realtimechatapplication.R;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient googleSignInClient = GoogleSignInHelper.getInstance(this).getGoogleSignInClient();
    FirebaseAuth mAuth;
    SignInButton googleLoginBtn;
    TextView passwordReset;
    TextView errorMsg;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        EditText userName = findViewById(R.id.userName);
        EditText passWord = findViewById(R.id.passWord);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView registerBtn = findViewById(R.id.regBtn);
        ProgressBar progressBar = findViewById(R.id.progressBarLogin);
         errorMsg = findViewById(R.id.errorMsg);
        googleLoginBtn = findViewById(R.id.googleLoginBtn);
        errorMsg.setVisibility(View.GONE);
        passwordReset= findViewById(R.id.forgetPassword);

        passwordReset.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            finish();
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, registrationActivity.class));
            finish();
        });

        userName.setOnTouchListener((v, event) -> {
            errorMsg.setVisibility(View.GONE);
            return false;
        });

        passWord.setOnTouchListener((v, event) -> {
            errorMsg.setVisibility(View.GONE);
            return false;
        });

        loginBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
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


            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                errorMsg.setTextColor(Color.RED);
                                errorMsg.setText("Incorrect Email or Password");
                                errorMsg.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                progressBar.setVisibility(View.GONE);
                errorMsg.setTextColor(Color.RED);
                errorMsg.setText("No Internet Connection");
                errorMsg.setVisibility(View.VISIBLE);
            }
        });

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                errorMsg.setTextColor(Color.RED);
                errorMsg.setText("Try again! Something wrong happened!");
                errorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}