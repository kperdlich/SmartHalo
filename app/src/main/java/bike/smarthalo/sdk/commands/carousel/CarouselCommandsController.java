package bike.smarthalo.sdk.commands.carousel;

import android.content.Context;

import org.apache.commons.lang3.ArrayUtils;

import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.commands.BaseCommandsController;
import bike.smarthalo.sdk.commands.CommandsConstants;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.models.TransceiveCallback;

public class CarouselCommandsController extends BaseCommandsController {
  public CarouselCommandsController(Context paramContext, TransceiveContract paramTransceiveContract) {
    super(paramContext, paramTransceiveContract);
  }
  
  public void showCarousel(MetricsCarouselPosition paramMetricsCarouselPosition, MetricsCarouselMask paramMetricsCarouselMask, GenericCommandPayload paramGenericCommandPayload, final CmdCallback callback) {
    byte b1 = CommandsConstants.cmd_carousel[0];
    byte b2 = CommandsConstants.cmd_carousel[1];
    byte b3 = (byte)paramMetricsCarouselPosition.getValue();
    byte b4 = (byte)(paramMetricsCarouselMask.getMask() & 0xFF);
    byte b5 = (byte)(paramMetricsCarouselMask.getMask() >> 8 & 0xFF);
    byte[] arrayOfByte = paramGenericCommandPayload.getBytes();
    arrayOfByte = ArrayUtils.addAll(new byte[] { b1, b2, b3, b4, b5, 0, 0 }, arrayOfByte);
    this.contract.transceive(arrayOfByte, true, "showCarousel", new TransceiveCallback() {
          public void onData(byte[] param1ArrayOfbyte) {
            CmdCallback cmdCallback = callback;
            if (cmdCallback != null)
              cmdCallback.onDone(param1ArrayOfbyte); 
          }
        });
  }
  
  public void showCarouselPosition(MetricsCarouselPosition paramMetricsCarouselPosition, MetricsCarouselMask paramMetricsCarouselMask, final CmdCallback callback) {
    byte b1 = CommandsConstants.cmd_carousel_position[0];
    byte b2 = CommandsConstants.cmd_carousel_position[1];
    byte b3 = (byte)paramMetricsCarouselPosition.getValue();
    byte b4 = (byte)(paramMetricsCarouselMask.getMask() & 0xFF);
    byte b5 = (byte)(paramMetricsCarouselMask.getMask() >> 8 & 0xFF);
    TransceiveContract transceiveContract = this.contract;
    TransceiveCallback transceiveCallback = new TransceiveCallback() {
        public void onData(byte[] param1ArrayOfbyte) {
          CmdCallback cmdCallback = callback;
          if (cmdCallback != null)
            cmdCallback.onDone(param1ArrayOfbyte); 
        }
      };
    transceiveContract.transceive(new byte[] { b1, b2, b3, b4, b5, 0, 0 }, true, "showCarouselPosition", transceiveCallback);
  }
}
