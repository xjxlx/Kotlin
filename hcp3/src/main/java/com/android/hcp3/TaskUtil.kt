package com.android.hcp3

import com.android.hcp3.ProcessUtil.process

object TaskUtil {
    private const val FILE_CLASS_NAME = "com.android.hcp3.File2Util"

    @JvmStatic
    fun main(args: Array<String>) {
        process(FILE_CLASS_NAME)
    }
}
