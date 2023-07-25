package bike.smarthalo.sdk.commands.CommandsModels;

public enum NotificationType {
  Text(1),
  Call(0),
  Weather(32),
  Horn(64),
  Clock(65),
  GoToHome(66),
  GoToWork(67),
  StopNav(68),
  Error(69),
  NavModeAsTheCrownFlies(70),
  NavModeTurnByTurn(71);

  private final int value;

  NotificationType(int i) {
    this.value = i;
  }

  public int getValue() {
    return this.value;
  }
}