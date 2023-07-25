package bike.smarthalo.sdk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

/* loaded from: classes.dex */
public class SHNotificationManager {
  public static final String NOTIFICATION_CHANNEL_ID = "sh_device_service_notification";
  private static final String NOTIFICATION_CHANNEL_NAME = "Missing bluetooth permissions";
  public static final String SERVICE_CHANNEL_ID = "sh_device_service";
  private static final String SERVICE_CHANNEL_NAME = "Bluetooth connection";

  /* loaded from: classes.dex */
  public enum NotifType {
    NO_FEATURE,
    NO_PERMISSION
  }

  public static Notification getServiceStopNotification(NotifType notifType, @NonNull Context context) {
    String string;
    String string2;
    PendingIntent pendingIntent;
    switch (notifType) {
      case NO_FEATURE:
        string = context.getString(R.string.no_feature_title);
        string2 = context.getString(R.string.no_feature);
        pendingIntent = null;
        break;
      case NO_PERMISSION:
        string = context.getString(R.string.no_permission_title);
        string2 = context.getString(R.string.no_permission);
        Intent intent = new Intent(context, RequestPermissionsActivity.class);
        intent.setFlags(603979776);
        pendingIntent = getNotificationPendingIntentAndCancelIfNeeded(intent, context);
        break;
      default:
        return null;
    }
    NotificationCompat.Builder notificationBuilder = getNotificationBuilder(context, NOTIFICATION_CHANNEL_ID);
    notificationBuilder.setDefaults(-1).setVibrate(null).setContentTitle(string).setContentText(string2).setStyle(new NotificationCompat.BigTextStyle().bigText(string2)).setAutoCancel(true).setOngoing(false).setSmallIcon(R.drawable.service_notification_connected).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
    if (!SHSdkHelpers.isAtLeastOreo()) {
      notificationBuilder.setPriority(1);
    }
    if (pendingIntent == null) {
      pendingIntent = getNotificationPendingIntentAndCancelIfNeeded(new Intent(SHDeviceServiceIntents.BROADCAST_LAUNCH_APP), context);
    }
    return notificationBuilder.setContentIntent(pendingIntent).build();
  }

  public static Notification getForegroundServiceNotification(Context context, String str, boolean z) {
    String string = context.getString(R.string.app_name);
    PendingIntent notificationPendingIntentAndCancelIfNeeded = getNotificationPendingIntentAndCancelIfNeeded(new Intent(SHDeviceServiceIntents.BROADCAST_LAUNCH_APP), context);
    String format = String.format(context.getString(z ? R.string.connecting : R.string.connected), str);
    NotificationCompat.Builder notificationBuilder = getNotificationBuilder(context, SERVICE_CHANNEL_ID);
    notificationBuilder.setContentTitle(string).setContentText(format).setStyle(new NotificationCompat.BigTextStyle().bigText(format)).setContentIntent(notificationPendingIntentAndCancelIfNeeded).setAutoCancel(false).setOngoing(true).setSmallIcon(z ? R.drawable.service_notification_connecting : R.drawable.service_notification_connected).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
    if (!SHSdkHelpers.isAtLeastOreo()) {
      notificationBuilder.setPriority(-1);
    }
    return notificationBuilder.build();
  }

  private static PendingIntent getNotificationPendingIntent(@NonNull Intent intent, @NonNull Context context) {
    if (SHDeviceServiceIntents.BROADCAST_LAUNCH_APP.equals(intent.getAction())) {
      return PendingIntent.getBroadcast(context, 55, intent, 268435456);
    }
    return PendingIntent.getActivity(context, 55, intent, 268435456);
  }

  public static PendingIntent getNotificationPendingIntentAndCancelIfNeeded(@NonNull Intent intent, @NonNull Context context) {
    return getNotificationPendingIntent(intent, context);
  }

  public static void showNotification(Notification notification, Context context) {
    NotificationManager notificationManager;
    if (context == null || (notificationManager = (NotificationManager) context.getSystemService("notification")) == null) {
      return;
    }
    notificationManager.notify(101, notification);
  }

  public static void initializeNotificationChannel(Context context) {
    if (SHSdkHelpers.isAtLeastOreo()) {
      NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, 4);
      notificationChannel.setLockscreenVisibility(1);
      NotificationChannel notificationChannel2 = new NotificationChannel(SERVICE_CHANNEL_ID, SERVICE_CHANNEL_NAME, 2);
      notificationChannel2.setLockscreenVisibility(1);
      notificationChannel2.enableVibration(false);
      notificationChannel2.enableLights(false);
      notificationChannel2.setSound(null, null);
      NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.createNotificationChannel(notificationChannel2);
      }
    }
  }

  private static NotificationCompat.Builder getNotificationBuilder(Context context, String str) {
    return SHSdkHelpers.isAtLeastOreo() ? new NotificationCompat.Builder(context, str) : new NotificationCompat.Builder(context);
  }
}