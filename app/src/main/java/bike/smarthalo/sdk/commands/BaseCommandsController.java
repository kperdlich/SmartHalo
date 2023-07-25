package bike.smarthalo.sdk.commands;

import android.content.Context;

import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;

public abstract class BaseCommandsController {
  public Context context;
  
  public TransceiveContract contract;
  
  public BaseCommandsController(Context paramContext, TransceiveContract paramTransceiveContract) {
    this.contract = paramTransceiveContract;
    this.context = paramContext;
  }
}
