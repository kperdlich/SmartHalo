package bike.smarthalo.sdk.commands.CommandsModels;

import bike.smarthalo.sdk.CmdCallback;

public abstract class GenericBleCommand {
  public CmdCallback callback = null;
  
  public byte[] commandIdentifier;
  
  public String description = "";
  
  public boolean isCrypted;
  
  private GenericBleCommand() {}
  
  public GenericBleCommand(byte[] paramArrayOfbyte, boolean paramBoolean, String paramString) {
    this.commandIdentifier = paramArrayOfbyte;
    this.isCrypted = paramBoolean;
    this.description = paramString;
  }
  
  public GenericBleCommand(byte[] paramArrayOfbyte, boolean paramBoolean, String paramString, CmdCallback paramCmdCallback) {
    this.callback = paramCmdCallback;
    this.commandIdentifier = paramArrayOfbyte;
    this.isCrypted = paramBoolean;
    this.description = paramString;
  }
  
  public abstract byte[] getBytes();
}
