package bike.smarthalo.sdk.commands.carousel;

/* loaded from: classes.dex */
public enum MetricsCarouselPosition {
  Navigation(0),
  Goal(1),
  Distance(2),
  Time(3),
  Speed(4),
  Calories(5),
  Clock(6),
  AverageSpeed(7),
  CarbonDioxide(8),
  Battery(9),
  Unknown(-1);

  private final int value;

  public int getValue() {
    return this.value;
  }

  MetricsCarouselPosition(int i) {
    this.value = i;
  }

  public static MetricsCarouselPosition getPosition(int i) {
    switch (i) {
      case 0:
        return Navigation;
      case 1:
        return Goal;
      case 2:
        return Distance;
      case 3:
        return Time;
      case 4:
        return Speed;
      case 5:
        return Calories;
      case 6:
        return Clock;
      case 7:
        return AverageSpeed;
      case 8:
        return CarbonDioxide;
      case 9:
        return Battery;
      default:
        return Unknown;
    }
  }
}