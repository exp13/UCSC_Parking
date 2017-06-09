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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;

import java.util.ArrayList;
import java.util.List;


public class AccountPrefs extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<PrefsElement> prefList;
    private PrefsAdapter prefAdap;
    private GoogleApiClient mGoogleApiClient;
    private final Context context = this;

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

        ele = new PrefsElement();
        ele.title = "Edit license plate";
        ele.subtitle = "With OCR!";
        ele.intent = new Intent(this, CameraActivity.class);
        ele.intent.putExtra("nextClass", "Acc");
        prefList.add(ele);

        prefAdap.notifyDataSetChanged();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    @Override
    public void onConnected(Bundle connectionHint) {
        sign_out(findViewById(R.id.button2));
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

    }
    public void sign_out(View v) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
        }else{

            mGoogleApiClient.connect();
        }
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

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
