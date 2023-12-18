package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityFeatureMapBinding
import com.xjx.kotlin.ui.activity.test.AidlActivity
import com.xjx.kotlin.ui.activity.test.TestCopyDataActivity
import com.xjx.kotlin.ui.activity.test.ViewPager2Activity
import com.xjx.kotlin.ui.activity.thread.ThreadMapActivity
import com.xjx.kotlin.utils.zmq.big.ServiceActivity

class FeatureMapActivity : BaseBindingTitleActivity<ActivityFeatureMapBinding>() {

	override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFeatureMapBinding {
		return ActivityFeatureMapBinding.inflate(inflater, container, true)
	}

	override fun getTitleContent(): String {
		return "功能测试"
	}

	override fun initData(savedInstanceState: Bundle?) {
		setonClickListener(R.id.tv_item_recording_video)

		mBinding.tvItemTestViewpager2.setOnClickListener {
			startActivity(ViewPager2Activity::class.java)
		}
		mBinding.tvItemZmqSend.setOnClickListener { startActivity(ZmqSendActivity::class.java) }
		mBinding.tvItemZmqReceive.setOnClickListener { startActivity(ZmqReceiverActivity::class.java) }
		mBinding.tvItemZmqTest.setOnClickListener { startActivity(ServiceActivity::class.java) }
		mBinding.tvItemCurrentIp.setOnClickListener { startActivity(CurrentIpActivity::class.java) }
		mBinding.tvSocketSend.setOnClickListener { startActivity(SocketSendActivity::class.java) }
		mBinding.tvSocketResult.setOnClickListener { startActivity(SocketResultActivity::class.java) }
		mBinding.tvRaf.setOnClickListener {}
		mBinding.tvTestHttp1.setOnClickListener { startActivity(TestHttp1Activity::class.java) }
		mBinding.tvTestHttp2.setOnClickListener { startActivity(TestHttp2Activity::class.java) }
		mBinding.tvTestGsyPlay.setOnClickListener { startActivity(GsyPlayerActivity::class.java) }
		mBinding.tvItemThread.setOnClickListener { startActivity(ThreadMapActivity::class.java) }
		mBinding.tvItemAidl.setOnClickListener { startActivity(AidlActivity::class.java) }
		mBinding.tvItemCopy.setOnClickListener { startActivity(TestCopyDataActivity::class.java) }
	}

	override fun onClick(v: View?) {
		super.onClick(v)
		when (v?.id) {
			R.id.tv_item_recording_video -> {
				startActivity(RecordActivity::class.java)
			}
		}
	}
}
