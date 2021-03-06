package com.cfltailgate.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfltailgate.android.adapters.GameAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class GameListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        getSupportActionBar().setHomeButtonEnabled(true);

        final ListView gamesListView = (ListView)findViewById(R.id.games_listview);
        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject game = (ParseObject)parent.getAdapter().getItem(position);

                Intent intent = new Intent(GameListActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.EXTRA_GAMEID, game.getObjectId());
                intent.putExtra(GameActivity.EXTRA_HOMETEAM, game.getParseObject("home_team").getString("name"));
                intent.putExtra(GameActivity.EXTRA_AWAYTEAM, game.getParseObject("away_team").getString("name"));
                intent.putExtra(GameActivity.EXTRA_HOMELOGORESID,
                        App.getLogoStore().getLogoResId(game.getParseObject("home_team").getObjectId()));
                intent.putExtra(GameActivity.EXTRA_AWAYLOGORESID,
                        App.getLogoStore().getLogoResId(game.getParseObject("away_team").getObjectId()));
                startActivity(intent);
            }
        });

        App.getDataController().getGames(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Log.e("GameListActivity", e.toString());
                    return;
                }

                gamesListView.setAdapter(new GameAdapter(GameListActivity.this, list));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_list, menu);
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
}
