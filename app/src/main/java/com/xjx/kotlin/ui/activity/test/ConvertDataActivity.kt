package com.xjx.kotlin.ui.activity.test

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.helper.utils.ToastUtil
import com.android.helper.utils.permission.RxPermissionsUtil
import com.android.helper.utils.permission.SinglePermissionsCallBackListener
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityConvertDataBinding
import com.xjx.kotlin.ui.activity.test.http.RetrofitUtil
import com.xjx.kotlin.ui.activity.test.http.TestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/** 1：数据转化 2：网络请求 */
const val aaa = 1

class ConvertDataActivity : BaseBindingTitleActivity<ActivityConvertDataBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityConvertDataBinding {
        return ActivityConvertDataBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "数据转换"
    }

    override fun initListener() {
        super.initListener()
        setonClickListener(R.id.btn_request_data)
    }

    var a: Any = ""
    override fun initData(savedInstanceState: Bundle?) {
        // 判断类型是否是String
        if (a is String) {
            // 数据强转
            (a as String).length
        }

        RxPermissionsUtil.Builder(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .setSinglePerMissionListener(object : SinglePermissionsCallBackListener {
                override fun onRxPermissions(status: Int, permission: String?) {
                    LogUtil.e("权限： $status")
                }
            }).build().startRequestPermission()

        // 可变变量
        // 只读变量，某种程度上可以当做常量使用
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_request_data -> {

                val testApi = RetrofitUtil.getApiService(TestApi::class.java)
                val map = hashMapOf<String, String>()
                map["unid"] = "o9RWl1EJPHolk8_7smU39k1-LqVs"
                map["service"] = "App.Coursenew.GetCourseContent"
                map["cont_suiji"] = "gp1E57"
                map["suiji"] = "newcL4"
                map["version"] = "2"

                testApi.getTestList(map).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val toString = response.body().toString()
                        LogUtil.e("response ---> :$toString")

                        // 设置目录位置
                        val filesDir = this@ConvertDataActivity.filesDir
                        // 根据文件File的路径，设置一个在该文件夹路径下的新文件
                        File(filesDir, "abc.txt")
                            // 写入文本数据
                            .writeText(toString, Charsets.UTF_8)

                        ToastUtil.show("文件读写完毕！")
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        LogUtil.e("onFailure ---> :" + t.message)
                    }
                })
            }
        }
    }
}
