package com.xjx.kotlin.utils

import java.io.File

/**
 * @author : 流星
 * @CreateDate: 2022/12/21-18:57
 * @Description:
 */
object FileUtil {

    // 只有使用了jvmField 注解才能把变量暴漏出去
    @JvmField
    public val pathUrl: String = ""

    // 只有使用了jvmStatic 才能把方法暴漏给java成为静态方法
    @JvmStatic
    fun getFilePath(file: File): String {
        return file.path
    }
}