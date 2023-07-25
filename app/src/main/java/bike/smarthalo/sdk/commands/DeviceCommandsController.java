package bike.smarthalo.sdk.commands;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.DeviceCommandsContract;
import bike.smarthalo.sdk.commands.CommandsContracts.DeviceInformationContract;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.firmware.FirmwareVersionHelper;
import bike.smarthalo.sdk.firmware.NordicFirmwareHelper;
import bike.smarthalo.sdk.firmware.NordicProtocolVersion;
import bike.smarthalo.sdk.models.DeviceInformation;
import bike.smarthalo.sdk.models.DeviceModel;
import bike.smarthalo.sdk.models.DeviceSerials;
import bike.smarthalo.sdk.models.SHDeviceState;
import bike.smarthalo.sdk.models.TransceiveCallback;

public class DeviceCommandsController extends BaseCommandsController implements DeviceInformationContract {
    private static final String TAG = "DeviceCommandsController";

    DeviceCommandsContract deviceContract;

    private DeviceInformation deviceInformation;

    public DeviceCommandsController(Context paramContext, TransceiveContract paramTransceiveContract, DeviceCommandsContract paramDeviceCommandsContract) {
        super(paramContext, paramTransceiveContract);
        this.deviceContract = paramDeviceCommandsContract;
    }

    private DeviceInformation saveDeviceInformation(byte[] paramArrayOfbyte, DeviceSerials paramDeviceSerials) {
        this.deviceInformation = DeviceInformation.build(paramArrayOfbyte, paramDeviceSerials);
        DeviceInformation deviceInformation = this.deviceInformation;
        if (deviceInformation != null)
            this.deviceContract.saveLastKnownFirmwareVersions(deviceInformation.nordicFirmwareVersion);
        return this.deviceInformation;
    }

    public void clearDeviceInformation() {
        this.deviceInformation = null;
    }

