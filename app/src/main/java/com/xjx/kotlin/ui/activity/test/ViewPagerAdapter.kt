package com.xjx.kotlin.ui.activity.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author : 流星
 * @CreateDate: 2022/7/28-23:30
 * @Description:
 */
class ViewPagerAdapter(activity: FragmentActivity, private val mList: ArrayList<Fragment>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mList[position]
    }

}