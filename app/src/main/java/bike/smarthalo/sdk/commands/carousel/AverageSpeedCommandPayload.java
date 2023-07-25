package bike.smarthalo.sdk.commands.carousel;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.models.SHColour;

public class AverageSpeedCommandPayload implements GenericCommandPayload {
  private int averageSpeedPerHour;
  
  private SHColour centerColour;
  
  private int currentSpeedPerHour;
  
  private SHColour higherColour;
  
  private boolean isMetric;
  
  private SHColour lowerColour;
  
  public AverageSpeedCommandPayload(int paramInt1, int paramInt2, boolean paramBoolean, SHColour paramSHColour1, SHColour paramSHColour2, SHColour paramSHColour3) {
    this.averageSpeedPerHour = paramInt1;
    this.currentSpeedPerHour = paramInt2;
    this.isMetric = paramBoolean;
    this.lowerColour = paramSHColour1;
    this.centerColour = paramSHColour2;
    this.higherColour = paramSHColour3;
  }
  
  public byte[] getBytes() {
    return ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(CommandsHelper.getFormatted4ByteMetric(this.averageSpeedPerHour, this.isMetric), CommandsHelper.getFormatted4ByteMetric(this.currentSpeedPerHour, this.isMetric)), this.lowerColour.getBytes()), this.centerColour.getBytes()), this.higherColour.getBytes());
  }
}
