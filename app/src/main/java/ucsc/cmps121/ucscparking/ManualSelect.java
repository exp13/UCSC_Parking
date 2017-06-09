package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManualSelect extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse{

    private ArrayList<MapsElement> mapsList;
    private MapsAdapter mapAdap;
    private Intent next;
    private String lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_select);

        ListView myList = (ListView) findViewById(R.id.manList);
        mapsList = new ArrayList<MapsElement>();
        mapAdap = new MapsAdapter(this, R.layout.map_list_row, mapsList);
        myList.setAdapter(mapAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapsElement ele = mapsList.get(position);
                Map<String, String> aMap = new HashMap<>();

                aMap.put("func", "GetParkingLot");
                aMap.put("lotid", ele.title);
                next = ele.intent;
                lot = ele.title;

                exTask(aMap);
            }
        });
        
        /*
            ADD NEW LIST ITEMS HERE (order matters)
         */
        MapsElement ele;

        // Copy this block and change for new list item
        ele = new MapsElement();
        ele.title = "North Remote";
        ele.subtitle = "next to Baskin";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "West Remote";
        ele.subtitle = "west side story";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "East Remote";
        ele.subtitle = "east bound and down";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Core West";
        ele.subtitle = "now with less free parking";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Performing Arts";
        ele.subtitle = "The DARCness";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Hahn Building";
        ele.subtitle = "Financial Aid";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Stevenson College";
        ele.subtitle = "Lot 109";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Merrill College";
        ele.subtitle = "Lot 119";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "College 10";
        ele.subtitle = "Lot 165";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Baskin 1";
        ele.subtitle = "Lot 138";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Baskin 2";
        ele.subtitle = "Lot 139A";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Kresge College";
        ele.subtitle = "Lot 143";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Porter College";
        ele.subtitle = "Lot 125";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Rachel Carson College";
        ele.subtitle = "Lot 146";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        ele = new MapsElement();
        ele.title = "Oakes College";
        ele.subtitle = "Lot 162";
        ele.intent = new Intent(this, ConfirmSpot.class);
        mapsList.add(ele);

        mapAdap.notifyDataSetChanged();
    }

    private void exTask(Map<String, String> aMap){
        new ServletPostAsyncTask(this).execute(aMap);
    }

    @Override
    public void processFinish(String result){
        int cursorB = 0;
        while(result.charAt(cursorB)!='!'){cursorB++;}

        int spot = Integer.parseInt(result.substring(0, cursorB));
        if(spot == 0){
            next.putExtra("lot_name", "none");
            next.putExtra("spot_count", "0");
        }else{
            next.putExtra("lot_name", lot);
            next.putExtra("spot_count", Integer.toString(spot));
        }

        startActivity(next);

    }

    public class MapsElement{

        public String title;
        public String subtitle;
        public Intent intent;

        MapsElement(){};

        MapsElement(String t, String s, Intent i){
            title = t;
            subtitle = s;
            intent = i;
        }
    }

    private class MapsAdapter extends ArrayAdapter<MapsElement> {

        Context context;
        int resource;

        public MapsAdapter(Context c, int r, List<MapsElement> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout newView;
            MapsElement ele = getItem(position);

            if(convertView == null){
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            }else{
                newView = (LinearLayout) convertView;
            }

            TextView t = (TextView) newView.findViewById(R.id.mapTitle);
            TextView s = (TextView) newView.findViewById(R.id.mapSubtitle);
            t.setText(ele.title);
            s.setText(ele.subtitle);

            return newView;
        }
    }
}
