package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.ConvertUtil
import com.android.common.utils.DimensUtil
import com.android.common.utils.ScreenUtil
import com.xjx.kotlin.databinding.ActivityDimensBinding

class DimensActivity : BaseBindingTitleActivity<ActivityDimensBinding>() {

    companion object {
        val s = 2
    }

    override fun getTitleContent(): String {
        return "SW适配"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityDimensBinding {
        return ActivityDimensBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnDimens.setOnClickListener {
            val screenWidth = ScreenUtil.getScreenWidth(this@DimensActivity)
            val wdp = ConvertUtil.px2dp(screenWidth)
            val screenHeight = ScreenUtil.getScreenHeight(this@DimensActivity)
            val hdp = ConvertUtil.px2dp(screenHeight)

            val smallScreenWidthDp = DimensUtil.getSmallScreenWidthDp(this@DimensActivity)
            val screenMinWidth = DimensUtil.getScreenMinWidth(this@DimensActivity)
            val content = "wdp:[$wdp]  hdp:[$hdp]\r\n\r\nsmallScreenWidthDp:$smallScreenWidthDp \r\n\n$screenMinWidth"
            mBinding.tvDimens.text = content
        }
    }
}