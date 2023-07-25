package bike.smarthalo.sdk.models;

/* loaded from: classes.dex */
public enum HardwareVersion {
  V1,
  V1_2,
  V2,
  Unknown;

  public String getStringValue() {
    switch (this) {
      case V1:
        return DeviceInformation.HARDWARE_VERSION_1;
      case V1_2:
        return DeviceInformation.HARDWARE_VERSION_1_2;
      case V2:
        return DeviceInformation.HARDWARE_VERSION_2;
      default:
        return "Unknown";
    }
  }

  public static HardwareVersion build(int i) {
    switch (i) {
      case 0:
        return V1;
      case 1:
        return V1_2;
      case 2:
        return V2;
      default:
        return Unknown;
    }
  }

  public static HardwareVersion blDeviceName(String str) {
    if (str.equalsIgnoreCase("SH_BL")) {
      return V1;
    }
    if (str.equalsIgnoreCase("SH2_BL")) {
      return V2;
    }
    return Unknown;
  }
}