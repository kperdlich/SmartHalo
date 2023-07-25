package bike.smarthalo.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationContract;
import bike.smarthalo.sdk.helpers.DebugLoggerContract;
import bike.smarthalo.sdk.models.DeviceConnectionState;
import bike.smarthalo.sdk.models.TransceiveTask;

public class BluetoothDataManager extends BluetoothGattCallback {
    public static String CLIENT_CHARACTERISTIC_CONFIG;

    public static String SHDESC = "00002902-0000-1000-8000-00805f9b34fb";

    public static String SHLINKCTRL = "92540001-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKPAYLOAD0 = "92540010-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKPAYLOAD1 = "92540011-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKPAYLOAD2 = "92540012-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKPAYLOAD3 = "92540013-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKUPCTRL = "92540101-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKUPPAYLOAD0 = "92540110-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKUPPAYLOAD1 = "92540111-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKUPPAYLOAD2 = "92540112-d6ed-4617-a4b4-d749f57710ce";

    public static String SHLINKUPPAYLOAD3 = "92540113-d6ed-4617-a4b4-d749f57710ce";

    public static String SHSERVICE1 = "92540000-d6ed-4617-a4b4-d749f57710ce";

    public static final ParcelUuid PARCEL_UUID_SHSERVICE1 = ParcelUuid.fromString(SHSERVICE1);

    private static final String TAG;

    public static final UUID UUID_SHLINKCTRL;

    public static final UUID UUID_SHLINKPAYLOAD0;

    public static final UUID UUID_SHLINKPAYLOAD1;

    public static final UUID UUID_SHLINKPAYLOAD2;

    public static final UUID UUID_SHLINKPAYLOAD3;

    public static final UUID UUID_SHLINKUPCTRL;

    public static final UUID UUID_SHLINKUPPAYLOAD0;

    public static final UUID UUID_SHLINKUPPAYLOAD1;

    public static final UUID UUID_SHLINKUPPAYLOAD2;

    public static final UUID UUID_SHLINKUPPAYLOAD3;

    public static final UUID UUID_SHSERVICE1 = UUID.fromString(SHSERVICE1);

    private BleNotificationContract bleNotificationController;

    private Queue<BluetoothGattCallback> cbBLETasks = new LinkedList<BluetoothGattCallback>();

