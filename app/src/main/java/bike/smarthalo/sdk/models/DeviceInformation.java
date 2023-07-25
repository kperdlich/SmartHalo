package bike.smarthalo.sdk.models;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class DeviceInformation {
  public static final String ACC_VERSION_0 = "CTR";
  
  public static final String ACC_VERSION_1 = "AGR";
  
  public static final String ACC_VERSION_UNKNOWN = "Unknown";
  
  public static final String HARDWARE_VERSION_1 = "1.0/1.1";
  
  public static final String HARDWARE_VERSION_1_2 = "1.2";
  
  public static final String HARDWARE_VERSION_2 = "2.0";
  
  public static final int HARDWARE_VERSION_INDEX = 9;
  
  public static final String HARDWARE_VERSION_UNKNOWN = "Unknown";
  
  public String accelerometer;
  
  @Nullable
  public DeviceSerials deviceSerials;
  
  public HardwareVersion hardwareVersion;
  
  public String nordicBootloaderVersion;
  
  public String nordicFirmwareVersion;
  
  public String stmBootloaderVersion;
  
  public String stmFirmwareVersion;
  
  public DeviceInformation(String paramString1, String paramString2, String paramString3, String paramString4, HardwareVersion paramHardwareVersion, String paramString5, @Nullable DeviceSerials paramDeviceSerials) {
    this.nordicFirmwareVersion = paramString1;
    this.nordicBootloaderVersion = paramString2;
    this.stmFirmwareVersion = paramString3;
    this.stmBootloaderVersion = paramString4;
    paramString1 = paramString5;
    if (paramString5 == null)
      paramString1 = "Null"; 
    this.accelerometer = paramString1;
    this.deviceSerials = paramDeviceSerials;
    this.hardwareVersion = paramHardwareVersion;
  }
  
  public static DeviceInformation build(byte[] paramArrayOfbyte, DeviceSerials paramDeviceSerials) {
    HardwareVersion hardwareVersion1;
    String str3;
    String str4;
    String str5;
    if (paramArrayOfbyte.length < 9)
      return null; 
    String str1 = makeFirmwareVersionString(Arrays.copyOfRange(paramArrayOfbyte, 1, 5));
    String str2 = makeFirmwareVersionString(Arrays.copyOfRange(paramArrayOfbyte, 5, 9));
    HardwareVersion hardwareVersion2 = HardwareVersion.Unknown;
    if (paramArrayOfbyte.length >= 19) {
      str3 = makeFirmwareVersionString(Arrays.copyOfRange(paramArrayOfbyte, 11, 15));
      str4 = makeFirmwareVersionString(Arrays.copyOfRange(paramArrayOfbyte, 15, 19));
    } else {
      str3 = null;
      str4 = str3;
    } 
    if (paramArrayOfbyte.length >= 11) {
      String str;
      HardwareVersion hardwareVersion = HardwareVersion.build(paramArrayOfbyte[9]);
      if (paramArrayOfbyte[10] == 0) {
        str = "CTR";
      } else if (paramArrayOfbyte[10] == 1) {
        str = "AGR";
      } else {
        str = "Unknown";
      } 
      str5 = str;
      hardwareVersion2 = hardwareVersion;
    } else {
      str5 = null;
    } 
    if (paramArrayOfbyte.length == 9) {
      hardwareVersion1 = HardwareVersion.V1;
    } else {
      hardwareVersion1 = hardwareVersion2;
    } 
    return new DeviceInformation(str1, str2, str3, str4, hardwareVersion1, str5, paramDeviceSerials);
  }
  
  private static String makeFirmwareVersionString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length == 4) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.valueOf(paramArrayOfbyte[0]));
      stringBuilder.append(".");
      stringBuilder.append(String.valueOf(paramArrayOfbyte[1]));
      stringBuilder.append(".");
      stringBuilder.append(String.valueOf(paramArrayOfbyte[2]));
      stringBuilder.append(".");
      stringBuilder.append(String.valueOf(paramArrayOfbyte[3]));
      return stringBuilder.toString();
    } 
    return null;
  }
  
  public DeviceModel getDeviceModel() {
    return DeviceModel.fromHardwareVersion(this.hardwareVersion);
  }
  
  public static interface DeviceInformationCallback {
    void onDeviceInformationReady(DeviceInformation param1DeviceInformation);
  }
}
