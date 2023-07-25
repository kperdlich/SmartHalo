package bike.smarthalo.sdk.bluetooth;

import androidx.annotation.NonNull;

import bike.smarthalo.sdk.models.DeviceConnectionState;
import bike.smarthalo.sdk.models.TransceiveTask;

public interface BluetoothDataManagerContract {
  void cleanUpDeviceConnection();
  
  void exchangeKeys();
  
  void onConnectionStateChanged(DeviceConnectionState paramDeviceConnectionState);
  
  void onNewTransceiveResult(byte[] paramArrayOfbyte, TransceiveTask paramTransceiveTask);
  
  void peekTransceiveQueue(@NonNull TransceiveQueueManager.GetItemCallback paramGetItemCallback);
  
  void startTransceiveTimeoutTimer();
  
  void transceiveRetryOrRestart(@NonNull TransceiveTask paramTransceiveTask);
}
