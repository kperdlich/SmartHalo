package bike.smarthalo;

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
import bike.smarthalo.sdk.RequestPermissionsActivity;
import bike.smarthalo.sdk.SHDeviceService;
import bike.smarthalo.sdk.SHDeviceServiceBinder;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private SHCentralService centralService;
    private SHDeviceServiceBinder deviceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonShowLogo.setEnabled(false);
        binding.buttonDisconnect.setEnabled(false);

        binding.buttonShowLogo.setOnClickListener(view -> {
            if (centralService == null || deviceBinder == null) {
                return;
            }
            deviceBinder.ui_logo(null);
        });

        binding.buttonDisconnect.setOnClickListener(view -> {
            if (centralService == null || deviceBinder == null) {
                return;
            }
            deviceBinder.forgetSavedDeviceAndDisconnect();

            binding.buttonShowLogo.setEnabled(false);
            binding.buttonDisconnect.setEnabled(false);
            binding.buttonConnect.setEnabled(true);
        });

        binding.buttonConnect.setOnClickListener(view -> {
            /*SHDeviceServiceStartHelper.requestLogout(Application.getAppContext());
            boolean isTester = false;
            SHDeviceServiceStartHelper.requestLogin(Application.getAppContext(), "password", "deviceId", isTester);*/

            Intent myIntent = new Intent(Application.getAppContext(), RequestPermissionsActivity.class);
            startActivity(myIntent);
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show();*/
            binding.buttonShowLogo.setEnabled(true);
            binding.buttonDisconnect.setEnabled(true);
            binding.buttonConnect.setEnabled(false);
        });
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
        unbindService(centralServiceConnection);
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