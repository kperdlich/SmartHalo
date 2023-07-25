package bike.smarthalo.sdk.commands.carousel;

import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;

/* loaded from: classes.dex */
public class SpeedometerCommandPayload implements GenericCommandPayload {
  private boolean isMetric;
  private int percentage;
  private int speed;

  public SpeedometerCommandPayload(int i, int i2, boolean z) {
    this.percentage = i;
    this.speed = i2;
    this.isMetric = z;
  }

  @Override // bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload
  public byte[] getBytes() {
    return new byte[]{(byte) this.percentage, (byte) this.speed, (byte) (!this.isMetric ? 1 : 0)};
  }
}