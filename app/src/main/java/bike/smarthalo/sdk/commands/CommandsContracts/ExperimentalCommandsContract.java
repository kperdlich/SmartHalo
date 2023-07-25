package bike.smarthalo.sdk.commands.CommandsContracts;

import bike.smarthalo.sdk.commands.UICommandsController;

public interface ExperimentalCommandsContract extends TransceiveContract {
  UICommandsController getUICommandsController();
}
