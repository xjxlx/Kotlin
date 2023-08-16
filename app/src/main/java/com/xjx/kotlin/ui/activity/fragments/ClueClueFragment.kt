package com.xjx.kotlin.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.base.BaseBindingFragment
import com.xjx.kotlin.databinding.FragmentClueClueBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ClueClueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClueClueFragment : BaseBindingFragment<FragmentClueClueBinding>() {

    companion object {

        @JvmStatic
        fun newInstance() = ClueClueFragment()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): FragmentClueClueBinding {
        return FragmentClueClueBinding.inflate(inflater)
    }
}