package bike.smarthalo.sdk.bluetooth;

import androidx.annotation.Nullable;

import java.math.BigInteger;

public class BleHelper {
  @Nullable
  public static String getBootloaderAddress(String paramString) {
    String[] arrayOfString = paramString.split(":");
    if (arrayOfString.length == 6) {
      paramString = arrayOfString[5];
      if (paramString.equalsIgnoreCase("ff")) {
        paramString = "00";
      } else {
        String str = (new BigInteger(paramString, 16)).add(BigInteger.ONE).toString(16);
        paramString = str;
        if (str.length() == 1)
          paramString = "0".concat(str); 
      } 
      arrayOfString[5] = paramString;
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 0; b < arrayOfString.length; b++) {
        stringBuilder.append(arrayOfString[b].toUpperCase());
        if (b < arrayOfString.length - 1)
          stringBuilder.append(":"); 
      } 
      return stringBuilder.toString();
    } 
    return null;
  }
}
