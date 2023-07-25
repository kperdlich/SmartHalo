package bike.smarthalo.sdk;

import android.content.Context;
import android.os.Handler;

public class ContextHelper {
  public static Handler getMainLooperHandler(Context paramContext) {
    return new Handler(paramContext.getMainLooper());
  }
  
  public static void runOnMainThread(Context paramContext, Runnable paramRunnable) {
    if (paramContext != null)
      getMainLooperHandler(paramContext).post(paramRunnable); 
  }
  
  public static void runOnMainThreadDelayed(Context paramContext, long paramLong, Runnable paramRunnable) {
    if (paramContext != null)
      getMainLooperHandler(paramContext).postDelayed(paramRunnable, paramLong); 
  }
}
