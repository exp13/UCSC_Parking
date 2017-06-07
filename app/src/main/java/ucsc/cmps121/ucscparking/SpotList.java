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

public class SpotList extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse{

    private ArrayList<SpotElement> spotList;
    private SpotAdapter spotAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);

        ListView myList = (ListView) findViewById(R.id.parkMapList);
        spotList = new ArrayList<>();
        spotAdap = new SpotAdapter(this, R.layout.map_list_row, spotList);
        myList.setAdapter(spotAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpotElement ele = spotList.get(position);
                Intent intent = ele.intent;
                startActivity(intent);
            }
        });

        Map<String, String> aMap = new HashMap<>();
        aMap.put("lotid", "North Remote");

        new ServletPostAsyncTask(this).execute(aMap);
    }

    public class SpotElement{

        public String title;
        public String subtitle;
        public int spotID;
        public Intent intent;

        SpotElement(){};

        SpotElement(String t, String s, Intent i){
            title = t;
            subtitle = s;
            intent = i;
        }
    }

    private class SpotAdapter extends ArrayAdapter<SpotElement> {

        Context context;
        int resource;

        public SpotAdapter(Context c, int r, List<SpotElement> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout newView;
            SpotElement ele = getItem(position);

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
    public void processFinish(String result){
        int cursorF = 0;
        int cursorB = 1;
        boolean notDone = true;
        SpotElement s = new SpotElement();
        int index = 1;

        while(notDone){

            s.spotID = index;
            s.title = "Spot " + Integer.toString(index);
            index++;

            if(Integer.parseInt(result.substring(cursorF, cursorB))==1){
                s.subtitle = "Occupied";
            }else{
                s.subtitle = "Available";
            }

            cursorB++;
            if(result.charAt(cursorB)=='!'){
                notDone = false;
            }else{
                cursorF = cursorB;
                cursorB++;
            }

            spotList.add(s);
        }

        spotAdap.notifyDataSetChanged();
    }
}
