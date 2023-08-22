package bike.smarthalo.sdk.commands;

public class CommandsConstants {
  public static final int FRONTLIGHT_BLINK = 1;
  
  public static final int FRONTLIGHT_CONSTANT = 0;
  
  public static final int MAX_COMMAND_LENGTH = 80;
  
  public static final int NAV_BACK = 8;
  
  public static final int NAV_BACK_LEFT = 7;
  
  public static final int NAV_BACK_RIGHT = 4;
  
  public static final int NAV_DESTINATION = 0;
  
  public static final int NAV_FRONT = 1;
  
  public static final int NAV_FRONT_LEFT = 5;
  
  public static final int NAV_FRONT_RIGHT = 2;
  
  public static final int NAV_LEFT = 6;
  
  public static final int NAV_MODE_CORRECTION = 0;
  
  public static final int NAV_MODE_NORMAL = 1;
  
  public static final int NAV_RIGHT = 3;
  
  public static final byte[] cmd_animOff;
  
  public static final byte[] cmd_arm;
  
  public static final byte[] cmd_authenticate;
  
  public static final byte[] cmd_carousel;
  
  public static final byte[] cmd_carousel_position;
  
  public static final byte[] cmd_clock;
  
  public static final byte[] cmd_clock_off;
  
  public static final byte[] cmd_compass;
  
  public static final byte[] cmd_compassCalibrate;
  
  public static final byte[] cmd_compassOff;
  
  public static final byte[] cmd_demo;
  
  public static final byte[] cmd_deviceStmDfuCrc;
  
  public static final byte[] cmd_deviceStmDfuData;
  
  public static final byte[] cmd_deviceStmDfuInstall;
  
  public static final byte[] cmd_disconnect;
  
  public static final byte[] cmd_enterBootloader;
  
  public static final byte[] cmd_experimental_nav_destination_angle;
  
  public static final byte[] cmd_experimental_oled_brightness;
  
  public static final byte[] cmd_experimental_oled_contrast;
  
  public static final byte[] cmd_experimental_show_oled;
  
  public static final byte[] cmd_experimental_stop_oled;
  
  public static final byte[] cmd_experimental_swipe_calibration;
  
  public static final byte[] cmd_experimental_touch_calibration;
  
  public static final byte[] cmd_fitness_intro;
  
  public static final byte[] cmd_forceInstallGolden;
  
  public static final byte[] cmd_front_external_toggle;
  
  public static final byte[] cmd_frontlight;
  
  public static final byte[] cmd_frontlightSettings;
  
  public static final byte[] cmd_getPeriphPubKey;
  
  public static final byte[] cmd_getSeed;
  
  public static final byte[] cmd_getSerial;
  
  public static final byte[] cmd_getState;
  
  public static final byte[] cmd_getVersions;
  
  public static final byte[] cmd_hb;
  
  public static final byte[] cmd_hbOff;
  
  public static final byte[] cmd_localization;
  
  public static final byte[] cmd_logo = new byte[] { 1, 0 };
  
  public static final byte[] cmd_lowBat;
  
  public static final byte[] cmd_nav = new byte[] { 1, 1 };
  
  public static final byte[] cmd_navAngle;
  
  public static final byte[] cmd_navOff;
  
  public static final byte[] cmd_navReroute = new byte[] { 1, 2 };
  
  public static final byte[] cmd_nav_pointer;
  
  public static final byte[] cmd_nav_pointer_off;
  
  public static final byte[] cmd_nav_pointer_standby;
  
  public static final byte[] cmd_notif;
  
  public static final byte[] cmd_notif_off;
  
  public static final byte[] cmd_play;
  
  public static final byte[] cmd_pointer_with_intro;
  
  public static final byte[] cmd_progress;
  
  public static final byte[] cmd_progressOff;
  
  public static final byte[] cmd_report;
  
  public static final byte[] cmd_roundabout;
  
  public static final byte[] cmd_roundabout_oled;
  
  public static final byte[] cmd_setBrightness;
  
  public static final byte[] cmd_setCentralPubKey;
  
  public static final byte[] cmd_setConfig;
  
  public static final byte[] cmd_setName;
  
  public static final byte[] cmd_setPassword;
  
  public static final byte[] cmd_show_state_of_charger;
  
  public static final byte[] cmd_shutdown;
  
  public static final byte[] cmd_speedometer;
  
  public static final byte[] cmd_speedometerOff;
  
  public static final byte[] cmd_speedometer_intro;
  
  public static final byte[] cmd_stop;
  
  public static final byte[] cmd_touchTest;
  
  public static final byte[] cmd_touch_onboarding;
  
  public static final byte[] cmd_touch_sounds;
  
  public static final byte[] cmd_turn_by_turn_intro;

