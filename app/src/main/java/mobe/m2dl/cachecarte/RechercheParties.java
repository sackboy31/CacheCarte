package mobe.m2dl.cachecarte;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;

public class RechercheParties extends AppCompatActivity {




    ListView listViewDetected;
    ListView listViewPaired;

    ArrayList<BluetoothDevice> arrayListBluetoothDevices;
    ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;

    ArrayList<String> arrayListdetected;
    ArrayList<String> arrayListpaired;

    BluetoothAdapter bluetoothAdapter = null;

    ArrayAdapter<String> pairedAdapter,detectedAdapter;

    BluetoothDevice bdDevice;

    ListItemDetected listItemDetected;
    ListItemPaired listItemPaired;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_parties);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayListdetected = new ArrayList<String>();
        arrayListpaired = new ArrayList<String>();

        detectedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        pairedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        detectedAdapter.notifyDataSetChanged();
        pairedAdapter.notifyDataSetChanged();

        listViewDetected = (ListView) findViewById(R.id.list_detected);
        listViewDetected.setAdapter(detectedAdapter);

        listViewPaired = (ListView) findViewById(R.id.list_paired);
        listViewPaired.setAdapter(pairedAdapter);

        arrayListBluetoothDevices = new ArrayList<>();
        arrayListPairedBluetoothDevices = new ArrayList<>();
        listItemDetected = new ListItemDetected();
        listItemPaired = new ListItemPaired();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if (!bluetoothAdapter.isEnabled())

        {
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        }

        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        listViewDetected.setOnItemClickListener(listItemDetected);
        listViewPaired.setOnItemClickListener(listItemPaired);
        this.registerReceiver(myReceiver, intentFilter); // If receive event


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)

        {
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        } else

        {
            bluetoothAdapter.startDiscovery();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(myReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION))
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    bluetoothAdapter.startDiscovery();
                } else {
                    Intent intentMainGame = new Intent(this, MainActivity.class);
                    startActivity(intentMainGame);
                }
        }

    }

    public void onClickCancel(View view) {
        finish();
    }

    private boolean isBonded( ) {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        boolean isbonded = false;
        if(pairedDevice.size()>0)
        {
            for(BluetoothDevice device : pairedDevice)
            {
                if(device.getAddress().equals(bdDevice.getAddress())){
                    isbonded = true;
                }
            }
        }

        return isbonded;

    }

    class ListItemDetected implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // TODO Auto-generated method stub
            bluetoothAdapter.cancelDiscovery();
            bdDevice = arrayListBluetoothDevices.get(position);
            Log.i("Log", "The dvice : "+bdDevice.toString());
            Boolean isBonded = isBonded();
            if(!isBonded){

                try {
                    isBonded = createBond();
                    if(isBonded)
                    {
                        beginPartie();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                beginPartie();
            }
        }
    }

    public void beginPartie( ){
        if(isBonded()){
            Propriété.setDevice(bdDevice);
            Intent intentJoinGame = new Intent(this, PlateauCarteActivity.class);
            startActivity(intentJoinGame);
        }
    }

    class ListItemPaired implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // TODO Auto-generated method stub
            bluetoothAdapter.cancelDiscovery();
            bdDevice = arrayListBluetoothDevices.get(position);
            Log.i("Log", "The dvice : "+bdDevice.toString());
            Boolean isBonded = isBonded();
            try {
                isBonded = createBond();
                if(isBonded)
                {
                    beginPartie();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }//connect(bdDevice);
            Log.i("Log", "The bond is created: "+isBonded);
        }
    }


    public boolean createBond( )
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(bdDevice);
        return returnValue.booleanValue();
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                Log.i("Log", "received event " + action);
                switch (action) {
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(arrayListBluetoothDevices.size()<1) // this checks if the size of bluetooth device is 0,then add the
                        {                                           // device to the arraylist.
                            detectedAdapter.add(device.getName()+"\n"+device.getAddress());
                            arrayListBluetoothDevices.add(device);
                            detectedAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                            for(int i = 0; i<arrayListBluetoothDevices.size();i++)
                            {
                                if(device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress()))
                                {
                                    flag = false;
                                }
                            }
                            if(flag == true)
                            {
                                detectedAdapter.add(device.getName()+"\n"+device.getAddress());
                                arrayListBluetoothDevices.add(device);
                                detectedAdapter.notifyDataSetChanged();
                            }
                        }
                        break;

                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                        progressBar.setVisibility(View.INVISIBLE);
                        pairedAdapter.notifyDataSetChanged();
                        break;
                }


        }


    };
}
