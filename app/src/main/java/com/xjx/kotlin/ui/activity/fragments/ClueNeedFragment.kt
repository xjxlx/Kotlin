package com.xjx.kotlin.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.base.BaseBindingFragment
import com.xjx.kotlin.databinding.FragmentClueNeedBinding

class ClueNeedFragment : BaseBindingFragment<FragmentClueNeedBinding>() {
    var mContent: String = ""

    companion object {
        private const val KEY = "content"
        @JvmStatic
        fun newInstance(content: String) = ClueNeedFragment().apply {
            arguments = Bundle().apply {
                putString(KEY, content)
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mArguments?.let {
            it.getString(KEY)
                ?.let { content->
                    mContent = content
                    mBinding.tvContent.text = mContent
                }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): FragmentClueNeedBinding {
        return FragmentClueNeedBinding.inflate(layoutInflater)
    }
}