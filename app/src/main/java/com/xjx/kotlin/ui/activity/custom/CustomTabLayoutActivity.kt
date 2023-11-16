package com.xjx.kotlin.ui.activity.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.BaseViewPager2FragmentAdapter
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    private val mFragmentList: MutableList<Fragment> by lazy {
        return@lazy mutableListOf<Fragment>().apply {
            add(ClueNeedFragment.newInstance("线索"))
            add(ClueNeedFragment.newInstance("需求"))
            add(ClueNeedFragment.newInstance("商城"))
            add(ClueNeedFragment.newInstance("我的"))
//            add(ClueNeedFragment.newInstance("你的"))
        }
    }

    private val mTitleArray: Array<String> by lazy {
        return@lazy arrayOf("线索", "需求", "商城", "我的")
    }

    override fun initData(savedInstanceState: Bundle?) {
        val adapter = BaseViewPager2FragmentAdapter(this.supportFragmentManager, this.lifecycle, mFragmentList)
        mBinding.vp.adapter = adapter

        mBinding.tb.setItemBackgroundColor(Color.YELLOW).setItemSize(50F).withViewPager2(mBinding.vp, mTitleArray)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }
}