package bike.smarthalo.sdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

/* loaded from: classes.dex */
public class SHSdkBuildConfigHelper {
  private static final String DEVELOPMENT_CONFIG_NAME = "debug";
  private static final String NIGHTLY_CONFIG_NAME = "nightly";
  private static final String RELEASE_CONFIG_NAME = "release";
  private static final String TAG = "SHSdkBuildConfigHelper";

  private static boolean isBuildConfig(String str) {
    return "release".equals(str);
  }

  public static boolean isReleaseBuildConfig() {
    return isBuildConfig("release");
  }

  public static boolean isDevelopmentBuildConfig() {
    return isBuildConfig(DEVELOPMENT_CONFIG_NAME);
  }

  /* JADX INFO: Access modifiers changed from: package-private */
  public static void launchParentServiceOnAuthentication(@NonNull Context context) {
    try {
      context.startService(new Intent(context, Class.forName(SHSDKConfigurationConstants.SERVICE_TO_LAUNCH_ON_AUTHENTICATION)));
    } catch (ClassNotFoundException unused) {
      Log.e(TAG, "Could not find parent service class.  Aborting launch");
    }
  }
}