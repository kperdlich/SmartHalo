package bike.smarthalo.sdk;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.ArrayUtils;

public class SHSdkHelpers {
  public static final int REQUEST_BLE_PERMISSION = 100;
  
  public static final String TAG = "SHSdkHelpers";
  
  public static final String[] blePermissions;
  
  protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
  
  public static final String[] storagePermissions = new String[] { "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE" };
  
  static {
    blePermissions = new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN" };
  }
  
  public static String bytesToHex(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return ""; 
    char[] arrayOfChar = new char[paramArrayOfbyte.length * 2];
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      int i = paramArrayOfbyte[b] & 0xFF;
      int j = b * 2;
      char[] arrayOfChar1 = hexArray;
      arrayOfChar[j] = (char)arrayOfChar1[i >>> 4];
      arrayOfChar[j + 1] = (char)arrayOfChar1[i & 0xF];
    } 
    return new String(arrayOfChar);
  }
  
  public static boolean checkPermission(Context paramContext, String paramString) {
    boolean bool;
    if (ContextCompat.checkSelfPermission(paramContext, paramString) == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean checkPermissions(Context paramContext, String[] paramArrayOfString) {
    if (paramArrayOfString == null || paramArrayOfString.length == 0)
      return true; 
    for (byte b = 0; b < paramArrayOfString.length; b++) {
      if (!checkPermission(paramContext, paramArrayOfString[b]))
        return false; 
    } 
    return true;
  }
  
  public static boolean checkStartServicePermissions(Context paramContext) {
    String[] arrayOfString1 = getBlePermissions();
    String[] arrayOfString2 = arrayOfString1;
    if (Build.VERSION.SDK_INT >= 29)
      arrayOfString2 = ArrayUtils.<String>add(arrayOfString1, "android.permission.ACCESS_BACKGROUND_LOCATION"); 
    return checkPermissions(paramContext, arrayOfString2);
  }
  
  public static String[] getBlePermissions() {
    String[] arrayOfString;
    if (SHSdkBuildConfigHelper.isReleaseBuildConfig()) {
      arrayOfString = blePermissions;
    } else {
      arrayOfString = ArrayUtils.<String>addAll(blePermissions, storagePermissions);
    } 
    return arrayOfString;
  }
  
  public static Integer getNumericFirmwareVersion(String paramString) {
    String[] arrayOfString = paramString.split("\\.");
    paramString = "";
    for (byte b = 0; b < arrayOfString.length; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(arrayOfString[b]);
      paramString = stringBuilder.toString();
    } 
    return Integer.valueOf(paramString);
  }
  
  public static boolean hasRequiredFeatures(Context paramContext) {
    boolean bool;
    PackageManager packageManager = paramContext.getPackageManager();
    if (packageManager != null && packageManager.hasSystemFeature("android.hardware.bluetooth_le")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isAtLeastNougat() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 24) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isAtLeastOreo() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 26) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isBluetoothEnabled(Context paramContext) {
    boolean bool;
    BluetoothManager bluetoothManager = (BluetoothManager)paramContext.getSystemService("bluetooth");
    if (bluetoothManager != null && bluetoothManager.getAdapter() != null && bluetoothManager.getAdapter().isEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isLocationEnabled(Context paramContext) {
    boolean bool;
    LocationManager locationManager = (LocationManager)paramContext.getSystemService("location");
    if (locationManager != null && (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network"))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static void requestPermissions(final Activity activity, final String[] permissions, final int PermissionRequest, int paramInt2) {
    if (checkPermissions((Context)activity, permissions)) {
      startScanningAndFinishActivity(activity);
      return;
    } 
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
      AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity, R.style.AlertDialogStyle);
      builder.setMessage(paramInt2).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              ActivityCompat.requestPermissions(activity, permissions, PermissionRequest);
            }
          }).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              SHSdkHelpers.startScanningAndFinishActivity(activity);
            }
          });
      builder.create().show();
    } else {
      ActivityCompat.requestPermissions(activity, permissions, PermissionRequest);
    } 
  }
  
  public static void startScanningAndFinishActivity(final Activity activity) {
    SHDeviceServiceStartHelper.requestStartScanning((Context)activity);
    (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
          public void run() {
            activity.finish();
          }
        },  350L);
  }
  
  public static boolean stringHasValue(String paramString) {
    boolean bool;
    if (paramString != null && !paramString.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}