    private final BluetoothGattCallback cbTransceiveWriteTask = new BluetoothGattCallback() {
        public void onCharacteristicWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattCharacteristic param1BluetoothGattCharacteristic, int param1Int) {
            BluetoothDataManager.this.processNextTransceiveTask();
        }
    };

    private Context context;

    private BluetoothDataManagerContract contract;

    private DebugLoggerContract debugLogger;

    private BluetoothGatt gatt;

    private BluetoothGattCharacteristic mSHCharCtrl;

    ArrayList<BluetoothGattCharacteristic> mSHCharPayload;

    private BluetoothGattCharacteristic mSHCharUpCtrl;

    ArrayList<BluetoothGattCharacteristic> mSHCharUpPayload;

    private ByteArrayOutputStream notifRecvPayloads = new ByteArrayOutputStream();

    private final BluetoothGattCallback notifySetupCallback = new BluetoothGattCallback() {
        public void onDescriptorWrite(BluetoothGatt param1BluetoothGatt, BluetoothGattDescriptor param1BluetoothGattDescriptor, int param1Int) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic = BluetoothDataManager.this.notifySetuplist.poll();
            if (bluetoothGattCharacteristic != null) {
                BluetoothDataManager bluetoothDataManager = BluetoothDataManager.this;
                bluetoothDataManager.setCharacteristicNotification(param1BluetoothGatt, bluetoothGattCharacteristic, true, bluetoothDataManager.notifySetupCallback);
            } else {
                BluetoothDataManager.this.contract.exchangeKeys();
            }
        }
    };

    private final Queue<BluetoothGattCharacteristic> notifySetuplist = new LinkedList<BluetoothGattCharacteristic>();

    static {
        UUID_SHLINKCTRL = UUID.fromString(SHLINKCTRL);
        UUID_SHLINKPAYLOAD0 = UUID.fromString(SHLINKPAYLOAD0);
        UUID_SHLINKPAYLOAD1 = UUID.fromString(SHLINKPAYLOAD1);
        UUID_SHLINKPAYLOAD2 = UUID.fromString(SHLINKPAYLOAD2);
        UUID_SHLINKPAYLOAD3 = UUID.fromString(SHLINKPAYLOAD3);
        UUID_SHLINKUPCTRL = UUID.fromString(SHLINKUPCTRL);
        UUID_SHLINKUPPAYLOAD0 = UUID.fromString(SHLINKUPPAYLOAD0);
        UUID_SHLINKUPPAYLOAD1 = UUID.fromString(SHLINKUPPAYLOAD1);
        UUID_SHLINKUPPAYLOAD2 = UUID.fromString(SHLINKUPPAYLOAD2);
        UUID_SHLINKUPPAYLOAD3 = UUID.fromString(SHLINKUPPAYLOAD3);
        CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
        TAG = BluetoothDataManager.class.getSimpleName();
    }

    public BluetoothDataManager(Context paramContext, BluetoothDataManagerContract paramBluetoothDataManagerContract, BleNotificationContract paramBleNotificationContract, DebugLoggerContract paramDebugLoggerContract) {
        Log.d(TAG, "Instantiating BluetoothDataManager");
        this.context = paramContext;
        this.contract = paramBluetoothDataManagerContract;
        this.debugLogger = paramDebugLoggerContract;
        this.bleNotificationController = paramBleNotificationContract;
    }

    private void closeGatt() {
        if (this.gatt != null) {
            DebugLoggerContract debugLoggerContract = this.debugLogger;
            String str = TAG;
            debugLoggerContract.log(str, "Closing gatt to device: " + this.gatt.getDevice());
            this.gatt.close();
            this.gatt = null;
        }
    }


    private void processNewNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic == this.mSHCharUpCtrl) {
            this.bleNotificationController.onNewBleNotification(bluetoothGattCharacteristic.getValue(), this.notifRecvPayloads.toByteArray());
            this.notifRecvPayloads = new ByteArrayOutputStream();
            return;
        }
        try {
            this.notifRecvPayloads.write(bluetoothGattCharacteristic.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void processTransceiveResult(final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.contract.peekTransceiveQueue(new TransceiveQueueManager.GetItemCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$BluetoothDataManager$_56X_P2pUvF7umeVddh2_54CCc8
            @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetItemCallback
            public final void onResult(TransceiveTask transceiveTask, int i) {
                if (transceiveTask == null) {
                    return;
                }
                if (bluetoothGattCharacteristic == mSHCharCtrl) {
                    contract.onNewTransceiveResult(bluetoothGattCharacteristic.getValue(), transceiveTask);
                    return;
                }
                try {
                    transceiveTask.recvPayloads.write(bluetoothGattCharacteristic.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error when processing transceive result payload, cleaning connection");
                    contract.cleanUpDeviceConnection();
                }
            }
        });
    }

    private void setCharacteristicNotification(BluetoothGatt paramBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean, BluetoothGattCallback paramBluetoothGattCallback) {
        if (paramBluetoothGatt == null) {
            Log.w(TAG, "Bluetooth gatt is not initialized");
            return;
        }
        if (paramBluetoothGattCharacteristic.getDescriptors().isEmpty()) {
            Log.e(TAG, "No descriptors in bluetooth characteristic, cleaning connection");
            this.contract.cleanUpDeviceConnection();
            return;
        }
        this.cbBLETasks.add(paramBluetoothGattCallback);
        paramBluetoothGatt.setCharacteristicNotification(paramBluetoothGattCharacteristic, paramBoolean);
        BluetoothGattDescriptor bluetoothGattDescriptor = paramBluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        paramBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
    }

    private boolean writeCharacteristic(BluetoothGatt paramBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, byte[] paramArrayOfbyte, BluetoothGattCallback paramBluetoothGattCallback) {
        if (paramBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        this.cbBLETasks.add(paramBluetoothGattCallback);
        paramBluetoothGattCharacteristic.setValue(paramArrayOfbyte);
        boolean bool = paramBluetoothGatt.writeCharacteristic(paramBluetoothGattCharacteristic);
        if (!bool)
            this.cbBLETasks.poll();
        return bool;
    }

    public void cleanUpSequence() {
        this.mSHCharCtrl = null;
        this.mSHCharUpCtrl = null;
        this.notifRecvPayloads = new ByteArrayOutputStream();
        this.mSHCharUpPayload = new ArrayList<BluetoothGattCharacteristic>();
        this.mSHCharPayload = new ArrayList<BluetoothGattCharacteristic>();
        this.cbBLETasks.clear();
        this.notifySetuplist.clear();
        closeGatt();
    }

    public BluetoothDevice getDevice() {
        BluetoothGatt bluetoothGatt = this.gatt;
        if (bluetoothGatt != null) {
            return bluetoothGatt.getDevice();
        }
        return null;
    }

    public boolean isInitialized() {
        boolean bool;
        if (this.gatt != null) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public void onCharacteristicChanged(BluetoothGatt paramBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic) {
        if (paramBluetoothGattCharacteristic == this.mSHCharCtrl || this.mSHCharPayload.indexOf(paramBluetoothGattCharacteristic) != -1)
            processTransceiveResult(paramBluetoothGattCharacteristic);
        if (paramBluetoothGattCharacteristic == this.mSHCharUpCtrl || this.mSHCharUpPayload.indexOf(paramBluetoothGattCharacteristic) != -1)
            processNewNotification(paramBluetoothGattCharacteristic);
    }

    public void onCharacteristicRead(BluetoothGatt paramBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int paramInt) {
        BluetoothGattCallback bluetoothGattCallback = this.cbBLETasks.poll();
        if (bluetoothGattCallback != null)
            bluetoothGattCallback.onCharacteristicRead(paramBluetoothGatt, paramBluetoothGattCharacteristic, paramInt);
    }

    public void onCharacteristicWrite(BluetoothGatt paramBluetoothGatt, BluetoothGattCharacteristic paramBluetoothGattCharacteristic, int paramInt) {
        BluetoothGattCallback bluetoothGattCallback = this.cbBLETasks.poll();
        if (bluetoothGattCallback != null)
            bluetoothGattCallback.onCharacteristicWrite(paramBluetoothGatt, paramBluetoothGattCharacteristic, paramInt);
    }

    public void onConnectionStateChange(BluetoothGatt paramBluetoothGatt, int paramInt1, int paramInt2) {
        this.gatt = paramBluetoothGatt;
        DebugLoggerContract debugLoggerContract = this.debugLogger;
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bluetooth gatt new connection state ");
        stringBuilder.append(paramInt2);
        debugLoggerContract.log(str, stringBuilder.toString());
        if (paramInt2 == 2) {
            paramBluetoothGatt.discoverServices();
            this.contract.onConnectionStateChanged(DeviceConnectionState.Connected);
        } else if (paramInt2 == 0) {
            this.contract.cleanUpDeviceConnection();
        }
    }

    public void onDescriptorRead(BluetoothGatt paramBluetoothGatt, BluetoothGattDescriptor paramBluetoothGattDescriptor, int paramInt) {
        BluetoothGattCallback bluetoothGattCallback = this.cbBLETasks.poll();
        if (bluetoothGattCallback != null)
            bluetoothGattCallback.onDescriptorRead(paramBluetoothGatt, paramBluetoothGattDescriptor, paramInt);
    }

    public void onDescriptorWrite(BluetoothGatt paramBluetoothGatt, BluetoothGattDescriptor paramBluetoothGattDescriptor, int paramInt) {
        BluetoothGattCallback bluetoothGattCallback = this.cbBLETasks.poll();
        if (bluetoothGattCallback != null)
            bluetoothGattCallback.onDescriptorWrite(paramBluetoothGatt, paramBluetoothGattDescriptor, paramInt);
    }

    public void onServicesDiscovered(BluetoothGatt paramBluetoothGatt, int paramInt) {
        DebugLoggerContract debugLoggerContract = this.debugLogger;
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onServicesDiscovered received: ");
        stringBuilder.append(paramInt);
        debugLoggerContract.log(str, stringBuilder.toString());
        if (paramInt == 0) {
            BluetoothGattService bluetoothGattService = paramBluetoothGatt.getService(UUID_SHSERVICE1);
            if (bluetoothGattService == null) {
                Log.i(TAG, "shGattService is null");
                return;
            }
            this.mSHCharCtrl = bluetoothGattService.getCharacteristic(UUID_SHLINKCTRL);
            BluetoothGattCharacteristic bluetoothGattCharacteristic3 = bluetoothGattService.getCharacteristic(UUID_SHLINKPAYLOAD0);
            BluetoothGattCharacteristic bluetoothGattCharacteristic5 = bluetoothGattService.getCharacteristic(UUID_SHLINKPAYLOAD1);
            BluetoothGattCharacteristic bluetoothGattCharacteristic2 = bluetoothGattService.getCharacteristic(UUID_SHLINKPAYLOAD2);
            BluetoothGattCharacteristic bluetoothGattCharacteristic6 = bluetoothGattService.getCharacteristic(UUID_SHLINKPAYLOAD3);
            this.mSHCharUpCtrl = bluetoothGattService.getCharacteristic(UUID_SHLINKUPCTRL);
            BluetoothGattCharacteristic bluetoothGattCharacteristic1 = bluetoothGattService.getCharacteristic(UUID_SHLINKUPPAYLOAD0);
            BluetoothGattCharacteristic bluetoothGattCharacteristic7 = bluetoothGattService.getCharacteristic(UUID_SHLINKUPPAYLOAD1);
            BluetoothGattCharacteristic bluetoothGattCharacteristic8 = bluetoothGattService.getCharacteristic(UUID_SHLINKUPPAYLOAD2);
            BluetoothGattCharacteristic bluetoothGattCharacteristic9 = bluetoothGattService.getCharacteristic(UUID_SHLINKUPPAYLOAD3);
            BluetoothGattCharacteristic bluetoothGattCharacteristic4 = this.mSHCharCtrl;
            if (bluetoothGattCharacteristic4 == null || bluetoothGattCharacteristic3 == null || bluetoothGattCharacteristic5 == null || bluetoothGattCharacteristic2 == null || bluetoothGattCharacteristic6 == null || bluetoothGattCharacteristic1 == null || bluetoothGattCharacteristic7 == null || bluetoothGattCharacteristic8 == null || bluetoothGattCharacteristic9 == null || this.mSHCharUpCtrl == null) {
                Log.e(TAG, "Error when discovering characteristics, cleaning connection");
                this.contract.cleanUpDeviceConnection();
                return;
            }
            bluetoothGattCharacteristic4.setWriteType(1);
            this.mSHCharPayload = new ArrayList<BluetoothGattCharacteristic>();
            bluetoothGattCharacteristic3.setWriteType(1);
            this.mSHCharPayload.add(bluetoothGattCharacteristic3);
            this.notifySetuplist.add(bluetoothGattCharacteristic3);
            bluetoothGattCharacteristic5.setWriteType(1);
            this.mSHCharPayload.add(bluetoothGattCharacteristic5);
            this.notifySetuplist.add(bluetoothGattCharacteristic5);
            bluetoothGattCharacteristic2.setWriteType(1);
            this.mSHCharPayload.add(bluetoothGattCharacteristic2);
            this.notifySetuplist.add(bluetoothGattCharacteristic2);
            bluetoothGattCharacteristic6.setWriteType(1);
            this.mSHCharPayload.add(bluetoothGattCharacteristic6);
            this.notifySetuplist.add(bluetoothGattCharacteristic6);
            this.mSHCharUpPayload = new ArrayList<BluetoothGattCharacteristic>();
            this.mSHCharUpCtrl.setWriteType(1);
            this.notifySetuplist.add(this.mSHCharUpCtrl);
            bluetoothGattCharacteristic1.setWriteType(1);
            this.mSHCharUpPayload.add(bluetoothGattCharacteristic1);
            this.notifySetuplist.add(bluetoothGattCharacteristic1);
            bluetoothGattCharacteristic7.setWriteType(1);
            this.mSHCharUpPayload.add(bluetoothGattCharacteristic7);
            this.notifySetuplist.add(bluetoothGattCharacteristic7);
            bluetoothGattCharacteristic8.setWriteType(1);
            this.mSHCharUpPayload.add(bluetoothGattCharacteristic8);
            this.notifySetuplist.add(bluetoothGattCharacteristic8);
            bluetoothGattCharacteristic9.setWriteType(1);
            this.mSHCharUpPayload.add(bluetoothGattCharacteristic9);
            this.notifySetuplist.add(bluetoothGattCharacteristic9);
            setCharacteristicNotification(paramBluetoothGatt, this.mSHCharCtrl, true, this.notifySetupCallback);
        }
    }

    public void processNextTransceiveTask() {
        this.contract.peekTransceiveQueue(new TransceiveQueueManager.GetItemCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$BluetoothDataManager$bfDmaPV8FoQ0Mkvzb1PbGqqUg2I
            @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetItemCallback
            public final void onResult(TransceiveTask transceiveTask, int i) {
                if (transceiveTask == null) {
                    return;
                }
                if (transceiveTask.currentSendPayloadIndex == 0) {
                    transceiveTask.sentAt = System.currentTimeMillis();
                    DebugLoggerContract debugLoggerContract = debugLogger;
                    String str = TAG;
                    debugLoggerContract.log(str, "Sending task " + transceiveTask.description + ".  Attempt #" + transceiveTask.retryCount);
                    contract.startTransceiveTimeoutTimer();
                }
                boolean z = false;
                if (transceiveTask.currentSendPayloadIndex < transceiveTask.sendPayloads.size()) {
                    byte[] bArr = transceiveTask.sendPayloads.get(transceiveTask.currentSendPayloadIndex);
                    ArrayList<BluetoothGattCharacteristic> arrayList = mSHCharPayload;
                    if (arrayList != null && arrayList.size() > transceiveTask.currentSendPayloadIndex) {
                        BluetoothGatt bluetoothGatt = gatt;
                        ArrayList<BluetoothGattCharacteristic> arrayList2 = mSHCharPayload;
                        int i2 = transceiveTask.currentSendPayloadIndex;
                        transceiveTask.currentSendPayloadIndex = i2 + 1;
                        if (writeCharacteristic(bluetoothGatt, arrayList2.get(i2), bArr, cbTransceiveWriteTask)) {
                            z = true;
                        }
                    }
                } else {
                    z = writeCharacteristic(gatt, mSHCharCtrl, new byte[]{(byte) transceiveTask.sendLen, (byte) transceiveTask.crypted}, null);
                }
                if (z) {
                    return;
                }
                debugLogger.log(TAG, "Write Failed!");
                contract.transceiveRetryOrRestart(transceiveTask);
            }
        });
    }


    public boolean setUpDeviceConnection(BluetoothDevice paramBluetoothDevice) {
        if (paramBluetoothDevice != null && this.gatt == null) {
            DebugLoggerContract debugLoggerContract = this.debugLogger;
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Setting connection with device: ");
            stringBuilder.append(paramBluetoothDevice.getAddress());
            debugLoggerContract.log(str, stringBuilder.toString());
            this.gatt = paramBluetoothDevice.connectGatt(this.context, false, this);
            return true;
        }
        return false;
    }
}
