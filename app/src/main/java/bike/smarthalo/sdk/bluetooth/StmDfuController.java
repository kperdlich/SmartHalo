package bike.smarthalo.sdk.bluetooth;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.bluetooth.stmDfuModels.StmDfuInformation;
import bike.smarthalo.sdk.bluetooth.stmDfuModels.StmDfuInstallResult;
import bike.smarthalo.sdk.bluetooth.stmDfuModels.StmDfuState;
import bike.smarthalo.sdk.commands.CommandResponse;
import bike.smarthalo.sdk.commands.DFUCommandsController;
import bike.smarthalo.sdk.commands.SuccessCallback;
import bike.smarthalo.sdk.helpers.DebugLoggerContract;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.ReplayProcessor;

/* loaded from: classes.dex */
public class StmDfuController {
    private static final int PACKET_MAX_SIZE = 64;
    private static final String TAG = "StmDfuController";
    private DebugLoggerContract debugLogger;
    private DFUCommandsController dfuCommandsController;
    private StmDfuInformation dfuInformation;
    private FlowableProcessor<Integer> dfuProgressSource = ReplayProcessor.<Integer>create(1).toSerialized();
    private boolean isTransferOngoing;

    /* loaded from: classes.dex */
    public interface DfuInstallCallback {
        void onResult(StmDfuInstallResult stmDfuInstallResult);
    }

    /* loaded from: classes.dex */
    public interface InitializeStmDfuCallback {
        void onResult(boolean z, StmDfuInformation stmDfuInformation);
    }

    public StmDfuController(DFUCommandsController dFUCommandsController, DebugLoggerContract debugLoggerContract) {
        this.dfuCommandsController = dFUCommandsController;
        this.dfuProgressSource.onNext(0);
        this.debugLogger = debugLoggerContract;
    }

    @Nullable
    public StmDfuInformation getCurrentStmDfuInformation() {
        return this.dfuInformation;
    }

    @Nullable
    public Flowable<Integer> getAndObserveDfuFlowSource() {
        return this.dfuProgressSource;
    }

