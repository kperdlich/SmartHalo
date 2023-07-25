package bike.smarthalo.sdk;

public class DemoCommandsHelper {
  public static int getDemoTapcodeValue(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramString.length(); b++) {
      String str;
      if (paramString.charAt(b) == 'L') {
        str = "1";
      } else {
        str = "0";
      } 
      stringBuilder.insert(0, str);
    } 
    return Integer.parseInt(stringBuilder.toString(), 2);
  }
}
