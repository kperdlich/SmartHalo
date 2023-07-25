package bike.smarthalo.sdk.encryption;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.KeyAgreement;

import bike.smarthalo.sdk.SHSdkHelpers;

public class CentralKeyEncryption {
  private static final String TAG = "CentralKeyEncryption";
  
  private AES_128_CBC_PKCS5 aes;
  
  private byte[] centralKey;
  
  public CentralKeyEncryption(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException {
    ECGenParameterSpec eCGenParameterSpec = new ECGenParameterSpec("secp256r1");
    PublicKey publicKey = KeyFactory.getInstance("ECDH", "SC").generatePublic(SHEncryptionHelper.convertP256Key(paramArrayOfbyte1));
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "SC");
    keyPairGenerator.initialize(eCGenParameterSpec);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "SC");
    keyAgreement.init(keyPair.getPrivate());
    keyAgreement.doPhase(publicKey, true);
    byte[] arrayOfByte3 = keyAgreement.generateSecret();
    arrayOfByte3[16] = (byte)paramArrayOfbyte2[0];
    arrayOfByte3[17] = (byte)paramArrayOfbyte2[1];
    arrayOfByte3[18] = (byte)paramArrayOfbyte2[2];
    arrayOfByte3[19] = (byte)paramArrayOfbyte2[3];
    String str = TAG;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("sharedSecret ");
    stringBuilder.append(SHSdkHelpers.bytesToHex(arrayOfByte3));
    Log.i(str, stringBuilder.toString());
    this.aes = new AES_128_CBC_PKCS5(arrayOfByte3);
    ECPublicKey eCPublicKey = (ECPublicKey)keyPair.getPublic();
    byte[] arrayOfByte2 = eCPublicKey.getW().getAffineX().toByteArray();
    arrayOfByte3 = eCPublicKey.getW().getAffineY().toByteArray();
    byte[] arrayOfByte1 = arrayOfByte2;
    if (arrayOfByte2.length == 33)
      arrayOfByte1 = Arrays.copyOfRange(arrayOfByte2, 1, 33); 
    arrayOfByte2 = arrayOfByte3;
    if (arrayOfByte3.length == 33)
      arrayOfByte2 = Arrays.copyOfRange(arrayOfByte3, 1, 33); 
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byteArrayOutputStream.write(arrayOfByte1);
    byteArrayOutputStream.write(arrayOfByte2);
    this.centralKey = byteArrayOutputStream.toByteArray();
  }
  
  public AES_128_CBC_PKCS5 getAes() {
    return this.aes;
  }
  
  public byte[] getCentralKey() {
    return this.centralKey;
  }
}
