package bike.smarthalo.sdk.bluetooth.bleNotifications;

import bike.smarthalo.sdk.commands.carousel.MetricsCarouselPosition;
import bike.smarthalo.sdk.models.SHDeviceState;
import io.reactivex.rxjava3.core.Flowable;

public interface BleNotificationSourceContract {
  Flowable<MetricsCarouselPosition> observeCarousel();
  
  Flowable<SHDeviceState> observeDeviceState();
  
  Flowable<LightNotification> observeLight();
  
  Flowable<String> observeStmLogs();
  
  Flowable<String> observeTouch();
  
  Flowable<Boolean> observerMovement();
}
