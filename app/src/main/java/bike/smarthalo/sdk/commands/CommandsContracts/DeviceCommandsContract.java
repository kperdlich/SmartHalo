package bike.smarthalo.sdk.commands.CommandsContracts;

public interface DeviceCommandsContract extends TransceiveContract {
  void hasEnteredBootloader(boolean paramBoolean);
  
  void saveLastKnownFirmwareVersions(String paramString);
}
