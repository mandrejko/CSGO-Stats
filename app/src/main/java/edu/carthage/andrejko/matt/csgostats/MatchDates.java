package edu.carthage.andrejko.matt.csgostats;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MatchDates extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private ListView dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_dates);

        TextView mapTitle = (TextView) findViewById(R.id.mapTitle);
        mapTitle.setText(getIntent().getStringExtra("Name"));

        dateList = (ListView) findViewById(R.id.dateList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.date_list_item, R.id.date, getIntent().getStringArrayListExtra("Dates"));

        dateList.setAdapter(adapter);
        dateList.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_dates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, MatchStats.class);

        String date = ((TextView)view.findViewById(R.id.date)).getText().toString();

        i.putExtra("Date", date);
        i.putExtra("Name", getIntent().getStringExtra("Name"));

        startActivity(i);
    }
}
