package com.android.hcp3;

// import com.github.javaparser.printer.PrettyPrinterConfiguration;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
  public static void main(String[] args) throws ClassNotFoundException {
    sss2();
  }

  public static void sss() {
    String path =
        "/Users/XJX/Wokrs/StudioWorks/MY/Kotlin/hcp3/src/main/java/com/android/hcp3/rsi/hvac/airdistributionpresets/AirDistributionPresetObjectEntity.java"; // 替换为你的文件路径
    Path filePath = Paths.get(path);

    try {
      // 读取文件所有行到一个String集合
      Iterable<String> lines = Files.readAllLines(filePath);
      for (String line : lines) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sss2() {
    //    hcp3/src/main/java/com/android/hcp3/TestFile.java
    //    File file = new File("hcp3/src/main/java/com/android/hcp3/TestFile.java");
    //    Class<? extends File> aClass = file.getClass();
    //    //    aClass.
    //    Class<?> aClass1 = Class.forName("com.android.hcp3.TestFile");
    //    Package aPackage = aClass1.getPackage();
    //    String name = aPackage.getName();

    String sourceFilePath = "hcp3/src/main/java/com/android/hcp3/TestFile.java"; // 源文件路径
    String targetFolderPath = "hcp3/src/main/java/com/android/hcp3/temp"; // 目标文件夹路径

    //    modifyFirstLine(sourceFilePath, "package com.android.hcp3.temp;");

    String filePath = "path/to/your/JavaFile.java"; // 原始Java文件路径
    String newPackageName = "com.example.newpackage"; // 新的包名

    Path path = Paths.get("src", "test", "java", "Example.java"); // 替换为你的源文件路径
  }
}
