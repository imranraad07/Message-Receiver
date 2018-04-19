package example.com.messageretrieve;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasReadSmsPermission()) {
            requestReadAndSendSmsPermission();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textViewSender = MainActivity.this.findViewById(R.id.textViewSender);
        textViewSender.setText(sharedPreferences.getString("Sender", "Sender.."));

        TextView textViewMessage = MainActivity.this.findViewById(R.id.textViewMessage);
        textViewMessage.setText(sharedPreferences.getString("Message", "Receiver.."));

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String sender, String messageText) {
                Log.e("Message", messageText);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Sender", sender);
                editor.putString("Message", messageText);
                editor.commit();

                TextView textViewSender = MainActivity.this.findViewById(R.id.textViewSender);
                textViewSender.setText(sharedPreferences.getString("Sender", ""));

                TextView textViewMessage = MainActivity.this.findViewById(R.id.textViewMessage);
                textViewMessage.setText(sharedPreferences.getString("Message", ""));
            }
        });
    }

    /**
     * Runtime permission shenanigans
     */
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d("Message", "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_CODE);
    }

}
