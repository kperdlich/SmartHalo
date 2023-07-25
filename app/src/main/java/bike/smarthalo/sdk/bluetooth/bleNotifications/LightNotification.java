package bike.smarthalo.sdk.bluetooth.bleNotifications;

public class LightNotification {
  public boolean isLightOn = false;
  
  public boolean isLightSettingOn = false;
  
  public Boolean isToggleFromTouch = Boolean.valueOf(false);
  
  public LightNotification(boolean paramBoolean1, Boolean paramBoolean, boolean paramBoolean2) {
    this.isLightOn = paramBoolean1;
    this.isToggleFromTouch = paramBoolean;
    this.isLightSettingOn = paramBoolean2;
  }
}
