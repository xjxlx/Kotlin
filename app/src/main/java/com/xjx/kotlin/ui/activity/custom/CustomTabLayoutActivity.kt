package com.xjx.kotlin.ui.activity.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.android.apphelper2.widget.TabLayout
import com.android.common.base.BaseViewPager2FragmentAdapter
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding
import com.xjx.kotlin.ui.activity.fragments.ClueClueFragment
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    // private val mTitleArray: Array<String> = arrayOf("item1", "item2", "item33333", "item")
    private val mTitleArray: Array<String> = arrayOf("线索", "需求", "商场111111", "我的", "我的")

    private val mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>().apply {
        add(ClueClueFragment.newInstance())
        add(ClueNeedFragment.newInstance())
        add(ClueNeedFragment.newInstance())
        add(ClueNeedFragment.newInstance())
        add(ClueClueFragment.newInstance())
    }

    override fun initData(savedInstanceState: Bundle?) {
        val adapter = BaseViewPager2FragmentAdapter(this.supportFragmentManager, this.lifecycle, mFragmentList)
        mBinding.vp.adapter = adapter

        mBinding.tb.setItemBackgroundColor(Color.YELLOW)
            .setItemSize(50F)
            .withViewPager2(mBinding.vp, mTitleArray)
            .setSelectorListener(object : TabLayout.SelectorListener {
                override fun onSelector(position: Int) {
                    mBinding.vp.currentItem = position
                }
            })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }
}