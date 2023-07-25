package bike.smarthalo.sdk.commands;

import android.content.Context;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.CommandsContracts.ExperimentalUICommandsContract;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.models.OledAnimationType;
import bike.smarthalo.sdk.models.SHColour;
import bike.smarthalo.sdk.models.TransceiveCallback;

public class ExperimentalCommandsController extends BaseCommandsController {
  private ExperimentalUICommandsContract uiContract;
  
  public ExperimentalCommandsController(Context paramContext, ExperimentalUICommandsContract paramExperimentalUICommandsContract, TransceiveContract paramTransceiveContract) {
    super(paramContext, paramTransceiveContract);
    this.uiContract = paramExperimentalUICommandsContract;
  }
  
  public void calibrateSwipe(int paramInt, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_swipe_calibration[0];
    byte b2 = CommandsConstants.cmd_experimental_swipe_calibration[1];
    byte b3 = (byte)paramInt;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3 }, true, "calibrateSwipe", transceiveCallback);
  }
  
  public void calibrateTouch(int paramInt, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_touch_calibration[0];
    byte b2 = CommandsConstants.cmd_experimental_touch_calibration[1];
    byte b3 = (byte)paramInt;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3 }, true, "calibrateTouch", transceiveCallback);
  }
  
  public void setOledBrightness(int paramInt, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_oled_brightness[0];
    byte b2 = CommandsConstants.cmd_experimental_oled_brightness[1];
    byte b3 = (byte)(int)Math.floor(paramInt * 2.55D);
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3 }, true, "setOledBrightness", transceiveCallback);
  }
  
  public void setOledContrast(int paramInt, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_oled_contrast[0];
    byte b2 = CommandsConstants.cmd_experimental_oled_contrast[1];
    byte b3 = (byte)(int)Math.floor(paramInt * 2.55D);
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3 }, true, "setOledContrast", transceiveCallback);
  }
  
  public void showOled(int paramInt1, int paramInt2, OledAnimationType paramOledAnimationType, int paramInt3, int paramInt4, final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_show_oled[0];
    byte b2 = CommandsConstants.cmd_experimental_show_oled[1];
    byte b3 = (byte)paramInt1;
    byte b4 = (byte)paramInt2;
    byte b5 = OledAnimationType.getByteValue(paramOledAnimationType);
    byte b6 = (byte)(paramInt3 >> 8 & 0xFF);
    byte b7 = (byte)(paramInt3 & 0xFF);
    byte b8 = (byte)paramInt4;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3, b4, b5, b6, b7, b8 }, true, "showOled", transceiveCallback);
  }
  
  public void stopOled(final CmdCallback callback) {
    TransceiveContract transceiveContract = this.contract;
    byte b1 = CommandsConstants.cmd_experimental_stop_oled[0];
    byte b2 = CommandsConstants.cmd_experimental_stop_oled[1];
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2 }, true, "stopOled", transceiveCallback);
  }
  
  public void ui_nav_experimental_destination_angle(SHColour paramSHColour, int paramInt1, int paramInt2, final CmdCallback cb) {
    byte b1 = CommandsConstants.cmd_experimental_nav_destination_angle[0];
    byte b2 = CommandsConstants.cmd_experimental_nav_destination_angle[1];
    byte b3 = (byte)paramSHColour.hue;
    byte b4 = (byte)paramSHColour.saturation;
    byte b5 = (byte)paramSHColour.lightness;
    byte b6 = (byte)(paramInt1 >> 8 & 0xFF);
    byte b7 = (byte)(paramInt1 & 0xFF);
    byte b8 = (byte)paramInt2;
    TransceiveContract transceiveContract = this.contract;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          super.onData(param1ArrayOfbyte);
          CmdCallback cmdCallback = cb;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3, b4, b5, b6, b7, b8 }, true, "ui_nav_experimental_destination_angle", transceiveCallback);
  }
}