    public void cmd_compass_calibrate(final CmdCallback cb) {
        this.contract.transceive(CommandsConstants.cmd_compassCalibrate, true, "cmd_compass_calibrate", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                CmdCallback cmdCallback = cb;
                if (cmdCallback != null)
                    cmdCallback.onDone(param1ArrayOfbyte);
            }
        });
    }

    public void config_name(byte[] paramArrayOfbyte, final CmdCallback cb) {
        byte[] arrayOfByte = new byte[CommandsConstants.cmd_setName.length + paramArrayOfbyte.length];
        System.arraycopy(CommandsConstants.cmd_setName, 0, arrayOfByte, 0, CommandsConstants.cmd_setName.length);
        System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, CommandsConstants.cmd_setName.length, paramArrayOfbyte.length);
        this.contract.transceive(arrayOfByte, true, "config_name", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                super.onData(param1ArrayOfbyte);
                CmdCallback cmdCallback = cb;
                if (cmdCallback != null)
                    cmdCallback.onDone(param1ArrayOfbyte);
            }
        });
    }

    public void forceInstallGoldenFirmware(@NonNull final SuccessCallback callback) {
        this.contract.transceive(CommandsConstants.cmd_forceInstallGolden, true, "forceInstallGoldenFirmware", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                SuccessCallback successCallback = callback;
                boolean bool = false;
                if (param1ArrayOfbyte[0] == 0)
                    bool = true;
                successCallback.onResult(bool);
            }

            public void onFail(String param1String) {
                callback.onResult(false);
            }
        });
    }

    @Nullable
    public DeviceInformation getDeviceInformation() {
        return this.deviceInformation;
    }

    public void getDeviceSerial(int paramInt, final CmdCallback cmdCallback) {
        byte b1 = CommandsConstants.cmd_getSerial[0];
        byte b2 = CommandsConstants.cmd_getSerial[1];
        byte b3 = (byte) paramInt;
        TransceiveContract transceiveContract = this.contract;
        TransceiveCallback transceiveCallback = new TransceiveCallback() {
            public void onData(byte[] bArr) {
                if (bArr[0] == 0 && bArr.length >= 2) {
                    try {
                        cmdCallback.onResult(new String(Arrays.copyOfRange(bArr, 1, bArr.length), "UTF-8"));
                        return;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        cmdCallback.onFail(CmdCallback.ERROR);
                        return;
                    }
                }
                cmdCallback.onFail(CmdCallback.ERROR);
            }

        };
        transceiveContract.transceive(new byte[]{b1, b2, b3}, true, "cmd_getSerial", true, true, transceiveCallback);
    }

    public void getDeviceSerials(final DeviceModel deviceModel, final DeviceSerials.DeviceSerialsInterface deviceSerialsInterface) {
        getDeviceSerial(0, new CmdCallback() {
            public void onFail(String param1String) {
                deviceSerialsInterface.onDeviceSerialsReady(null);
            }

            public void onResult(final String product) {
                if (deviceModel == DeviceModel.SH2) {
                    deviceSerialsInterface.onDeviceSerialsReady(new DeviceSerials(product));
                    return;
                }
                DeviceCommandsController.this.getDeviceSerial(1, new CmdCallback() {
                    public void onFail(String param2String) {
                        deviceSerialsInterface.onDeviceSerialsReady(null);
                    }

                    public void onResult(final String pcba) {
                        DeviceCommandsController.this.getDeviceSerial(2, new CmdCallback() {
                            public void onFail(String param3String) {
                                deviceSerialsInterface.onDeviceSerialsReady(new DeviceSerials(product, pcba));
                            }

                            public void onResult(String param3String) {
                                deviceSerialsInterface.onDeviceSerialsReady(new DeviceSerials(product, pcba, param3String));
                            }
                        });
                    }
                });
            }
        });
    }

    public void getDeviceState(final SHDeviceState.DeviceStateInterface deviceStateInterface) {
        this.contract.transceive(CommandsConstants.cmd_getState, true, "device_getState", true, true, new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                deviceStateInterface.onDeviceStateReady(SHDeviceState.getSHDeviceState(param1ArrayOfbyte));
            }
        });
    }

    public boolean isNordicFirmwareSufficient(NordicProtocolVersion paramNordicProtocolVersion) {
        String str;
        DeviceInformation deviceInformation = this.deviceInformation;
        if (deviceInformation != null) {
            str = deviceInformation.nordicFirmwareVersion;
        } else {
            str = "";
        }
        return FirmwareVersionHelper.isFirmwareVersionSufficient(str, NordicFirmwareHelper.getProtocolString(paramNordicProtocolVersion));
    }

    public void requestBootloader(final CmdCallback cb, final boolean shouldRestartScan) {
        this.contract.transceive(CommandsConstants.cmd_enterBootloader, true, "device_enterBootloader", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                DeviceCommandsController.this.deviceContract.hasEnteredBootloader(shouldRestartScan);
                CmdCallback cmdCallback = cb;
                if (cmdCallback != null)
                    cmdCallback.onDone(param1ArrayOfbyte);
            }

            public void onFail(String param1String) {
                CmdCallback cmdCallback = cb;
                if (cmdCallback != null)
                    cmdCallback.onFail(param1String);
            }
        });
    }

    public void startTouchTest(@NonNull final SuccessCallback callback) {
        this.contract.transceive(CommandsConstants.cmd_touchTest, true, "touchTest", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                SuccessCallback successCallback = callback;
                boolean bool = false;
                if (param1ArrayOfbyte[0] == 0)
                    bool = true;
                successCallback.onResult(bool);
            }

            public void onFail(String param1String) {
                callback.onResult(false);
            }
        });
    }

    public void updateDeviceDate(final SuccessCallback callback) {
        byte b1 = CommandsConstants.cmd_localization[0];
        byte b2 = CommandsConstants.cmd_localization[1];
        byte[] arrayOfByte = CommandsHelper.getBytesFromLong(Math.round(System.currentTimeMillis() / 1000.0D), true);
        arrayOfByte = ArrayUtils.addAll(ArrayUtils.addAll(new byte[]{b1, b2}, arrayOfByte), Locale.getDefault().getLanguage().getBytes(StandardCharsets.ISO_8859_1));
        this.contract.transceive(arrayOfByte, true, "updateDeviceTime", new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                SuccessCallback successCallback = callback;
                if (successCallback != null) {
                    boolean bool = false;
                    if (param1ArrayOfbyte[0] == 0)
                        bool = true;
                    successCallback.onResult(bool);
                }
            }

            public void onFail(String param1String) {
                SuccessCallback successCallback = callback;
                if (successCallback != null)
                    successCallback.onResult(false);
            }
        });
    }

    public void updateDeviceInformation(final DeviceInformation.DeviceInformationCallback callback) {
        this.contract.transceive(CommandsConstants.cmd_getVersions, true, "device_getVersions", true, true, new TransceiveCallback() {
            public void onData(byte[] param1ArrayOfbyte) {
                DeviceCommandsController.this.getDeviceSerials(DeviceModel.geDeviceModel(param1ArrayOfbyte), new DeviceSerials.DeviceSerialsInterface() {
                    @Override
                    public void onDeviceSerialsReady(DeviceSerials param1DeviceSerials) {
                        callback.onDeviceInformationReady(DeviceCommandsController.this.saveDeviceInformation(param1ArrayOfbyte, param1DeviceSerials));
                    }
                });
            }
        });
    }
}
