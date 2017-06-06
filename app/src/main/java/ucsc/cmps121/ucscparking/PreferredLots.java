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

public class PreferredLots extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse {

    private ArrayList<PreferredLots.PrefsElement> prefList;
    private PrefsAdapter prefAdap;

    private AppInfo appInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_lots);

        appInfo = AppInfo.getInstance(this);

        ListView myList = (ListView) findViewById(R.id.prefListView);
        prefList = new ArrayList<>();
        prefAdap = new PreferredLots.PrefsAdapter(this, R.layout.pref_list_row, prefList);
        myList.setAdapter(prefAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreferredLots.PrefsElement ele = prefList.get(position);
                Intent intent = ele.intent;
                startActivity(intent);
            }
        });

        Map<String, String> aMap = new HashMap<>();

        aMap.put("func", "GetLots");
        aMap.put("userid", appInfo.getEmail());

        new ServletPostAsyncTask(this).execute(aMap);


    }

    public class PrefsElement{

        public String title;
        public String subtitle;
        public Intent intent;

        PrefsElement(){};

        PrefsElement(String t, String s, Intent i){
            title = t;
            subtitle = s;
            intent = i;
        }
    }

    private class PrefsAdapter extends ArrayAdapter<PreferredLots.PrefsElement> {

        Context context;
        int resource;

        public PrefsAdapter(Context c, int r, List<PreferredLots.PrefsElement> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout newView;
            PreferredLots.PrefsElement ele = getItem(position);

            if(convertView == null){
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            }else{
                newView = (LinearLayout) convertView;
            }

            TextView t = (TextView) newView.findViewById(R.id.prefListTitle);
            TextView s = (TextView) newView.findViewById(R.id.prefListSubtitle);

            t.setText(ele.title);
            s.setText(ele.subtitle);

            return newView;
        }

    }

    @Override
    public void processFinish(String result) {

        PrefsElement p = new PrefsElement();

        if(!result.contentEquals("empty list")) {

            boolean notDone = true;
            int cursorF = 0;
            int cursorB = 0;

            while (notDone) {
                p.title = "";
                while (result.charAt(cursorB) != '|') {
                    cursorB++;
                }
                p.title += result.substring(cursorF, cursorB);

                cursorF = cursorB;
                while (result.charAt(cursorB) != ';') {
                    cursorB++;
                }
                p.title += " : " + result.substring(cursorF + 1, cursorB);

                cursorF = cursorB;
                p.subtitle = "";
                for (int i = 0; i < 3; i++) {
                    while (result.charAt(cursorB) != ',') {
                        cursorB++;
                    }
                    p.subtitle += result.substring(cursorF + 1, cursorB);
                    if (i != 2) {
                        p.subtitle += "\n";
                    }
                    cursorF = cursorB;
                    cursorB++;
                }

                prefList.add(p);

                if(result.charAt(cursorB) == '!'){
                    notDone = false;
                }
            }
        }else{
            p.title = "No preferences saved";
            p.subtitle = "Please use the add button at the bottom";

            prefList.add(p);
        }

        prefAdap.notifyDataSetChanged();

    }
}
