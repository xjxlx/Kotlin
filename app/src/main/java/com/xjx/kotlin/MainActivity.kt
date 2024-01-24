package com.xjx.kotlin

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.utils.permission.RxPermissionsUtil
import com.xjx.kotlin.databinding.ActivityMainBinding
import com.xjx.kotlin.ui.activity.animations.AnimationMapActivity
import com.xjx.kotlin.ui.activity.compose.ComposeMapActivity
import com.xjx.kotlin.ui.activity.custom.CustomViewMapActivity
import com.xjx.kotlin.ui.activity.feature.FeatureMapActivity
import com.xjx.kotlin.ui.activity.kotlin.KotlinMapActivity

class MainActivity : BaseBindingTitleActivity<ActivityMainBinding>() {

    override fun getTitleContent(): String {
        return ""
    }

    override fun initListener() {
        super.initListener()
        setonClickListener(
            R.id.tv_item_xc, R.id.tv_item_flow, R.id.tv_item_custom_view, R.id.tv_item_function, R.id.tv_item_animation_map
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val strings = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        RxPermissionsUtil.Builder(this, *strings).setSinglePerMissionListener { permissionStatus: Int, _: String? ->
            // LogUtil.e("permission:$permissionStatus")
        }.build().startRequestPermission()

        mBinding.tvItemKotlin.setOnClickListener {
            startActivity(KotlinMapActivity::class.java)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_item_custom_view -> {
                startActivity(CustomViewMapActivity::class.java)
            }

            R.id.tv_item_function -> {
                startActivity(FeatureMapActivity::class.java)
            }

            R.id.tv_item_animation_map -> {
                startActivity(AnimationMapActivity::class.java)
            }

            R.id.tv_item_compose -> {
                startActivity(ComposeMapActivity::class.java)
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater, container, true)
    }
}
