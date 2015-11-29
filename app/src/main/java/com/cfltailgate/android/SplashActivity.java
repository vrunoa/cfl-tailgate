package com.cfltailgate.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cfltailgate.android.activities.TeamActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vruno on 11/28/15.
 */
public class SplashActivity extends Activity {

    private List<String> permissions = Arrays.asList("public_profile", "email", "user_friends", "user_location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirect();
            }
        }, 2000);
    }

    private void redirect() {
        ParseUser user = ParseUser.getCurrentUser();
        if(user != null) {
            enter();
        }else{
            login();
        }
    }

    private void enter() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void selectTeam() {
        Intent intent = new Intent(SplashActivity.this, TeamActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(){
        Button fbBtt = (Button) findViewById(R.id.fbBtt);
        fbBtt.setVisibility(View.VISIBLE);
        fbBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(SplashActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e != null) {
                            Toast.makeText(SplashActivity.this, R.string.fb_signin_error, Toast.LENGTH_LONG);
                            return;
                        }
                        if(parseUser == null) {
                            Toast.makeText(SplashActivity.this, R.string.fb_coward, Toast.LENGTH_LONG);
                            return;
                        }
                        if(!parseUser.isNew()) {
                            enter();
                        }else{
                            selectTeam();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
