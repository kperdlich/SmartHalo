package bike.smarthalo.sdk.bluetooth;

import bike.smarthalo.sdk.models.TransceiveTask;

public class BluetoothSpeedMonitor {
  private BluetoothSpeedMetrics bluetoothSpeedMetrics = new BluetoothSpeedMetrics();
  
  private long getNewAverage(long paramLong1, long paramLong2, long paramLong3) {
    return Math.round((float)(paramLong2 * paramLong1 + paramLong3) / ((float)paramLong1 + 1.0F));
  }
  
  public void clearMetrics() {
    this.bluetoothSpeedMetrics = new BluetoothSpeedMetrics();
  }
  
  public BluetoothSpeedMetrics getBluetoothSpeedMetrics() {
    return this.bluetoothSpeedMetrics;
  }
  
  public void updateQueueSize(int paramInt) {
    this.bluetoothSpeedMetrics.currentQueueSize = paramInt;
  }
  
  public void updateSpeedMetrics(TransceiveTask paramTransceiveTask) {
    this.bluetoothSpeedMetrics.lastTaskSpeed = paramTransceiveTask.receivedDataAt - paramTransceiveTask.sentAt;
    this.bluetoothSpeedMetrics.lastTaskTimeSpentInQueue = paramTransceiveTask.sentAt - paramTransceiveTask.createdAt;
    if (this.bluetoothSpeedMetrics.itemsProcessedSinceReconnection == 0L) {
      BluetoothSpeedMetrics bluetoothSpeedMetrics1 = this.bluetoothSpeedMetrics;
      bluetoothSpeedMetrics1.averageTaskSpeed = bluetoothSpeedMetrics1.lastTaskSpeed;
      bluetoothSpeedMetrics1 = this.bluetoothSpeedMetrics;
      bluetoothSpeedMetrics1.averageTaskTimeSpentInQueue = bluetoothSpeedMetrics1.lastTaskTimeSpentInQueue;
    } else {
      BluetoothSpeedMetrics bluetoothSpeedMetrics1 = this.bluetoothSpeedMetrics;
      bluetoothSpeedMetrics1.averageTaskSpeed = getNewAverage(bluetoothSpeedMetrics1.itemsProcessedSinceReconnection, this.bluetoothSpeedMetrics.averageTaskSpeed, this.bluetoothSpeedMetrics.lastTaskSpeed);
      bluetoothSpeedMetrics1 = this.bluetoothSpeedMetrics;
      bluetoothSpeedMetrics1.averageTaskTimeSpentInQueue = getNewAverage(bluetoothSpeedMetrics1.itemsProcessedSinceReconnection, this.bluetoothSpeedMetrics.averageTaskTimeSpentInQueue, this.bluetoothSpeedMetrics.lastTaskTimeSpentInQueue);
    } 
    BluetoothSpeedMetrics bluetoothSpeedMetrics = this.bluetoothSpeedMetrics;
    bluetoothSpeedMetrics.itemsProcessedSinceReconnection++;
  }
}
