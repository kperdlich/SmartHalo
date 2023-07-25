package bike.smarthalo.sdk.commands.CommandsModels;

public enum TurnByTurnIntroMode {
  GreenCircle(0),
  ReverseDestination(1),
  TriplePulse(2),
  Samples(3);

  private final int value;

  TurnByTurnIntroMode(int i) {
    this.value = i;
  }

  public int getValue() {
    return this.value;
  }
}
