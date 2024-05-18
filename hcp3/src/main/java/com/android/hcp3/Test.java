package com.android.hcp3;

public class Test {
  public static void main(String[] args) {
    String path = "hcp3/src/main/java/com/android/hcp3/ValueControlObjectEntity.java";
    RandomAccessFileUtil.changeFileContent(
        path, "package com.android.hcp3;", "package com.android;");
    //    import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueControlObject;

  }
}
