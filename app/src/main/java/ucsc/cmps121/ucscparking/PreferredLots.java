package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PreferredLots extends AppCompatActivity {

    private ArrayList<PreferredLots.parkingLot> prefList;
    private PreferredLots.PrefsAdapter prefAdap;
    private Parking parking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_lots);

        parking = new Parking();
        parking.initialize();

        ListView myList = (ListView) findViewById(R.id.parkingLotPrefs);
        prefList = new ArrayList<>();
        prefAdap = new PreferredLots.PrefsAdapter(this, R.layout.parking_prefs_row, prefList);
        myList.setAdapter(prefAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreferredLots.parkingLot ele = prefList.get(position);
                Intent intent = ele.intent;
                startActivity(intent);
            }
        });

        /*
            ADD NEW LIST ITEMS HERE (order matters)
         */
        PreferredLots.parkingLot ele;
        Parking.lot other;
        for(int k = 0; k < parking.getSize(); k++) {
            other = parking.grabData(k);

            // Copy this block and change for new list item
            ele = new PreferredLots.parkingLot();
            ele.name = other.name;
            ele.location = other.location;
            ele.capacity = other.capacity;
            ele.available = other.available;
            ele.intent = new Intent(this, PreferredLots.class);

            // Will likely add extras
            prefList.add(ele);
        }
        prefAdap.notifyDataSetChanged();
    }

    private class parkingLot {
        public String name;
        public String location;
        public int capacity;
        public int available;
        public Intent intent;

        parkingLot(){};

        parkingLot(String _name, String _location, int _capacity, int _available, Intent _intent){
            name = _name;
            location = _location;
            capacity = _capacity;
            available = _available;
            intent = _intent;
        }
    }

    private class PrefsAdapter extends ArrayAdapter<PreferredLots.parkingLot> {

        Context context;
        int resource;

        public PrefsAdapter(Context c, int r, List<PreferredLots.parkingLot> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout newView;
            PreferredLots.parkingLot ele = getItem(position);

            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            TextView t = (TextView) newView.findViewById(R.id.name);
            TextView s = (TextView) newView.findViewById(R.id.location);
            t.setText(ele.name);
            s.setText(ele.location);

            Button dlt_button = (Button) newView.findViewById(R.id.delete_btn);
            dlt_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DO THINGS HERE
                    prefList.remove(position);
                    prefAdap.notifyDataSetChanged();
                }
            });


            return newView;
        }
    }
}