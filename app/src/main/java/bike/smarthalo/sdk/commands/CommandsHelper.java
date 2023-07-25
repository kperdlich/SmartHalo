package bike.smarthalo.sdk.commands;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;

public class CommandsHelper {
  public static byte[] getBytesFromLong(long paramLong, boolean paramBoolean) {
    byte[] arrayOfByte = new byte[8];
    for (byte b = 7; b >= 0; b--) {
      arrayOfByte[b] = (byte)(byte)(int)(0xFFL & paramLong);
      paramLong >>= 8L;
    } 
    if (paramBoolean)
      ArrayUtils.reverse(arrayOfByte); 
    return arrayOfByte;
  }
  
  public static byte[] getFormatted4ByteMetric(int paramInt, boolean paramBoolean) {
    return new byte[] { (byte)((paramInt >> 24 & 0xFF) + getIsMetricModifier(paramBoolean)), (byte)(paramInt >> 16 & 0xFF), (byte)(paramInt >> 8 & 0xFF), (byte)(paramInt & 0xFF) };
  }
  
  public static byte[] getFormattedDescription(String paramString, int paramInt) {
    String str = paramString;
    if (paramString == null)
      str = ""; 
    int i = 80 - paramInt;
    paramInt = i;
    if (str.length() + 1 < i)
      paramInt = str.length() + 1; 
    byte[] arrayOfByte1 = new byte[paramInt];
    byte[] arrayOfByte2 = str.getBytes(StandardCharsets.ISO_8859_1);
    for (paramInt = 0; paramInt < arrayOfByte1.length - 1; paramInt++)
      arrayOfByte1[paramInt] = (byte)arrayOfByte2[paramInt]; 
    arrayOfByte1[arrayOfByte1.length - 1] = (byte)0;
    return arrayOfByte1;
  }
  
  public static int getIsMetricModifier(boolean paramBoolean) {
    char c;
    if (paramBoolean) {
      c = Character.MIN_VALUE;
    } else {
      c = '';
    } 
    return c;
  }
  
  public static String getStringPayload(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      byte b1 = paramArrayOfbyte[b];
      if (b1 < 0)
        b1 += 256; 
      stringBuilder.append(b1);
      if (b < paramArrayOfbyte.length - 1)
        stringBuilder.append(","); 
    } 
    return stringBuilder.toString();
  }
}
