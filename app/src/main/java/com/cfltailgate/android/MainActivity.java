package com.cfltailgate.android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.cfltailgate.android.activities.ChallengeActivity;
import com.cfltailgate.android.activities.PrizesActivity;
import com.cfltailgate.android.activities.TriviaActivity;
import com.cfltailgate.android.adapters.GameAdapter;
import com.cfltailgate.android.utils.Utils;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.okhttp.Challenge;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View.OnClickListener mainButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activity = null;
                if (v.getId() == R.id.trivia_button) {
                    activity = TriviaActivity.class;
                } else if (v.getId() == R.id.bet_button) {
                    activity = GameListActivity.class;
                } else if (v.getId() == R.id.challenge_button) {
                    activity = ChallengeActivity.class;
                }
                if (activity != null) {
                    startActivityHelper(activity);
                }
            }
        };

        int[] buttonIds = new int[] { R.id.trivia_button, R.id.bet_button, R.id.challenge_button };
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(mainButtonClickListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_logout) {
            final Dialog preloader = Utils.Preloader(MainActivity.this, null, getResources().getString(R.string.logging_out));
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    preloader.dismiss();
                    if (e != null) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG);
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        if (id == R.id.nav_challenges) {
            startActivityHelper(ChallengeActivity.class);
        } else if (id == R.id.nav_bets) {
            startActivityHelper(GameListActivity.class);
        }else if(id == R.id.nav_news) {
            // http://cfl.ca/news
        }else if(id == R.id.nav_prizes) {
            startActivityHelper(PrizesActivity.class);
        }

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void startActivityHelper(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
