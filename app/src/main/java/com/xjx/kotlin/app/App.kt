package com.xjx.kotlin.app

import RefreshManager
import android.app.Application
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import com.android.apphelper2.app.AppHelper2
import com.android.common.utils.LogUtil
import com.android.helper.app.ApplicationInterface
import com.android.helper.app.BaseApplication
import com.android.helper.base.title.PageLayoutBuilder
import com.android.helper.base.title.PageLayoutManager
import com.android.helper.httpclient.AutoInterceptor
import com.android.http.utils.client.HttpLogInterceptor
import com.android.http.utils.client.RetrofitHelper
import com.tencent.bugly.crashreport.CrashReport
import com.xjx.kotlin.BuildConfig
import com.xjx.kotlin.R
import okhttp3.Interceptor
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @author : 流星
 * @CreateDate: 2022/7/24-11:33
 * @Description:
 */
class App : Application() {

    companion object {
        private const val TAG = "APP"
    }

    override fun onCreate() {
        super.onCreate()

        AppHelper2.init(this, AppHelper2.Builder())

        BaseApplication.getInstance()
            .setApplication(object : ApplicationInterface {
                override fun initApp() {
                    // 设置title的资源信息
                    val builder = PageLayoutBuilder().setTitleLayoutId(com.android.helper.R.layout.base_title_activity)
                        .setTitleBarLayoutId(com.android.helper.R.id.base_title)
                        .setLeftBackLayoutId(com.android.helper.R.id.ll_base_title_back)
                        .setTitleId(com.android.helper.R.id.tv_base_title)
                        .setRightLayoutId(com.android.helper.R.id.fl_base_title_right_parent)
                        .setRightTextId(com.android.helper.R.id.tv_base_title_right_title)
                        .setContentLayoutId(com.android.helper.R.id.fl_activity_content)
                        .setPlaceHolderLayoutId(com.android.helper.R.id.fl_placeholder)
                    PageLayoutManager.setGlobalTitleBar(builder)
                }

                override fun getApplication(): Application {
                    return this@App
                }

                override fun isDebug(): Boolean {
                    return BuildConfig.DEBUG
                }

                override fun logTag(): String {
                    return "AppHelper-kotlin"
                }

                override fun getAppName(): String {
                    return resources.getString(R.string.app_name)
                }

                override fun getBaseUrl(): String {
                    return "http://api-zhgj-app.beixin.hi-cloud.net:8000/gateway-api/"
                }

                override fun getInterceptors(): Array<Interceptor> {
                    return arrayOf(AutoInterceptor())
                }
            })

        // init crash
        runCatching {
            val strategy = CrashReport.UserStrategy(this)
            val id = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            if (!TextUtils.isEmpty(id)) {
                strategy.deviceID = id
            }
            val processName = getProcessName(Process.myPid())
            val packageName: String = this.packageName
            strategy.isUploadProcess = processName == null || processName == packageName;
            // 设置anr时是否获取系统trace文件，默认为false
            strategy.isEnableCatchAnrTrace = true
            // 设置是否获取anr过程中的主线程堆栈，默认为true
            strategy.isEnableRecordAnrMainStack = true
            CrashReport.initCrashReport(this, "b6b92710db", BuildConfig.DEBUG, strategy);
        }.onFailure {
            LogUtil.e(TAG, "init crash error: ${it.message}")
        }

        RetrofitHelper.addInterceptor(AutoInterceptor())
        RetrofitHelper.addInterceptor(HttpLogInterceptor())

        // init refresh
        RefreshManager.init(this)
    }

    /**
     * return process name
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }
}