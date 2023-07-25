package bike.smarthalo.sdk.commands.CommandsContracts;

import bike.smarthalo.sdk.models.TransceiveCallback;

public interface TransceiveContract {
  void transceive(byte[] paramArrayOfbyte, boolean paramBoolean, String paramString, TransceiveCallback paramTransceiveCallback);
  
  void transceive(byte[] paramArrayOfbyte, boolean paramBoolean1, String paramString, boolean paramBoolean2, boolean paramBoolean3, TransceiveCallback paramTransceiveCallback);
}
