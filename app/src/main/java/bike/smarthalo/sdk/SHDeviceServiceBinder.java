package bike.smarthalo.sdk;

import android.os.Binder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import bike.smarthalo.sdk.bluetooth.BluetoothSpeedMetrics;
import bike.smarthalo.sdk.bluetooth.StmDfuController;
import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationSourceContract;
import bike.smarthalo.sdk.bluetooth.stmDfuModels.StmDfuInformation;
import bike.smarthalo.sdk.commands.CommandsModels.AlarmReport;
import bike.smarthalo.sdk.commands.CommandsModels.GenericBleCommand;
import bike.smarthalo.sdk.commands.CommandsModels.GenericCommandPayload;
import bike.smarthalo.sdk.commands.CommandsModels.NotificationCommand;
import bike.smarthalo.sdk.commands.CommandsModels.TurnByTurnIntroMode;
import bike.smarthalo.sdk.commands.SuccessCallback;
import bike.smarthalo.sdk.commands.carousel.ClockCommandPayload;
import bike.smarthalo.sdk.commands.carousel.MetricsCarouselMask;
import bike.smarthalo.sdk.commands.carousel.MetricsCarouselPosition;
import bike.smarthalo.sdk.commands.carousel.ProgressCommandPayload;
import bike.smarthalo.sdk.commands.carousel.SpeedometerCommandPayload;
import bike.smarthalo.sdk.helpers.DebugLoggerDataContract;
import bike.smarthalo.sdk.models.BleDevice;
import bike.smarthalo.sdk.models.DeviceConnectionState;
import bike.smarthalo.sdk.models.DeviceInformation;
import bike.smarthalo.sdk.models.OledAnimationType;
import bike.smarthalo.sdk.models.SHColour;
import bike.smarthalo.sdk.models.SHDeviceState;
import bike.smarthalo.sdk.stmUtils.SmartHaloOSCrash;
import io.reactivex.rxjava3.core.Flowable;

public class SHDeviceServiceBinder extends Binder {
  private SHDeviceService service;
  
  public SHDeviceServiceBinder(SHDeviceService paramSHDeviceService) {
    this.service = paramSHDeviceService;
  }
  
  public Boolean areTouchInputsSupported() {
    return this.service.areTouchInputsSupported();
  }
  
  public void calibrateSwipe(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().calibrateSwipe(paramInt, paramCmdCallback);
  }
  
  public void calibrateTouch(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().calibrateTouch(paramInt, paramCmdCallback);
  }
  
  public void config_name(byte[] paramArrayOfbyte, CmdCallback paramCmdCallback) {
    this.service.getDeviceCommandsController().config_name(paramArrayOfbyte, paramCmdCallback);
  }
  
  public void configureAlarm(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, CmdCallback paramCmdCallback) {
    this.service.getAlarmCommandsController().configureAlarm(paramArrayOfbyte, paramInt1, paramInt2, paramBoolean1, paramBoolean2, false, paramCmdCallback);
  }
  
  public void configureAlarmSeverity(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, CmdCallback paramCmdCallback) {
    this.service.getAlarmCommandsController().configureAlarm(paramArrayOfbyte, paramInt1, paramInt2, paramBoolean1, paramBoolean2, true, paramCmdCallback);
  }
  
  public boolean connect() {
    return this.service.manualConnect();
  }
  
  public void forceInstallGoldenFirmware(SuccessCallback paramSuccessCallback) {
    this.service.getStmDfuController().forceInstallGoldenFirmware(paramSuccessCallback);
  }
  
  public void forgetSavedDeviceAndDisconnect() {
    this.service.resetLocalStorageAndDisconnect();
  }
  
  public void getAlarmReport(AlarmReport.AlarmReportContract paramAlarmReportContract) {
    this.service.getAlarmCommandsController().getAlarmReport(paramAlarmReportContract);
  }
  
  @Nullable
  public Flowable<Integer> getAndObserveStmDfuSource() {
    return this.service.getStmDfuController().getAndObserveDfuFlowSource();
  }
  
  public BleDevice getBleDeviceFromDeviceId(String paramString) {
    return this.service.getBleDeviceFromId(paramString);
  }
  
