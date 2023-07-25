package bike.smarthalo.sdk.encryption;

import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_128_CBC_PKCS5 {
  private static final String TAG = "AES_128_CBC_PKCS5";
  
  byte[] IV = null;
  
  byte[] key = null;
  
  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }
  
  public AES_128_CBC_PKCS5(byte[] paramArrayOfbyte) {
    this.key = Arrays.copyOfRange(paramArrayOfbyte, 0, 16);
    this.IV = Arrays.copyOfRange(paramArrayOfbyte, 16, 32);
  }

  public byte[] crypt(int i, byte[] bArr) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
      cipher.init(i, new SecretKeySpec(this.key, "AES"), new IvParameterSpec(this.IV));
      return cipher.doFinal(bArr);
    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
      e.printStackTrace();
      Log.e(TAG, "ENCRYPTION EXCEPTION");
      return null;
    }
  }
}
