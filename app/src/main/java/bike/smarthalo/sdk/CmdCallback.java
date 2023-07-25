package bike.smarthalo.sdk;

public abstract class CmdCallback {
  public static final String ERROR = "Error";
  
  public static final String REASON_TIMEOUT = "Timeout";
  
  public static final String REASON_WRITEFAILED = "Write Failed";
  
  public void onDone() {}
  
  public void onDone(byte[] paramArrayOfbyte) {}
  
  public void onErr(byte paramByte) {}
  
  public void onFail() {}
  
  public void onFail(String paramString) {}
  
  public void onResult(int paramInt) {}
  
  public void onResult(String paramString) {}
  
  public void onResult(boolean paramBoolean) {}
  
  public void onSuccess() {}
}
