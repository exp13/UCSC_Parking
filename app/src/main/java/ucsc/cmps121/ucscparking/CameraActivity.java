package ucsc.cmps121.ucscparking;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.text.Text;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    private boolean firstPic;
    private TextView saveText;
    private Button saveButton;
    private TextView camT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camT = (TextView) findViewById(R.id.camText);
        saveText = (TextView) findViewById(R.id.saveConfirmText);
        saveButton = (Button) findViewById(R.id.saveBut);

        saveText.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        firstPic = true;

        findViewById(R.id.camBut).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.camBut) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            intent.putExtra(OcrCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    camT.setText(text);
                    Log.d(TAG, "Text read: " + text);

                    if (firstPic) {
                        saveText.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                        firstPic = false;
                    }


                } else {
                    System.out.println("OCR done goofed");
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                camT.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveButOnClick(View v) {


    }
}
