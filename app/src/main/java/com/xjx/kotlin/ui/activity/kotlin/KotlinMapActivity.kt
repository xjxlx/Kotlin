package com.xjx.kotlin.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityKotlinMapBinding
import com.xjx.kotlin.ui.activity.test.ClasssActivity
import com.xjx.kotlin.ui.activity.test.ControlActivity
import com.xjx.kotlin.ui.activity.test.ConvertDataActivity
import com.xjx.kotlin.ui.activity.test.FsActivity
import com.xjx.kotlin.ui.activity.test.FunActivity
import com.xjx.kotlin.ui.activity.test.FxActivity
import com.xjx.kotlin.ui.activity.test.ListActivity
import com.xjx.kotlin.ui.activity.test.TestArrayActivity
import com.xjx.kotlin.ui.activity.test.block.BlockActivity
import com.xjx.kotlin.ui.activity.test.coroutine.CoroutineMapActivity
import com.xjx.kotlin.ui.activity.test.flow.FlowMapActivity

class KotlinMapActivity : BaseBindingTitleActivity<ActivityKotlinMapBinding>() {

    override fun getTitleContent(): String {
        return "Kotlin 集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.tvItemArray.setOnClickListener {
            startActivity(TestArrayActivity::class.java)
        }
        mBinding.tvItemList.setOnClickListener {
            startActivity(ListActivity::class.java)
        }
        mBinding.tvItemFun.setOnClickListener {
            startActivity(FunActivity::class.java)
        }
        mBinding.tvItemClass.setOnClickListener {
            startActivity(ClasssActivity::class.java)
        }
        mBinding.tvItemConvertData.setOnClickListener {
            startActivity(ConvertDataActivity::class.java)
        }
        mBinding.tvItemControl.setOnClickListener {
            startActivity(ControlActivity::class.java)
        }
        mBinding.tvItemFx.setOnClickListener {
            startActivity(FxActivity::class.java)
        }
        mBinding.tvItemFs.setOnClickListener {
            startActivity(FsActivity::class.java)
        }
        mBinding.tvItemBlock.setOnClickListener {
            startActivity(BlockActivity::class.java)
        }
        mBinding.tvItemFlowMap.setOnClickListener {
            startActivity(FlowMapActivity::class.java)
        }
        mBinding.tvItemCoroutineMap.setOnClickListener {
            startActivity(CoroutineMapActivity::class.java)
        }

//        R.id.tv_item_thread -> {
//            startActivity(ThreadMapActivity::class.java)
//        }
    }
}