  public static final byte[] cmd_tst_auth_cmd_getSeed;

  public static final byte[] tst_auth_cmd_enterFactory;

  public static final byte ret_denied = 2;
  
  public static final byte ret_fail = 1;
  
  public static final byte ret_ok = 0;
  
  public static final byte ret_unimplemented = 3;
  
  public static final byte ret_unnecessary = 4;
  
  static {
    cmd_navOff = new byte[] { 1, 3 };
    cmd_frontlight = new byte[] { 1, 4 };
    cmd_progress = new byte[] { 1, 5 };
    cmd_progressOff = new byte[] { 1, 6 };
    cmd_notif = new byte[] { 1, 7 };
    cmd_notif_off = new byte[] { 1, 8 };
    cmd_hb = new byte[] { 1, 9 };
    cmd_hbOff = new byte[] { 1, 10 };
    cmd_compass = new byte[] { 1, 11 };
    cmd_compassOff = new byte[] { 1, 12 };
    cmd_disconnect = new byte[] { 1, 13 };
    cmd_animOff = new byte[] { 1, 14 };
    cmd_setBrightness = new byte[] { 1, 15 };
    cmd_frontlightSettings = new byte[] { 1, 16 };
    cmd_navAngle = new byte[] { 1, 17 };
    cmd_speedometer = new byte[] { 1, 18 };
    cmd_speedometerOff = new byte[] { 1, 19 };
    cmd_roundabout = new byte[] { 1, 20 };
    cmd_lowBat = new byte[] { 1, 21 };
    cmd_nav_pointer = new byte[] { 1, 22 };
    cmd_nav_pointer_off = new byte[] { 1, 23 };
    cmd_nav_pointer_standby = new byte[] { 1, 24 };
    cmd_demo = new byte[] { 1, 25 };
    cmd_speedometer_intro = new byte[] { 1, 26 };
    cmd_fitness_intro = new byte[] { 1, 27 };
    cmd_clock = new byte[] { 1, 28 };
    cmd_clock_off = new byte[] { 1, 29 };
    cmd_front_external_toggle = new byte[] { 1, 30 };
    cmd_show_state_of_charger = new byte[] { 1, 31 };
    cmd_turn_by_turn_intro = new byte[] { 1, 32 };
    cmd_pointer_with_intro = new byte[] { 1, 33 };
    cmd_roundabout_oled = new byte[] { 1, 34 };
    cmd_carousel = new byte[] { 1, 35 };
    cmd_carousel_position = new byte[] { 1, 36 };
    cmd_touch_onboarding = new byte[] { 1, 37 };
    cmd_play = new byte[] { 2, 0 };
    cmd_stop = new byte[] { 2, 1 };
    cmd_touch_sounds = new byte[] { 2, 2 };
    cmd_report = new byte[] { 3, 0 };
    cmd_getSeed = new byte[] { 3, 1 };
    cmd_arm = new byte[] { 3, 2 };
    cmd_setConfig = new byte[] { 3, 3 };
    cmd_enterBootloader = new byte[] { 4, 0 };
    cmd_getState = new byte[] { 4, 1 };
    cmd_setName = new byte[] { 4, 2 };
    cmd_compassCalibrate = new byte[] { 4, 3 };
    cmd_shutdown = new byte[] { 4, 4 };
    cmd_getSerial = new byte[] { 4, 5 };
    cmd_deviceStmDfuCrc = new byte[] { 4, 7 };
    cmd_deviceStmDfuData = new byte[] { 4, 8 };
    cmd_deviceStmDfuInstall = new byte[] { 4, 9 };
    cmd_forceInstallGolden = new byte[] { 4, 10 };
    cmd_localization = new byte[] { 4, 6 };
    cmd_getVersions = new byte[] { 0, 0 };
    cmd_getPeriphPubKey = new byte[] { 0, 1 };
    cmd_setCentralPubKey = new byte[] { 0, 2 };
    cmd_authenticate = new byte[] { 0, 3 };
    cmd_setPassword = new byte[] { 0, 4 };
    cmd_touchTest = new byte[] { -14, 0 };
    cmd_experimental_nav_destination_angle = new byte[] { -8, 13 };
    cmd_experimental_show_oled = new byte[] { -8, 21 };
    cmd_experimental_stop_oled = new byte[] { -8, 22 };
    cmd_experimental_oled_contrast = new byte[] { -8, 23 };
    cmd_experimental_oled_brightness = new byte[] { -8, 24 };
    cmd_experimental_touch_calibration = new byte[] { -8, 25 };
    cmd_experimental_swipe_calibration = new byte[] { -8, 26 };
    cmd_tst_auth_cmd_getSeed = new byte[] { 0xF, 0 };
    tst_auth_cmd_enterFactory = new byte[] { 0xF, 1 };
  }
}
