package com.android.hcp3;

public class Test {
  public static void main(String[] args) {
    String path = "hcp3/src/main/java/com/android/hcp3/TestFile.java";
    RandomAccessFileUtil.changeFileContent(
        path, "public class TestFile {", "public class TestFiles {");
    //        path, "package com.android.hcp3;", "package com.android;");
  }
}
