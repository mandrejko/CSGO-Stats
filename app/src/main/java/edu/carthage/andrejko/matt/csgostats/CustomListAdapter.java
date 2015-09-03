package edu.carthage.andrejko.matt.csgostats;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 1/27/15.
 */
public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mapList;

    public CustomListAdapter(Context context, List<Map<String, Object>> mapList){
        this.context = context;
        this.mapList = mapList;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> currentMap = mapList.get(position);
        if(!currentMap.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.list_item, null);

            TextView mapTitle = (TextView) view.findViewById(R.id.mapTitle);
            TextView kdrValue = (TextView) view.findViewById(R.id.kdrValue);
            TextView ctAvgValue = (TextView) view.findViewById(R.id.ctAvgValue);
            TextView tAvgValue = (TextView) view.findViewById(R.id.tAvgValue);

            mapTitle.setText(getMapName(position));
            kdrValue.setText(String.format("%.2f", (double) currentMap.get("kdr")));
            ctAvgValue.setText(String.format("%.2f", (double) currentMap.get("CT Avg")));
            tAvgValue.setText(String.format("%.2f", (double) currentMap.get("T Avg")));

            return view;
        }
        else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.list_item, null);

            TextView mapTitle = (TextView) view.findViewById(R.id.mapTitle);
            TextView kdrValue = (TextView) view.findViewById(R.id.kdrValue);
            TextView ctAvgValue = (TextView) view.findViewById(R.id.ctAvgValue);
            TextView tAvgValue = (TextView) view.findViewById(R.id.tAvgValue);

            mapTitle.setText(getMapName(position));
            kdrValue.setText("N/A");
            ctAvgValue.setText("N/A");
            tAvgValue.setText("N/A");
            kdrValue.setTextColor(Color.RED);
            ctAvgValue.setTextColor(Color.RED);
            tAvgValue.setTextColor(Color.RED);

            return view;
        }
    }

    public String getMapName(int position){
        if(position == 0)
            return "Cache";
        else if(position == 1)
            return "Cobblestone";
        else if(position == 2)
            return "Dust II";
        else if(position == 3)
            return "Inferno";
        else if(position == 4)
            return "Mirage";
        else if(position == 5)
            return "Nuke";
        else
            return "Overpass";
    }
}