package bike.smarthalo.sdk;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RequestPermissionsActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    //setContentView(R.layout.activity_dialog);
    setContentView(bike.smarthalo.R.layout.activity_main2);

    if (!SHSdkHelpers.checkPermissions((Context)this, SHSdkHelpers.getBlePermissions())) {
      SHSdkHelpers.requestPermissions(this, SHSdkHelpers.getBlePermissions(), 100, R.string.app_name);
    } else {
      SHSdkHelpers.startScanningAndFinishActivity(this);
    } 
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
    if (paramInt == 100)
      if (paramArrayOfint.length > 0 && paramArrayOfint[0] == 0) {
        SHSdkHelpers.startScanningAndFinishActivity(this);
      } else {
        SHSdkHelpers.startScanningAndFinishActivity(this);
      }  
  }
}
