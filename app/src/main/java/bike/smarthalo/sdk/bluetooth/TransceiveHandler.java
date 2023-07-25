package bike.smarthalo.sdk.bluetooth;

import android.os.Handler;
import android.util.Log;

public class TransceiveHandler {
  private static final String TAG = "TransceiveHandler";
  
  private static final String TRANSCEIVE_HANDLER_THREAD_NAME = "transceive_handler_thread";
  
  private Handler innerHandler;
  
  private TransceiveHandlerThread transceiveHandlerThread;
  
  public TransceiveHandler() {
    initialize();
  }
  
  private void initialize() {
    this.transceiveHandlerThread = new TransceiveHandlerThread("transceive_handler_thread");
    this.transceiveHandlerThread.start();
    this.innerHandler = new Handler(this.transceiveHandlerThread.getLooper());
  }
  
  public void post(Runnable paramRunnable) {
    if (!this.transceiveHandlerThread.isAlive() && !this.transceiveHandlerThread.isInitializing()) {
      Log.e(TAG, "Handler thread is dead, restarting connection");
      initialize();
    } else if (this.transceiveHandlerThread.isAlive() && !this.transceiveHandlerThread.isInitializing()) {
      this.innerHandler.post(paramRunnable);
    } 
  }
  
  public void quit() {
    this.transceiveHandlerThread.quitSafely();
  }
}
