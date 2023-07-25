package bike.smarthalo.sdk.bluetooth.bleNotifications;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.commands.DeviceCommandsController;
import bike.smarthalo.sdk.commands.carousel.MetricsCarouselPosition;
import bike.smarthalo.sdk.encryption.EncryptionContract;
import bike.smarthalo.sdk.encryption.SHEncryptionHelper;
import bike.smarthalo.sdk.helpers.DebugLoggerContract;
import bike.smarthalo.sdk.models.SHDeviceState;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;


public class BleNotificationController implements BleNotificationContract {
    private static final byte NOTIF_CARROUSEL_INDEX = 5;

    private static final byte NOTIF_FRONTLIGHT = 3;

    private static final byte NOTIF_LOG = -8;

    private static final byte NOTIF_MOVEMENT = 2;

    private static final byte NOTIF_SH_INFO = 4;

    private static final byte NOTIF_TOUCH = 0;

    private static final String TAG = "BleNotificationController";

    private FlowableProcessor<MetricsCarouselPosition> carouselPositionEventSource;

    private DebugLoggerContract debuggerLogger;

    private DeviceCommandsController deviceCommandsController;

    private FlowableProcessor<Boolean> deviceMovementSource;

    private FlowableProcessor<SHDeviceState> deviceStateEventSource;

    private EncryptionContract encryptionController;

    private FlowableProcessor<LightNotification> lightEventSource;

    private FlowableProcessor<String> stmLogsSource;

    private FlowableProcessor<String> touchEventSource;

    public BleNotificationController(DeviceCommandsController paramDeviceCommandsController, EncryptionContract paramEncryptionContract, DebugLoggerContract paramDebugLoggerContract) {
        this.deviceCommandsController = paramDeviceCommandsController;
        this.encryptionController = paramEncryptionContract;
        this.debuggerLogger = paramDebugLoggerContract;
        this.deviceMovementSource = PublishProcessor.<Boolean>create().toSerialized();
        this.touchEventSource = PublishProcessor.<String>create().toSerialized();
        this.carouselPositionEventSource = PublishProcessor.<MetricsCarouselPosition>create().toSerialized();
        this.lightEventSource = PublishProcessor.<LightNotification>create().toSerialized();
        this.deviceStateEventSource = PublishProcessor.<SHDeviceState>create().toSerialized();
        this.stmLogsSource = PublishProcessor.<String>create().toSerialized();
    }

    public Flowable<MetricsCarouselPosition> observeCarousel() {
        return this.carouselPositionEventSource;
    }

    public Flowable<SHDeviceState> observeDeviceState() {
        return this.deviceStateEventSource;
    }

    public Flowable<LightNotification> observeLight() {
        return this.lightEventSource;
    }

    public Flowable<String> observeStmLogs() {
        return this.stmLogsSource;
    }

    public Flowable<String> observeTouch() {
        return this.touchEventSource;
    }

    public Flowable<Boolean> observerMovement() {
        return this.deviceMovementSource;
    }

    public void onNewBleNotification(byte[] bArr, byte[] bArr2) {
        byte[] decryptBlePacket = SHEncryptionHelper.decryptBlePacket(bArr, bArr2, this.encryptionController.getEncryptionKey());
        if (decryptBlePacket == null || decryptBlePacket.length <= 0) {
            return;
        }
        if (decryptBlePacket[0] == 0) {
            if (decryptBlePacket.length == 3 && decryptBlePacket[2] >= 1 && decryptBlePacket[2] <= 32) {
                String str = "";
                int i = decryptBlePacket[1] & 255;
                for (int i2 = 0; i2 < decryptBlePacket[2]; i2++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(((i >> i2) & 1) == 1 ? 'l' : 's');
                    str = sb.toString();
                }
                this.touchEventSource.onNext(str);
                this.debuggerLogger.log(TAG, "TOUCH NOTIFICATION : " + str);
            }
        } else if (decryptBlePacket[0] == 2) {
            if (decryptBlePacket.length == 2 && decryptBlePacket[1] >= 0 && decryptBlePacket[1] <= 1) {
                this.deviceMovementSource.onNext(Boolean.valueOf(decryptBlePacket[1] == 1));
                this.debuggerLogger.log(TAG, "MOVEMENT NOTIFICATION: " + decryptBlePacket[1]);
            }
        } else if (decryptBlePacket[0] == 3) {
            if (decryptBlePacket.length < 3) {
                return;
            }
            boolean z = decryptBlePacket[1] == 1;
            this.lightEventSource.onNext(new LightNotification(decryptBlePacket[2] == 1, decryptBlePacket.length > 3 ? Boolean.valueOf(decryptBlePacket[3] == 1) : null, z));
            this.debuggerLogger.log(TAG, "LIGHT NOTIFICATION");
        } else if (decryptBlePacket[0] == 4) {
            this.deviceCommandsController.getDeviceState(new SHDeviceState.DeviceStateInterface() { // from class: bike.smarthalo.sdk.bluetooth.bleNotifications.-$$Lambda$BleNotificationController$S2Fh8luJO2En_AUwSUUswQ0phoM

                @Override // bike.smarthalo.sdk.models.SHDeviceState.DeviceStateInterface
                public final void onDeviceStateReady(SHDeviceState sHDeviceState) {
                    if (sHDeviceState != null) {
                        deviceStateEventSource.onNext(sHDeviceState);
                    }
                }
            });
        } else if (decryptBlePacket[0] == 5) {
            this.carouselPositionEventSource.onNext(MetricsCarouselPosition.getPosition(decryptBlePacket[1]));
        } else if (decryptBlePacket[0] == -8) {
            String str2 = new String(ArrayUtils.subarray(decryptBlePacket, 1, decryptBlePacket.length - 1));
            this.debuggerLogger.log(TAG, "STM LOG : " + str2);
            this.stmLogsSource.onNext(str2);
        } else {
            this.debuggerLogger.log(TAG, "Unknown notification " + ((int) decryptBlePacket[0]));
        }

    }
}
