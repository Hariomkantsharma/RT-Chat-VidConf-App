package com.haridroid.realtimechatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.haridroid.realtimechatapplication.activities.LoginActivity;
import com.haridroid.realtimechatapplication.fragments.groupChat;
import com.haridroid.realtimechatapplication.fragments.singleChat;
import com.haridroid.realtimechatapplication.fragments.videoConf;
import com.haridroid.realtimechatapplication.utils.GoogleSignInHelper;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout;
    FirebaseUser firebaseUser;

    boolean flag= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        GoogleSignInClient googleSignInClient = GoogleSignInHelper.getInstance(this).getGoogleSignInClient();
        auth= FirebaseAuth.getInstance();
        firebaseUser= auth.getCurrentUser();
        logout= findViewById(R.id.logoutBtn);
        TextView userName= findViewById(R.id.usernameToShow);

        if(firebaseUser==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            userName.setText(firebaseUser.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut()
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });




        //bottom navigation setup
        BottomNavigationView bnView= findViewById(R.id.bottom_navigation);
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.navigation_singleChat){
                    loadFrag(new singleChat());
                }
                else if(id==R.id.navigation_groupChat){
                    loadFrag(new groupChat());
                }
                else if(id==R.id.navigation_videoConf){
                    loadFrag(new videoConf());
            }
                else{
                    loadFrag(new singleChat());
                }
                return true;
            }
        });
        bnView.setSelectedItemId(R.id.navigation_singleChat);

    }

    public void loadFrag(Fragment fragment){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        if(!flag){
            ft.add(R.id.fragmentContainer, fragment);
            flag=true;
        }
        else{
            ft.replace(R.id.fragmentContainer, fragment);
        }

        ft.commit();

    }
}