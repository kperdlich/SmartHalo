package bike.smarthalo.sdk.commands;

public enum CommandResponse {
  OK(0),
  Fail(1),
  Denied(2),
  Unimplemented(3),
  Unnecessary(4);

  private final int value;

  CommandResponse(int i) {
    this.value = i;
  }

  public int getValue() {
    return this.value;
  }

  public static CommandResponse fromInt(int i) {
    if (i == 0) {
      return OK;
    }
    switch (i) {
      case 2:
        return Denied;
      case 3:
        return Unimplemented;
      case 4:
        return Unnecessary;
      default:
        return Fail;
    }
  }
}
