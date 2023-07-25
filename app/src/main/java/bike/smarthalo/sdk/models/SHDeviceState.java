package bike.smarthalo.sdk.models;

public class SHDeviceState {
  private int chargeLevel;
  
  private double compassHeading;
  
  private boolean isCharging;
  
  private boolean isLightOn;
  
  private boolean isUsbPlugged;
  
  private double temperature;
  
  private boolean willMovementTriggerLight;
  
  private SHDeviceState(int paramInt, boolean paramBoolean1, double paramDouble1, boolean paramBoolean2, boolean paramBoolean3, double paramDouble2, boolean paramBoolean4) {
    this.chargeLevel = paramInt;
    this.isCharging = paramBoolean1;
    this.compassHeading = paramDouble1;
    this.willMovementTriggerLight = paramBoolean2;
    this.isLightOn = paramBoolean3;
    this.temperature = paramDouble2;
    this.isUsbPlugged = paramBoolean4;
  }
  
  public static SHDeviceState getSHDeviceState(byte[] paramArrayOfbyte) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    if (paramArrayOfbyte == null || paramArrayOfbyte.length < 8)
      return null; 
    byte b = paramArrayOfbyte[1];
    if (paramArrayOfbyte[2] == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    double d1 = (paramArrayOfbyte[3] & 0xFF00 | paramArrayOfbyte[4] & 0xFF);
    if (paramArrayOfbyte[5] == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (paramArrayOfbyte[6] == 1) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    double d2 = (paramArrayOfbyte[7] & 0xFF);
    if (paramArrayOfbyte.length >= 9 && paramArrayOfbyte[8] == 1) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    return new SHDeviceState(b, bool1, d1, bool2, bool3, d2, bool4);
  }
  
  public int getChargeLevel() {
    return this.chargeLevel;
  }
  
  public double getCompassHeading() {
    return this.compassHeading;
  }
  
  public boolean getIsCharging() {
    return this.isCharging;
  }
  
  public boolean getIsLightOn() {
    return this.isLightOn;
  }
  
  public boolean getIsUsbPlugged() {
    return this.isUsbPlugged;
  }
  
  public double getTemperature() {
    return this.temperature;
  }
  
  public boolean getWillMovementTriggerLight() {
    return this.willMovementTriggerLight;
  }
  
  public static interface DeviceStateInterface {
    void onDeviceStateReady(SHDeviceState param1SHDeviceState);
  }
}
