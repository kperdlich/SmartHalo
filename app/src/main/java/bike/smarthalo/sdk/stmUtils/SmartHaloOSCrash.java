package bike.smarthalo.sdk.stmUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes.dex */
public class SmartHaloOSCrash {
  public static final String CRASH_DATA_PART_1 = "eCRASHD0";
  public static final String CRASH_DATA_PART_2 = "eCRASHD1";
  public static final String CRASH_DATA_PART_3 = "eCRASHD2";
  public static final String CRASH_DATA_PART_4 = "eCRASHD3";
  private static final int CRASH_ID_INDEX = 4;
  private static final int CRASH_LOG_MIN_SIZE = 5;
  private static final int CRASH_SEQUENCE_NUMBER_INDEX = 1;
  private static final int CRASH_TIMESTAMP_INDEX = 0;
  public static final String CRASH_TYPE = "eCRASH";
  private static final int CRASH_TYPE_INDEX = 3;
  public static final String IWDG_TYPE = "eIWDG";
  public static final String SOFT_CRASH_TYPE = "eSOFTCRASH";
  public static final String STACKOVF_TYPE = "eSTACKOVF";
  public static final String SWDG_TYPE = "eSOFTWD";
  public static final String WWDG_TYPE = "eWWDG";
  public List<String> crashData = new ArrayList();
  public CrashType crashType;
  public String id;
  public int sequenceNumber;
  public long timestamp;

  /* loaded from: classes.dex */
  public enum CrashType {
    IWDG,
    WWDG,
    SoftCrash,
    Crash,
    SWDG,
    StackOverflow,
    CrashDataPart,
    Other
  }

  private SmartHaloOSCrash() {
  }

  public static SmartHaloOSCrash build(String str) {
    CrashType fromString;
    if (str == null || str.isEmpty()) {
      return null;
    }
    String[] split = str.split("\\|");
    if (split.length < 5 || (fromString = fromString(split[3])) == CrashType.Other) {
      return null;
    }
    SmartHaloOSCrash smartHaloOSCrash = new SmartHaloOSCrash();
    smartHaloOSCrash.crashType = fromString;
    smartHaloOSCrash.id = split[4];
    try {
      smartHaloOSCrash.sequenceNumber = Integer.parseInt(split[1], 16);
      String[] split2 = split[0].split(":");
      if (split2.length == 3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, Integer.parseInt(split2[0]));
        calendar.set(12, Integer.parseInt(split2[1]));
        calendar.set(13, Integer.parseInt(split2[2]));
        smartHaloOSCrash.timestamp = calendar.getTimeInMillis();
      }
    } catch (NumberFormatException unused) {
      smartHaloOSCrash.sequenceNumber = -1;
      smartHaloOSCrash.timestamp = -1L;
    }
    return smartHaloOSCrash;
  }

  /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
  public static CrashType fromString(String str) {
    char c;
    switch (str.hashCode()) {
      case -1949816104:
        if (str.equals(SOFT_CRASH_TYPE)) {
          c = 1;
          break;
        }
        c = 65535;
        break;
      case -1339039166:
        if (str.equals(CRASH_TYPE)) {
          c = 4;
          break;
        }
        c = 65535;
        break;
      case -78955716:
        if (str.equals(STACKOVF_TYPE)) {
          c = 5;
          break;
        }
        c = 65535;
        break;
      case 95536150:
        if (str.equals(IWDG_TYPE)) {
          c = 0;
          break;
        }
        c = 65535;
        break;
      case 95953224:
        if (str.equals(WWDG_TYPE)) {
          c = 2;
          break;
        }
        c = 65535;
        break;
      case 1673552430:
        if (str.equals(CRASH_DATA_PART_1)) {
          c = 6;
          break;
        }
        c = 65535;
        break;
      case 1673552431:
        if (str.equals(CRASH_DATA_PART_2)) {
          c = 7;
          break;
        }
        c = 65535;
        break;
      case 1673552432:
        if (str.equals(CRASH_DATA_PART_3)) {
          c = '\b';
          break;
        }
        c = 65535;
        break;
      case 1673552433:
        if (str.equals(CRASH_DATA_PART_4)) {
          c = '\t';
          break;
        }
        c = 65535;
        break;
      case 1894905116:
        if (str.equals(SWDG_TYPE)) {
          c = 3;
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
        return CrashType.IWDG;
      case 1:
        return CrashType.SoftCrash;
      case 2:
        return CrashType.WWDG;
      case 3:
        return CrashType.SWDG;
      case 4:
        return CrashType.Crash;
      case 5:
        return CrashType.StackOverflow;
      case 6:
      case 7:
      case '\b':
      case '\t':
        return CrashType.CrashDataPart;
      default:
        return CrashType.Other;
    }
  }

  public boolean isValid() {
    switch (this.crashType) {
      case WWDG:
      case Crash:
        return this.crashData.size() == 4;
      case SWDG:
      case IWDG:
      case SoftCrash:
      case StackOverflow:
        return true;
      default:
        return false;
    }
  }

  public String getMessage() {
    return this.crashType.toString() + "-" + this.id;
  }
}