package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainMenu extends AppCompatActivity  implements View.OnClickListener{

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mAuth = FirebaseAuth.getInstance();
        mStatusTextView = (TextView) findViewById(R.id.textView);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        FirebaseUser user = mAuth.getCurrentUser();
        mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }

    public void goAccountPrefs(View v){
        Intent intent = new Intent(this, AccountPrefs.class);
        startActivity(intent);
    }

    public void goMapList(View v){
        Intent intent = new Intent(this, MapList.class);
        startActivity(intent);
    }

    public void goFindLots(View v){
        Intent intent = new Intent(this, FindLots.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.sign_out_button) {
            signOut();
        }
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        updateUI(null);
                    }
                });
    }
    public  void updateUI(FirebaseUser user) {
       // hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
           /* mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));*/
            DBHandler db = new DBHandler(this);
            AccountInfo accountInfo = new AccountInfo(user.getUid(), user.getEmail());
            db.addAccount(accountInfo);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("accounts");
            myRef.child(user.getUid()).setValue(user.getEmail());

            // myRef.setValue(user.getEmail());

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

}
