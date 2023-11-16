package com.xjx.kotlin

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.permission.RxPermissionsUtil
import com.xjx.kotlin.databinding.ActivityMainBinding
import com.xjx.kotlin.ui.activity.custom.CustomViewMapActivity
import com.xjx.kotlin.ui.activity.feature.FeatureMapActivity
import com.xjx.kotlin.ui.activity.test.AidlActivity
import com.xjx.kotlin.ui.activity.test.ClasssActivity
import com.xjx.kotlin.ui.activity.test.ControlActivity
import com.xjx.kotlin.ui.activity.test.ConvertDataActivity
import com.xjx.kotlin.ui.activity.test.FsActivity
import com.xjx.kotlin.ui.activity.test.FunActivity
import com.xjx.kotlin.ui.activity.test.FxActivity
import com.xjx.kotlin.ui.activity.test.ListActivity
import com.xjx.kotlin.ui.activity.test.TestArrayActivity
import com.xjx.kotlin.ui.activity.test.ViewPager2Activity
import com.xjx.kotlin.ui.activity.test.block.BlockActivity
import com.xjx.kotlin.ui.activity.test.coroutine.CoroutineMapActivity
import com.xjx.kotlin.ui.activity.test.flow.FlowMapActivity
import com.xjx.kotlin.ui.activity.thread.ThreadMapActivity

class MainActivity : AppBaseBindingTitleActivity<ActivityMainBinding>() {

    override fun initListener() {
        super.initListener()
        setonClickListener(R.id.tv_item_test_viewpager2, R.id.tv_item_array, R.id.tv_item_list, R.id.tv_item_fun, R.id.tv_item_class,
            R.id.tv_item_convert_data, R.id.tv_item_xc, R.id.tv_item_aidl, R.id.tv_item_control, R.id.tv_item_fx, R.id.tv_item_fs,
            R.id.tv_item_flow, R.id.tv_item_block, R.id.tv_item_custom_view, R.id.tv_item_thread, R.id.tv_item_function,
            R.id.tv_item_flow_map, R.id.tv_item_coroutine_map)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        val strings = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        RxPermissionsUtil.Builder(this, *strings).setSinglePerMissionListener { permissionStatus: Int, permission: String? ->
                LogUtil.e("permission:$permissionStatus")
            }.build().startRequestPermission()
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

            R.id.tv_item_block -> {
                startActivity(BlockActivity::class.java)
            }

            R.id.tv_item_custom_view -> {
                startActivity(CustomViewMapActivity::class.java)
            }

            R.id.tv_item_thread -> {
                startActivity(ThreadMapActivity::class.java)
            }

            R.id.tv_item_function -> {
                startActivity(FeatureMapActivity::class.java)
            }

            R.id.tv_item_flow_map -> {
                startActivity(FlowMapActivity::class.java)
            }

            R.id.tv_item_coroutine_map -> {
                startActivity(CoroutineMapActivity::class.java)
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater, container, true)
    }
}