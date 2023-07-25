package bike.smarthalo.sdk.encryption;

import android.util.Base64;

import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class SHEncryptionHelper {
  private static byte[] P256_HEAD = Base64.decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE", 0);
  
  public static X509EncodedKeySpec convertP256Key(byte[] paramArrayOfbyte) {
    byte[] arrayOfByte1 = P256_HEAD;
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + paramArrayOfbyte.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(paramArrayOfbyte, 0, arrayOfByte2, P256_HEAD.length, paramArrayOfbyte.length);
    return new X509EncodedKeySpec(arrayOfByte2);
  }
  
  public static byte[] decryptBlePacket(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, AES_128_CBC_PKCS5 paramAES_128_CBC_PKCS5) {
    byte b1 = paramArrayOfbyte1[0];
    byte b2 = paramArrayOfbyte1[1];
    paramArrayOfbyte1 = Arrays.copyOfRange(paramArrayOfbyte2, 0, b1 & 0xFF);
    return (b2 == 1) ? ((paramAES_128_CBC_PKCS5 != null) ? paramAES_128_CBC_PKCS5.crypt(2, paramArrayOfbyte1) : null) : paramArrayOfbyte1;
  }
  
  public static int get_CRC_CCITT_16_Checksum(byte[] paramArrayOfbyte) {
    byte b = 0;
    int i = 65535;
    while (b < paramArrayOfbyte.length) {
      i = (i << 8 & 0xFF00 | i >> 8 & 0xFF) ^ paramArrayOfbyte[b] & 0xFF;
      i ^= (i & 0xFF) >> 4;
      i ^= i << 8 << 4 & 0xFFFF;
      i ^= (i & 0xFF) << 4 << 1 & 0xFFFF;
      b++;
    } 
    return i;
  }
}
