package bike.smarthalo.sdk.commands.carousel;

import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;

public class EmptyCommandPayload implements GenericCommandPayload {
  public byte[] getBytes() {
    return new byte[0];
  }
}
