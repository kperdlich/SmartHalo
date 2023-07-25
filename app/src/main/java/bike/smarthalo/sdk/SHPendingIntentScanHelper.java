package bike.smarthalo.sdk;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

@TargetApi(26)
public class SHPendingIntentScanHelper {
  private static final int DEVICE_PENDING_INTENT_CODE = 0;
  
  private static final String TAG = "SHPendingIntentScanHelper";
  
  public static PendingIntent getScanIntent(@NonNull Context paramContext) {
    return PendingIntent.getBroadcast(paramContext, 0, new Intent(paramContext, ScanReceiver.class), 134217728);
  }
  
  public static void startPendingIntentScan(Context paramContext, BluetoothAdapter paramBluetoothAdapter, String paramString) {
    if (SHSdkHelpers.isAtLeastOreo() && paramContext != null && SHSdkHelpers.checkStartServicePermissions(paramContext) && paramBluetoothAdapter != null && paramBluetoothAdapter.isEnabled() && paramString != null && !paramString.isEmpty()) {
      String str = TAG;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Starting pending intent scan for device ");
      stringBuilder.append(paramString);
      Log.i(str, stringBuilder.toString());
      ScanSettings scanSettings = (new ScanSettings.Builder()).setScanMode(1).build();
      ArrayList<ScanFilter> arrayList = new ArrayList();
      arrayList.add((new ScanFilter.Builder()).setDeviceAddress(paramString).build());
      paramBluetoothAdapter.getBluetoothLeScanner().startScan(arrayList, scanSettings, getScanIntent(paramContext));
    } 
  }
}
