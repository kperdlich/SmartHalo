package bike.smarthalo.sdk.stmUtils;

import io.reactivex.rxjava3.core.Flowable;

public interface SmartHaloOSCrashReporterContract {
  void dispose();
  
  Flowable<SmartHaloOSCrash> observeSmartHaloOSCrash();
}
