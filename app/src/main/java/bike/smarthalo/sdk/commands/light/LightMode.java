package bike.smarthalo.sdk.commands.light;

/* loaded from: classes.dex */
public enum LightMode {
  Normal,
  Blinking,
  Unknown;

  public static final int LIGHT_MODE_BLINKING = 1;
  public static final int LIGHT_MODE_NORMAL = 0;

  public int getValue() {
    switch (this) {
      case Normal:
        return 0;
      case Blinking:
        return 1;
      default:
        return -1;
    }
  }

  public static LightMode build(int i) {
    switch (i) {
      case 0:
        return Normal;
      case 1:
        return Blinking;
      default:
        return Unknown;
    }
  }
}