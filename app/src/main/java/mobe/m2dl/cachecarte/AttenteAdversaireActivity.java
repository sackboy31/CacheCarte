package mobe.m2dl.cachecarte;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AttenteAdversaireActivity extends AppCompatActivity {


    BluetoothAdapter bluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attente_adversaire);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!bluetoothAdapter.isEnabled())

        {
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            sendIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION))
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendIntent();
                }else{
                    Intent intentMainGame = new Intent(this, MainActivity.class);
                    startActivity(intentMainGame);
                }
        }
    }

   private void sendIntent() {
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        startActivity(discoverableIntent);
        final String CUSTOM_INTENT = "mobe.m2dl.cachecarte.WAITING";
        Intent intent = new Intent(this, OutgoingReceiver.class);
        intent.setAction(CUSTOM_INTENT);
        intent.putExtra("partie_name", "badasspartie");
        intent.putExtra("pseudo", "KikouDU69");
        Log.e("Log","SendIntend" + intent.toString());
        sendBroadcast(intent);

    }

    private class OutgoingReceiver extends BroadcastReceiver {

        public static final String CUSTOM_INTENT = "mobe.m2dl.cachecarte.OK";

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Log.i("Log", "received event " + action);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    break;
            }
        }
    }
    ;

    public void onClickCancel(View view) {
        finish();
    }
}
