package mobe.m2dl.cachecarte;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Sacky on 17/03/2017.
 */

public class Propriété {
    private static BluetoothDevice device;

    public static void setDevice(BluetoothDevice d){
        device = d;
    }

    public  static BluetoothDevice getDevice(){
        return device;
    }
}
