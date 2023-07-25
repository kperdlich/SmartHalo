package bike.smarthalo.sdk.bluetooth.stmDfuModels;

import java.util.List;

public class StmDfuInformation {
  private int currentPosition;
  
  private byte[] firmwareBytes;
  
  private List<byte[]> payloads;
  
  public StmDfuInformation(List<byte[]> paramList, int paramInt, byte[] paramArrayOfbyte) {
    this.payloads = paramList;
    this.firmwareBytes = paramArrayOfbyte;
    this.currentPosition = Math.min(paramInt, paramList.size());
  }
  
  public int getCurrentPosition() {
    return this.currentPosition;
  }
  
  public StmDfuState getCurrentState() {
    int i = this.currentPosition;
    return (i > 0 && i < this.payloads.size()) ? StmDfuState.Ongoing : ((this.currentPosition == this.payloads.size()) ? StmDfuState.Completed : StmDfuState.NotStarted);
  }
  
  public byte[] getFirmwareBytes() {
    return this.firmwareBytes;
  }
  
  public List<byte[]> getPayloads() {
    return this.payloads;
  }
  
  public int getProgressPercentage() {
    float f;
    if (this.payloads.size() > 0) {
      f = this.currentPosition / this.payloads.size() * 100.0F;
    } else {
      f = 0.0F;
    } 
    return Math.round(f);
  }
  
  public void incrementCurrentPosition() {
    this.currentPosition++;
  }
}
