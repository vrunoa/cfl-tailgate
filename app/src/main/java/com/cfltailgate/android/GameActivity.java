package com.cfltailgate.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cfltailgate.android.R;
import com.cfltailgate.android.model.Bet;
import com.cfltailgate.android.model.GameUpdater;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements GameUpdater.UpdateListener {

    private TextView _homeNameTextView;
    private TextView _homeScoreTextView;
    private TextView _awayNameTextView;
    private TextView _awayScoreTextView;
    private ListView _playsListView;

    private ArrayList<String> _plays = new ArrayList<>();
    private GameUpdater _gameUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        _homeNameTextView = (TextView)findViewById(R.id.home_name_textview);
        _homeScoreTextView = (TextView)findViewById(R.id.home_score_textview);
        _awayNameTextView = (TextView)findViewById(R.id.away_name_textview);
        _awayScoreTextView = (TextView)findViewById(R.id.away_score_textview);

        _homeNameTextView.setText("BC Lions");
        _homeScoreTextView.setText("20");
        _awayNameTextView.setText("Toronto Argonauts");
        _awayScoreTextView.setText("17");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_play, R.id.play_textview, _plays);
        _playsListView = (ListView)findViewById(R.id.plays_listview);
        _playsListView.setAdapter(adapter);

        //ListView betsListView = (ListView)findViewById(R.id.bets_listview);
        //betsListView.setAdapter(adapter);

        _gameUpdater = new GameUpdater("VfPrNxlFuB");
        _gameUpdater.setUpdateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    // GameUpdater.UpdateListener

    public void onBetResolved(Bet bet) {

    }

    public void onNewPlay(String text) {
        _plays.add(0, text);
        ((BaseAdapter)_playsListView.getAdapter()).notifyDataSetChanged();
    }

    public View getPoster() {
        return _homeNameTextView; // just get any view so that GameUpdater can post to the UI thread
    }
}
