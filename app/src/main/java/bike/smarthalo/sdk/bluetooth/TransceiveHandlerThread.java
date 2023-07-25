package bike.smarthalo.sdk.bluetooth;

import android.os.HandlerThread;
import android.util.Log;

/* loaded from: classes.dex */
public class TransceiveHandlerThread extends HandlerThread {
  private static final String TAG = "TransceiveHandlerThread";
  private boolean isInitializing;

  public TransceiveHandlerThread(String str) {
    super(str);
    this.isInitializing = false;
  }

  @Override // android.os.HandlerThread
  protected void onLooperPrepared() {
    Log.e(TAG, "Transceiver Handler Thread looper is prepared");
    this.isInitializing = false;
  }

  @Override // java.lang.Thread
  public synchronized void start() {
    this.isInitializing = true;
    super.start();
  }

  public synchronized boolean isInitializing() {
    return this.isInitializing;
  }
}