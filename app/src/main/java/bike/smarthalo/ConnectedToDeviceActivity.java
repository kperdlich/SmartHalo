package bike.smarthalo;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import bike.smarthalo.sdk.SHDeviceService;
import bike.smarthalo.sdk.SHDeviceServiceBinder;
import bike.smarthalo.sdk.SHDeviceServiceIntents;
import bike.smarthalo.sdk.models.DeviceConnectionState;

public class ConnectedToDeviceActivity extends AppCompatActivity {

    private SHDeviceServiceBinder deviceBinder;

    private ServiceConnection deviceServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            deviceBinder = (SHDeviceServiceBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            deviceBinder = null;
        }
    };

    private final BroadcastReceiver deviceServiceUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (SHDeviceServiceIntents.BROADCAST_ERROR.equals(action)) {

            } else if (SHDeviceServiceIntents.BROADCAST_CONNECTION_STATE.equals(action)) {
                if (deviceBinder.getConnectionState() == DeviceConnectionState.Disconnected) {
                    Intent mainActivity = new Intent(ConnectedToDeviceActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        }
    };

    private void showProgressBarForDisconnecting() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Disconnecting from device ..");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(deviceServiceUpdateReceiver, SHDeviceService.getDeviceServiceUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deviceServiceUpdateReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_to_device);

        Button disconnectButton = findViewById(R.id.disconnect_button);
        disconnectButton.setOnClickListener(v -> {
            showProgressBarForDisconnecting();
            deviceBinder.forgetSavedDeviceAndDisconnect();
        });

        Button showLogoButton = findViewById(R.id.show_logo_button);
        showLogoButton.setOnClickListener(v -> deviceBinder.ui_logo(null));

        Button speedometerIntroButton = findViewById(R.id.speedometer_intro_button);
        speedometerIntroButton.setOnClickListener(v -> deviceBinder.ui_speedometer_intro(null));

        Button chargeStateButton = findViewById(R.id.charge_state_button);
        chargeStateButton.setOnClickListener(v -> deviceBinder.showChargeState(null));

        registerReceiver(deviceServiceUpdateReceiver, SHDeviceService.getDeviceServiceUpdateIntentFilter());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent shDeviceServiceIntent = new Intent(this, SHDeviceService.class);
        bindService(shDeviceServiceIntent, deviceServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(deviceServiceConnection);
    }
}