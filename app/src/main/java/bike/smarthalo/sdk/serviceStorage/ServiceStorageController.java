package bike.smarthalo.sdk.serviceStorage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import bike.smarthalo.sdk.models.BleDevice;
import bike.smarthalo.sdk.models.DeviceModel;

public class ServiceStorageController implements CurrentDeviceContract {
  public static final String ADDRESS_KEY = "ADDRESS_KEY";
  
  public static final String DEFAULT_NAME = "SmartHalo";
  
  public static final String DEFAULT_PASSWORD = "";
  
  public static final String DEVICE_VERSION_KEY = "DEVICE_VERSION_KEY";
  
  public static final String ID_KEY = "ID_KEY";
  
  public static final String IS_TESTER_KEY = "IS_TESTER_KEY";
  
  public static final String LAST_KNOWN_FIRMWARE_VERSION_KEY = "LAST_KNOWN_FIRMWARE_VERSION_KEY";
  
  public static final String NAME_KEY = "NAME_KEY";
  
  public static final String PASSWORD_KEY = "PASSWORD_KEY";
  
  public static final String TAG = "ServiceStorageController";
  
  public String address;
  
  private Context context;
  
  public String deviceVersion;
  
  public String id;
  
  public boolean isTester;
  
  public String lastKnownFirmwareVersion;
  
  public String name;
  
  public String password;
  
  private ServiceStorageController(Context paramContext) {
    this.context = paramContext;
    this.name = "SmartHalo";
    this.address = "";
    this.id = "";
    this.password = "YKd57tOAWOkWjxMIZUX6xyFMOWvr5pdK";
    this.deviceVersion = "";
    this.isTester = false;
  }
  
  public static ServiceStorageController getInstance(Context paramContext) {
    SharedPreferences sharedPreferences = paramContext.getSharedPreferences(TAG, 0);
    ServiceStorageController serviceStorageController = new ServiceStorageController(paramContext);
    if (sharedPreferences.contains("NAME_KEY"))
      serviceStorageController.name = sharedPreferences.getString("NAME_KEY", "SmartHalo"); 
    if (sharedPreferences.contains("ADDRESS_KEY"))
      serviceStorageController.address = sharedPreferences.getString("ADDRESS_KEY", ""); 
    if (sharedPreferences.contains("ID_KEY"))
      serviceStorageController.id = sharedPreferences.getString("ID_KEY", ""); 
    if (sharedPreferences.contains("PASSWORD_KEY"))
      serviceStorageController.password = sharedPreferences.getString("PASSWORD_KEY", ""); 
    if (sharedPreferences.contains("LAST_KNOWN_FIRMWARE_VERSION_KEY"))
      serviceStorageController.lastKnownFirmwareVersion = sharedPreferences.getString("LAST_KNOWN_FIRMWARE_VERSION_KEY", ""); 
    if (sharedPreferences.contains("IS_TESTER_KEY"))
      serviceStorageController.isTester = sharedPreferences.getBoolean("IS_TESTER_KEY", false); 
    if (sharedPreferences.contains("DEVICE_VERSION_KEY"))
      serviceStorageController.deviceVersion = sharedPreferences.getString("DEVICE_VERSION_KEY", ""); 
    return serviceStorageController;
  }
  
