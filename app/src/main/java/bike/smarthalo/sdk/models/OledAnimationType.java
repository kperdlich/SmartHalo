package bike.smarthalo.sdk.models;

/* loaded from: classes.dex */
public enum OledAnimationType {
  None,
  Left,
  Right,
  Bottom,
  Top;

  public static byte getByteValue(OledAnimationType oledAnimationType) {
    switch (oledAnimationType) {
      case None:
        return (byte) 0;
      case Left:
        return (byte) 1;
      case Right:
        return (byte) 2;
      case Bottom:
        return (byte) 3;
      case Top:
        return (byte) 4;
      default:
        return (byte) -1;
    }
  }
}