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
    fun getPackageSimple(str: String): String {
        if (str.isEmpty()) {
            return ""
        }
        if (str.contains(".")) {
            return str.substring(str.lastIndexOf(".") + 1)
        }
        return ""
    }

    /**
     * 把一个文件的的路径，删除后面的格式后，返回文件不带格式的路径，例如：com.android.hcp3.rsi.hvac.VcRestrictionReason.java
     * 返回：com.android.hcp3.rsi.hvac.VcRestrictionReason
     */
    @JvmStatic
    fun deleteFileFormat(filePath: String): String {
        if (filePath.isEmpty()) {
            return ""
        }
        if (filePath.contains(".")) {
            return filePath.substring(0, filePath.lastIndexOf("."))
        }
        return ""
    }

    /**
     * 把一个文件的的路径，删除后面的格式后，返回文件的名字，例如：hcp3/src/main/java/com/android/hcp3/rsi/hvac/VcRestrictionReason.java
     * 返回：VcRestrictionReason
     */
    @JvmStatic
    fun getFileNameForPath(filePath: String): String {
        if (filePath.isEmpty()) {
            return ""
        }
        if (filePath.contains("/")) {
            val lastIndexOf = filePath.lastIndexOf("/")
            val format = filePath.substring(lastIndexOf + 1)
            if (format.contains(".")) {
                val formatLastIndexOf = format.lastIndexOf(".")
                return format.substring(0, formatLastIndexOf)
            }
        }
        return ""
    }
}
