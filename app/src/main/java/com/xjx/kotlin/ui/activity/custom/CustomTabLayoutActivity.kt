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
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    private val mFragmentList: MutableList<Fragment> by lazy {
        return@lazy mutableListOf<Fragment>().apply {
            add(ClueNeedFragment.newInstance("线索")
                .apply {
                    mTitleArray[0] = mContent
                })
            add(ClueNeedFragment.newInstance("需求")
                .apply {
                    mTitleArray[1] = mContent
                })
            add(ClueNeedFragment.newInstance("商城")
                .apply {
                    mTitleArray[2] = mContent
                })
            add(ClueNeedFragment.newInstance("我的")
                .apply {
                    mTitleArray[3] = mContent
                })
            add(ClueNeedFragment.newInstance("你的")
                .apply {
                    mTitleArray[4] = mContent
                })
        }
    }

    private val mTitleArray: Array<String> by lazy {
        return@lazy Array<String>(mFragmentList.size) { "" }
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