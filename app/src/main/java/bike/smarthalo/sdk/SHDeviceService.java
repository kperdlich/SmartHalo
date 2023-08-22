package bike.smarthalo.sdk;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.time.DateUtils;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import bike.smarthalo.sdk.bluetooth.BluetoothDataManager;
import bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract;
import bike.smarthalo.sdk.bluetooth.BluetoothSpeedMonitor;
import bike.smarthalo.sdk.bluetooth.BootloaderScanner;
import bike.smarthalo.sdk.bluetooth.DeviceComparator;
import bike.smarthalo.sdk.bluetooth.StmDfuController;
import bike.smarthalo.sdk.bluetooth.TransceiveQueueManager;
import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationContract;
import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationController;
import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationSourceContract;
import bike.smarthalo.sdk.commands.AlarmCommandsController;
import bike.smarthalo.sdk.commands.CommandsConstants;
import bike.smarthalo.sdk.commands.CommandsContracts.DeviceCommandsContract;
import bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract;
import bike.smarthalo.sdk.commands.CommandsHelper;
import bike.smarthalo.sdk.commands.DFUCommandsController;
import bike.smarthalo.sdk.commands.DeviceCommandsController;
import bike.smarthalo.sdk.commands.ExperimentalCommandsController;
import bike.smarthalo.sdk.commands.SoundsCommandsController;
import bike.smarthalo.sdk.commands.UICommandsController;
import bike.smarthalo.sdk.commands.carousel.CarouselCommandsController;
import bike.smarthalo.sdk.encryption.AES_128_CBC_PKCS5;
import bike.smarthalo.sdk.encryption.CentralKeyEncryption;
import bike.smarthalo.sdk.encryption.EncryptionContract;
import bike.smarthalo.sdk.encryption.SHEncryptionHelper;
import bike.smarthalo.sdk.firmware.FirmwareVersionHelper;
import bike.smarthalo.sdk.firmware.NordicFirmwareHelper;
import bike.smarthalo.sdk.firmware.NordicProtocolVersion;
import bike.smarthalo.sdk.helpers.DebugLogger;
import bike.smarthalo.sdk.helpers.DebugLoggerContract;
import bike.smarthalo.sdk.models.BleDevice;
import bike.smarthalo.sdk.models.DeviceConnectionState;
import bike.smarthalo.sdk.models.DeviceInformation;
import bike.smarthalo.sdk.models.DeviceModel;
import bike.smarthalo.sdk.models.TransceiveCallback;
import bike.smarthalo.sdk.models.TransceiveTask;
import bike.smarthalo.sdk.serviceStorage.ServiceStorageController;
import bike.smarthalo.sdk.stmUtils.SmartHaloOSCrashReporter;
import bike.smarthalo.sdk.stmUtils.SmartHaloOSCrashReporterContract;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/* loaded from: classes.dex */
public class SHDeviceService extends Service implements TransceiveContract, DeviceCommandsContract, BluetoothDataManagerContract, SHScanCallback.ScanCallbackContract, EncryptionContract {
  public static final long AUTHENTICATION_TIME_OUT = 15000;
  public static final int AUTH_FAILED = 0;
  public static final int CONNECT_FAILED = 1;
  public static final long DIRECT_CONNECTION_TIME_OUT = 25000;
  public static final int FOREGROUND_NOTIF_ID = 401;
  public static final int FORGET_DEVICE_FAILED = 3;
  public static final int LOCK_SERIAL_ID = 2;
  public static final int NOTIF_ID = 101;
  public static final long ON_CLEANUP_SCAN_DELAY = 1000;
  public static final int PCBA_SERIAL_ID = 1;
  private static final int PERIPH_PUB_KEY_PAYLOAD_SIZE = 69;
  private static final int PERIPH_PUB_KEY_SIZE = 65;
  public static final int PRODUCT_SERIAL_ID = 0;
  public static final int RESET_PASSWORD_FAILED = 2;
  public static final int SCAN_RESTART_DELAY = 30000;
  public static final int SH_MANUFACTURER_ID = 1126;
  private static final int SH_MANUFACTURER_ID_1 = 102;
  private static final int SH_MANUFACTURER_ID_2 = 4;
  private static final String TAG = "SHDeviceService";
  public static final int TRANSCEIVE_RETRY_COUNT = 2;
  public static final long TRANSCEIVE_TASK_TIME_OUT = 5000;
  private AlarmCommandsController alarmCommandsController;
  private Disposable authenticationDisposable;
  private BleNotificationContract bleNotificationController;
  private BluetoothDataManager bluetoothDataManager;
  private CarouselCommandsController carouselCommandsController;
  private DebugLoggerContract debugLogger;
  private DeviceCommandsController deviceCommandsController;
  private DFUCommandsController dfuCommandsController;
  private Disposable directConnectionDisposable;
  private ExperimentalCommandsController experimentalCommandsController;
  private AES_128_CBC_PKCS5 mAES;
  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothManager mBluetoothManager;
  private ServiceStorageController serviceStorageController;
  private SHScanCallback shScanCallback;
  private SmartHaloOSCrashReporter smartHaloOSCrashReporter;
  private SoundsCommandsController soundsCommandsController;
  private StmDfuController stmDfuController;
  private Disposable stopScanDisposable;
  private TransceiveQueueManager transceiveQueueManager;
  private Disposable transceiveTimeoutDisposable;
  private UICommandsController uiCommandsController;
  private final IBinder mBinder = new SHDeviceServiceBinder(this);
  private ArrayList<BleDevice> mDeviceList = new ArrayList<>();
  private BluetoothSpeedMonitor bluetoothSpeedMonitor = new BluetoothSpeedMonitor();
  private DeviceConnectionState connectionState = DeviceConnectionState.Disconnected;
  private DeviceConnectionState previousConnectionState = DeviceConnectionState.Disconnected;
  private boolean mIsScanning = false;
  private final BroadcastReceiver bluetoothActionStateChangedReceiver = new BroadcastReceiver() { // from class: bike.smarthalo.sdk.SHDeviceService.6
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
      if (intent != null && intent.hasExtra("android.bluetooth.adapter.extra.STATE") && intent.hasExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE")) {
        Integer valueOf = Integer.valueOf(intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", -100));
        Integer valueOf2 = Integer.valueOf(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -100));
        if (!valueOf.equals(12) && valueOf2.equals(12)) {
          SHDeviceService.this.shouldStopSelfSetNotification();
          SHDeviceService.this.lookForKnownDevice(false);
        } else if (valueOf2.equals(13)) {
          SHDeviceService.this.stopScan();
          SHDeviceService.this.cleanUpDeviceConnection(false);
        }
      }
    }
  };

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static boolean hasJustChangedToThisState(Intent intent, DeviceConnectionState deviceConnectionState) {
    if (intent != null && intent.hasExtra(SHDeviceServiceIntents.EXTRA_CONNECTION_STATE) && intent.hasExtra(SHDeviceServiceIntents.EXTRA_PREVIOUS_CONNECTION_STATE)) {
      DeviceConnectionState deviceConnectionState2 = (DeviceConnectionState) intent.getSerializableExtra(SHDeviceServiceIntents.EXTRA_CONNECTION_STATE);
      return deviceConnectionState2 == deviceConnectionState && deviceConnectionState2 != ((DeviceConnectionState) intent.getSerializableExtra(SHDeviceServiceIntents.EXTRA_PREVIOUS_CONNECTION_STATE));
    }
    return false;
  }

  public static IntentFilter getDeviceServiceUpdateIntentFilter() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(SHDeviceServiceIntents.BROADCAST_CONNECTION_STATE);
    intentFilter.addAction(SHDeviceServiceIntents.BROADCAST_ERROR);
    intentFilter.addAction(SHDeviceServiceIntents.BROADCAST_DEVICE_LIST_UPDATED);
    intentFilter.addAction(SHDeviceServiceIntents.BROADCAST_CONNECTED_STOPPING_SCAN);
    return intentFilter;
  }

  @Override // android.app.Service
  public int onStartCommand(Intent intent, int i, int i2) {
    if (!SHSdkHelpers.hasRequiredFeatures(this) || !SHSdkHelpers.checkStartServicePermissions(this)) {
      stopSelf();
      return 1;
    } else if (intent == null) {
      return 1;
    } else {
      String action = intent.getAction();
      if (SHDeviceServiceIntents.ACTION_START_SCAN.equals(action)) {
        startScan();
      } else if (SHDeviceServiceIntents.ACTION_CONNECT_TO_KNOWN_DEVICE.equals(action)) {
        lookForKnownDevice(false);
      } else if (SHDeviceServiceIntents.ACTION_ACTIVE_SCAN_FOR_KNOWN_DEVICE.equals(action)) {
        lookForKnownDevice(true);
      } else if (SHDeviceServiceIntents.ACTION_LOGIN.equals(action)) {
        loginUser(intent);
      } else if (SHDeviceServiceIntents.ACTION_STOP_SCAN.equals(action)) {
        stopScan();
      } else if (SHDeviceServiceIntents.ACTION_CONNECT_TO_DISCOVERED_DEVICE.equals(action)) {
        connectToSavedDevice(true);
      }
      initializeBluetooth();
      return 1;
    }
  }

  private void loginUser(Intent intent) {
    if (intent.hasExtra(SHDeviceServiceIntents.EXTRA_PASSWORD)) {
      this.serviceStorageController.setPassword(intent.getStringExtra(SHDeviceServiceIntents.EXTRA_PASSWORD));
    }
    if (intent.hasExtra(SHDeviceServiceIntents.EXTRA_DEVICE_ID)) {
      this.serviceStorageController.setId(intent.getStringExtra(SHDeviceServiceIntents.EXTRA_DEVICE_ID));
    }
    if (intent.hasExtra(SHDeviceServiceIntents.EXTRA_IS_TESTER)) {
      this.serviceStorageController.setIsTester(intent.getBooleanExtra(SHDeviceServiceIntents.EXTRA_IS_TESTER, false));
    }
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void lookForKnownDevice(boolean z) {
    stopScan();
    if (this.bluetoothDataManager.isInitialized() || this.serviceStorageController.id == null || this.serviceStorageController.id.isEmpty()) {
      return;
    }
    if (z || !SHSdkHelpers.isAtLeastOreo() || !this.serviceStorageController.hasValidAddress()) {
      startScan();
    } else {
      startPendingIntentScan();
    }
  }

  @Override // android.app.Service
  public IBinder onBind(Intent intent) {
    return this.mBinder;
  }

  private void setNotification(SHNotificationManager.NotifType notifType) {
    if (this.serviceStorageController == null) {
      this.serviceStorageController = ServiceStorageController.getInstance(this);
    }
    Notification serviceStopNotification = SHNotificationManager.getServiceStopNotification(notifType, this);
    if (serviceStopNotification != null) {
      SHNotificationManager.showNotification(serviceStopNotification, this);
    }
  }

  /* JADX INFO: Access modifiers changed from: private */
  public boolean shouldStopSelfSetNotification() {
    if (!SHSdkHelpers.hasRequiredFeatures(this)) {
      setNotification(SHNotificationManager.NotifType.NO_FEATURE);
      return true;
    } else if (SHSdkHelpers.checkStartServicePermissions(this)) {
      initializeBluetooth();
      return false;
    } else {
      return true;
    }
  }

  private void clearNotifications() {
    ((NotificationManager) getSystemService("notification")).cancel(101);
  }

  @Override // android.app.Service
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Device Service created");
    this.serviceStorageController = ServiceStorageController.getInstance(this);
    this.shScanCallback = new SHScanCallback(this);
    this.debugLogger = new DebugLogger(this.serviceStorageController);
    this.deviceCommandsController = new DeviceCommandsController(this, this, this);
    this.bleNotificationController = new BleNotificationController(this.deviceCommandsController, this, this.debugLogger);
    this.bluetoothDataManager = new BluetoothDataManager(this, this, this.bleNotificationController, this.debugLogger);
    this.transceiveQueueManager = new TransceiveQueueManager();
    this.smartHaloOSCrashReporter = new SmartHaloOSCrashReporter(this.bleNotificationController, this.debugLogger);
    SHNotificationManager.initializeNotificationChannel(this);
    if (shouldStopSelfSetNotification()) {
      stopSelf();
      return;
    }
    this.uiCommandsController = new UICommandsController(this, this, this.deviceCommandsController);
    this.soundsCommandsController = new SoundsCommandsController(this, this);
    this.alarmCommandsController = new AlarmCommandsController(this, this, this.deviceCommandsController);
    this.experimentalCommandsController = new ExperimentalCommandsController(this, this.uiCommandsController, this);
    this.dfuCommandsController = new DFUCommandsController(this, this);
    this.stmDfuController = new StmDfuController(this.dfuCommandsController, this.debugLogger);
    this.carouselCommandsController = new CarouselCommandsController(this, this);
    registerReceiver(this.bluetoothActionStateChangedReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
  }

  public UICommandsController getUICommandsController() {
    return this.uiCommandsController;
  }

  public SoundsCommandsController getSoundsCommandsController() {
    return this.soundsCommandsController;
  }

  public AlarmCommandsController getAlarmCommandsController() {
    return this.alarmCommandsController;
  }

  public ExperimentalCommandsController getExperimentalCommandsController() {
    return this.experimentalCommandsController;
  }

  public DeviceCommandsController getDeviceCommandsController() {
    return this.deviceCommandsController;
  }

  public BleNotificationSourceContract getBleNotificationSourceController() {
    return this.bleNotificationController;
  }

  public StmDfuController getStmDfuController() {
    return this.stmDfuController;
  }

  public ServiceStorageController getServiceStorage() {
    return this.serviceStorageController;
  }

  public BluetoothSpeedMonitor getBluetoothSpeedMonitor() {
    return this.bluetoothSpeedMonitor;
  }

  public CarouselCommandsController getCarouselCommandsController() {
    return this.carouselCommandsController;
  }

  public DebugLoggerContract getDebugLogger() {
    return this.debugLogger;
  }

  public SmartHaloOSCrashReporterContract getSmartHaloOSCrashReporter() {
    return this.smartHaloOSCrashReporter;
  }

  @Override // android.app.Service
  public void onDestroy() {
    Log.i(TAG, "Device service destroyed");
    stopServiceSequence();
    super.onDestroy();
  }

  @Override // android.app.Service
  public void onTaskRemoved(Intent intent) {
    String str = TAG;
    Log.d(str, "onTaskRemoved:" + getConnectionState());
    if (this.connectionState != DeviceConnectionState.Authenticated) {
      stopServiceSequence();
    }
    super.onTaskRemoved(intent);
  }

  private void stopServiceSequence() {
    stopScan();
    this.transceiveQueueManager.clearQueue();
    this.transceiveQueueManager.dispose();
    this.bluetoothDataManager.cleanUpSequence();
    clearStopScanDisposable();
    this.debugLogger.dispose();
    this.smartHaloOSCrashReporter.dispose();
    sendBroadcast(new Intent(SHDeviceServiceIntents.DEVICE_SERVICE_DESTROYED));
    try {
      unregisterReceiver(this.bluetoothActionStateChangedReceiver);
    } catch (IllegalArgumentException unused) {
    }
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void cleanUpDeviceConnection() {
    cleanUpDeviceConnection(true);
  }

  public void cleanUpDeviceConnection(final boolean z) {
    ContextHelper.runOnMainThread(this, new Runnable() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$iN_bBMF4sLjZCNwZXD2XHBR1mvY
      @Override // java.lang.Runnable
      public final void run() {
        SHDeviceService.lambda$cleanUpDeviceConnection$0(SHDeviceService.this, z);
      }
    });
  }

  public static /* synthetic */ void lambda$cleanUpDeviceConnection$0(SHDeviceService sHDeviceService, boolean z) {
    sHDeviceService.debugLogger.log(TAG, "CLEANING UP DEVICE CONNECTION");
    sHDeviceService.transceiveQueueManager.clearQueue();
    sHDeviceService.bluetoothSpeedMonitor.clearMetrics();
    ContextHelper.getMainLooperHandler(sHDeviceService).removeCallbacksAndMessages(null);
    sHDeviceService.clearTransceiveTimeout();
    sHDeviceService.clearAuthenticationTimeout();
    sHDeviceService.stopPendingIntentScan();
    sHDeviceService.stmDfuController.cancelDfuFlow();
    sHDeviceService.bluetoothDataManager.cleanUpSequence();
    sHDeviceService.mAES = null;
    sHDeviceService.mDeviceList.clear();
    sHDeviceService.onConnectionStateChanged(DeviceConnectionState.Disconnected);
    sHDeviceService.deviceCommandsController.clearDeviceInformation();
    sHDeviceService.stopForeground(true);
    if (z) {
      sHDeviceService.launchReconnection();
    }
  }

  private void launchReconnection() {
    ContextHelper.runOnMainThreadDelayed(this, 1000L, new Runnable() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$qZdqN76gNYobwgM6pwTArvtBTlE
      @Override // java.lang.Runnable
      public final void run() {
        SHDeviceService.lambda$launchReconnection$1(SHDeviceService.this);
      }
    });
  }

  public static /* synthetic */ void lambda$launchReconnection$1(SHDeviceService sHDeviceService) {
    if (SHSdkHelpers.isAtLeastOreo()) {
      sHDeviceService.startPendingIntentScan();
    } else {
      sHDeviceService.startScan();
    }
  }

  @Override // bike.smarthalo.sdk.commands.CommandsContracts.DeviceCommandsContract
  public void hasEnteredBootloader(boolean z) {
    cleanUpDeviceConnection(false);
    if (z) {
      ContextHelper.runOnMainThreadDelayed(this, 200L, new Runnable() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$qHURayQlnNWdQQrGu5B5ps3oZVo
        @Override // java.lang.Runnable
        public final void run() {
          SHDeviceService.lambda$hasEnteredBootloader$2(SHDeviceService.this);
        }
      });
    }
  }

  public static /* synthetic */ void lambda$hasEnteredBootloader$2(SHDeviceService sHDeviceService) {
    if (SHSdkHelpers.isAtLeastOreo()) {
      BootloaderScanner.startPendingIntentScan(sHDeviceService, sHDeviceService.mBluetoothAdapter, sHDeviceService.serviceStorageController.address);
    } else {
      sHDeviceService.startScan();
    }
  }

  private void initializeBluetooth() {
    BluetoothManager bluetoothManager;
    if (this.mBluetoothManager == null) {
      this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
    }
    if (this.mBluetoothAdapter != null || (bluetoothManager = this.mBluetoothManager) == null) {
      return;
    }
    this.mBluetoothAdapter = bluetoothManager.getAdapter();
  }

  @Override // bike.smarthalo.sdk.commands.CommandsContracts.DeviceCommandsContract
  public void saveLastKnownFirmwareVersions(String str) {
    this.serviceStorageController.set(ServiceStorageController.LAST_KNOWN_FIRMWARE_VERSION_KEY, str);
  }

  public Boolean areTouchInputsSupported() {
    if (this.serviceStorageController.lastKnownFirmwareVersion == null) {
      return null;
    }
    return Boolean.valueOf(FirmwareVersionHelper.isFirmwareVersionSufficient(this.serviceStorageController.lastKnownFirmwareVersion, NordicFirmwareHelper.getProtocolString(NordicProtocolVersion.V1_7)));
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void onNewTransceiveResult(byte[] bArr, TransceiveTask transceiveTask) {
    byte[] decryptBlePacket = SHEncryptionHelper.decryptBlePacket(bArr, transceiveTask.recvPayloads.toByteArray(), this.mAES);
    if (decryptBlePacket != null && decryptBlePacket.length > 0) {
      clearTransceiveTimeout();
      if (decryptBlePacket[0] == 2) {
        DebugLoggerContract debugLoggerContract = this.debugLogger;
        String str = TAG;
        debugLoggerContract.reportNonFatalException(str, "Denied Command " + transceiveTask.description);
        cleanUpDeviceConnection();
        return;
      }
      DebugLoggerContract debugLoggerContract2 = this.debugLogger;
      String str2 = TAG;
      debugLoggerContract2.log(str2, transceiveTask.description + " result : " + CommandsHelper.getStringPayload(decryptBlePacket));
      if (transceiveTask.cb != null) {
        transceiveTask.cb.onData(decryptBlePacket);
      }
      transceiveTask.receivedDataAt = System.currentTimeMillis();
      this.bluetoothSpeedMonitor.updateSpeedMetrics(transceiveTask);
      this.transceiveQueueManager.pollQueue(new TransceiveQueueManager.GetItemCallback() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$BXwFzBo8jhx59-e65HLQZqA1444
        @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetItemCallback
        public final void onResult(TransceiveTask transceiveTask2, int i) {
          SHDeviceService.lambda$onNewTransceiveResult$3(SHDeviceService.this, transceiveTask2, i);
        }
      });
      return;
    }
    this.debugLogger.log(TAG, "Encryption error when processing transceive result");
    transceiveRetryOrRestart(transceiveTask);
  }

  public static /* synthetic */ void lambda$onNewTransceiveResult$3(SHDeviceService sHDeviceService, TransceiveTask transceiveTask, int i) {
    sHDeviceService.bluetoothSpeedMonitor.updateQueueSize(i);
    if (i > 0) {
      sHDeviceService.bluetoothDataManager.processNextTransceiveTask();
    }
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void peekTransceiveQueue(@NonNull TransceiveQueueManager.GetItemCallback getItemCallback) {
    this.transceiveQueueManager.peekQueue(getItemCallback);
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void startTransceiveTimeoutTimer() {
    clearTransceiveTimeout();
    this.transceiveTimeoutDisposable = ObservableTimerHelper.getTimerOnMainThread(5000L).subscribe(new Consumer() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$V8EHHzVHa6jKWb356hBXSLO8QH0
      @Override // io.reactivex.functions.Consumer
      public final void accept(Object obj) {
        Long l = (Long) obj;
        SHDeviceService.this.onTransceiveTimeout();
      }
    });
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void transceiveRetryOrRestart(@NonNull TransceiveTask transceiveTask) {
    if (transceiveTask.allowRetry && transceiveTask.retryCount < 2) {
      DebugLoggerContract debugLoggerContract = this.debugLogger;
      String str = TAG;
      debugLoggerContract.log(str, "RETRYING TRANSCEIVE TASK " + transceiveTask.description);
      transceiveTask.currentSendPayloadIndex = 0;
      transceiveTask.retryCount = transceiveTask.retryCount + 1;
      transceiveTask.recvPayloads = new ByteArrayOutputStream();
      this.bluetoothDataManager.processNextTransceiveTask();
      return;
    }
    DebugLoggerContract debugLoggerContract2 = this.debugLogger;
    String str2 = TAG;
    debugLoggerContract2.log(str2, "TRANSCEIVE TIME OUT FOR TASK " + transceiveTask.description + ", RESTARTING SERVICE");
    cleanUpDeviceConnection();
  }

  @Override // bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract
  public void transceive(byte[] bArr, boolean z, String str, TransceiveCallback transceiveCallback) {
    transceive(bArr, z, str, true, false, transceiveCallback);
  }

  @Override // bike.smarthalo.sdk.commands.CommandsContracts.TransceiveContract
  public void transceive(byte[] bArr, boolean z, String str, boolean z2, boolean z3, TransceiveCallback transceiveCallback) {
    AES_128_CBC_PKCS5 aes_128_cbc_pkcs5;
    if (this.connectionState == DeviceConnectionState.Disconnected || (this.connectionState == DeviceConnectionState.Connected && !z3)) {
      DebugLoggerContract debugLoggerContract = this.debugLogger;
      String str2 = TAG;
      debugLoggerContract.log(str2, str + " cannot be added to command queue.  Not authenticated");
      return;
    }
    DebugLoggerContract debugLoggerContract2 = this.debugLogger;
    String str3 = TAG;
    debugLoggerContract2.log(str3, "Creating task " + str + " : " + CommandsHelper.getStringPayload(bArr));
    TransceiveTask transceiveTask = new TransceiveTask();
    transceiveTask.cb = transceiveCallback;
    if (z && (aes_128_cbc_pkcs5 = this.mAES) != null) {
      transceiveTask.crypted = 1;
      bArr = aes_128_cbc_pkcs5.crypt(1, bArr);
    }
    if (bArr == null) {
      Log.e(TAG, "Encrypted transceive payload is null, cleaning connection");
      cleanUpDeviceConnection();
      return;
    }
    transceiveTask.description = str;
    transceiveTask.recvPayloads = new ByteArrayOutputStream();
    transceiveTask.sendPayloads = new ArrayList();
    transceiveTask.sendLen = bArr.length;
    transceiveTask.allowRetry = z2;
    transceiveTask.createdAt = System.currentTimeMillis();
    int i = 0;
    while (i < bArr.length) {
      int i2 = i + 20;
      transceiveTask.sendPayloads.add(Arrays.copyOfRange(bArr, i, i2));
      i = i2;
    }
    this.transceiveQueueManager.addItem(transceiveTask, new TransceiveQueueManager.GetSizeCallback() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$aQ7f3P5c6Vmh5ZTiRy-bS5F-sYE
      @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetSizeCallback
      public final void onResult(int i3) {
        SHDeviceService.lambda$transceive$5(SHDeviceService.this, i3);
      }
    });
  }

  public static /* synthetic */ void lambda$transceive$5(SHDeviceService sHDeviceService, int i) {
    sHDeviceService.bluetoothSpeedMonitor.updateQueueSize(i);
    if (i == 1) {
      sHDeviceService.bluetoothDataManager.processNextTransceiveTask();
    }
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void exchangeKeys() {
    transceive(CommandsConstants.cmd_getPeriphPubKey, false, "cmd_getPeriphPubKey", false, true, new TransceiveCallback() { // from class: bike.smarthalo.sdk.SHDeviceService.1
      @Override // bike.smarthalo.sdk.models.TransceiveCallback
      public void onData(byte[] bArr) {
        if (bArr == null || bArr.length < 69) {
          SHDeviceService.this.onAuthenticationError("Incorrect received payload size");
          return;
        }
        byte[] copyOfRange = Arrays.copyOfRange(bArr, 1, 65);
        byte[] copyOfRange2 = Arrays.copyOfRange(bArr, 65, 69);
        if (!SHSdkHelpers.bytesToHex(Arrays.copyOfRange(copyOfRange, 0, SHDeviceService.this.serviceStorageController.id.length() / 2)).equals(SHDeviceService.this.serviceStorageController.id)) {
          SHDeviceService.this.bluetoothDataManager.getDevice();
          SHDeviceService.this.onAuthenticationError("id and public key mismatch!");
          return;
        }
        try {
          CentralKeyEncryption centralKeyEncryption = new CentralKeyEncryption(copyOfRange, copyOfRange2);
          byte[] centralKey = centralKeyEncryption.getCentralKey();
          SHDeviceService.this.mAES = centralKeyEncryption.getAes();
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          byteArrayOutputStream.write(CommandsConstants.cmd_setCentralPubKey);
          byteArrayOutputStream.write(centralKey);
          SHDeviceService.this.transceive(byteArrayOutputStream.toByteArray(), false, "setCentralKey", false, true, new TransceiveCallback() { // from class: bike.smarthalo.sdk.SHDeviceService.1.1
            @Override // bike.smarthalo.sdk.models.TransceiveCallback
            public void onData(byte[] bArr2) {
              String str = SHDeviceService.TAG;
              Log.i(str, "setCentralKey " + SHSdkHelpers.bytesToHex(bArr2));
              SHDeviceService.this.authenticateWithDevice(SHDeviceService.this.serviceStorageController.password);
            }
          });
        } catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
          SHDeviceService.this.onAuthenticationError(e.getMessage());
          e.printStackTrace();
        }
      }
    });
  }

  /* JADX INFO: Access modifiers changed from: package-private */
  /* renamed from: bike.smarthalo.sdk.SHDeviceService$2  reason: invalid class name */
  /* loaded from: classes.dex */
  public class AnonymousClass2 extends CmdCallback {
    final /* synthetic */ String val$password;

    AnonymousClass2(String str) {
      this.val$password = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: bike.smarthalo.sdk.SHDeviceService$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends CmdCallback {
      AnonymousClass1() {
      }

      @Override // bike.smarthalo.sdk.CmdCallback
      public void onSuccess() {
        SHDeviceService.this.deviceCommandsController.updateDeviceInformation(new DeviceInformation.DeviceInformationCallback() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$2$1$U6m4KLFOVLKB5lK-9Ps376hoeSI
          @Override // bike.smarthalo.sdk.models.DeviceInformation.DeviceInformationCallback
          public final void onDeviceInformationReady(DeviceInformation deviceInformation) {
            SHDeviceService.this.getUICommandsController().ui_logo(null);
            SHDeviceService.this.onConnectionStateChanged(DeviceConnectionState.Authenticated);
          }
        });
      }

      @Override // bike.smarthalo.sdk.CmdCallback
      public void onErr(byte b) {
        SHDeviceService.this.onAuthenticationError();
      }
    }

    @Override // bike.smarthalo.sdk.CmdCallback
    public void onSuccess() {
      if (!"".equals(this.val$password)) {
        SHDeviceService.this.deviceCommandsController.updateDeviceInformation(new DeviceInformation.DeviceInformationCallback() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$2$ofyn-XLj5jbzPZI65vTFVyheEks
          @Override // bike.smarthalo.sdk.models.DeviceInformation.DeviceInformationCallback
          public final void onDeviceInformationReady(DeviceInformation deviceInformation) {
            SHDeviceService.this.onConnectionStateChanged(DeviceConnectionState.Authenticated);
          }
        });
        return;
      }
      SHDeviceService sHDeviceService = SHDeviceService.this;
      sHDeviceService.auth_setPassword(sHDeviceService.serviceStorageController.password, new AnonymousClass1());
    }

    @Override // bike.smarthalo.sdk.CmdCallback
    public void onErr(byte b) {
      if (!"".equals(this.val$password)) {
        SHDeviceService.this.authenticateWithDevice("");
      } else {
        SHDeviceService.this.onAuthenticationError();
      }
    }
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void authenticateWithDevice(String str) {
    sendAuthenticationCommand(str, new AnonymousClass2(str));
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void onAuthenticationError(String str) {
    this.debugLogger.log(TAG, str);
    broadcastError(1, str);
    cleanUpDeviceConnection();
  }

  private void sendAuthenticationCommand(String str, final CmdCallback cmdCallback) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byteArrayOutputStream.write(CommandsConstants.cmd_authenticate);
      byteArrayOutputStream.write(str.getBytes());
      transceive(byteArrayOutputStream.toByteArray(), true, "sendAuthenticationCommand", false, true, new TransceiveCallback() { // from class: bike.smarthalo.sdk.SHDeviceService.3
        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          if (bArr[0] == 0) {
            cmdCallback.onSuccess();
          } else {
            cmdCallback.onErr(bArr[0]);
          }
        }

        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onFail(String str2) {
          SHDeviceService sHDeviceService = SHDeviceService.this;
          sHDeviceService.broadcastError(1, "auth_authenticate:" + str2);
          SHDeviceService.this.debugLogger.log(SHDeviceService.TAG, "Authentication failure, cleaning connection");
          SHDeviceService.this.cleanUpDeviceConnection();
          cmdCallback.onFail(str2);
        }
      });
    } catch (IOException e) {
      broadcastError(1, "auth_authenticate:catch: " + e.getMessage());
      cleanUpDeviceConnection();
      e.printStackTrace();
    }
  }

  private void onBleConnectedToDevice() {
    clearDirectConnectionTimeout();
    startAuthenticationTimeout();
  }

  private void onSuccessfulAuthentication() {
    //startForeground(FOREGROUND_NOTIF_ID, SHNotificationManager.getForegroundServiceNotification(this, this.serviceStorageController.name, false));
    removeDeviceWithIdFromList(this.serviceStorageController.id);
    this.debugLogger.log(TAG, "Successful authentication, Clearing AUTHENTICATION timeout");
    clearAuthenticationTimeout();
    stopScan();
    DeviceInformation deviceInformation = this.deviceCommandsController.getDeviceInformation();
    if (deviceInformation != null) {
      this.serviceStorageController.set(ServiceStorageController.DEVICE_VERSION_KEY, deviceInformation.getDeviceModel().getSimpleValue());
      if (deviceInformation.getDeviceModel() == DeviceModel.SH2) {
        this.deviceCommandsController.updateDeviceDate(null);
      }
    }
    BootloaderScanner.stopScan(this, this.mBluetoothAdapter);
    SHSdkBuildConfigHelper.launchParentServiceOnAuthentication(this);
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void onAuthenticationError() {
    broadcastError(0, "authFailed");
    resetLocalStorageAndDisconnect();
  }

  public void resetLocalStorageAndDisconnect() {
    this.serviceStorageController.forgetSavedDevice();
    if (!this.serviceStorageController.hasValidDevice()) {
      cleanUpDeviceConnection(false);
    } else {
      broadcastError(3, "forgetSavedDevice");
    }
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void auth_setPassword(String str, final CmdCallback cmdCallback) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] bytes = str.getBytes();
      byteArrayOutputStream.write(CommandsConstants.cmd_setPassword);
      byteArrayOutputStream.write(bytes);
      if (this.deviceCommandsController.isNordicFirmwareSufficient(NordicProtocolVersion.V1_2_1) && !str.equals("")) {
        int i = SHEncryptionHelper.get_CRC_CCITT_16_Checksum(bytes);
        byteArrayOutputStream.write((i >> 8) & 255);
        byteArrayOutputStream.write(i & 255);
      }
      transceive(byteArrayOutputStream.toByteArray(), true, "auth_setPassword", false, true, new TransceiveCallback() { // from class: bike.smarthalo.sdk.SHDeviceService.4
        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onData(byte[] bArr) {
          cmdCallback.onDone();
          cmdCallback.onDone(bArr);
          if (bArr[0] == 0) {
            cmdCallback.onSuccess();
          } else {
            cmdCallback.onErr(bArr[0]);
          }
        }

        @Override // bike.smarthalo.sdk.models.TransceiveCallback
        public void onFail(String str2) {
          cmdCallback.onFail(str2);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
      cmdCallback.onErr((byte) 0);
    }
  }

  @Override // bike.smarthalo.sdk.bluetooth.BluetoothDataManagerContract
  public void onConnectionStateChanged(DeviceConnectionState deviceConnectionState) {
    if ((deviceConnectionState == DeviceConnectionState.Connected && this.connectionState == DeviceConnectionState.Authenticated) || deviceConnectionState == this.connectionState) {
      return;
    }
    DebugLoggerContract debugLoggerContract = this.debugLogger;
    String str = TAG;
    debugLoggerContract.log(str, "ON CONNECTION STATE changed from : " + this.connectionState.toString() + " to " + deviceConnectionState.toString());
    this.previousConnectionState = this.connectionState;
    this.connectionState = deviceConnectionState;
    switch (deviceConnectionState) {
      case Disconnected:
        stopForeground(true);
        break;
      case Connected:
        clearDirectConnectionTimeout();
        onBleConnectedToDevice();
        break;
      case Authenticated:
        onSuccessfulAuthentication();
        break;
    }
    broadcastConnectionState();
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void broadcastError(int i, String str) {
    Intent intent = new Intent(SHDeviceServiceIntents.BROADCAST_ERROR);
    intent.putExtra(SHDeviceServiceIntents.EXTRA_ERROR_CODE, i);
    sendBroadcast(intent);
  }

  private BluetoothDevice getBluetoothDevice(String str) {
    BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
    if (bluetoothAdapter == null || str == null) {
      return null;
    }
    try {
      return bluetoothAdapter.getRemoteDevice(str);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void setMyDevice(String str, String str2) {
    this.serviceStorageController.set(ServiceStorageController.ADDRESS_KEY, str);
    this.serviceStorageController.set(ServiceStorageController.ID_KEY, str2);
  }

  public boolean manualConnect() {
    BluetoothDataManager bluetoothDataManager = this.bluetoothDataManager;
    if (bluetoothDataManager != null) {
      bluetoothDataManager.cleanUpSequence();
    }
    return connectToSavedDevice(false);
  }

  private boolean connectToSavedDevice(boolean z) {
    BluetoothDevice bluetoothDevice;
    if (SHSdkHelpers.isAtLeastOreo()) {
      startForeground(FOREGROUND_NOTIF_ID, SHNotificationManager.getForegroundServiceNotification(this, this.serviceStorageController.name, true));
    }
    BluetoothDataManager bluetoothDataManager = this.bluetoothDataManager;
    boolean z2 = false;
    boolean z3 = bluetoothDataManager != null && bluetoothDataManager.isInitialized();
    BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
    if (bluetoothAdapter != null && bluetoothAdapter.isEnabled() && !z3 && this.serviceStorageController.isValidForConnection() && getConnectionState() == DeviceConnectionState.Disconnected && (bluetoothDevice = getBluetoothDevice(this.serviceStorageController.address)) != null) {
      stopPendingIntentScan();
      startDirectConnectionTimeout();
      this.bluetoothDataManager.setUpDeviceConnection(bluetoothDevice);
      this.serviceStorageController.set(ServiceStorageController.NAME_KEY, bluetoothDevice.getName());
      z2 = true;
    }
    if (SHSdkHelpers.isAtLeastOreo() && !z2 && z && !z3) {
      stopForeground(true);
      stopSelf();
    }
    return z2;
  }

  public void startDirectConnectionTimeout() {
    clearDirectConnectionTimeout();
    this.directConnectionDisposable = ObservableTimerHelper.getTimerOnMainThread(DIRECT_CONNECTION_TIME_OUT).subscribe(new Consumer() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$JJme4ctodE0yXIOlUlBN7qpDYIc
      @Override // io.reactivex.functions.Consumer
      public final void accept(Object obj) {
        Long l = (Long) obj;
        SHDeviceService.this.onDirectConnectionTimeout();
      }
    });
  }

  public void startAuthenticationTimeout() {
    clearAuthenticationTimeout();
    this.authenticationDisposable = ObservableTimerHelper.getTimerOnMainThread(AUTHENTICATION_TIME_OUT).subscribe(new Consumer() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$CrsJ9pNauOggTphJM7C04g-mtdc
      @Override // io.reactivex.functions.Consumer
      public final void accept(Object obj) {
        Long l = (Long) obj;
        SHDeviceService.this.onAuthenticationTimeout();
      }
    });
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void onAuthenticationTimeout() {
    this.debugLogger.log(TAG, "Authentication time out, restarting connection process");
    cleanUpDeviceConnection();
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void onDirectConnectionTimeout() {
    this.debugLogger.log(TAG, "Direct connection time out, cleaning connection");
    cleanUpDeviceConnection();
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void onTransceiveTimeout() {
    this.transceiveQueueManager.peekQueue(new TransceiveQueueManager.GetItemCallback() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$p8UGci62ozhpAzaOPNIgeI9mcg4
      @Override // bike.smarthalo.sdk.bluetooth.TransceiveQueueManager.GetItemCallback
      public final void onResult(TransceiveTask transceiveTask, int i) {
        SHDeviceService.lambda$onTransceiveTimeout$8(SHDeviceService.this, transceiveTask, i);
      }
    });
  }

  public static /* synthetic */ void lambda$onTransceiveTimeout$8(SHDeviceService sHDeviceService, TransceiveTask transceiveTask, int i) {
    if (transceiveTask != null) {
      DebugLoggerContract debugLoggerContract = sHDeviceService.debugLogger;
      String str = TAG;
      debugLoggerContract.log(str, "TIME OUT for task " + transceiveTask.description);
      sHDeviceService.transceiveRetryOrRestart(transceiveTask);
    }
  }

  private void clearTransceiveTimeout() {
    Disposable disposable = this.transceiveTimeoutDisposable;
    if (disposable == null || disposable.isDisposed()) {
      return;
    }
    this.transceiveTimeoutDisposable.dispose();
    this.transceiveTimeoutDisposable = null;
  }

  private void clearDirectConnectionTimeout() {
    Disposable disposable = this.directConnectionDisposable;
    if (disposable == null || disposable.isDisposed()) {
      return;
    }
    this.directConnectionDisposable.dispose();
    this.directConnectionDisposable = null;
  }

  private void clearAuthenticationTimeout() {
    Disposable disposable = this.authenticationDisposable;
    if (disposable == null || disposable.isDisposed()) {
      return;
    }
    this.authenticationDisposable.dispose();
    this.authenticationDisposable = null;
  }

  private void clearStopScanDisposable() {
    Disposable disposable = this.stopScanDisposable;
    if (disposable == null || disposable.isDisposed()) {
      return;
    }
    this.stopScanDisposable.dispose();
    this.stopScanDisposable = null;
  }

  public void resetPasswordAndDisconnect() {
    resetPasswordAndDisconnect(null);
  }

  public void resetPasswordAndDisconnect(final CmdCallback cmdCallback) {
    auth_setPassword("", new CmdCallback() { // from class: bike.smarthalo.sdk.SHDeviceService.5
      @Override // bike.smarthalo.sdk.CmdCallback
      public void onSuccess() {
        SHDeviceService.this.transceiveQueueManager.pollQueue();
        SHDeviceService.this.resetLocalStorageAndDisconnect();
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onSuccess();
        }
      }

      @Override // bike.smarthalo.sdk.CmdCallback
      public void onErr(byte b) {
        SHDeviceService sHDeviceService = SHDeviceService.this;
        sHDeviceService.broadcastError(2, "resetPasswordAndDisconnect:" + ((int) b));
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onFail();
        }
      }

      @Override // bike.smarthalo.sdk.CmdCallback
      public void onFail(String str) {
        SHDeviceService sHDeviceService = SHDeviceService.this;
        sHDeviceService.broadcastError(2, "resetPasswordAndDisconnect:" + str);
        CmdCallback cmdCallback2 = cmdCallback;
        if (cmdCallback2 != null) {
          cmdCallback2.onFail();
        }
      }
    });
  }

  public DeviceConnectionState getConnectionState() {
    return this.connectionState;
  }

  public DeviceConnectionState getPreviousConnectionState() {
    return this.previousConnectionState;
  }

  private void broadcastConnectionState() {
    Intent intent = new Intent(SHDeviceServiceIntents.BROADCAST_CONNECTION_STATE);
    DeviceConnectionState connectionState = getConnectionState();
    DeviceConnectionState previousConnectionState = getPreviousConnectionState();
    if (connectionState == DeviceConnectionState.Authenticated && this.serviceStorageController.hasValidDevice()) {
      intent.putExtra(SHDeviceServiceIntents.EXTRA_DEVICE_NAME, this.serviceStorageController.name);
      intent.putExtra(SHDeviceServiceIntents.EXTRA_DEVICE_ADDRESS, this.serviceStorageController.address);
      intent.putExtra(SHDeviceServiceIntents.EXTRA_DEVICE_ID, this.serviceStorageController.id);
    }
    intent.putExtra(SHDeviceServiceIntents.EXTRA_CONNECTION_STATE, connectionState);
    intent.putExtra(SHDeviceServiceIntents.EXTRA_PREVIOUS_CONNECTION_STATE, previousConnectionState);
    sendBroadcast(intent);
  }

  public ArrayList<BleDevice> getDeviceList() {
    ArrayList<BleDevice> arrayList = new ArrayList<>(this.mDeviceList);
    Collections.sort(arrayList, new DeviceComparator());
    return arrayList;
  }

  public BleDevice getBleDeviceFromId(String str) {
    Iterator<BleDevice> it = this.mDeviceList.iterator();
    while (it.hasNext()) {
      BleDevice next = it.next();
      if (next.id.equals(str)) {
        return next;
      }
    }
    return null;
  }

  private void logout() {
    this.serviceStorageController.logout();
    cleanUpDeviceConnection(false);
    stopSelf();
  }

  private void startScan() {
    BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
    if (this.mIsScanning || this.mBluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
      Log.i(TAG, "Bluetooth is disabled or not available");
      return;
    }
    Log.i(TAG, "Starting active ble scan");
    BluetoothLeScanner bluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
    if (bluetoothLeScanner == null) {
      return;
    }
    try {
      bluetoothLeScanner.startScan(new ArrayList(), new ScanSettings.Builder().setScanMode(1).setReportDelay(0L).build(), this.shScanCallback);
    } catch (Exception e) {
      e.printStackTrace();
      stopScan();
    }
    this.mIsScanning = true;
    pruneDeviceListDelayed();
    stopScanDelayed();
  }

  private void stopScanDelayed() {
    clearStopScanDisposable();
    this.stopScanDisposable = Single.timer(30000L, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: bike.smarthalo.sdk.-$$Lambda$SHDeviceService$t64vSrpXS4AOU1NYt8q8yms_NGI
      @Override // io.reactivex.functions.Consumer
      public final void accept(Object obj) {
        if (mIsScanning) {
          if (getConnectionState() == DeviceConnectionState.Authenticated) {
            sendBroadcast(new Intent(SHDeviceServiceIntents.BROADCAST_CONNECTED_STOPPING_SCAN));
            return;
          }
          Log.i(TAG, "restarting scan");
          stopScan();
          startScan();
        }
      }
    });
  }


  @TargetApi(26)
  private void startPendingIntentScan() {
    if (this.serviceStorageController != null) {
      DebugLoggerContract debugLoggerContract = this.debugLogger;
      String str = TAG;
      debugLoggerContract.log(str, "Starting pending intent scan for " + this.serviceStorageController.address);
      SHPendingIntentScanHelper.startPendingIntentScan(this, this.mBluetoothAdapter, this.serviceStorageController.address);
    }
  }

  @TargetApi(26)
  private void stopPendingIntentScan() {
    BluetoothAdapter bluetoothAdapter;
    if (!SHSdkHelpers.isAtLeastOreo() || (bluetoothAdapter = this.mBluetoothAdapter) == null || !bluetoothAdapter.isEnabled() || this.mBluetoothAdapter.getBluetoothLeScanner() == null) {
      return;
    }
    this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(SHPendingIntentScanHelper.getScanIntent(this));
  }

  private void pruneDeviceListDelayed() {
    ContextHelper.runOnMainThreadDelayed(this, 10000L, new Runnable() { // from class: bike.smarthalo.sdk.SHDeviceService.7
      @Override // java.lang.Runnable
      public void run() {
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        boolean z = false;
        for (int i = 0; i < SHDeviceService.this.mDeviceList.size(); i++) {
          if (valueOf.longValue() - ((BleDevice) SHDeviceService.this.mDeviceList.get(i)).timestamp.longValue() > DateUtils.MILLIS_PER_MINUTE) {
            String str = SHDeviceService.TAG;
            Log.i(str, "pruning:address " + ((BleDevice) SHDeviceService.this.mDeviceList.get(i)).address);
            SHDeviceService.this.mDeviceList.remove(i);
            z = true;
          }
        }
        if (z) {
          SHDeviceService.this.broadcastDeviceListUpdated();
        }
      }
    });
  }

  public void stopScan() {
    BluetoothLeScanner bluetoothLeScanner;
    if (this.mIsScanning) {
      Log.i(TAG, "Stopping active ble scan");
      BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
      if (bluetoothAdapter != null && bluetoothAdapter.isEnabled() && (bluetoothLeScanner = this.mBluetoothAdapter.getBluetoothLeScanner()) != null) {
        bluetoothLeScanner.stopScan(this.shScanCallback);
      }
      this.mIsScanning = false;
    }
  }

  @Override // bike.smarthalo.sdk.SHScanCallback.ScanCallbackContract
  public void handleDeviceFound(BluetoothDevice bluetoothDevice, String str, String str2, int i, boolean z, DeviceModel deviceModel) {
    String address;
    if (bluetoothDevice == null || str == null || (address = bluetoothDevice.getAddress()) == null) {
      return;
    }
    if (this.serviceStorageController.hasValidId() && str.equals(this.serviceStorageController.id)) {
      if (!this.serviceStorageController.hasValidAddress() && !z) {
        this.serviceStorageController.set(ServiceStorageController.ADDRESS_KEY, address);
      }
      if (z) {
        Intent intent = new Intent(SHDeviceServiceIntents.BROADCAST_SH_BL_ADDRESS);
        intent.putExtra(SHDeviceServiceIntents.EXTRA_BOOTLOADER_ADDRESS, address);
        intent.putExtra(SHDeviceServiceIntents.EXTRA_DEVICE_ID, str);
        intent.putExtra(SHDeviceServiceIntents.EXTRA_DEVICE_NAME, str2);
        sendBroadcast(intent);
        return;
      } else if (getConnectionState() == DeviceConnectionState.Disconnected) {
        connectToSavedDevice(false);
        return;
      } else {
        return;
      }
    }
    String name = bluetoothDevice.getName() != null ? bluetoothDevice.getName() : "Device";
    BleDevice bleDevice = new BleDevice();
    bleDevice.name = name;
    bleDevice.address = address;
    bleDevice.id = str;
    bleDevice.timestamp = Long.valueOf(System.currentTimeMillis());
    bleDevice.rssi = Integer.valueOf(i);
    bleDevice.deviceModel = deviceModel;
    if (this.mDeviceList.contains(bleDevice)) {
      int indexOf = this.mDeviceList.indexOf(bleDevice);
      if (indexOf > 0) {
        this.mDeviceList.set(indexOf, bleDevice);
      }
    } else {
      this.mDeviceList.add(bleDevice);
    }
    broadcastDeviceListUpdated();
  }

  private void removeDeviceWithIdFromList(String str) {
    if (str == null) {
      return;
    }
    for (int i = 0; i < this.mDeviceList.size(); i++) {
      if (str.equals(this.mDeviceList.get(i).id)) {
        this.mDeviceList.remove(i);
        broadcastDeviceListUpdated();
        return;
      }
    }
  }

  /* JADX INFO: Access modifiers changed from: private */
  public void broadcastDeviceListUpdated() {
    sendBroadcast(new Intent(SHDeviceServiceIntents.BROADCAST_DEVICE_LIST_UPDATED));
  }

  @Override // bike.smarthalo.sdk.encryption.EncryptionContract
  public AES_128_CBC_PKCS5 getEncryptionKey() {
    return this.mAES;
  }
}