package com.android.hcp3

import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.GenerateUtil.LOCAL_NODE_FILE_LIST

object File2Util {
    @JvmStatic
    fun main(args: Array<String>) {
        ReadJarFile.execute()
        LOCAL_NODE_FILE_LIST

        RSI_TARGET_NODE_LIST
    }
}
