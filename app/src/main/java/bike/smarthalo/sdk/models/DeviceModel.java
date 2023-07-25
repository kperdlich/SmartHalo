package bike.smarthalo.sdk.models;

/* loaded from: classes.dex */
public enum DeviceModel {
  SH1,
  SH2,
  Unknown;

  public static final byte BOOTLOADER_ADVERTISEMENT_DATA = 1;
  public static final String SMARTHALO_1 = "1";
  public static final byte SMARTHALO_1_ADVERTISEMENT_DATA = 0;
  public static final String SMARTHALO_2 = "2";
  public static final byte SMARTHALO_2_ADVERTISEMENT_DATA = 2;

  public static DeviceModel fromHardwareVersion(HardwareVersion hardwareVersion) {
    switch (hardwareVersion) {
      case V1:
      case V1_2:
        return SH1;
      case V2:
        return SH2;
      default:
        return Unknown;
    }
  }

  /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
  public static DeviceModel fromSavedData(String str) {
    char c;
    switch (str.hashCode()) {
      case 49:
        if (str.equals("1")) {
          c = 0;
          break;
        }
        c = 65535;
        break;
      case 50:
        if (str.equals("2")) {
          c = 1;
          break;
        }
        c = 65535;
        break;
      default:
        c = 65535;
        break;
    }
    switch (c) {
      case 0:
        return SH1;
      case 1:
        return SH2;
      default:
        return Unknown;
    }
  }

  public static DeviceModel fromAdvertisementData(byte b) {
    if (b != 0) {
      if (b == 2) {
        return SH2;
      }
      return Unknown;
    }
    return SH1;
  }

  public String getSimpleValue() {
    switch (this) {
      case SH1:
        return "1";
      case SH2:
        return "2";
      default:
        return "";
    }
  }

  public static DeviceModel geDeviceModel(byte[] bArr) {
    if (bArr.length > 9) {
      return fromHardwareVersion(HardwareVersion.build(bArr[9]));
    }
    return Unknown;
  }
}