package bike.smarthalo.sdk.models;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TransceiveTask {
  public boolean allowRetry;
  
  public TransceiveCallback cb;
  
  public long createdAt;
  
  public int crypted;
  
  public int currentSendPayloadIndex;
  
  public String description = "";
  
  public long receivedDataAt;
  
  public ByteArrayOutputStream recvPayloads;
  
  public int retryCount;
  
  public int sendLen;
  
  public List<byte[]> sendPayloads;
  
  public long sentAt;
}
