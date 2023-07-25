package bike.smarthalo.sdk.bluetooth;

import java.util.Comparator;

import bike.smarthalo.sdk.models.BleDevice;

public class DeviceComparator implements Comparator<BleDevice> {
  public int compare(BleDevice paramBleDevice1, BleDevice paramBleDevice2) {
    Integer integer = paramBleDevice1.rssi;
    byte b = -1;
    if (integer == null && paramBleDevice2.rssi == null) {
      b = 0;
    } else if (paramBleDevice1.rssi == null) {
      b = 1;
    } else if (paramBleDevice2.rssi != null && paramBleDevice1.rssi.intValue() <= paramBleDevice2.rssi.intValue()) {
      if (paramBleDevice1.rssi.intValue() < paramBleDevice2.rssi.intValue()) {
        b = 1;
      } else {
        b = 0;
      } 
    } 
    return b;
  }
}
