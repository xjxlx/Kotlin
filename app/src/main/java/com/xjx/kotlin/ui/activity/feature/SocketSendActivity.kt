package com.xjx.kotlin.ui.activity.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivitySocketSendBinding

class SocketSendActivity : AppBaseBindingTitleActivity<ActivitySocketSendBinding>() {

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun setTitleContent(): String {
        return "Socket - 发送端"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivitySocketSendBinding {
        return ActivitySocketSendBinding.inflate(inflater, container, true)
    }
}