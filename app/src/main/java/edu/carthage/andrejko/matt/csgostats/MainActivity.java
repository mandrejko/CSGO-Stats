package edu.carthage.andrejko.matt.csgostats;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static Firebase ref;
    //hash maps to hold the stats for each game map
    private Map<String, Object> cacheMap;
    private Map<String, Object> cobbleMap;
    private Map<String, Object> dustMap;
    private Map<String, Object> infernoMap;
    private Map<String, Object> mirageMap;
    private Map<String, Object> nukeMap;
    private Map<String, Object> overpassMap;

    private List<Map<String, Object>> hashMapList;

    private ListView statsListView;

    private CustomListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize all the hash maps
        cacheMap = new HashMap<>();
        cobbleMap = new HashMap<>();
        dustMap = new HashMap<>();
        infernoMap = new HashMap<>();
        mirageMap = new HashMap<>();
        nukeMap = new HashMap<>();
        overpassMap = new HashMap<>();

        hashMapList = new ArrayList<>();
        hashMapList.add(cacheMap);
        hashMapList.add(cobbleMap);
        hashMapList.add(dustMap);
        hashMapList.add(infernoMap);
        hashMapList.add(mirageMap);
        hashMapList.add(nukeMap);
        hashMapList.add(overpassMap);

        ref.setAndroidContext(this);
        ref = new Firebase("https://csgostats.firebaseio.com/");

        listAdapter = new CustomListAdapter(this, hashMapList);
        statsListView = (ListView) findViewById(R.id.mapStatsView);
        statsListView.setAdapter(listAdapter);

        statsListView.setOnItemClickListener(this);

        ref.addValueEventListener(new ValueEventListener() {
            Map<String, Object> varMap;
            double matchCount;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clear the maps for fresh data every time it changes
                cacheMap.clear();
                cobbleMap.clear();
                dustMap.clear();
                infernoMap.clear();
                mirageMap.clear();
                nukeMap.clear();
                overpassMap.clear();

                //for each map
                for (DataSnapshot map : snapshot.getChildren()) {
                    varMap = setMap(map.getKey());
                    matchCount = 0;

                    //Log.d("MainActivity", "Map: " + map.getKey());

                    //for each match
                    for (DataSnapshot match : map.getChildren()) {
                        matchCount++;

                        //Log.d("MainActivity", "--Match: " + match.getKey());

                        //for each stat type
                        for (DataSnapshot stats : match.getChildren()) {
                            //set the hash map data

                            //Log.d("MainActivity", "---" + stats.getKey() + ": " + stats.getValue());

                            setMapData(varMap, stats.getKey(), stats.getValue(), matchCount);
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_match) {
            Intent i = new Intent(this, AddMatch.class);

            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public Map<String, Object> setMap(String mapName){
        Map<String, Object> retVal;

        if(mapName.equals("Cache"))
            retVal = cacheMap;
        else if(mapName.equals("Cobblestone"))
            retVal = cobbleMap;
        else if(mapName.equals("Dust II"))
            retVal = dustMap;
        else if(mapName.equals("Inferno"))
            retVal = infernoMap;
        else if(mapName.equals("Mirage"))
            retVal = mirageMap;
        else if(mapName.equals("Nuke"))
            retVal = nukeMap;
        else
            retVal = overpassMap;

        return retVal;
    }

    public void setMapData(Map<String, Object> map, String stat, Object value, double matchCount){

        if(stat.equals("CT Rounds")){
            if(map.get("CT Avg") != null){
                map.put("CT Avg", newAverage((Double.parseDouble(map.get("CT Avg").toString())), (double)value, matchCount));
            }
            else{
                map.put("CT Avg", value);
            }
        }
        else if(stat.equals("Date")){
            if(map.get("Dates") != null){
                @SuppressWarnings("unchecked")
                ArrayList<String> temp = (ArrayList<String>) map.get("Dates");
                temp.add(value.toString());

                map.put("Dates", temp);
            }
            else{
                ArrayList<String> dates = new ArrayList<>();
                dates.add(value.toString());

                map.put("Dates", dates);
            }
        }
        else if(stat.equals("Deaths")){
            if(map.get("Deaths") != null){
                map.put("Deaths", Double.parseDouble(map.get("Deaths").toString()) + (double)value);
                map.put("kdr", Double.parseDouble(map.get("Kills").toString()) / Double.parseDouble(map.get("Deaths").toString()));
            }
            else{
                map.put("Deaths", value);
            }
        }
        else if(stat.equals("Kills")){
            if(map.get("Kills") != null){
                map.put("Kills", Double.parseDouble(map.get("Kills").toString()) + (double)value);
                map.put("kdr", Double.parseDouble(map.get("Kills").toString()) / Double.parseDouble(map.get("Deaths").toString()));
            }
            else{
                map.put("Kills", value);
                map.put("kdr", ((double) value) / (Double.parseDouble(map.get("Deaths").toString())));
            }
        }
        else if(stat.equals("T Rounds")){
            if(map.get("T Avg") != null){
                map.put("T Avg", newAverage((Double.parseDouble(map.get("T Avg").toString())), (double)value, matchCount));
            }
            else{
                map.put("T Avg", value);
            }
        }
    }

    public double newAverage(double currentAvg, double value, double matchCount){
        double retVal;

        if(matchCount == 1){
            retVal = currentAvg + value;
        }
        else if(matchCount == 2){
            retVal = (currentAvg + value) / 2.0;
        }
        else{
            retVal = ((currentAvg * (matchCount - 1)) + value) / matchCount;
        }

        return retVal;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, MatchDates.class);
        ArrayList<String> dates = new ArrayList<>();

        if(position == 0) {
            dates = (ArrayList<String>) cacheMap.get("Dates");
            i.putExtra("Name", "Cache");
        }
        else if(position == 1){
            dates = (ArrayList<String>) cobbleMap.get("Dates");
            i.putExtra("Name", "Cobblestone");
        }
        else if(position == 2){
            dates = (ArrayList<String>) dustMap.get("Dates");
            i.putExtra("Name", "Dust II");
        }
        else if(position == 3){
            dates = (ArrayList<String>) infernoMap.get("Dates");
            i.putExtra("Name", "Inferno");
        }
        else if(position == 4){
            dates = (ArrayList<String>) mirageMap.get("Dates");
            i.putExtra("Name", "Mirage");
        }
        else if(position == 5){
            dates = (ArrayList<String>) overpassMap.get("Dates");
            i.putExtra("Name", "Overpass");
        }

        if(dates == null){
            CharSequence message = "ERROR: No match data for selected map";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, message, duration);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            i.putStringArrayListExtra("Dates", dates);
            startActivity(i);
        }
    }

    public static Firebase getFirebase(){
        return ref;
    }
}
