package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomBannerBinding
import com.xjx.kotlin.databinding.CustomBannerBinding

class CustomBannerActivity : BaseBindingTitleActivity<ActivityCustomBannerBinding>() {
    private val list: MutableList<String> = mutableListOf()
    private val mAdapter: BannerAdapter = BannerAdapter()
    override fun getTitleContent(): String {
        return "自定义Banner"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomBannerBinding {
        return ActivityCustomBannerBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        for (index in 0..5) {
            list.add("$index")
        }
        mAdapter.setList(list)
        mBinding.bv.setAdapter(mAdapter).setInterval(3000).withLifecycle(lifecycleScope).start()
    }

    class BannerAdapter : com.android.common.widget.banner.BannerAdapter<String, VH>() {
        override fun onBind(holder: VH, position: Int) {
            val get = mList[position]
            holder.mBinding.tvContent.text = get
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val from = LayoutInflater.from(parent.context)
            val binding = CustomBannerBinding.inflate(from, parent, false)
            return VH(binding)
        }
    }

    class VH(val mBinding: CustomBannerBinding) : com.android.common.widget.banner.BannerAdapter.BannerVH(mBinding)
}
