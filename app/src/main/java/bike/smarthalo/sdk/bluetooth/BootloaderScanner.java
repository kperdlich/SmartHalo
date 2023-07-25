package bike.smarthalo.sdk.bluetooth;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import bike.smarthalo.sdk.SHPendingIntentScanHelper;
import bike.smarthalo.sdk.SHSdkHelpers;

@TargetApi(26)
public class BootloaderScanner extends BroadcastReceiver {
  private static final int BOOTLOADER_PENDING_INTENT_CODE = 1;
  
  private static final String TAG = "BootloaderScanner";
  
  private static PendingIntent getPendingIntent(Context paramContext) {
    return PendingIntent.getBroadcast(paramContext, 1, new Intent(paramContext, BootloaderScanner.class), 134217728);
  }
  
  public static void startPendingIntentScan(Context paramContext, BluetoothAdapter paramBluetoothAdapter, String paramString) {
    if (SHSdkHelpers.isAtLeastOreo() && paramContext != null && paramBluetoothAdapter != null && paramBluetoothAdapter.isEnabled() && paramString != null && !paramString.isEmpty()) {
      paramString = BleHelper.getBootloaderAddress(paramString);
      String str = TAG;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Starting pending intent scan for bootloader ");
      stringBuilder.append(paramString);
      Log.i(str, stringBuilder.toString());
      ScanSettings scanSettings = (new ScanSettings.Builder()).setScanMode(1).build();
      ArrayList<ScanFilter> arrayList = new ArrayList();
      arrayList.add((new ScanFilter.Builder()).setDeviceAddress(paramString).build());
      paramBluetoothAdapter.getBluetoothLeScanner().startScan(arrayList, scanSettings, getPendingIntent(paramContext));
    } 
  }
  
  public static void stopScan(Context paramContext, BluetoothAdapter paramBluetoothAdapter) {
    if (SHSdkHelpers.isAtLeastOreo() && paramBluetoothAdapter != null && paramBluetoothAdapter.isEnabled() && paramBluetoothAdapter.getBluetoothLeScanner() != null)
      paramBluetoothAdapter.getBluetoothLeScanner().stopScan(SHPendingIntentScanHelper.getScanIntent(paramContext)); 
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    BluetoothManager bluetoothManager = (BluetoothManager)paramContext.getSystemService("bluetooth");
    if (bluetoothManager != null) {
      BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
      if (SHSdkHelpers.isAtLeastOreo() && bluetoothAdapter.isEnabled()) {
        ArrayList<ScanResult> arrayList = paramIntent.getParcelableArrayListExtra("android.bluetooth.le.extra.LIST_SCAN_RESULT");
        if (arrayList != null && arrayList.size() > 0) {
          BluetoothDevice bluetoothDevice = ((ScanResult)arrayList.get(0)).getDevice();
          Intent intent = new Intent("bike.smarthalo.sdk.BROADCAST_SH_BL_ADDRESS");
          intent.putExtra("bike.smarthalo.sdk.EXTRA_BOOTLOADER_ADDRESS", bluetoothDevice.getAddress());
          intent.putExtra("bike.smarthalo.sdk.EXTRA_DEVICE_NAME", bluetoothDevice.getName());
          paramContext.sendBroadcast(intent);
        } 
      } 
    } 
  }
}
