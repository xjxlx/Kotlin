package com.android.hcp3;

public class Test {
  public static void main(String[] args) {
    String path = "hcp3/src/main/java/com/android/hcp3/TestFile.java";
    RandomAccessFileUtil.deleteFileContent(path, "// item = 3");
  }
}
