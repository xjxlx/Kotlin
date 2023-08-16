package com.xjx.kotlin.ui.activity.custom

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.android.common.base.BaseViewPager2FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding
import com.xjx.kotlin.ui.activity.fragments.ClueClueFragment
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    //    private val mTitleArray: Array<String> = arrayOf("item1", "item2", "item33333", "item")
    private val mTitleArray: Array<String> = arrayOf("线索", "需求", "商场111111", "我的", "我的")

    private val mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>().apply {
        add(ClueClueFragment.newInstance())
        add(ClueNeedFragment.newInstance())
        add(ClueNeedFragment.newInstance())
        add(ClueNeedFragment.newInstance())
    }

    override fun initData(savedInstanceState: Bundle?) {
        val adapter = BaseViewPager2FragmentAdapter(this.supportFragmentManager, this.lifecycle, mFragmentList)
        mBinding.vp.adapter = adapter

//        TabLayoutUtil.Builder()
//            .setViewPager2TitleList(mTitleArray2)
//            .Build()
//            .setupWithViewPager(mBinding.vp, mBinding.tbLayout)
//


        // 1: add tab
        for (element in mTitleArray) {
            mBinding.tbLayout.addTab(mBinding.tbLayout.newTab()
                .setText(element))
        }
        // 2：滑动方式
        mBinding.tbLayout.tabMode = TabLayout.MODE_FIXED // tablelayout的滑动方式，这里只有两个，所以设置为固定模式

        // 4:关联TabLayout 和 ViewPager
        TabLayoutMediator(mBinding.tbLayout, mBinding.vp, object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = mTitleArray[position]
            }
        }).attach()

        val mItemTitleArray: Array<String> = arrayOf("线索", "需求", "商场111111", "我的", "我的")
        mBinding.tb.setItemBackgroundColor(Color.YELLOW)
            .withViewPager2(mBinding.vp, mBinding.tbLayout)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }
}