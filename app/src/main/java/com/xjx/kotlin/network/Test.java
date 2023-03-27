package com.xjx.kotlin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 流星
 * @CreateDate: 2023/3/15-09:46
 * @Description:
 */
public class Test {
    public static String exec(String cmd) {
        try {
            StringBuilder sb = new StringBuilder();
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bufferedReader.close();
            process.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String grabIP(String content) {
        String reg = "inet\\s(\\d+?\\.\\d+?\\.\\d+?\\.\\d+?)/\\d+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    public static void main(String[] args) {
        String result = exec(" adb shell ip addr show wlan0");
        String ip = grabIP(result);
        System.out.println(ip);
    }


}
