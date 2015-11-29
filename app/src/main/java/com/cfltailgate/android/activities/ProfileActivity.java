package com.cfltailgate.android.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cfltailgate.android.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

/**
 * Created by vruno on 11/28/15.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ParseUser user = ParseUser.getCurrentUser();
        if(user == null) {
            finish();
        }

        RoundedImageView userPic = (RoundedImageView) findViewById(R.id.userPic);
        //String userPicURI = String.format(getResources().getString(R.string.fb_url), )
        // Picasso.with(ProfileActivity.this).load(Uri.parse(userPicURI)).into(userPic);


    }
}