  public BleNotificationSourceContract getBleNotificationController() {
    return this.service.getBleNotificationSourceController();
  }
  
  public BluetoothSpeedMetrics getBluetoothSpeedMetrics() {
    return this.service.getBluetoothSpeedMonitor().getBluetoothSpeedMetrics();
  }
  
  public DeviceConnectionState getConnectionState() {
    return this.service.getConnectionState();
  }
  
  public BleDevice getCurrentDevice() {
    return this.service.getServiceStorage().getCurrentDevice();
  }
  
  @Nullable
  public StmDfuInformation getCurrentStmDfuInformation() {
    return this.service.getStmDfuController().getCurrentStmDfuInformation();
  }
  
  public DebugLoggerDataContract getDebugLoggerData() {
    return this.service.getDebugLogger();
  }
  
  @Nullable
  public DeviceInformation getDeviceInformation() {
    return this.service.getDeviceCommandsController().getDeviceInformation();
  }
  
  public ArrayList<BleDevice> getDeviceList() {
    return this.service.getDeviceList();
  }
  
  public void getDeviceState(SHDeviceState.DeviceStateInterface paramDeviceStateInterface) {
    this.service.getDeviceCommandsController().getDeviceState(paramDeviceStateInterface);
  }
  
  public DeviceConnectionState getPreviousConnectionState() {
    return this.service.getPreviousConnectionState();
  }
  
  public void get_alarm_seed(CmdCallback paramCmdCallback) {
    this.service.getAlarmCommandsController().get_alarm_seed(paramCmdCallback);
  }
  
  public void initializeStmDfu(byte[] paramArrayOfbyte, StmDfuController.InitializeStmDfuCallback paramInitializeStmDfuCallback) {
    this.service.getStmDfuController().initializeStmDfu(paramArrayOfbyte, paramInitializeStmDfuCallback);
  }
  
  public void installTransferredFirmware(StmDfuController.DfuInstallCallback paramDfuInstallCallback) {
    this.service.getStmDfuController().installTransferredFirmware(paramDfuInstallCallback);
  }
  
  public Flowable<SmartHaloOSCrash> observeSmartHaloOSCrash() {
    return this.service.getSmartHaloOSCrashReporter().observeSmartHaloOSCrash();
  }
  
  public void play_sound(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, CmdCallback paramCmdCallback) {
    this.service.getSoundsCommandsController().play_sound(paramInt1, paramInt2, paramArrayOfbyte, paramCmdCallback);
  }
  
  public void requestBootloader(CmdCallback paramCmdCallback, boolean paramBoolean) {
    this.service.getDeviceCommandsController().requestBootloader(paramCmdCallback, paramBoolean);
  }
  
  public void requireExternalCommandForLight(boolean paramBoolean, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_front_external_toggle(paramBoolean, paramCmdCallback);
  }
  
  public void resetPasswordAndDisconnect() {
    this.service.resetPasswordAndDisconnect();
  }
  
  public void resetPasswordAndDisconnect(CmdCallback paramCmdCallback) {
    this.service.resetPasswordAndDisconnect(paramCmdCallback);
  }
  
  public void sendGenericCommand(GenericBleCommand paramGenericBleCommand) {
    this.service.getUICommandsController().sendGenericCommand(paramGenericBleCommand);
  }
  
  public void sendNotificationCommand(NotificationCommand paramNotificationCommand) {
    this.service.getUICommandsController().sendNotificationCommand(paramNotificationCommand);
  }
  
  public SHDeviceServiceBinder setId(String paramString) {
    this.service.getServiceStorage().setId(paramString);
    return this;
  }
  
  public SHDeviceServiceBinder setMyDevice(String paramString1, String paramString2) {
    this.service.setMyDevice(paramString1, paramString2);
    return this;
  }
  
  public void setOledBrightness(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().setOledBrightness(paramInt, paramCmdCallback);
  }
  
  public void setOledContrast(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().setOledContrast(paramInt, paramCmdCallback);
  }
  
  public SHDeviceServiceBinder setPassword(String paramString) {
    this.service.getServiceStorage().setPassword(paramString);
    return this;
  }
  
