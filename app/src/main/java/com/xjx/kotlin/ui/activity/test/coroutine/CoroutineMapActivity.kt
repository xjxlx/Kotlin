package com.xjx.kotlin.ui.activity.test.coroutine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityCoroutineMapBinding
import com.xjx.kotlin.network.NetWorkActivity
import com.xjx.kotlin.network.NetWorkRefreshActivity
import com.xjx.kotlin.ui.activity.test.xc.*

class CoroutineMapActivity : AppBaseBindingTitleActivity<ActivityCoroutineMapBinding>() {

    override fun setTitleContent(): String {
        return "协程集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCoroutineMapBinding {
        return ActivityCoroutineMapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(R.id.tv_item_xc, R.id.tv_item_xc_2, R.id.tv_item_xc_3, R.id.tv_item_xc_4, R.id.tv_item_xc_5,
            R.id.tv_item_flow_call, R.id.tv_item_fz, R.id.tv_item_refresh)
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        when (v?.id) {
            R.id.tv_item_xc -> {
                startActivity(XCActivity::class.java)
            }

            R.id.tv_item_xc_2 -> {
                startActivity(XC2Activity::class.java)
            }
            R.id.tv_item_xc_3 -> {
                startActivity(XC3Activity::class.java)
            }

            R.id.tv_item_xc_4 -> {
                startActivity(XC4Activity::class.java)
            }
            R.id.tv_item_xc_5 -> {
                startActivity(Xc5Activity::class.java)
            }
            R.id.tv_item_fz -> {
                startActivity(NetWorkActivity::class.java)
            }
            R.id.tv_item_refresh -> {
                startActivity(NetWorkRefreshActivity::class.java)
            }
        }
    }
}