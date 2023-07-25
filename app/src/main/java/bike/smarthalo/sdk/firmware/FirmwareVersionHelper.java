package bike.smarthalo.sdk.firmware;

/* loaded from: classes.dex */
public class FirmwareVersionHelper {
  public static boolean isFirmwareVersionSufficient(String str, String str2) {
    boolean z;
    boolean z2 = false;
    if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
      return false;
    }
    String[] split = str2.split("\\.");
    String[] split2 = str.split("\\.");
    if (split2.length < split.length) {
      return false;
    }
    if (split2.length != 4 || Integer.valueOf(split2[3]).intValue() <= 0) {
      int i = 0;
      boolean z3 = false;
      while (true) {
        if (i >= split.length) {
          z2 = z3;
          z = false;
          break;
        }
        int intValue = Integer.valueOf(split[i]).intValue();
        int intValue2 = Integer.valueOf(split2[i]).intValue();
        if (intValue2 > intValue) {
          z = true;
          break;
        } else if (intValue2 < intValue) {
          z = false;
          break;
        } else {
          if (intValue2 == intValue) {
            z3 = true;
          }
          i++;
        }
      }
      if (z2) {
        return true;
      }
      return z;
    }
    return true;
  }

  public static String getFormattedFirmwareVersion(String str) {
    if (str == null) {
      return null;
    }
    return str.split("\\-")[0];
  }
}