  public void set_alarm_state(int paramInt1, int paramInt2, CmdCallback paramCmdCallback) {
    this.service.getAlarmCommandsController().set_alarm_state(paramInt1, paramInt2, paramCmdCallback);
  }
  
  public void showCarousel(MetricsCarouselPosition paramMetricsCarouselPosition, MetricsCarouselMask paramMetricsCarouselMask, GenericCommandPayload paramGenericCommandPayload, CmdCallback paramCmdCallback) {
    this.service.getCarouselCommandsController().showCarousel(paramMetricsCarouselPosition, paramMetricsCarouselMask, paramGenericCommandPayload, paramCmdCallback);
  }
  
  public void showCarouselPosition(MetricsCarouselPosition paramMetricsCarouselPosition, MetricsCarouselMask paramMetricsCarouselMask, CmdCallback paramCmdCallback) {
    this.service.getCarouselCommandsController().showCarouselPosition(paramMetricsCarouselPosition, paramMetricsCarouselMask, paramCmdCallback);
  }
  
  public void showChargeState(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_show_charge_state(paramCmdCallback);
  }
  
  public void showClock(ClockCommandPayload paramClockCommandPayload, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_clock(paramClockCommandPayload, paramCmdCallback);
  }
  
  public void showDemo(int paramInt1, int paramInt2, int paramInt3, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().showDemo(paramInt1, paramInt2, paramInt3, paramCmdCallback);
  }
  
  public void showDemo(int paramInt1, int paramInt2, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().showDemo(paramInt1, paramInt2, -1, paramCmdCallback);
  }
  
  public void showDemo(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().showDemo(paramInt, -1, -1, paramCmdCallback);
  }
  
  public void showOled(int paramInt1, int paramInt2, OledAnimationType paramOledAnimationType, int paramInt3, int paramInt4, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().showOled(paramInt1, paramInt2, paramOledAnimationType, paramInt3, paramInt4, paramCmdCallback);
  }
  
  public void showPointerIntro(SHColour paramSHColour, int paramInt1, int paramInt2, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_pointer_intro(paramSHColour, paramInt1, paramInt2, paramCmdCallback);
  }
  
  public void showPointerIntroStandby(SHColour paramSHColour, int paramInt, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_pointer_intro_standby(paramSHColour, paramInt, paramCmdCallback);
  }
  
  public void showTurnByTurnIntro(TurnByTurnIntroMode paramTurnByTurnIntroMode, String paramString, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_turn_by_turn_intro(paramTurnByTurnIntroMode, paramString, paramCmdCallback);
  }
  
  public Flowable<Integer> startStmDfu() {
    return this.service.getStmDfuController().startStmDfu();
  }
  
  public void startTouchTest(SuccessCallback paramSuccessCallback) {
    this.service.getDeviceCommandsController().startTouchTest(paramSuccessCallback);
  }
  
  public void stopAndRestartService() {
    this.service.cleanUpDeviceConnection();
  }
  
  public void stopClock(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_clock_off(paramCmdCallback);
  }
  
  public void stopOled(CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().stopOled(paramCmdCallback);
  }
  
  public void stopScan() {
    this.service.stopScan();
  }
  
  public void stop_sound(CmdCallback paramCmdCallback) {
    this.service.getSoundsCommandsController().stop_sound(paramCmdCallback);
  }
  
  public void toggleTouchSounds(boolean paramBoolean, int paramInt, CmdCallback paramCmdCallback) {
    this.service.getSoundsCommandsController().touch_sounds(paramBoolean, paramInt, paramCmdCallback);
  }
  
