package com.xjx.kotlin.ui.activity.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.BaseViewPager2FragmentAdapter
import com.android.common.utils.TabLayoutUtil2
import com.google.android.material.tabs.TabLayout
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    private val mFragmentList: MutableList<Fragment> = mutableListOf()
    private val mTitleArray: Array<String> by lazy {
        return@lazy arrayOf("线索", "需求1111111", "商城", "我", "我的2", "我的3")
    }

    override fun initData(savedInstanceState: Bundle?) {
        for (element in mTitleArray) {
            mFragmentList.add(ClueNeedFragment.newInstance(element))
        }

//        // 2：滑动方式
        mBinding.tbLayout.tabMode = TabLayout.MODE_AUTO // tablelayout的滑动方式，这里只有两个，所以设置为固定模式
        val adapter = BaseViewPager2FragmentAdapter(supportFragmentManager, this.lifecycle, mFragmentList)
        TabLayoutUtil2.Build()
            .setTabLayout(mBinding.tbLayout)
            .setViewPager2(mBinding.vp)
            .setAdapter(adapter)
            .setTitleList(mTitleArray.toMutableList())
            .build()
            .withViewPager2()
            .setIndicator(this)
//            .setIndicatorRatio(1f)
            .isIndicatorFullWidth(false)
//            .setIndicatorOffsetX(100)
            .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setWidth()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

        mBinding.tb.setItemBackgroundColor(Color.YELLOW)
            .setItemSize(50F)
            .withViewPager2(mBinding.vp, mTitleArray)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }

    fun setWidth() {
        val selectedTabPosition = mBinding.tbLayout.selectedTabPosition

//        val tabSelectedIndicator = mBinding.tbLayout.tabSelectedIndicator
//        if (tabSelectedIndicator is LayerDrawable) {
//            tabSelectedIndicator.setLayerInsetLeft(0, -50)
//        }
    }
}
