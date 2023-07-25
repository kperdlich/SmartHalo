package bike.smarthalo.sdk.firmware;

public class NordicFirmwareHelper {
  private static final String SH1_PROTOCOL_1 = "1.1.0.0";
  
  private static final String SH1_PROTOCOL_2 = "1.2.0.0";
  
  private static final String SH1_PROTOCOL_2_1 = "1.2.1.0";
  
  private static final String SH1_PROTOCOL_3 = "1.3.0.0";
  
  private static final String SH1_PROTOCOL_6 = "1.6.0.0";
  
  private static final String SH1_PROTOCOL_7 = "1.7.0.0";
  
  private static final String SH2_PROTOCOL = "2.0.0.0";
  
  public static String getProtocolString(NordicProtocolVersion paramNordicProtocolVersion) {
    switch (paramNordicProtocolVersion) {
      default:
        return "";
      case V2:
        return "2.0.0.0";
      case V1_7:
        return "1.7.0.0";
      case V1_6:
        return "1.6.0.0";
      case V1_3:
        return "1.3.0.0";
      case V1_2_1:
        return "1.2.1.0";
      case V1_2:
        return "1.2.0.0";
      case V1_1:
        break;
    } 
    return "1.1.0.0";
  }
}
