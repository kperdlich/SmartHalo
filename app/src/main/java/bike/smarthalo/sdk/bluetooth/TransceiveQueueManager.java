package bike.smarthalo.sdk.bluetooth;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.Queue;

import bike.smarthalo.sdk.models.TransceiveTask;

/* loaded from: classes.dex */
public class TransceiveQueueManager {
  private static final String TAG = "TransceiveQueueManager";
  private TransceiveHandler transceiveHandler = new TransceiveHandler();
  private Queue<TransceiveTask> transceiveQueue = new LinkedList();

  /* loaded from: classes.dex */
  public interface GetItemCallback {
    void onResult(TransceiveTask transceiveTask, int i);
  }

  /* loaded from: classes.dex */
  public interface GetSizeCallback {
    void onResult(int i);
  }

  /* JADX INFO: Access modifiers changed from: package-private */
  public static /* synthetic */ void lambda$clearQueue$3() {
  }

  /* JADX INFO: Access modifiers changed from: package-private */
  public static /* synthetic */ void lambda$pollQueue$1(TransceiveTask transceiveTask, int i) {
  }

  public void dispose() {
    this.transceiveHandler.quit();
  }

  public void peekQueue(@NonNull final GetItemCallback getItemCallback) {
    this.transceiveHandler.post(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$tsBeG5r5JOWPY7PdTsLm9LGRZwM
      @Override // java.lang.Runnable
      public final void run() {
        getItemCallback.onResult(transceiveQueue.peek(), TransceiveQueueManager.this.transceiveQueue.size());
      }
    });
  }

  public void pollQueue() {
    pollQueue(new GetItemCallback() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$APwqaDx7qLcFZDYtzJdzmf6x3Vo
      @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetItemCallback
      public final void onResult(TransceiveTask transceiveTask, int i) {
        TransceiveQueueManager.lambda$pollQueue$1(transceiveTask, i);
      }
    });
  }

  public void pollQueue(@NonNull final GetItemCallback getItemCallback) {
    this.transceiveHandler.post(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$3CZojSJcWIJsePzh9SNWZrFsVrg
      @Override // java.lang.Runnable
      public final void run() {
        getItemCallback.onResult(transceiveQueue.poll(), TransceiveQueueManager.this.transceiveQueue.size());
      }
    });
  }

  public void clearQueue() {
    clearQueue(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$E4r_54PZoUvKDtjXyyENoNh4KLk
      @Override // java.lang.Runnable
      public final void run() {
        TransceiveQueueManager.lambda$clearQueue$3();
      }
    });
  }

  public void clearQueue(@NonNull final Runnable runnable) {
    this.transceiveHandler.post(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$XUOAusizTUz0hJeI_w5augzSZu0
      @Override // java.lang.Runnable
      public final void run() {
        TransceiveQueueManager.lambda$clearQueue$4(TransceiveQueueManager.this, runnable);
      }
    });
  }

  public static /* synthetic */ void lambda$clearQueue$4(TransceiveQueueManager transceiveQueueManager, Runnable runnable) {
    while (transceiveQueueManager.transceiveQueue.size() > 0) {
      TransceiveTask poll = transceiveQueueManager.transceiveQueue.poll();
      if (poll.cb != null) {
        poll.cb.onFail("Timeout");
      }
    }
    runnable.run();
  }

  public void getQueueSize(final GetSizeCallback getSizeCallback) {
    this.transceiveHandler.post(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$6yuuf0fr4MYxgXBi4ViKA38w9ys
      @Override // java.lang.Runnable
      public final void run() {
        getSizeCallback.onResult(TransceiveQueueManager.this.transceiveQueue.size());
      }
    });
  }

  public void addItem(final TransceiveTask transceiveTask, @NonNull final GetSizeCallback getSizeCallback) {
    this.transceiveHandler.post(new Runnable() { // from class: bike.smarthalo.sdk.bluetooth.-$$Lambda$TransceiveQueueManager$IiiCXyd-wFdeco8Kn_AU6ulwoh4
      @Override // java.lang.Runnable
      public final void run() {
        TransceiveQueueManager.lambda$addItem$6(TransceiveQueueManager.this, transceiveTask, getSizeCallback);
      }
    });
  }

  public static /* synthetic */ void lambda$addItem$6(TransceiveQueueManager transceiveQueueManager, TransceiveTask transceiveTask, GetSizeCallback getSizeCallback) {
    transceiveQueueManager.transceiveQueue.add(transceiveTask);
    getSizeCallback.onResult(transceiveQueueManager.transceiveQueue.size());
  }
}