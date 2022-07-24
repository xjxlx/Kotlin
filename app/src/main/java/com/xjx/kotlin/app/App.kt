package com.xjx.kotlin.app

import android.app.Application
import com.android.helper.app.ApplicationInterface
import com.android.helper.app.BaseApplication
import com.android.helper.base.title.PageLayoutBuilder
import com.android.helper.base.title.PageLayoutManager
import com.android.helper.httpclient.AutoInterceptor
import com.xjx.kotlin.BuildConfig
import com.xjx.kotlin.R
import okhttp3.Interceptor

/**
 * @author : 流星
 * @CreateDate: 2022/7/24-11:33
 * @Description:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        BaseApplication.getInstance().setApplication(object : ApplicationInterface {
            override fun initApp() {
                // 设置title的资源信息
                val builder = PageLayoutBuilder()
                    .setTitleLayoutId(R.layout.base_title_activity)
                    .setTitleBarLayoutId(R.id.base_title)
                    .setLeftBackLayoutId(R.id.ll_base_title_back)
                    .setTitleId(R.id.tv_base_title)
                    .setRightLayoutId(R.id.fl_base_title_right_parent)
                    .setRightTextId(R.id.tv_base_title_right_title)
                    .setContentLayoutId(R.id.fl_activity_content)
                    .setPlaceHolderLayoutId(R.id.fl_placeholder)
                PageLayoutManager.setGlobalTitleBar(builder)
            }

            override fun getApplication(): Application {
                return this@App
            }

            override fun isDebug(): Boolean {
                return BuildConfig.DEBUG
            }

            override fun logTag(): String {
                return "AppHelper-kltlin"
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
    }
}