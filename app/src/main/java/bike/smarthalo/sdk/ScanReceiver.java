package bike.smarthalo.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import bike.smarthalo.sdk.serviceStorage.ServiceStorageController;

public class ScanReceiver extends BroadcastReceiver {
  public void onReceive(Context paramContext, Intent paramIntent) {
    BluetoothManager bluetoothManager = (BluetoothManager)paramContext.getSystemService("bluetooth");
    if (bluetoothManager != null && SHSdkHelpers.checkStartServicePermissions(paramContext)) {
      BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
      ServiceStorageController serviceStorageController = ServiceStorageController.getInstance(paramContext);
      if (SHSdkHelpers.isAtLeastOreo() && bluetoothAdapter.isEnabled()) {
        ArrayList<ScanResult> arrayList = paramIntent.getParcelableArrayListExtra("android.bluetooth.le.extra.LIST_SCAN_RESULT");
        if (arrayList != null && arrayList.size() > 0 && ((ScanResult)arrayList.get(0)).getDevice().getAddress().equals(serviceStorageController.address)) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Found device ");
          stringBuilder.append(((ScanResult)arrayList.get(0)).getDevice().getAddress());
          Log.i("ScanReceiver", stringBuilder.toString());
          SHDeviceServiceStartHelper.connectToDiscoveredDevice(paramContext);
        } 
      } 
    } 
  }
}