    public void initializeStmDfu(final byte[] bArr, final InitializeStmDfuCallback initializeStmDfuCallback) {
        CRC32 crc32 = new CRC32();
        int i = 0;
        crc32.update(bArr, 0, bArr.length);
        long value = crc32.getValue();
        final ArrayList arrayList = new ArrayList();
        int ceil = (int) Math.ceil(bArr.length / 64.0d);
        while (i < ceil) {
            int i2 = i * 64;
            arrayList.add(Arrays.copyOfRange(bArr, i2, i == ceil + (-1) ? bArr.length : i2 + 64));
            i++;
        }
        this.dfuCommandsController.sendStmDfuCrc(value, bArr.length, new DFUCommandsController.CrcCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$StmDfuController$-wDwtmqk3z7PJthSM22LZwaGXzQ
            @Override // bike.smarthalo.sdk.commands.DFUCommandsController.CrcCallback
            public final void onResult(CommandResponse commandResponse, int i3) {
                StmDfuController.lambda$initializeStmDfu$0(StmDfuController.this, arrayList, bArr, initializeStmDfuCallback, commandResponse, i3);
            }
        });
    }

    public static /* synthetic */ void lambda$initializeStmDfu$0(StmDfuController stmDfuController, List list, byte[] bArr, InitializeStmDfuCallback initializeStmDfuCallback, CommandResponse commandResponse, int i) {
        if (commandResponse == CommandResponse.OK || commandResponse == CommandResponse.Unnecessary) {
            if (commandResponse != CommandResponse.OK) {
                i = list.size();
            }
            stmDfuController.dfuInformation = new StmDfuInformation(list, i, bArr);
            stmDfuController.dfuProgressSource.onNext(Integer.valueOf(stmDfuController.dfuInformation.getProgressPercentage()));
            initializeStmDfuCallback.onResult(true, stmDfuController.dfuInformation);
            return;
        }
        initializeStmDfuCallback.onResult(false, null);
    }

    public void cancelDfuFlow() {
        cleanUpSequence();
    }

    private void sendNextFirmwareWindow() {
        if (!isUpdateInitialized()) {
            this.debugLogger.log(TAG, "DFU was cancelled by user");
            return;
        }
        byte[] bArr = this.dfuInformation.getPayloads().get(this.dfuInformation.getCurrentPosition());
        this.dfuCommandsController.sendStmDfuData(this.dfuInformation.getCurrentPosition(), bArr.length, bArr, new SuccessCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$StmDfuController$5oakvkY6ckhuGQQk9n1yw2QGFuU
            @Override // bike.smarthalo.sdk.commands.SuccessCallback
            public final void onResult(boolean z) {
                StmDfuController.lambda$sendNextFirmwareWindow$2(StmDfuController.this, z);
            }
        });
    }

    public static /* synthetic */ void lambda$sendNextFirmwareWindow$2(final StmDfuController stmDfuController, boolean z) {
        if (stmDfuController.isUpdateInitialized()) {
            if (!z) {
                stmDfuController.debugLogger.log(TAG, "Error on payload sent, resending CRC");
                stmDfuController.isTransferOngoing = false;
                StmDfuInformation stmDfuInformation = stmDfuController.dfuInformation;
                if (stmDfuInformation == null) {
                    return;
                }
                stmDfuController.initializeStmDfu(stmDfuInformation.getFirmwareBytes(), new InitializeStmDfuCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$StmDfuController$RLvJ462gfaVRBhM3CtLMaqdUEDE
                    @Override
                    // bike.smarthalo.sdk.bluetooth.StmDfuController.InitializeStmDfuCallback
                    public final void onResult(boolean z2, StmDfuInformation stmDfuInformation2) {
                        if (z2) {
                            stmDfuController.startStmDfu();
                        } else {
                            stmDfuController.cancelDfuFlow();
                        }
                    }
                });
                return;
            }
            int progressPercentage = stmDfuController.dfuInformation.getProgressPercentage();
            stmDfuController.dfuInformation.incrementCurrentPosition();
            int progressPercentage2 = stmDfuController.dfuInformation.getProgressPercentage();
            if (progressPercentage2 > progressPercentage) {
                DebugLoggerContract debugLoggerContract = stmDfuController.debugLogger;
                String str = TAG;
                debugLoggerContract.log(str, "DFU Progress : " + progressPercentage2 + "%");
            }
            stmDfuController.dfuProgressSource.onNext(Integer.valueOf(progressPercentage2));
            if (stmDfuController.dfuInformation.getCurrentState() == StmDfuState.Ongoing) {
                stmDfuController.sendNextFirmwareWindow();
            }
        }
    }

    public static /* synthetic */ void lambda$null$1(StmDfuController stmDfuController, boolean z, StmDfuInformation stmDfuInformation) {
        if (z) {
            stmDfuController.startStmDfu();
        } else {
            stmDfuController.cancelDfuFlow();
        }
    }

    public Flowable<Integer> startStmDfu() {
        if (isUpdateInitialized() && this.dfuInformation.getCurrentState() != StmDfuState.Completed && !this.isTransferOngoing) {
            this.isTransferOngoing = true;
            sendNextFirmwareWindow();
        }
        return this.dfuProgressSource;
    }

    public void installTransferredFirmware(final DfuInstallCallback dfuInstallCallback) {
        StmDfuInformation stmDfuInformation = this.dfuInformation;
        if (stmDfuInformation != null && stmDfuInformation.getCurrentState() == StmDfuState.Completed) {
            this.dfuCommandsController.installStmDfu(new CmdCallback() { // from class: bike.smarthalo.sdk.bluetooth.StmDfuController.1
                @Override // bike.smarthalo.sdk.CmdCallback
                public void onDone(byte[] bArr) {
                    if (bArr[0] == 0) {
                        dfuInstallCallback.onResult(StmDfuInstallResult.Success);
                        StmDfuController.this.cleanUpSequence();
                    } else if (bArr[0] == 1) {
                        dfuInstallCallback.onResult(StmDfuInstallResult.InstallError);
                    } else if (bArr[0] == 2) {
                        dfuInstallCallback.onResult(StmDfuInstallResult.LowBattery);
                    }
                }
            });
        } else {
            dfuInstallCallback.onResult(StmDfuInstallResult.NotTransferred);
        }
    }

    public void forceInstallGoldenFirmware(final SuccessCallback successCallback) {
        this.dfuCommandsController.forceInstallGoldenFirmware(new SuccessCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$StmDfuController$Lp_k5axpWUk4A6dHgF2Io_JNptw
            @Override // bike.smarthalo.sdk.commands.SuccessCallback
            public final void onResult(boolean z) {
                StmDfuController.lambda$forceInstallGoldenFirmware$3(StmDfuController.this, successCallback, z);
            }
        });
    }

    public static /* synthetic */ void lambda$forceInstallGoldenFirmware$3(StmDfuController stmDfuController, SuccessCallback successCallback, boolean z) {
        if (z) {
            stmDfuController.cleanUpSequence();
        }
        successCallback.onResult(z);
    }

    private boolean isUpdateInitialized() {
        return this.dfuInformation != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpSequence() {
        this.dfuInformation = null;
        this.isTransferOngoing = false;
    }
}