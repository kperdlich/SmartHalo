package bike.smarthalo.sdk.commands.carousel;

import java.util.ArrayList;

public class MetricsCarouselMask {
  private static final int MAX_MASK_SIZE = 16;
  
  public boolean hasAvgSpeed = true;
  
  public boolean hasBattery = true;
  
  public boolean hasCO2 = false;
  
  public boolean hasCalories = true;
  
  public boolean hasClock = true;
  
  public boolean hasDistance = true;
  
  public boolean hasElapsedTime = true;
  
  public boolean hasGoal = false;
  
  public boolean hasNavigation = false;
  
  public boolean hasSpeedometer = true;
  
  public int getMask() {
    ArrayList<Boolean> arrayList = new ArrayList();
    arrayList.add(Boolean.valueOf(this.hasNavigation));
    arrayList.add(Boolean.valueOf(this.hasGoal));
    arrayList.add(Boolean.valueOf(this.hasDistance));
    arrayList.add(Boolean.valueOf(this.hasElapsedTime));
    arrayList.add(Boolean.valueOf(this.hasSpeedometer));
    arrayList.add(Boolean.valueOf(this.hasCalories));
    arrayList.add(Boolean.valueOf(this.hasClock));
    arrayList.add(Boolean.valueOf(this.hasAvgSpeed));
    arrayList.add(Boolean.valueOf(this.hasCO2));
    arrayList.add(Boolean.valueOf(this.hasBattery));
    int i = arrayList.size();
    while (true) {
      int j = 0;
      if (i < 16) {
        arrayList.add(Boolean.valueOf(false));
        i++;
        continue;
      } 
      int k = 0;
      i = j;
      while (i < arrayList.size()) {
        j = k;
        if (((Boolean)arrayList.get(i)).booleanValue())
          j = k | 1 << i; 
        i++;
        k = j;
      } 
      return k;
    } 
  }
}
