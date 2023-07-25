package bike.smarthalo.sdk.commands;

import android.content.Context;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.DeviceInformationContract;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.commands.CommandsModels.AlarmReport;
import bike.smarthalo.sdk.firmware.NordicProtocolVersion;
import bike.smarthalo.sdk.models.TransceiveCallback;

/* loaded from: classes.dex */
public class AlarmCommandsController extends BaseCommandsController {
  private DeviceInformationContract deviceInformationContract;

  public AlarmCommandsController(Context context, TransceiveContract transceiveContract, DeviceInformationContract deviceInformationContract) {
    super(context, transceiveContract);
    this.deviceInformationContract = deviceInformationContract;
  }

  public void getAlarmReport(final AlarmReport.AlarmReportContract alarmReportContract) {
    this.contract.transceive(CommandsConstants.cmd_report, true, "alarm_report", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.AlarmCommandsController.1
      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        AlarmReport.AlarmReportContract alarmReportContract2 = alarmReportContract;
        if (alarmReportContract2 != null) {
          alarmReportContract2.onAlarmReportReady(new AlarmReport(bArr));
        }
      }
    });
  }

  public void get_alarm_seed(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_getSeed, true, "get_alarm_seed", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.AlarmCommandsController.2
      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void set_alarm_state(int i, int i2, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_arm[0], CommandsConstants.cmd_arm[1], (byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) (i2 & 255)}, true, "set_alarm_state", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.AlarmCommandsController.3
      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void resetAlarmConfig(byte[] bArr, CmdCallback cmdCallback) {
    configureAlarm(bArr, 0, 0, false, false, false, cmdCallback);
  }

  public void configureAlarm(byte[] bArr, int i, int i2, boolean z, boolean z2, boolean z3, final CmdCallback cmdCallback) {
    boolean z4;
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_2)) {
      z4 = true;
    } else if (z3) {
      if (cmdCallback != null) {
        cmdCallback.onResult(false);
        return;
      }
      return;
    } else {
      z4 = false;
    }
    if (bArr == null || bArr.length < 5) {
      if (cmdCallback != null) {
        cmdCallback.onResult(false);
        return;
      }
      return;
    }
    byte b = (byte) ((z2 ? 1 : 0) & 255);
    byte b2 = (byte) (i & 255);
    byte b3 = (byte) (i2 & 255);
    byte b4 = (byte) ((z ? 1 : 0) & 255);
    this.contract.transceive(z4 ? new byte[]{CommandsConstants.cmd_setConfig[0], CommandsConstants.cmd_setConfig[1], bArr[1], bArr[2], bArr[3], bArr[4], b2, b3, b4, b} : new byte[]{CommandsConstants.cmd_setConfig[0], CommandsConstants.cmd_setConfig[1], bArr[1], bArr[2], bArr[3], bArr[4], b2, b3, b4}, true, "configureAlarm", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.AlarmCommandsController.4
      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr2) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr2);
          cmdCallback.onResult(true);
        }
      }
    });
  }
}