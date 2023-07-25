package bike.smarthalo.sdk.commands.carousel;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.models.SHColour;

/* loaded from: classes.dex */
public class ProgressCommandPayload implements GenericCommandPayload {
  private String description;
  private SHColour firstColour;
  private int percentage;
  private int period;
  private SHColour secondColour;
  private boolean useFitnessMode;

  public ProgressCommandPayload(SHColour sHColour, SHColour sHColour2, int i, int i2, boolean z, String str) {
    this.firstColour = sHColour;
    this.secondColour = sHColour2;
    this.period = i;
    this.percentage = i2;
    this.useFitnessMode = z;
    this.description = str;
  }

  @Override // bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload
  public byte[] getBytes() {
    int i = this.period;
    byte[] bArr = {(byte) this.firstColour.hue, (byte) this.firstColour.saturation, (byte) this.firstColour.lightness, (byte) this.secondColour.hue, (byte) this.secondColour.saturation, (byte) this.secondColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) this.percentage, this.useFitnessMode ? (byte) 1 : (byte) 0};
    return ArrayUtils.addAll(bArr, CommandsHelper.getFormattedDescription(this.description, bArr.length));
  }
}