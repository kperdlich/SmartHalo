package bike.smarthalo.sdk.helpers;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface DebugLoggerContract extends DebugLoggerDataContract {
  void dispose();
  
  List<String> getLogs();
  
  void log(String paramString1, String paramString2);
  
  Flowable<Exception> observeNonFatalExceptions();
  
  void reportNonFatalException(String paramString1, String paramString2);
}
