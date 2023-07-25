package bike.smarthalo.sdk.commands.CommandsContracts;

import androidx.annotation.Nullable;

import bike.smarthalo.sdk.firmware.NordicProtocolVersion;
import bike.smarthalo.sdk.models.DeviceInformation;

public interface DeviceInformationContract {
  @Nullable
  DeviceInformation getDeviceInformation();
  
  boolean isNordicFirmwareSufficient(NordicProtocolVersion paramNordicProtocolVersion);
}
