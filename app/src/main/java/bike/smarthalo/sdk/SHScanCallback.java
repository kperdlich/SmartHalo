package bike.smarthalo.sdk;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.util.SparseArray;

import java.util.Arrays;

import bike.smarthalo.sdk.models.DeviceModel;

public class SHScanCallback extends ScanCallback {
  private ScanCallbackContract callbackContract;
  
  public SHScanCallback(ScanCallbackContract paramScanCallbackContract) {
    this.callbackContract = paramScanCallbackContract;
  }
  
  public void onScanFailed(int paramInt) {
    String str = SHScanCallback.class.getSimpleName();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onScanFailed: ");
    stringBuilder.append(paramInt);
    Log.i(str, stringBuilder.toString());
  }
  
  public void onScanResult(int paramInt, ScanResult paramScanResult) {
    ScanRecord scanRecord = paramScanResult.getScanRecord();
    if (scanRecord != null) {
      SparseArray sparseArray = scanRecord.getManufacturerSpecificData();
      if (sparseArray != null) {
        byte[] arrayOfByte = (byte[])sparseArray.get(1126);
        if (arrayOfByte != null) {
          boolean bool;
          byte[] arrayOfByte1 = Arrays.copyOfRange(arrayOfByte, 2, arrayOfByte.length);
          DeviceModel deviceModel = DeviceModel.fromAdvertisementData(arrayOfByte[1]);
          String str = SHSdkHelpers.bytesToHex(arrayOfByte1);
          if (arrayOfByte[1] == 1) {
            bool = true;
          } else {
            bool = false;
          } 
          BluetoothDevice bluetoothDevice = paramScanResult.getDevice();
          this.callbackContract.handleDeviceFound(bluetoothDevice, str, paramScanResult.getScanRecord().getDeviceName(), paramScanResult.getRssi(), bool, deviceModel);
        } 
      } 
    } 
  }
  
  public static interface ScanCallbackContract {
    void handleDeviceFound(BluetoothDevice param1BluetoothDevice, String param1String1, String param1String2, int param1Int, boolean param1Boolean, DeviceModel param1DeviceModel);
  }
}
