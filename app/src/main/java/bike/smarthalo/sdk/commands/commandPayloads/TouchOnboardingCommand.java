package bike.smarthalo.sdk.commands.commandPayloads;

import bike.smarthalo.sdk.commands.CommandsConstants;
import bike.smarthalo.sdk.commands.CommandsModels.GenericBleCommand;

public class TouchOnboardingCommand extends GenericBleCommand {
  public TouchOnboardingCommand() {
    super(CommandsConstants.cmd_touch_onboarding, true, "touch_onboarding");
  }
  
  public byte[] getBytes() {
    return this.commandIdentifier;
  }
}
