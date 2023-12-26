package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityCustomWrapTextViewBinding
import kotlinx.coroutines.launch

class CustomWrapTextViewActivity : BaseBindingTitleActivity<ActivityCustomWrapTextViewBinding>() {

	override fun getTitleContent(): String {
		return "自动换行View"
	}

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?,
		attachToRoot: Boolean): ActivityCustomWrapTextViewBinding {
		return ActivityCustomWrapTextViewBinding.inflate(inflater, container, true)
	}

	override fun initData(savedInstanceState: Bundle?) {
		mBinding.twvContent.init(this)

		mBinding.btnStart.setOnClickListener {
			lifecycleScope.launch {
				mBinding.twvContent.setExplain("加油",
					"呼吸时长足够，注意保持均匀的呼吸次数和平缓的心率，让情绪更稳定些效果更好哦！")
			}
		}
	}
}
