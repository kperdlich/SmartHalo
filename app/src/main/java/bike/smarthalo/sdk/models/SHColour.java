package bike.smarthalo.sdk.models;

public class SHColour {
  public int hue = 0;
  
  public int lightness = 0;
  
  public int saturation = 0;
  
  public SHColour() {}
  
  public SHColour(int paramInt1, int paramInt2, int paramInt3) {}
  
  public byte[] getBytes() {
    return new byte[] { (byte)this.hue, (byte)this.saturation, (byte)this.lightness };
  }
}