  public void ui_anim_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_anim_off(paramCmdCallback);
  }
  
  @Deprecated
  public void ui_central(SHColour paramSHColour1, SHColour paramSHColour2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().sendNotificationCommand(new NotificationCommand(paramSHColour1, paramSHColour2, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramCmdCallback));
  }
  
  public void ui_central_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_central_off(paramCmdCallback);
  }
  
  public void ui_compass(SHColour paramSHColour, int paramInt, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_compass(paramSHColour, paramInt, paramCmdCallback);
  }
  
  public void ui_compass_calibrate(CmdCallback paramCmdCallback) {
    this.service.getDeviceCommandsController().cmd_compass_calibrate(paramCmdCallback);
  }
  
  public void ui_compass_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_compass_off(paramCmdCallback);
  }
  
  public void ui_disconnect(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_disconnect(paramCmdCallback);
  }
  
  public void ui_fitness_intro(SHColour paramSHColour1, SHColour paramSHColour2, int paramInt, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_fitness_intro(paramSHColour1, paramSHColour2, paramInt, paramCmdCallback);
  }
  
  public void ui_frontLight(boolean paramBoolean1, boolean paramBoolean2, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_frontLight(paramBoolean1, paramBoolean2, paramCmdCallback);
  }
  
  public void ui_hb(SHColour paramSHColour1, SHColour paramSHColour2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_hb(paramSHColour1, paramSHColour2, paramInt1, paramInt2, paramInt3, paramInt4, paramCmdCallback);
  }
  
  public void ui_hb_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_hb_off(paramCmdCallback);
  }
  
  public void ui_logo(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_logo(paramCmdCallback);
  }
  
  public void ui_lowBat(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_lowBat(paramCmdCallback);
  }
  
  public void ui_nav(SHColour paramSHColour, int paramInt1, int paramInt2, int paramInt3, String paramString, boolean paramBoolean, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav(paramSHColour, paramInt1, paramInt2, paramInt3, paramString, paramBoolean, paramCmdCallback);
  }
  
  public void ui_nav_angle_quick(SHColour paramSHColour1, int paramInt1, int paramInt2, SHColour paramSHColour2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString, boolean paramBoolean1, boolean paramBoolean2, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav_angle_quick(paramSHColour1, paramInt1, paramInt2, paramSHColour2, paramInt3, paramInt4, paramInt5, paramInt6, paramString, paramBoolean1, paramBoolean2, paramCmdCallback);
  }
  
  public void ui_nav_experimental_destination_angle(SHColour paramSHColour, int paramInt1, int paramInt2, CmdCallback paramCmdCallback) {
    this.service.getExperimentalCommandsController().ui_nav_experimental_destination_angle(paramSHColour, paramInt1, paramInt2, paramCmdCallback);
  }
  
  public void ui_nav_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav_off(paramCmdCallback);
  }
  
  public void ui_nav_pointer(SHColour paramSHColour, int paramInt1, boolean paramBoolean, int paramInt2, String paramString, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav_pointer(paramSHColour, paramInt1, paramBoolean, paramInt2, paramString, paramCmdCallback);
  }
  
  public void ui_nav_pointer_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav_pointer_off(paramCmdCallback);
  }
  
  public void ui_nav_pointer_standby(SHColour paramSHColour, boolean paramBoolean, int paramInt, String paramString, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_pointer_standby(paramSHColour, paramBoolean, paramInt, paramString, paramCmdCallback);
  }
  
  public void ui_nav_reroute(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_nav_reroute(paramCmdCallback);
  }
  
  public void ui_nav_roundabout(int paramInt1, int paramInt2, SHColour paramSHColour1, SHColour paramSHColour2, int[] paramArrayOfint, int paramInt3, boolean paramBoolean, String paramString) {
    this.service.getUICommandsController().ui_nav_roundabout(paramInt1, paramInt2, paramSHColour1, paramSHColour2, paramArrayOfint, paramInt3, paramBoolean, paramString);
  }
  
  public void ui_progress(ProgressCommandPayload paramProgressCommandPayload, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_progress(paramProgressCommandPayload, paramCmdCallback);
  }
  
  public void ui_progress_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_progress_off(paramCmdCallback);
  }
  
  public void ui_setBrightness(int paramInt, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_setBrightness(paramInt, paramCmdCallback);
  }
  
  public void ui_speedometer_intro(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_speedometer_intro(paramCmdCallback);
  }
  
  public void ui_speedometer_off(CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_speedometer_off(paramCmdCallback);
  }
  
  public void ui_speedometer_speed(SpeedometerCommandPayload paramSpeedometerCommandPayload, CmdCallback paramCmdCallback) {
    this.service.getUICommandsController().ui_speedometer_speed(paramSpeedometerCommandPayload, paramCmdCallback);
  }
}
