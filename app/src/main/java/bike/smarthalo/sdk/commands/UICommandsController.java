package bike.smarthalo.sdk.commands;

import android.content.Context;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.DeviceInformationContract;
import bike.smarthalo.sdk.commands.CommandsContracts.ExperimentalUICommandsContract;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.commands.CommandsModels.GenericBleCommand;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.commands.CommandsModels.NotificationCommand;
import bike.smarthalo.sdk.commands.CommandsModels.TurnByTurnIntroMode;
import bike.smarthalo.sdk.commands.carousel.ProgressCommandPayload;
import bike.smarthalo.sdk.commands.carousel.SpeedometerCommandPayload;
import bike.smarthalo.sdk.firmware.NordicProtocolVersion;
import bike.smarthalo.sdk.models.DeviceInformation;
import bike.smarthalo.sdk.models.DeviceModel;
import bike.smarthalo.sdk.models.SHColour;
import bike.smarthalo.sdk.models.TransceiveCallback;

/* loaded from: classes.dex */
public class UICommandsController extends BaseCommandsController implements ExperimentalUICommandsContract {
  private DeviceInformationContract deviceInformationContract;

  public UICommandsController(Context context, TransceiveContract transceiveContract, DeviceInformationContract deviceInformationContract) {
    super(context, transceiveContract);
    this.deviceInformationContract = deviceInformationContract;
  }

