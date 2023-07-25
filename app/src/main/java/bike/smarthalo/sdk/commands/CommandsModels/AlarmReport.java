package bike.smarthalo.sdk.commands.CommandsModels;

public class AlarmReport {
  public boolean hasValue;
  public boolean isArmed;
  public boolean isVigilant;
  public int triggerCount;

  /* loaded from: classes.dex */
  public interface AlarmReportContract {
    void onAlarmReportReady(AlarmReport alarmReport);
  }

  public AlarmReport(byte[] bArr) {
    this.hasValue = false;
    if (bArr.length == 5 && bArr[0] == 0) {
      this.hasValue = true;
      this.isArmed = bArr[1] == 1;
      this.triggerCount = bArr[2] & 255;
      this.isVigilant = bArr[3] == 1;
    }
  }
}
