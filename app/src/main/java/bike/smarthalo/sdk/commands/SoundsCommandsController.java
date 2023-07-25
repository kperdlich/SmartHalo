package bike.smarthalo.sdk.commands;

import android.content.Context;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.models.TransceiveCallback;

public class SoundsCommandsController extends BaseCommandsController {
  public SoundsCommandsController(Context paramContext, TransceiveContract paramTransceiveContract) {
    super(paramContext, paramTransceiveContract);
  }

  public void play_sound(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, final CmdCallback cb) {
    paramInt1 = StrictMath.min(100, paramInt1);
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length + 4];
    arrayOfByte[0] = (byte)CommandsConstants.cmd_play[0];
    arrayOfByte[1] = (byte)CommandsConstants.cmd_play[1];
    arrayOfByte[2] = (byte)(byte)paramInt1;
    arrayOfByte[3] = (byte)(byte)paramInt2;
    System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 4, paramArrayOfbyte.length);
    this.contract.transceive(arrayOfByte, true, "play_sound", new TransceiveCallback() {
          public void onData(byte[] param1ArrayOfbyte) {
            CmdCallback cmdCallback = cb;
            if (cmdCallback != null)
              cmdCallback.onDone(param1ArrayOfbyte);
          }
        });
  }

  public void stop_sound(final CmdCallback cb) {
    byte b1 = CommandsConstants.cmd_stop[0];
    byte b2 = CommandsConstants.cmd_stop[1];
    TransceiveContract transceiveContract = this.contract;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = cb;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte);
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2 }, true, "stop_sound", transceiveCallback);
  }

  public void touch_sounds(boolean paramBoolean, int paramInt, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_touch_sounds[0];
    byte b2 = CommandsConstants.cmd_touch_sounds[1];
    byte b3 = (byte)paramInt;
    byte b4 = (byte)(paramBoolean ? 1 : 0);
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte);
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3, b4 }, true, "touch_sounds", transceiveCallback);
  }
}
