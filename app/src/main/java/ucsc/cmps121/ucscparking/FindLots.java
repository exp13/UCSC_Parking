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
import java.util.List;

public class FindLots extends AppCompatActivity {

    private ArrayList<PreferredLots> lotsList;
    private LotsAdapter mapAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lots);

        ListView myList = (ListView) findViewById(R.id.prefferedParking);
        lotsList = new ArrayList<PreferredLots>();
        mapAdap = new LotsAdapter(this, R.layout.map_list_row, lotsList);
        myList.setAdapter(mapAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreferredLots ele = lotsList.get(position);
                Intent intent = ele.intent;
                startActivity(intent);
            }
        });
        PreferredLots ele;

        //add first item
        ele = new PreferredLots();
        ele.title = "East Remote Lot";
        ele.subtitle = "Parking Spot 15D";
        ele.intent = new Intent(this, FindLots.class);
        lotsList.add(ele);
        mapAdap.notifyDataSetChanged();
    }

    private class PreferredLots{

        public String title;
        public String subtitle;
        public Intent intent;

        PreferredLots(){};

        PreferredLots(String t, String s, Intent i){
            title = t;
            subtitle = s;
            intent = i;
        }
    }
    private class LotsAdapter extends ArrayAdapter<PreferredLots> {

        Context context;
        int resource;

        public LotsAdapter(Context c, int r, List<PreferredLots> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout newView;
            PreferredLots ele = getItem(position);

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

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

