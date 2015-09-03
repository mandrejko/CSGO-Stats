package edu.carthage.andrejko.matt.csgostats;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddMatch extends ActionBarActivity {
    private ArrayList<String> mapList;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        ref.setAndroidContext(this);
        ref = new Firebase("https://csgostats.firebaseio.com/");

        mapList = new ArrayList<>();
        fillMapList();

        Spinner mapSpinner = (Spinner) findViewById(R.id.mapSpinner);
        ArrayAdapter<String> mapAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, mapList);

        mapAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        mapSpinner.setAdapter(mapAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_match, menu);
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

    public void onClick(View theButton) {
        //close the keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //get the references to the data fields
        Spinner mapSpinner = (Spinner) findViewById(R.id.mapSpinner);
        TextView killsAmt = (TextView) findViewById(R.id.killsAmt);
        TextView deathsAmt = (TextView) findViewById(R.id.deathsAmt);
        TextView ctRoundsAmt = (TextView) findViewById(R.id.ctRoundsAmt);
        TextView tRoundsAmt = (TextView) findViewById(R.id.tRoundsAmt);

        if (checkIfValid(killsAmt, deathsAmt, ctRoundsAmt, tRoundsAmt)) {
            //grab the data out of the references
            String mapName = mapSpinner.getSelectedItem().toString();
            Double kills = Double.parseDouble(killsAmt.getText().toString());
            Double deaths = Double.parseDouble(deathsAmt.getText().toString());
            Double ctRounds = Double.parseDouble(ctRoundsAmt.getText().toString());
            Double tRounds = Double.parseDouble(tRoundsAmt.getText().toString());

            //get the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:mm a");
            Date date = new Date();
            String thisDate = dateFormat.format(date);

            //make a map for the details of the match
            Map<String, Object> map = new HashMap<>();
            map.put("Kills", kills);
            map.put("Deaths", deaths);
            map.put("CT Rounds", ctRounds);
            map.put("T Rounds", tRounds);
            map.put("Date", thisDate);

            //fill firebase
            ref.child(mapName);
            Firebase match = ref.child(mapName).push();
            match.updateChildren(map);

            //set the boxes back
            mapSpinner.setSelection(0);
            killsAmt.setText("");
            deathsAmt.setText("");
            ctRoundsAmt.setText("");
            tRoundsAmt.setText("");

            //toast to successful input
            CharSequence message = "Successfully added the match";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, message, duration);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            CharSequence message = "ERROR: At least one box is empty";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, message, duration);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public Boolean checkIfValid(TextView killsAmt, TextView deathsAmt, TextView ctRoundsAmt, TextView tRoundsAmt) {
        if (killsAmt.getText().toString().matches(""))
            return false;
        else if (deathsAmt.getText().toString().matches(""))
            return false;
        else if (ctRoundsAmt.getText().toString().matches(""))
            return false;
        else if (tRoundsAmt.getText().toString().matches(""))
            return false;

        return true;
    }

    public void fillMapList() {
        mapList.add("Dust II");
        mapList.add("Nuke");
        mapList.add("Cobblestone");
        mapList.add("Inferno");
        mapList.add("Cache");
        mapList.add("Mirage");
        mapList.add("Overpass");
    }
}
