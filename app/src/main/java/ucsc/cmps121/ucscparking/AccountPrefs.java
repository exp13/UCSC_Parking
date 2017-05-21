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

import static java.security.AccessController.getContext;

public class AccountPrefs extends AppCompatActivity {

    private ArrayList<PrefsElement> prefList;
    private PrefsAdapter prefAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_prefs);

        ListView myList = (ListView) findViewById(R.id.accPrefList);
        prefList = new ArrayList<PrefsElement>();
        prefAdap = new PrefsAdapter(this, R.layout.account_prefs_row, prefList);
        myList.setAdapter(prefAdap);

        final Context context = this;
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrefsElement ele = prefList.get(position);
                Intent intent = ele.intent;
                startActivity(intent);
            }
        });

        /*
            ADD NEW LIST ITEMS HERE (order matters)
         */
        PrefsElement ele;

        // Copy this block and change for new list item
        ele = new PrefsElement();
        ele.title = "Preferred Lots Schedule";
        ele.subtitle = "set up your preferred lot times";
        ele.intent = new Intent(this, PreferredLots.class);
        prefList.add(ele);

        ele = new PrefsElement();
        ele.title = "Payment Information";
        ele.subtitle = "set up or edit your payment preferences";
        ele.intent = new Intent(this, PaymentSettings.class);
        prefList.add(ele);

        ele = new PrefsElement();
        ele.title = "Payment History";
        ele.subtitle = "view your recent payments";
        ele.intent = new Intent(this, PaymentHistory.class);
        prefList.add(ele);

        prefAdap.notifyDataSetChanged();
    }

    private class PrefsElement{

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

    private class PrefsAdapter extends ArrayAdapter<PrefsElement> {

        Context context;
        int resource;

        public PrefsAdapter(Context c, int r, List<PrefsElement> news){
            super(c, r, news);
            context = c;
            resource = r;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;
            PrefsElement ele = getItem(position);

            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            TextView t = (TextView) newView.findViewById(R.id.accTitle);
            TextView s = (TextView) newView.findViewById(R.id.accSubtitle);
            t.setText(ele.title);
            s.setText(ele.subtitle);

            return newView;
        }
    }
}
