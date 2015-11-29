package com.cfltailgate.android;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cfltailgate.android.model.GameUpdater;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements GameUpdater.UpdateListener {

    private TextView _homeNameTextView;
    private TextView _homeScoreTextView;
    private TextView _awayNameTextView;
    private TextView _awayScoreTextView;
    private ListView _playsListView;
    private ListView _betsListView;

    private ArrayList<String> _plays = new ArrayList<>();
    private ArrayList<ParseObject> _bets = new ArrayList<>();
    private GameUpdater _gameUpdater;

    private enum BetState {
        Unanswered,
        AnsweredYes,
        AnsweredNo,
        Correct,
        Incorrect
    };

    private static final String BET_PARSE_FIELD_TEXT = "text";
    private static final String BET_PARSE_FIELD_GAME = "game";
    private static final String BET_PARSE_TICK_START = "tickStart";
    private static final String BET_PARSE_TICK_END = "tickEnd";
    private static final String BET_PARSE_ANSWER = "answer";        // the actual answer
    private static final String BET_PARSE_BETSTATE = "betstate";    // stored locally only

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


        ArrayAdapter<String> playAdapter = new ArrayAdapter<>(this, R.layout.row_play, R.id.play_textview, _plays);
        _playsListView = (ListView)findViewById(R.id.plays_listview);
        _playsListView.setAdapter(playAdapter);

        BetAdapter betAdapter = new BetAdapter(_bets);
        _betsListView = (ListView)findViewById(R.id.bets_listview);
        _betsListView.setAdapter(betAdapter);

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

    public void onBetResolved(final ParseObject bet) {
        // Get the betstate that we've stashed in the ParseObject
        BetState betState = BetState.values()[bet.getInt(BET_PARSE_BETSTATE)];

        // If the user answered the bet, then mark the bet as correct or incorrect,
        // then leave the bet on screen for a bit before removing it.
        if (betState == BetState.AnsweredYes || betState == BetState.AnsweredNo) {
            boolean userAnswer = (betState == BetState.AnsweredYes);
            BetState newState = (userAnswer == bet.getBoolean(BET_PARSE_ANSWER)) ? BetState.Correct : BetState.Incorrect;
            bet.put(BET_PARSE_BETSTATE, newState.ordinal());
            ((BaseAdapter)_betsListView.getAdapter()).notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((BetAdapter)_betsListView.getAdapter()).remove(bet);
                }
            }, 6 * 1000);
        }
        // Otherwise just remove the bet since it wasn't relevant to the user anyway.
        else {
            ((BetAdapter)_betsListView.getAdapter()).remove(bet);
        }
    }

    public void onNewBet(ParseObject bet) {
        // Set the bet state to Unanswered to signify that the user hasn't bet on it yet.
        // This is used for styling the list item.
        bet.put(BET_PARSE_BETSTATE, BetState.Unanswered.ordinal());
        _bets.add(bet);
        ((BaseAdapter)_betsListView.getAdapter()).notifyDataSetChanged();
    }

    public void onNewPlay(String text) {
        _plays.add(0, text);
        ((BaseAdapter)_playsListView.getAdapter()).notifyDataSetChanged();
    }

    private class BetAdapter extends ArrayAdapter<ParseObject> {
        Context _context = GameActivity.this;

        public BetAdapter(List<ParseObject> bets) {
            super(GameActivity.this.getApplicationContext(), R.layout.row_bet, bets);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.row_bet, parent, false);
            }

            final ParseObject bet = getItem(position);

            // Set the background depending on the bet state.
            BetState betState = BetState.values()[bet.getInt(BET_PARSE_BETSTATE)];
            int backgroundColor = Color.TRANSPARENT;
            if (betState == BetState.AnsweredNo || betState == BetState.AnsweredYes) {
                backgroundColor = Color.YELLOW;
            } else if (betState == BetState.Correct) {
                backgroundColor = Color.GREEN;
            } else if (betState == BetState.Incorrect) {
                backgroundColor = Color.RED;
            }
            rowView.setBackgroundColor(backgroundColor);

            // Set the bet text.
            TextView betTextView = (TextView)rowView.findViewById(R.id.bet_textview);
            betTextView.setText(bet.getString(BET_PARSE_FIELD_TEXT));

            // Answer click handlers should stash the new bet state, then update the background.
            final View thatRowView = rowView;
            final Button yesButton = (Button)rowView.findViewById(R.id.yes_button);
            final Button noButton = (Button)rowView.findViewById(R.id.no_button);
            View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thatRowView.setBackgroundColor(Color.YELLOW);
                    yesButton.setEnabled(false);
                    noButton.setEnabled(false);
                    bet.put(BET_PARSE_BETSTATE, (v == yesButton) ? BetState.AnsweredYes.ordinal() : BetState.AnsweredNo.ordinal());
                }
            };
            yesButton.setOnClickListener(answerButtonClickListener);
            noButton.setOnClickListener(answerButtonClickListener);

            return rowView;
        }
    }
}
