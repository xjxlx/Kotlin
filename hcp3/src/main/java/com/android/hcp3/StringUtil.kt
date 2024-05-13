package com.android.hcp3

import java.nio.file.Paths

object StringUtil {
    /**
     * 把一个字符串首字母大写
     */
    @JvmStatic
    fun capitalize(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return Character.toUpperCase(str.toCharArray()[0]) + str.substring(1)
    }

    /**
     * 把一个路径，从带/的转换成带.的，例如：de/esolutions/fw/rudi/viwi/service/hvac/v3/Hvac 转换成：
     * de.esolutions.fw.rudi.viwi.service.hvac.v3.Hvac
     */
    @JvmStatic
    fun transitionPackage(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return Paths.get(str).toString().replace("/", ".")
    }

    /**
     * 把包名转换为路径
     */
    @JvmStatic
    fun transitionPath(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return Paths.get(str).toString().replace(".", "/")
    }

    /**
     * 全部转换为小写
     */
    @JvmStatic
    fun lowercase(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        return str.lowercase()
    }

    @JvmStatic
    fun getSimpleForPath(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        if (str.contains(".")) {
            return str.substring(str.lastIndexOf(".") + 1)
        }
        return ""
    }
}
