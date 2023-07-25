package bike.smarthalo.sdk.models;

public abstract class TransceiveCallback {
  public static final String REASON_TIMEOUT = "Timeout";
  
  public static final String REASON_WRITEFAILED = "Write Failed";
  
  public void onData(byte[] paramArrayOfbyte) {}
  
  public void onFail(String paramString) {}
}
