package bike.smarthalo.sdk.commands.carousel;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.models.SHColour;

public class NavigationCommandPayload implements GenericCommandPayload {
  private int angle;
  
  private SHColour colour;
  
  private String description;
  
  private int distance;
  
  private boolean isMetric;
  
  public NavigationCommandPayload(int paramInt1, boolean paramBoolean, int paramInt2, SHColour paramSHColour, String paramString) {
    this.distance = paramInt1;
    this.isMetric = paramBoolean;
    this.angle = paramInt2;
    this.colour = paramSHColour;
    this.description = paramString;
  }
  
  public byte[] getBytes() {
    byte[] arrayOfByte = CommandsHelper.getFormatted4ByteMetric(this.distance, this.isMetric);
    int i = this.angle;
    arrayOfByte = ArrayUtils.addAll(ArrayUtils.addAll(arrayOfByte, new byte[] { (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) }), this.colour.getBytes());
    return ArrayUtils.addAll(arrayOfByte, CommandsHelper.getFormattedDescription(this.description, arrayOfByte.length));
  }
}
