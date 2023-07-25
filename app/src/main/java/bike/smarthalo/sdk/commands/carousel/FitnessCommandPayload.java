package bike.smarthalo.sdk.commands.carousel;

import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;

public class FitnessCommandPayload implements GenericCommandPayload {
  private int genericFitnessMetric;
  
  public FitnessCommandPayload(int paramInt) {
    this.genericFitnessMetric = paramInt;
  }
  
  public byte[] getBytes() {
    int i = this.genericFitnessMetric;
    return new byte[] { (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) };
  }
}
