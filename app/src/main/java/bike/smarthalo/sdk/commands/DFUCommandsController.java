package bike.smarthalo.sdk.commands;

import android.content.Context;

import androidx.annotation.NonNull;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.models.TransceiveCallback;

/* loaded from: classes.dex */
public class DFUCommandsController extends BaseCommandsController {
    private static final int STM_DFU_PACKET_HEADERS_SIZE = 5;

    /* loaded from: classes.dex */
    public interface CrcCallback {
        void onResult(CommandResponse commandResponse, int i);
    }

    public DFUCommandsController(Context context, TransceiveContract transceiveContract) {
        super(context, transceiveContract);
    }

    public void installStmDfu(@NonNull final CmdCallback cmdCallback) {
        this.contract.transceive(CommandsConstants.cmd_deviceStmDfuInstall, true, "installSTMDFU", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.DFUCommandsController.1
            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onData(byte[] bArr) {
                cmdCallback.onDone(bArr);
            }

            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onFail(String str) {
                cmdCallback.onFail();
            }
        });
    }

    public void forceInstallGoldenFirmware(@NonNull final SuccessCallback successCallback) {
        this.contract.transceive(CommandsConstants.cmd_forceInstallGolden, true, "forceInstallGoldenFirmware", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.DFUCommandsController.2
            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onData(byte[] bArr) {
                successCallback.onResult(bArr[0] == 0);
            }

            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onFail(String str) {
                successCallback.onResult(false);
            }
        });
    }

    public void sendStmDfuData(int i, int i2, byte[] bArr, @NonNull final SuccessCallback successCallback) {
        byte[] bArr2 = new byte[bArr.length + 5];
        bArr2[0] = CommandsConstants.cmd_deviceStmDfuData[0];
        bArr2[1] = CommandsConstants.cmd_deviceStmDfuData[1];
        bArr2[2] = (byte) ((i >> 8) & 255);
        bArr2[3] = (byte) (i & 255);
        bArr2[4] = (byte) i2;
        for (int i3 = 5; i3 < bArr2.length; i3++) {
            bArr2[i3] = bArr[i3 - 5];
        }
        this.contract.transceive(bArr2, true, "sendStmDfuData", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.DFUCommandsController.3
            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onData(byte[] bArr3) {
                successCallback.onResult(bArr3[0] == 0);
            }

            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onFail(String str) {
                successCallback.onResult(false);
            }
        });
    }

    public void sendStmDfuCrc(long j, int i, @NonNull final CrcCallback crcCallback) {
        this.contract.transceive(new byte[]{CommandsConstants.cmd_deviceStmDfuCrc[0], CommandsConstants.cmd_deviceStmDfuCrc[1], (byte) ((j >> 24) & 255), (byte) ((j >> 16) & 255), (byte) ((j >> 8) & 255), (byte) (j & 255), (byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)}, true, "sendStmDfuCrc", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.DFUCommandsController.4
            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onData(byte[] bArr) {
                int i2;
                CommandResponse fromInt = CommandResponse.fromInt(bArr[0]);
                if ((fromInt == CommandResponse.OK || fromInt == CommandResponse.Unnecessary) && bArr.length == 3) {
                    i2 = ((bArr[1] & 255) * 256) + (bArr[2] & 255);
                } else {
                    fromInt = CommandResponse.Fail;
                    i2 = -1;
                }
                crcCallback.onResult(fromInt, i2);
            }

            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onFail(String str) {
                crcCallback.onResult(CommandResponse.Fail, -1);
            }
        });
    }
}