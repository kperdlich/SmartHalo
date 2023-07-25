package bike.smarthalo.sdk.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bike.smarthalo.sdk.serviceStorage.CurrentDeviceContract;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;

/* loaded from: classes.dex */
public class DebugLogger implements DebugLoggerContract {
  private CurrentDeviceContract currentDeviceController;
  private List<String> logs = Collections.synchronizedList(new ArrayList());
  private FlowableProcessor<Exception> debugLogEvents = PublishProcessor.<Exception>create().toSerialized();

  public DebugLogger(CurrentDeviceContract currentDeviceContract) {
    this.currentDeviceController = currentDeviceContract;
  }

  @Override // bike.smarthalo.sdk.helpers.DebugLoggerContract
  public void dispose() {
    this.logs.clear();
  }

  @Override // bike.smarthalo.sdk.helpers.DebugLoggerContract
  public void log(String str, String str2) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    String str3 = simpleDateFormat.format(new Date()) + "  " + str;
    String str4 = "";
    if (this.currentDeviceController.getCurrentDevice() != null) {
      str4 = "" + this.currentDeviceController.getCurrentDevice().id.toUpperCase() + " ";
    }
    String str5 = str4 + str2;
    this.logs.add(str3 + " " + str5);
    Log.i(str, str5);
  }

  @Override // bike.smarthalo.sdk.helpers.DebugLoggerContract, bike.smarthalo.sdk.helpers.DebugLoggerDataContract
  public synchronized List<String> getLogs() {
    return new ArrayList(this.logs);
  }

  @Override // bike.smarthalo.sdk.helpers.DebugLoggerContract
  public void reportNonFatalException(String str, String str2) {
    this.debugLogEvents.onNext(new Exception(str2));
    Log.e(str, str2);
  }

  @Override // bike.smarthalo.sdk.helpers.DebugLoggerContract, bike.smarthalo.sdk.helpers.DebugLoggerDataContract
  public Flowable<Exception> observeNonFatalExceptions() {
    return this.debugLogEvents;
  }
}