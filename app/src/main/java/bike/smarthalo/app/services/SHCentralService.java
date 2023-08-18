package bike.smarthalo.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import bike.smarthalo.app.services.ServiceBinders.SHCentralServiceBinder;
import bike.smarthalo.sdk.SHDeviceServiceBinder;

public class SHCentralService extends Service {
    private String TAG = SHCentralService.class.getSimpleName();
    private final IBinder mBinder = new SHCentralServiceBinder(this);

    private SHDeviceServiceBinder deviceService;


    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.i(this.TAG, "Central Service has been created");
    }

        @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return Service.START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
