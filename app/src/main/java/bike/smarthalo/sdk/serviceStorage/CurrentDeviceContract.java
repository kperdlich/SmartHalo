package bike.smarthalo.sdk.serviceStorage;

import androidx.annotation.Nullable;

import bike.smarthalo.sdk.models.BleDevice;

public interface CurrentDeviceContract {
  @Nullable
  BleDevice getCurrentDevice();
}