  public void ui_nav(SHColour sHColour, int i, int i2, int i3, String str, boolean z, final CmdCallback cmdCallback) {
    byte[] addAll = ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_nav[0], CommandsConstants.cmd_nav[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) i, (byte) i2}, CommandsHelper.getFormatted4ByteMetric(i3, z));
    this.contract.transceive(ArrayUtils.addAll(addAll, CommandsHelper.getFormattedDescription(str, addAll.length)), true, "ui_nav", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.1


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_nav_reroute(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_navReroute, true, "ui_nav_reroute", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.2


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_nav_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_navOff, true, "ui_nav_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.3


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_frontLight(boolean z, boolean z2, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_frontlight[0], CommandsConstants.cmd_frontlight[1], z ? (byte) 1 : (byte) 0, z2 ? (byte) 1 : (byte) 0}, true, "ui_frontLight", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.4


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_nav_angle(SHColour sHColour, int i, int i2, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_navAngle[0], CommandsConstants.cmd_navAngle[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) i2}, true, "ui_nav_angle", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.5


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_nav_angle_quick(SHColour sHColour, int i, int i2, SHColour sHColour2, int i3, int i4, int i5, int i6, String str, boolean z, boolean z2, final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_2)) {
      byte[] formatted4ByteMetric = CommandsHelper.getFormatted4ByteMetric(i5, z);
      byte[] formatted4ByteMetric2 = CommandsHelper.getFormatted4ByteMetric(i6, z);
      if (z2 && this.deviceInformationContract.getDeviceInformation().getDeviceModel() == DeviceModel.SH2) {
        i2 += 128;
      }
      byte[] addAll = ArrayUtils.addAll(ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_navAngle[0], CommandsConstants.cmd_navAngle[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) i2, (byte) sHColour2.hue, (byte) sHColour2.saturation, (byte) sHColour2.lightness, (byte) ((i3 >> 8) & 255), (byte) (i3 & 255), (byte) i4}, formatted4ByteMetric), formatted4ByteMetric2);
      this.contract.transceive(ArrayUtils.addAll(addAll, CommandsHelper.getFormattedDescription(str, addAll.length)), true, "ui_nav_angle_quick", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.6


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
      return;
    }
    ui_nav_angle(sHColour, i, i2, cmdCallback);
  }

  public void ui_speedometer_speed(SpeedometerCommandPayload speedometerCommandPayload, final CmdCallback cmdCallback) {
    this.contract.transceive(ArrayUtils.addAll(CommandsConstants.cmd_speedometer, speedometerCommandPayload.getBytes()), true, "ui_speedometer_speed", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.7


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_speedometer_off(final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_speedometerOff[0], CommandsConstants.cmd_speedometerOff[1]}, true, "ui_speedometer_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.8


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_nav_roundabout(int i, int i2, SHColour sHColour, SHColour sHColour2, int[] iArr, int i3, boolean z, String str) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_2)) {
      int length = iArr.length <= 10 ? iArr.length * 2 : 20;
      byte[] bArr = new byte[CommandsConstants.cmd_roundabout.length + 1 + 1 + 3 + 3 + length];
      bArr[0] = CommandsConstants.cmd_roundabout[0];
      bArr[1] = CommandsConstants.cmd_roundabout[1];
      bArr[2] = (byte) i;
      bArr[3] = (byte) i2;
      bArr[4] = (byte) sHColour.hue;
      bArr[5] = (byte) sHColour.saturation;
      bArr[6] = (byte) sHColour.lightness;
      bArr[7] = (byte) sHColour2.hue;
      bArr[8] = (byte) sHColour2.saturation;
      bArr[9] = (byte) sHColour2.lightness;
      for (int i4 = 0; i4 < length; i4 += 2) {
        int i5 = iArr[i4 / 2];
        int i6 = i4 + 10;
        bArr[i6] = (byte) ((i5 >> 8) & 255);
        bArr[i6 + 1] = (byte) (i5 & 255);
      }
      this.contract.transceive(bArr, true, "ui_nav_roundabout", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.9

      });
      DeviceInformation deviceInformation = this.deviceInformationContract.getDeviceInformation();
      if (deviceInformation == null || deviceInformation.getDeviceModel() != DeviceModel.SH2 || str == null || str.isEmpty()) {
        return;
      }
      byte[] addAll = ArrayUtils.addAll(CommandsConstants.cmd_roundabout_oled, CommandsHelper.getFormatted4ByteMetric(i3, z));
      this.contract.transceive(ArrayUtils.addAll(addAll, CommandsHelper.getFormattedDescription(str, addAll.length)), true, "ui_roundabout_oled", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.10

      });
      return;
    }
    ui_nav_angle(sHColour, iArr[0], i == 1 ? 100 : 99, new CmdCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.11

    });
  }

  public void ui_progress(ProgressCommandPayload progressCommandPayload, final CmdCallback cmdCallback) {
    this.contract.transceive(ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_progress[0], CommandsConstants.cmd_progress[1]}, progressCommandPayload.getBytes()), true, "ui_progress", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.12


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_progress_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_progressOff, true, "ui_progress_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.13


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void sendNotificationCommand(final NotificationCommand notificationCommand) {
    DeviceInformation deviceInformation = this.deviceInformationContract.getDeviceInformation();
    if (deviceInformation == null) {
      return;
    }
    this.contract.transceive(notificationCommand.getBytes(deviceInformation.getDeviceModel()), true, "ui_notif", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.14


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        if (notificationCommand.callback != null) {
          notificationCommand.callback.onDone(bArr);
        }
      }
    });
  }

  public void ui_central_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_notif_off, true, "ui_central_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.15


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_hb(SHColour sHColour, SHColour sHColour2, int i, int i2, int i3, int i4, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_hb[0], CommandsConstants.cmd_hb[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) sHColour2.hue, (byte) sHColour2.saturation, (byte) sHColour2.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), (byte) ((i3 >> 8) & 255), (byte) (i3 & 255), (byte) ((i4 >> 8) & 255), (byte) (i4 & 255)}, true, "ui_hb", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.16


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_hb_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_hbOff, true, "ui_hb_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.17


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_compass(SHColour sHColour, int i, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_compass[0], CommandsConstants.cmd_compass[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255)}, true, "ui_compass", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.18


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_compass_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_compassOff, true, "ui_compass_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.19


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_logo(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_logo, true, "ui_logo", true, true, new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.20


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_disconnect(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_disconnect, true, "ui_disconnect", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.21


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_anim_off(final CmdCallback cmdCallback) {
    this.contract.transceive(CommandsConstants.cmd_animOff, true, "ui_anim_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.22


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_setBrightness(int i, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_setBrightness[0], CommandsConstants.cmd_setBrightness[1], (byte) i}, true, "ui_setBrightness", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.23


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_lowBat(final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_2_1)) {
      this.contract.transceive(new byte[]{CommandsConstants.cmd_lowBat[0], CommandsConstants.cmd_lowBat[1], 0}, true, "ui_lowbat", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.24


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
    }
  }

  public void ui_fitness_intro(SHColour sHColour, SHColour sHColour2, int i, final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_6)) {
      this.contract.transceive(new byte[]{CommandsConstants.cmd_fitness_intro[0], CommandsConstants.cmd_fitness_intro[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) sHColour2.hue, (byte) sHColour2.saturation, (byte) sHColour2.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255)}, true, "experimental_fitness_intro", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.25


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
    } else if (cmdCallback != null) {
      cmdCallback.onFail("Not supported");
    }
  }

  public void ui_speedometer_intro(final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_6)) {
      this.contract.transceive(CommandsConstants.cmd_speedometer_intro, true, "ui_progress_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.26


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
    } else if (cmdCallback != null) {
      cmdCallback.onFail("Not supported");
    }
  }

  public void ui_nav_pointer(SHColour sHColour, int i, boolean z, int i2, String str, final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_3)) {
      byte[] addAll = ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_nav_pointer[0], CommandsConstants.cmd_nav_pointer[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255)}, CommandsHelper.getFormatted4ByteMetric(i2, z));
      this.contract.transceive(ArrayUtils.addAll(addAll, CommandsHelper.getFormattedDescription(str, addAll.length)), true, "ui_nav_pointer", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.27


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
      return;
    }
    ui_nav_angle(sHColour, i, 99, cmdCallback);
  }

  public void ui_nav_pointer_off(final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_3)) {
      this.contract.transceive(new byte[]{CommandsConstants.cmd_nav_pointer_off[0], CommandsConstants.cmd_nav_pointer_off[1]}, true, "ui_nav_pointer_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.28


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          super.onData(bArr);
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
    } else {
      ui_nav_off(cmdCallback);
    }
  }

  public void ui_pointer_standby(SHColour sHColour, boolean z, int i, String str, final CmdCallback cmdCallback) {
    if (this.deviceInformationContract.isNordicFirmwareSufficient(NordicProtocolVersion.V1_3)) {
      byte[] addAll = ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_nav_pointer_standby[0], CommandsConstants.cmd_nav_pointer_standby[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness}, CommandsHelper.getFormatted4ByteMetric(i, z));
      this.contract.transceive(ArrayUtils.addAll(addAll, CommandsHelper.getFormattedDescription(str, addAll.length)), true, "ui_pointer_standby", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.29


        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          CmdCallback cmdCallback2 = cmdCallback;
          if (cmdCallback2 != null) {
            cmdCallback2.onDone(bArr);
          }
        }
      });
      return;
    }
    cmdCallback.onFail("Not supported");
  }

  public void ui_clock(GenericCommandPayload genericCommandPayload, final CmdCallback cmdCallback) {
    this.contract.transceive(ArrayUtils.addAll(new byte[]{CommandsConstants.cmd_clock[0], CommandsConstants.cmd_clock[1]}, genericCommandPayload.getBytes()), true, "ui_clock", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.30


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_clock_off(final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_clock_off[0], CommandsConstants.cmd_clock_off[1]}, true, "ui_clock_off", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.31


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_front_external_toggle(boolean z, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_front_external_toggle[0], CommandsConstants.cmd_front_external_toggle[1], z ? (byte) 1 : (byte) 0}, true, "ui_front_external_toggle", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.32


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_show_charge_state(final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_show_state_of_charger[0], CommandsConstants.cmd_show_state_of_charger[1]}, true, "exp_show_charge_state", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.33


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_turn_by_turn_intro(TurnByTurnIntroMode turnByTurnIntroMode, String str, final CmdCallback cmdCallback) {
    byte[] bArr = {CommandsConstants.cmd_turn_by_turn_intro[0], CommandsConstants.cmd_turn_by_turn_intro[1], (byte) turnByTurnIntroMode.getValue()};
    this.contract.transceive(ArrayUtils.addAll(bArr, CommandsHelper.getFormattedDescription(str, bArr.length)), true, "ui_turn_by_turn_intro", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.34


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr2) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr2);
        }
      }
    });
  }

  public void ui_pointer_intro_standby(SHColour sHColour, int i, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_pointer_with_intro[0], CommandsConstants.cmd_pointer_with_intro[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) i}, true, "ui_pointer_intro_standby", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.35


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void ui_pointer_intro(SHColour sHColour, int i, int i2, final CmdCallback cmdCallback) {
    this.contract.transceive(new byte[]{CommandsConstants.cmd_pointer_with_intro[0], CommandsConstants.cmd_pointer_with_intro[1], (byte) sHColour.hue, (byte) sHColour.saturation, (byte) sHColour.lightness, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) i2}, true, "ui_pointer_intro", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.36


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr);
        }
      }
    });
  }

  public void showDemo(int i, int i2, int i3, final CmdCallback cmdCallback) {
    byte[] bArr;
    if (i2 >= 0 && i3 >= 0) {
      bArr = new byte[]{0, 0, 0, (byte) i2, (byte) i3};
    } else if (i2 >= 0) {
      bArr = new byte[4];
      bArr[3] = (byte) i2;
    } else {
      bArr = new byte[3];
    }
    bArr[0] = CommandsConstants.cmd_demo[0];
    bArr[1] = CommandsConstants.cmd_demo[1];
    bArr[2] = (byte) i;
    this.contract.transceive(bArr, true, "showDemo", new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.37


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr2) {
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onDone(bArr2);
        }
      }
    });
  }

  public void sendGenericCommand(final GenericBleCommand genericBleCommand) {
    this.contract.transceive(genericBleCommand.getBytes(), genericBleCommand.isCrypted, genericBleCommand.description, new TransceiveCallback() { // from class: bike.smarthalo.sdk.commands.UICommandsController.38


      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        if (genericBleCommand.callback != null) {
          genericBleCommand.callback.onDone(bArr);
        }
      }
    });
  }
}