  private boolean isValidString(String paramString) {
    boolean bool;
    if (paramString != null && paramString.length() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void forgetPassword() {
    SharedPreferences sharedPreferences = this.context.getSharedPreferences(TAG, 0);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    if (sharedPreferences.contains("PASSWORD_KEY"))
      editor.remove("PASSWORD_KEY"); 
    if (editor.commit())
      this.password = ""; 
  }
  
  public void forgetSavedDevice() {
    SharedPreferences sharedPreferences = this.context.getSharedPreferences(TAG, 0);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    if (sharedPreferences.contains("NAME_KEY"))
      editor.remove("NAME_KEY"); 
    if (sharedPreferences.contains("ADDRESS_KEY"))
      editor.remove("ADDRESS_KEY"); 
    if (sharedPreferences.contains("ID_KEY"))
      editor.remove("ID_KEY"); 
    if (sharedPreferences.contains("LAST_KNOWN_FIRMWARE_VERSION_KEY"))
      editor.remove("LAST_KNOWN_FIRMWARE_VERSION_KEY"); 
    if (sharedPreferences.contains("DEVICE_VERSION_KEY"))
      editor.remove("DEVICE_VERSION_KEY"); 
    if (editor.commit()) {
      this.name = "SmartHalo";
      this.address = "";
      this.id = "";
      this.lastKnownFirmwareVersion = "";
      this.deviceVersion = "";
    } 
  }
  
  @Nullable
  public BleDevice getCurrentDevice() {
    if (hasValidDevice()) {
      BleDevice bleDevice = new BleDevice();
      bleDevice.name = this.name;
      bleDevice.address = this.address;
      bleDevice.id = this.id;
      bleDevice.deviceModel = DeviceModel.fromSavedData(this.deviceVersion);
      return bleDevice;
    } 
    return null;
  }
  
  public boolean hasValidAddress() {
    return isValidString(this.address);
  }
  
  public boolean hasValidDevice() {
    boolean bool;
    if (isValidString(this.id) && isValidString(this.address)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasValidId() {
    return isValidString(this.id);
  }
  
  public boolean hasValidPassword() {
    return isValidString(this.password);
  }
  
  public boolean isValidForConnection() {
    boolean bool;
    if (hasValidDevice() && hasValidPassword()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void logout() {
    forgetSavedDevice();
    forgetPassword();
  }
  
  public void set(String paramString1, String paramString2) {
    SharedPreferences.Editor editor = this.context.getSharedPreferences(TAG, 0).edit();
    if ("PASSWORD_KEY".equals(paramString1)) {
      if (isValidString(paramString2))
        editor.putString(paramString1, paramString2); 
    } else {
      editor.putString(paramString1, paramString2);
    } 
    if (editor.commit())
      if ("NAME_KEY".equals(paramString1)) {
        paramString1 = paramString2;
        if (paramString2 == null)
          paramString1 = "SmartHalo"; 
        this.name = paramString1;
      } else if ("ADDRESS_KEY".equals(paramString1)) {
        paramString1 = paramString2;
        if (paramString2 == null)
          paramString1 = ""; 
        this.address = paramString1;
      } else if ("DEVICE_VERSION_KEY".equals(paramString1)) {
        paramString1 = paramString2;
        if (paramString2 == null)
          paramString1 = ""; 
        this.deviceVersion = paramString1;
      } else if ("ID_KEY".equals(paramString1)) {
        paramString1 = paramString2;
        if (paramString2 == null)
          paramString1 = ""; 
        this.id = paramString1;
      } else if ("PASSWORD_KEY".equals(paramString1)) {
        if (isValidString(paramString2))
          this.password = paramString2; 
      } else if ("LAST_KNOWN_FIRMWARE_VERSION_KEY".equals(paramString1) && isValidString(paramString2)) {
        this.lastKnownFirmwareVersion = paramString2;
      }  
  }
  
  public void setId(String paramString) {
    if (paramString == null || paramString.length() == 0)
      return; 
    set("ID_KEY", paramString);
  }
  
  public void setIsTester(boolean paramBoolean) {
    SharedPreferences.Editor editor = this.context.getSharedPreferences(TAG, 0).edit();
    editor.putBoolean("IS_TESTER_KEY", paramBoolean);
    if (editor.commit())
      this.isTester = paramBoolean; 
  }
  
  public void setName(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null && paramArrayOfbyte.length > 0)
      set("NAME_KEY", new String(paramArrayOfbyte)); 
  }
  
  public void setPassword(String paramString) {
    if (paramString == null || paramString.length() == 0)
      return; 
    set("PASSWORD_KEY", paramString);
  }
}
