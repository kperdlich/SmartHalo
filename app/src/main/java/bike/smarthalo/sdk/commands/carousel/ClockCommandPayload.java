package bike.smarthalo.sdk.commands.carousel;

import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.models.SHColour;

/* loaded from: classes.dex */
public class ClockCommandPayload implements GenericCommandPayload {
  private int duration;
  private int hour;
  private SHColour hourColour;
  private boolean is24HourFormat;
  private int minute;
  private SHColour minuteColour;
  private boolean shouldDisplayIntro;
  private boolean shouldFade;
  private boolean shouldPulse;

  public ClockCommandPayload(int i, SHColour sHColour, int i2, SHColour sHColour2, int i3, boolean z, boolean z2, boolean z3, boolean z4) {
    this.hour = i;
    this.hourColour = sHColour;
    this.minute = i2;
    this.minuteColour = sHColour2;
    this.duration = i3;
    this.shouldFade = z;
    this.shouldDisplayIntro = z2;
    this.shouldPulse = z3;
    this.is24HourFormat = z4;
  }

  @Override // bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload
  public byte[] getBytes() {
    int i = this.duration;
    return new byte[]{(byte) this.hour, (byte) this.hourColour.hue, (byte) this.hourColour.saturation, (byte) this.hourColour.lightness, (byte) this.minute, (byte) this.minuteColour.hue, (byte) this.minuteColour.saturation, (byte) this.minuteColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), this.shouldFade ? (byte) 1 : (byte) 0, this.shouldDisplayIntro ? (byte) 1 : (byte) 0, this.shouldPulse ? (byte) 1 : (byte) 0, this.is24HourFormat ? (byte) 1 : (byte) 0};
  }
}