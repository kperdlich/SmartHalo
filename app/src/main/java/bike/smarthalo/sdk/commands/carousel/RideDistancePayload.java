package bike.smarthalo.sdk.commands.carousel;

import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;

public class RideDistancePayload implements GenericCommandPayload {
  private int distance;
  
  private boolean isMetric;
  
  public RideDistancePayload(int paramInt, boolean paramBoolean) {
    this.distance = paramInt;
    this.isMetric = paramBoolean;
  }
  
  public byte[] getBytes() {
    return CommandsHelper.getFormatted4ByteMetric(this.distance, this.isMetric);
  }
}
