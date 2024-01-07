package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
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

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): ActivityCustomBannerBinding {
        return ActivityCustomBannerBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        for (index in 0..4) {
            list.add("$index")
        }
        mAdapter.setList(list)
        mBinding.bv.setAdapter(mAdapter).setInterval(3000).withLifecycle(lifecycleScope).start()

        mBinding.itv.setAdapter(mAdapter)
        mBinding.itv.registerViewPager(mBinding.bv.getViewPager2())
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
