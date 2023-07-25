package bike.smarthalo.sdk.helpers;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface DebugLoggerDataContract {
  List<String> getLogs();
  
  Flowable<Exception> observeNonFatalExceptions();
}
