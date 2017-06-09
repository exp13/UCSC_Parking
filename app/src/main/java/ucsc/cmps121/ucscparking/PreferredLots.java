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

    public void onAddClick(View v){
        Intent intent = new Intent(this, AddPref.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String result) {

        PrefsElement p;

        if(!result.contentEquals("empty list")) {
            boolean notDone = true;
            int cursorF = 0;
            int cursorB = 0;
            String s = new String();

            while (notDone) {
                p = new PrefsElement();
                p.intent = new Intent(this, EditPref.class);
                p.title = "";
                while (result.charAt(cursorB) != '|') {
                    cursorB++;
                }
                s = result.substring(cursorF, cursorB);
                p.intent.putExtra("time", s);
                p.title += s;

                cursorF = cursorB;
                while (result.charAt(cursorB) != ';') {
                    cursorB++;
                }
                s = result.substring(cursorF + 1, cursorB);
                p.intent.putExtra("name", s);
                p.title += " : " + s;

                cursorF = cursorB;
                p.subtitle = "";
                for (int i = 0; i < 3; i++) {
                    while (result.charAt(cursorB) != ',') {
                        cursorB++;
                    }
                    s = result.substring(cursorF + 1, cursorB);
                    // add lots to intent extras for editing
                    switch(i){
                        case 0:
                            p.intent.putExtra("lot1", s);
                            break;
                        case 1:
                            p.intent.putExtra("lot2", s);
                            break;
                        case 2:
                            p.intent.putExtra("lot3", s);
                            break;
                    }
                    p.subtitle += s;
                    if (i != 2) {
                        p.subtitle += "\n";
                    }
                    cursorF = cursorB;
                    cursorB++;
                }

                prefList.add(p);

                if(result.charAt(cursorB) == '!'){
                    notDone = false;
                }else{
                    cursorF++;
                }
            }
        }else{
            p = new PrefsElement();
            p.title = "No preferences saved";
            p.subtitle = "Please use the add button at the bottom";
            p.intent = new Intent(this, this.getClass());

            prefList.add(p);
        }

        prefAdap.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountPrefs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
