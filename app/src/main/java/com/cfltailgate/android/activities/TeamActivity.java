package com.cfltailgate.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cfltailgate.android.R;

/**
 * Created by vruno on 11/28/15.
 */
public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        FrameLayout teamSelection = (FrameLayout) findViewById(R.id.horizontalScrollView);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout teamList = (LinearLayout) inflater.inflate(R.layout.activity_team_list, null);
        teamSelection.addView(teamList);
     }
}
