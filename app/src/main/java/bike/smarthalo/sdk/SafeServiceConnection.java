package bike.smarthalo.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SafeServiceConnection implements ServiceConnection {
  protected Context context;
  
  private int flags;
  
  private ServiceConnection innerConnection;
  
  private boolean isBound = false;
  
  private Class serviceClass;
  
  private Intent serviceIntent;
  
  public SafeServiceConnection(ServiceConnection paramServiceConnection, Context paramContext, Class paramClass, int paramInt) {
    this.innerConnection = paramServiceConnection;
    this.serviceClass = paramClass;
    this.flags = paramInt;
    this.context = paramContext;
    this.serviceIntent = new Intent(paramContext, paramClass);
  }
  
  public void bindService() {
    if (!this.isBound) {
      this.context.bindService(this.serviceIntent, this, this.flags);
      this.isBound = true;
    } 
  }
  
  public boolean isServiceBound() {
    return this.isBound;
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder) {
    this.innerConnection.onServiceConnected(paramComponentName, paramIBinder);
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName) {
    this.innerConnection.onServiceDisconnected(paramComponentName);
    unbindService();
  }
  
  public void setContext(Context paramContext) {
    unbindService();
    this.context = paramContext;
    this.serviceIntent = new Intent(this.context, this.serviceClass);
  }
  
  public void unbindService() {
    if (this.isBound) {
      this.context.unbindService(this);
      this.isBound = false;
    } 
  }
}
