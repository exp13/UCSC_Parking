package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static java.security.AccessController.getContext;

public class AccountPrefs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_prefs);
    }

    private class PrefsElement{

        public String title;
        public String subtitle;
        public Object activity;

        PrefsElement(){};

        PrefsElement(String t, String s, Object a){
            title = t;
            subtitle = s;
            activity = a;
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

            TextView t = (TextView) newView.findViewById(R.id.accTitle);
            TextView s = (TextView) newView.findViewById(R.id.accSubtitle);
            t.setText(ele.title);
            s.setText(ele.subtitle);

            return newView;
        }

    }
}
