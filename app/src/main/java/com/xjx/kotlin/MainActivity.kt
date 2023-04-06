package com.xjx.kotlin

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.android.helper.utils.permission.RxPermissionsUtil
import com.xjx.kotlin.databinding.ActivityMainBinding
import com.xjx.kotlin.network.NetWorkActivity
import com.xjx.kotlin.ui.activity.test.*
import com.xjx.kotlin.ui.activity.test.flow.FlowActivity
import com.xjx.kotlin.ui.activity.test.xc.XC2Activity
import com.xjx.kotlin.ui.activity.test.xc.XC3Activity
import com.xjx.kotlin.ui.activity.test.xc.XC4Activity
import com.xjx.kotlin.ui.activity.test.xc.XCActivity

class MainActivity : AppBaseBindingTitleActivity<ActivityMainBinding>() {

    override fun initListener() {
        super.initListener()
        setonClickListener(
            R.id.tv_item_test_viewpager2,
            R.id.tv_item_array,
            R.id.tv_item_list,
            R.id.tv_item_fun,
            R.id.tv_item_class,
            R.id.tv_item_convert_data,
            R.id.tv_item_xc,
            R.id.tv_item_aidl,
            R.id.tv_item_control,
            R.id.tv_item_fx,
            R.id.tv_item_fs,
            R.id.tv_item_xc_2,
            R.id.tv_item_xc_3,
            R.id.tv_item_xc_4,
            R.id.tv_item_fz,
            R.id.tv_item_flow
        )
    }

    override fun initData(savedInstanceState: Bundle?) {

        val strings = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        RxPermissionsUtil
            .Builder(this, *strings)
            .setSinglePerMissionListener { permissionStatus: Int, permission: String? ->
                LogUtil.e("permission:$permissionStatus")
            }
            .build()
            .startRequestPermission()
    }

    override fun setTitleContent(): String {
        return ""
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_item_test_viewpager2 -> {
                startActivity(ViewPager2Activity::class.java)
            }

            R.id.tv_item_array -> {
                startActivity(TestArrayActivity::class.java)
            }

            R.id.tv_item_list -> {
                startActivity(ListActivity::class.java)
            }

            R.id.tv_item_fun -> {
                startActivity(FunActivity::class.java)
            }

            R.id.tv_item_class -> {
                startActivity(ClasssActivity::class.java)
            }

            R.id.tv_item_convert_data -> {
                startActivity(ConvertDataActivity::class.java)
            }

            R.id.tv_item_xc -> {
                startActivity(XCActivity::class.java)
            }

            R.id.tv_item_aidl -> {
                startActivity(AidlActivity::class.java)
            }

            R.id.tv_item_control -> {
                startActivity(ControlActivity::class.java)
            }

            R.id.tv_item_fx -> {
                startActivity(FxActivity::class.java)
            }

            R.id.tv_item_fs -> {
                startActivity(FsActivity::class.java)
            }

            R.id.tv_item_xc_2 -> {
                startActivity(XC2Activity::class.java)
            }
            R.id.tv_item_xc_3 -> {
                startActivity(XC3Activity::class.java)
            }

            R.id.tv_item_xc_4 -> {
                startActivity(XC4Activity::class.java)
            }

            R.id.tv_item_fz -> {
                startActivity(NetWorkActivity::class.java)
            }
            R.id.tv_item_flow -> {
                startActivity(FlowActivity::class.java)
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater, container, true)
    }
}