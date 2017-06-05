package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
            GoogleApiClient.ConnectionCallbacks,
            View.OnClickListener,
            ServletPostAsyncTask.AsyncResponse
{

    private GoogleApiClient mGoogleApiClient;
    private final Context context = this;
    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appInfo = AppInfo.getInstance(this);

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

       findViewById(R.id.sign_in_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.sign_in_button:
                signIn();
                break;
        }

    }

    public void goMainMenu(View v)
    {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        System.out.print("tough shit\n");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            TextView resultView = (TextView) findViewById(R.id.googleAccountView);
            String emailChk = acct.getEmail();
            // Check if account us a ucsc Blue account and sign out if it isnt
            if(!emailChk.contains("ucsc.edu")){
                resultView.setText("Please log in with a UCSC Blue account");
                sign_out();
            }else {
                appInfo.setEmail(emailChk);

                Map<String, String> aMap = new HashMap<>();
                aMap.put("func", "CheckUserExists");
                aMap.put("userid", emailChk);

                new ServletPostAsyncTask(this).execute(aMap);

            }
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    private void sign_out() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            Toast.makeText(context, status.getStatusMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else{

            mGoogleApiClient.connect();
        }
    }

    @Override
    public void processFinish(String result) {
        Intent intent;
        if (result.contentEquals("true")){
            intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        }else if(result.contentEquals("false")){
            intent = new Intent(this, PreferredLots.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "salt"+result+"moresalt", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        sign_out();
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

}

