package edu.carthage.andrejko.matt.csgostats;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;


public class MatchStats extends ActionBarActivity {
    private Firebase ref;
    private String date;
    private String mapName;
    private int kills;
    private int deaths;
    private double kdr;
    private int ctRounds;
    private int tRounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_stats);

        date = getIntent().getStringExtra("Date");
        mapName = getIntent().getStringExtra("Name");

        TextView dateTitle = (TextView) findViewById(R.id.dateTitle);
        dateTitle.setText(date);

        TextView mapTitle = (TextView) findViewById(R.id.mapTitle);
        mapTitle.setText(mapName);

        ref = MainActivity.getFirebase();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //for each map
                for (DataSnapshot map : snapshot.getChildren()) {
                    if(map.getKey().equals(mapName)) {
                        //for each match
                        for (DataSnapshot match : map.getChildren()) {
                            if(match.child("Date").getValue().equals(date)) {
                                //for each stat type
                                for (DataSnapshot stats : match.getChildren()) {
                                    if(stats.getKey().equals("CT Rounds"))
                                        ctRounds = (int)Double.parseDouble(stats.getValue().toString());
                                    else if(stats.getKey().equals("T Rounds"))
                                        tRounds = (int)Double.parseDouble(stats.getValue().toString());
                                    else if(stats.getKey().equals("Kills"))
                                        kills = (int)Double.parseDouble(stats.getValue().toString());
                                    else if(stats.getKey().equals("Deaths"))
                                        deaths = (int)Double.parseDouble(stats.getValue().toString());
                                }
                            }
                        }
                    }
                }
                kdr = (double)kills / (double)deaths;

                TextView kdrValue = (TextView) findViewById(R.id.kdrValue);
                String kdrFormatted = String.format("%.2f", kdr);
                kdrValue.setText("" + kdrFormatted + " (" + kills + " / " + deaths + ")");

                TextView ctValue = (TextView) findViewById(R.id.ctValue);
                ctValue.setText("" + ctRounds);

                TextView tValue = (TextView) findViewById(R.id.tValue);
                tValue.setText("" + tRounds);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
