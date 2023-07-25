package bike.smarthalo.sdk.models;

public class DeviceSerials {
  public String lock;
  
  public String pcba;
  
  public String product;
  
  public DeviceSerials(String paramString) {
    this.product = paramString;
    this.pcba = null;
    this.lock = null;
  }
  
  public DeviceSerials(String paramString1, String paramString2) {
    this.product = paramString1;
    this.pcba = paramString2;
    this.lock = null;
  }
  
  public DeviceSerials(String paramString1, String paramString2, String paramString3) {
    this.product = paramString1;
    this.pcba = paramString2;
    this.lock = paramString3;
  }
  
  public static interface DeviceSerialsInterface {
    void onDeviceSerialsReady(DeviceSerials param1DeviceSerials);
  }
}
