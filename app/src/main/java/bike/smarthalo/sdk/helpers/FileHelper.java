package bike.smarthalo.sdk.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileHelper {
  public static byte[] getBytesFromFile(File file) throws IOException {
    int length = (int) file.length();
    byte[] bArr = new byte[length];
    byte[] bArr2 = new byte[length];
    FileInputStream fileInputStream = new FileInputStream(file);
    try {
      try {
        int read = fileInputStream.read(bArr, 0, length);
        if (read < length) {
          int i = length - read;
          while (i > 0) {
            int read2 = fileInputStream.read(bArr2, 0, i);
            System.arraycopy(bArr2, 0, bArr, length - i, read2);
            i -= read2;
          }
        }
        return bArr;
      } catch (IOException e) {
        throw e;
      }
    } finally {
      fileInputStream.close();
    }
  }
}