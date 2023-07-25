package bike.smarthalo.sdk;

import android.content.Context;
import android.content.Intent;

import bike.smarthalo.sdk.serviceStorage.ServiceStorageController;

public class SHDeviceServiceStartHelper {
  public static void connectToDiscoveredDevice(Context paramContext) {
    if (SHSdkHelpers.isAtLeastOreo()) {
      Intent intent = new Intent(paramContext, SHDeviceService.class);
      intent.setAction("bike.smarthalo.sdk.ACTION_CONNECT_TO_DISCOVERED_DEVICE");
      paramContext.startForegroundService(intent);
    } 
  }
  
  public static Intent getActiveScanForKnownDeviceIntent(Context paramContext) {
    Intent intent = new Intent(paramContext, SHDeviceService.class);
    intent.setAction("bike.smarthalo.sdk.ACTION_CONNECT_TO_KNOWN_DEVICE");
    return intent;
  }
  
  public static void requestActiveScanForKnownDevice(Context paramContext) {
    Intent intent = new Intent(paramContext, SHDeviceService.class);
    intent.setAction("bike.smarthalo.sdk.ACTION_ACTIVE_SCAN_FOR_KNOWN_DEVICE");
    paramContext.startService(intent);
  }
  
  public static void requestConnectToKnownDevice(Context paramContext) {
    paramContext.startService(getActiveScanForKnownDeviceIntent(paramContext));
  }
  
  public static void requestLogin(Context paramContext, String paramString1, String paramString2, boolean paramBoolean) {
    Intent intent = new Intent(paramContext, SHDeviceService.class);
    intent.setAction("bike.smarthalo.sdk.ACTION_LOGIN");
    if (paramString1 != null && paramString1.length() > 0)
      intent.putExtra("bike.smarthalo.sdk.EXTRA_PASSWORD", paramString1); 
    if (paramString2 != null && paramString2.length() > 0)
      intent.putExtra("bike.smarthalo.sdk.EXTRA_DEVICE_ID", paramString2); 
    intent.putExtra("bike.smarthalo.sdk.EXTRA_IS_TESTER", paramBoolean);
    paramContext.startService(intent);
  }
  
  public static void requestLogout(Context paramContext) {
    ServiceStorageController.getInstance(paramContext).logout();
  }
  
  public static void requestStartScanning(Context paramContext) {
    Intent intent = new Intent(paramContext, SHDeviceService.class);
    intent.setAction("bike.smarthalo.sdk.ACTION_START_SCAN");
    paramContext.startService(intent);
  }
  
  public static void requestStopScan(Context paramContext) {
    Intent intent = new Intent(paramContext, SHDeviceService.class);
    intent.setAction("bike.smarthalo.sdk.ACTION_STOP_SCAN");
    paramContext.startService(intent);
  }
}
