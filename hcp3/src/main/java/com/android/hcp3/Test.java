package com.android.hcp3;

public class Test {
  public static void main(String[] args) {
    String path = "hcp3/src/main/java/com/android/hcp3/VcSpecialValue.java";
    RandomAccessFileUtil.changeFileContent(
        path, "package com.android.hcp3;", "package com.android.hcp4;");
    //        path, "package com.android.hcp3;", "package com.android;");
  }
}
