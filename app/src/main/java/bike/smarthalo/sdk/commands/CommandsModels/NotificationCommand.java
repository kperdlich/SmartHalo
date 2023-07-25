package bike.smarthalo.sdk.commands.CommandsModels;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsConstants;
import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.models.DeviceModel;
import bike.smarthalo.sdk.models.SHColour;

public class NotificationCommand {
  public CmdCallback callback;
  
  public int fadeIn;
  
  public int fadeout;
  
  @NonNull
  public SHColour firstColour;
  
  public int off;
  
  public int on;
  
  public int repeat;
  
  @NonNull
  public SHColour secondColour;
  
  public String text;
  
  public NotificationType type;
  
  public NotificationCommand(@NonNull SHColour paramSHColour1, @NonNull SHColour paramSHColour2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CmdCallback paramCmdCallback) {
    this.callback = paramCmdCallback;
    this.fadeout = paramInt3;
    this.fadeIn = paramInt1;
    this.on = paramInt2;
    this.off = paramInt4;
    this.firstColour = paramSHColour1;
    this.secondColour = paramSHColour2;
    this.repeat = paramInt5;
    this.type = null;
    this.text = null;
  }
  
  public NotificationCommand(@NonNull SHColour paramSHColour1, @NonNull SHColour paramSHColour2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, NotificationType paramNotificationType, String paramString, CmdCallback paramCmdCallback) {
    this.callback = paramCmdCallback;
    this.fadeout = paramInt3;
    this.fadeIn = paramInt1;
    this.on = paramInt2;
    this.off = paramInt4;
    this.firstColour = paramSHColour1;
    this.secondColour = paramSHColour2;
    this.repeat = paramInt5;
    this.type = paramNotificationType;
    this.text = paramString;
  }
  
  public byte[] getBytes(DeviceModel paramDeviceModel) {
    int i = this.fadeIn;
    byte b1 = (byte)(i >> 8 & 0xFF);
    byte b2 = (byte)(i & 0xFF);
    int j = this.on;
    i = (byte)(j >> 8 & 0xFF);
    j = (byte)(j & 0xFF);
    int k = this.fadeout;
    byte b3 = (byte)(k >> 8 & 0xFF);
    byte b4 = (byte)(k & 0xFF);
    int m = this.off;
    k = (byte)(m >> 8 & 0xFF);
    m = (byte)(m & 0xFF);
    byte[] arrayOfByte1 = new byte[17];
    arrayOfByte1[0] = (byte)CommandsConstants.cmd_notif[0];
    arrayOfByte1[1] = (byte)CommandsConstants.cmd_notif[1];
    arrayOfByte1[2] = (byte)(byte)this.firstColour.hue;
    arrayOfByte1[3] = (byte)(byte)this.firstColour.saturation;
    arrayOfByte1[4] = (byte)(byte)this.firstColour.lightness;
    arrayOfByte1[5] = (byte)(byte)this.secondColour.hue;
    arrayOfByte1[6] = (byte)(byte)this.secondColour.saturation;
    arrayOfByte1[7] = (byte)(byte)this.secondColour.lightness;
    arrayOfByte1[8] = (byte)b1;
    arrayOfByte1[9] = (byte)b2;
    arrayOfByte1[10] = (byte)i;
    arrayOfByte1[11] = (byte)j;
    arrayOfByte1[12] = (byte)b3;
    arrayOfByte1[13] = (byte)b4;
    arrayOfByte1[14] = (byte)k;
    arrayOfByte1[15] = (byte)m;
    arrayOfByte1[16] = (byte)(byte)this.repeat;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (paramDeviceModel == DeviceModel.SH2) {
      byte[] arrayOfByte = ArrayUtils.addAll(arrayOfByte1, new byte[] { (byte)this.type.getValue() });
      arrayOfByte2 = ArrayUtils.addAll(arrayOfByte, CommandsHelper.getFormattedDescription(this.text, arrayOfByte.length));
    } 
    return arrayOfByte2;
  }
}
