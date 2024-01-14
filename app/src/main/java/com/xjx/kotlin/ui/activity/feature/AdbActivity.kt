package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityAdbBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class AdbActivity : BaseBindingTitleActivity<ActivityAdbBinding>() {

    private val tag = "adb"

    override fun getTitleContent(): String {
        return "使用ADB功能"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAdbBinding {
        return ActivityAdbBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {
            adb()
        }
    }

    private fun adb() {
        // 这里是要执行的ADB命令
        try {
            val process = Runtime.getRuntime().exec("adb shell settings get global adb_wifi_enabled")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String
            while (reader.readLine().also { line = it } != null) {
                output.append(line + "\n")
            }
            val exitVal = process.waitFor()
            if (exitVal == 0) {
                // 命令执行成功
                LogUtil.e(tag, "Command executed successfully")
                LogUtil.e(tag, output.toString())
            } else {
                // 命令执行失败
                LogUtil.e(tag, "Command execution failed")
            }
        } catch (e: IOException) {
            LogUtil.e(tag, "error - 1 :${e.message}")
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            LogUtil.e(tag, "error - 2 :${e.message}")
        }
    }
}