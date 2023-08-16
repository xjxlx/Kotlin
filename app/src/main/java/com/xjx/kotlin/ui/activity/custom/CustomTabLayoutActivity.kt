package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.android.common.base.BaseViewPager2FragmentAdapter
import com.android.common.utils.TabLayoutUtil
import com.xjx.kotlin.databinding.ActivityCustomTabLayoutBinding
import com.xjx.kotlin.ui.activity.fragments.ClueClueFragment
import com.xjx.kotlin.ui.activity.fragments.ClueNeedFragment

class CustomTabLayoutActivity : BaseBindingTitleActivity<ActivityCustomTabLayoutBinding>() {

    private val mTitleArray: Array<String> = arrayOf("线索", "需求")
    private val mTitleArray2: List<String> = mutableListOf("线索", "需求")

    private val mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>().apply {
        add(ClueClueFragment.newInstance())
        add(ClueNeedFragment.newInstance())
    }

    override fun initData(savedInstanceState: Bundle?) {
        val adapter = BaseViewPager2FragmentAdapter(this.supportFragmentManager, this.lifecycle, mFragmentList)
        mBinding.vp.adapter = adapter


        TabLayoutUtil.Builder()
            .setViewPager2TitleList(mTitleArray2)
            .Build()
            .setupWithViewPager(mBinding.vp, mBinding.tbLayout)

        mBinding.tb.initData()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomTabLayoutBinding {
        return ActivityCustomTabLayoutBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "TabLayout"
    }
}