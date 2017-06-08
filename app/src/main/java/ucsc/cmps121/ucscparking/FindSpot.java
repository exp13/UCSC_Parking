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

public class FindSpot extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse {
    
    private AppInfo appInfo;
    private ArrayList<PrefsElement> prefList;
    private PrefsAdapter prefAdap;
    private int packetCounter;
    private PrefsElement selectedEle;
    private Map<String, String> map1;
    private Map<String, String> map2;
    private Map<String, String> map3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_spot);

        appInfo = AppInfo.getInstance(this);

        packetCounter = 0;

        ListView myList = (ListView) findViewById(R.id.prefListView);
        prefList = new ArrayList<>();
        prefAdap = new PrefsAdapter(this, R.layout.pref_list_row, prefList);
        myList.setAdapter(prefAdap);


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEle = prefList.get(position);

                map1 = new HashMap<>();
                map2 = new HashMap<>();
                map3 = new HashMap<>();

                map1.put("func", "GetParkingLot");
                map2.put("func", "GetParkingLot");
                map3.put("func", "GetParkingLot");

                map1.put("lotid", selectedEle.prefLots.get(0));
                map2.put("lotid", selectedEle.prefLots.get(1));
                map3.put("lotid", selectedEle.prefLots.get(2));

                checkLots(1);

            }
        });

        Map<String, String> aMap = new HashMap<>();

        aMap.put("func", "GetLots");
        aMap.put("userid", appInfo.getEmail());

        new ServletPostAsyncTask(this).execute(aMap);

    }

    private void checkLots(int mapNum){
        switch(mapNum){
            case 1:
                new ServletPostAsyncTask(this).execute(map1);
                break;
            case 2:
                new ServletPostAsyncTask(this).execute(map2);
                break;
            case 3:
                new ServletPostAsyncTask(this).execute(map3);
                break;
        }
    }

    public class PrefsElement{

        public String title;
        public String subtitle;
        public Intent intent;
        public int lot1Count;
        public int lot2Count;
        public int lot3Count;
        public ArrayList<String> prefLots;

        PrefsElement(){};

        PrefsElement(String t, String s, Intent i){
            title = t;
            subtitle = s;
            intent = i;
        }
    }

    private class PrefsAdapter extends ArrayAdapter<PrefsElement> {

        Context context;
        int resource;

        public PrefsAdapter(Context c, int r, List<PrefsElement> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout newView;
            PrefsElement ele = getItem(position);

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

        switch (packetCounter) {

            case 0:

                PrefsElement p;

                if (!result.contentEquals("empty list")) {
                    boolean notDone = true;
                    int cursorF = 0;
                    int cursorB = 0;
                    String s = new String();

                    while (notDone) {
                        p = new PrefsElement();
                        p.prefLots = new ArrayList<>();
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
                            switch (i) {
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
                            p.prefLots.add(s);
                            if (i != 2) {
                                p.subtitle += "\n";
                            }
                            cursorF = cursorB;
                            cursorB++;
                        }

                        prefList.add(p);

                        if (result.charAt(cursorB) == '!') {
                            notDone = false;
                        } else {
                            cursorF++;
                        }
                    }
                } else {
                    p = new PrefsElement();
                    p.title = "No preferences saved";
                    p.subtitle = "Please use the add button at the bottom";
                    p.intent = new Intent(this, this.getClass());

                    prefList.add(p);
                }

                prefAdap.notifyDataSetChanged();
                break;

            case 1:
                setLotNum(1, result);
                checkLots(2);
                break;
            case 2:
                setLotNum(2, result);
                checkLots(3);
                break;
            case 3:
                setLotNum(3, result);

                Intent intent = new Intent(this, ConfirmSpot.class);

                if (selectedEle.lot1Count == 0 && selectedEle.lot2Count == 0 && selectedEle.lot3Count == 0) {

                    intent.putExtra("lot_name", "none");
                }
                else {

                    if (selectedEle.lot1Count != 0) {
                        intent.putExtra("lot_name", selectedEle.prefLots.get(0));
                        intent.putExtra("spot_count", Integer.toString(selectedEle.lot1Count));
                    }
                    else if (selectedEle.lot2Count != 0) {
                        intent.putExtra("lot_name", selectedEle.prefLots.get(1));
                        intent.putExtra("spot_count", Integer.toString(selectedEle.lot2Count));
                    }
                    else if (selectedEle.lot3Count != 0) {
                        intent.putExtra("lot_name", selectedEle.prefLots.get(2));
                        intent.putExtra("spot_count", Integer.toString(selectedEle.lot3Count));
                    }
                }

                startActivity(intent);
                break;
        }

        packetCounter++;

    }

    private void setLotNum(int id, String result) {

        int cursorB = 0;

        while(result.charAt(cursorB)!='!') {cursorB++;}

        int value = Integer.parseInt(result.substring(0, cursorB));

        switch(id) {

            case 1:
                selectedEle.lot1Count = value;
                break;
            case 2:
                selectedEle.lot2Count = value;
                break;
            case 3:
                selectedEle.lot3Count = value;
                break;

        }
    }


}
