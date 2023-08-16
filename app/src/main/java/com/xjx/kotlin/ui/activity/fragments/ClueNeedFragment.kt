package com.xjx.kotlin.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.base.BaseBindingFragment
import com.xjx.kotlin.databinding.FragmentClueNeedBinding

class ClueNeedFragment : BaseBindingFragment<FragmentClueNeedBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = ClueNeedFragment()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): FragmentClueNeedBinding {
        return FragmentClueNeedBinding.inflate(layoutInflater)
    }
}