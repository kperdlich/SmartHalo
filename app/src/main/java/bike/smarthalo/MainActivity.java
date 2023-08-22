package bike.smarthalo;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import bike.smarthalo.app.services.SHCentralService;
import bike.smarthalo.app.services.ServiceBinders.SHCentralServiceBinder;
import bike.smarthalo.databinding.ActivityMainBinding;
import bike.smarthalo.sdk.SHDeviceService;
import bike.smarthalo.sdk.SHDeviceServiceBinder;
import bike.smarthalo.sdk.SHDeviceServiceIntents;
import bike.smarthalo.sdk.SHSdkHelpers;
import bike.smarthalo.sdk.models.BleDevice;
import bike.smarthalo.sdk.models.DeviceConnectionState;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ListView deviceList;
    private BleDeviceAdapter flavorAdapter;
    private SHCentralService centralService;
    private SHDeviceServiceBinder deviceBinder;

    private final BroadcastReceiver deviceServiceUpdateReceiver = new BroadcastReceiver() { // from class: bike.smarthalo.app.activities.ScanResultsActivity.1
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (SHDeviceServiceIntents.BROADCAST_ERROR.equals(action)) {
            } else if (SHDeviceServiceIntents.BROADCAST_CONNECTION_STATE.equals(action)) {
                if (deviceBinder.getConnectionState() == DeviceConnectionState.Authenticated) {
                    Intent connectedToDevice = new Intent(MainActivity.this, ConnectedToDeviceActivity.class);
                    startActivity(connectedToDevice);
                }
            } else if (SHDeviceServiceIntents.BROADCAST_DEVICE_LIST_UPDATED.equals(action)) {
                updateDeviceList();
            } else if (SHDeviceServiceIntents.BROADCAST_CONNECTED_STOPPING_SCAN.equals(action)) {
            }
        }
    };

    private void showProgressBarForDeviceConnection(BleDevice device) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Connecting to device ..");
        progressDialog.setMessage(device.name + " " + device.address);
        progressDialog.show();
    }

    private void updateDeviceList() {
        flavorAdapter.replaceDeviceList(deviceBinder.getDeviceList());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        flavorAdapter = new BleDeviceAdapter(this, new ArrayList<>());

        deviceList = (ListView) findViewById(R.id.listview_dessert);
        deviceList.setAdapter(flavorAdapter);
        deviceList.setOnItemClickListener((adapterView, view, i, l) -> {
            final BleDevice device = deviceBinder.getDeviceList().get(i);
            if (deviceBinder
                    .setMyDevice(device.address, device.id)
                    .connect()) {
                showProgressBarForDeviceConnection(device);
            } else {
                Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
            }
        });

        if (!SHSdkHelpers.checkPermissions((Context) this, SHSdkHelpers.getBlePermissions())) {
            SHSdkHelpers.requestPermissions(this, SHSdkHelpers.getBlePermissions(), 100, bike.smarthalo.sdk.R.string.app_name);
        } else {
            SHSdkHelpers.startScanning(this);
        }

        registerReceiver(deviceServiceUpdateReceiver, SHDeviceService.getDeviceServiceUpdateIntentFilter());
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
    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
        super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
        if (paramInt == 100)
            if (paramArrayOfint.length > 0 && paramArrayOfint[0] == 0) {
                SHSdkHelpers.startScanning(this);
            } else {
                SHSdkHelpers.startScanning(this);
            }
    }

    private ServiceConnection centralServiceConnection = new ServiceConnection() { // from class: bike.smarthalo.app.activities.ScanResultsActivity.3
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(MainActivity.TAG, "centralServiceConnection onServiceConnected");
            SHCentralServiceBinder binder = (SHCentralServiceBinder) iBinder;
            centralService = binder.getService();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(MainActivity.TAG, "centralServiceConnection onServiceDisconnected");
            centralService = null;
        }
    };

    private ServiceConnection deviceServiceConnection = new ServiceConnection() { // from class: bike.smarthalo.app.activities.ScanResultsActivity.3
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(MainActivity.TAG, "deviceServiceConnection onServiceConnected");
            deviceBinder = (SHDeviceServiceBinder) iBinder;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(MainActivity.TAG, "deviceServiceConnection onServiceDisconnected");
            deviceBinder = null;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        Intent centralServiceIntent = new Intent(this, SHCentralService.class);
        bindService(centralServiceIntent, centralServiceConnection, Context.BIND_AUTO_CREATE);

        Intent shDeviceServiceIntent = new Intent(this, SHDeviceService.class);
        bindService(shDeviceServiceIntent, deviceServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(deviceServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}