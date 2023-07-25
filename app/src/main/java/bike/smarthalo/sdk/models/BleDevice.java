package bike.smarthalo.sdk.models;

import bike.smarthalo.sdk.bluetooth.BleHelper;

public class BleDevice {
  private static final String DEFAULT_DEVICE_NAME = "Smarthal";
  
  public String address;
  
  public DeviceModel deviceModel;
  
  public String id;
  
  public String name;
  
  public Integer rssi;
  
  public Long timestamp;
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof BleDevice;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramObject = paramObject;
      bool2 = bool1;
      if (this.address.equals(((BleDevice)paramObject).address)) {
        bool2 = bool1;
        if (this.id.equals(((BleDevice)paramObject).id))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public String getBootloaderAddress() {
    return BleHelper.getBootloaderAddress(this.address);
  }
  
  public String getDisplayText() {
    String str1 = this.name;
    String str2 = this.deviceModel.getSimpleValue();
    String str3 = this.id;
    return String.format("%1$s %2$s (%3$s)", new Object[] { str1, str2, str3.substring(str3.length() - 4) });
  }
  
  public String getShortenedDeviceId() {
    String str = this.id;
    return str.substring(str.length() - 4);
  }
  
  public boolean isDeviceOwned() {
    return this.name.equalsIgnoreCase("Smarthal") ^ true;
  }
}
