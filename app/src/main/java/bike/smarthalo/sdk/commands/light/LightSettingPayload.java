package bike.smarthalo.sdk.commands.light;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsConstants;
import bike.smarthalo.sdk.commands.CommandsModels.GenericBleCommand;
import bike.smarthalo.sdk.commands.SuccessCallback;

/* loaded from: classes.dex */
public class LightSettingPayload extends GenericBleCommand {
  private int brightness;
  private boolean isBlinkingLocked;
  private LightMode lightMode;
  private boolean shouldSilenceRocker;

  public LightSettingPayload(LightMode lightMode, int i, boolean z, boolean z2, final SuccessCallback successCallback) {
    super(CommandsConstants.cmd_frontlightSettings, true, "light_settings", new CmdCallback() { // from class: bike.smarthalo.sdk.commands.light.LightSettingPayload.1
      @Override // bike.smarthalo.sdk.CmdCallback
      public void onDone(byte[] bArr) {
        successCallback.onResult(bArr[0] == 0);
      }
    });
    this.brightness = i;
    this.lightMode = lightMode;
    this.shouldSilenceRocker = z2;
  }

  @Override // bike.smarthalo.sdk.commands.CommandsModels.GenericBleCommand
  public byte[] getBytes() {
    return new byte[]{CommandsConstants.cmd_frontlightSettings[0], CommandsConstants.cmd_frontlightSettings[1], (byte) this.lightMode.getValue(), (byte) this.brightness, this.isBlinkingLocked ? (byte) 1 : (byte) 0, this.shouldSilenceRocker ? (byte) 1 : (byte) 0};
  }
}