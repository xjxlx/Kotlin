package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityZmqTwoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.zeromq.SocketType
import org.zeromq.ZContext

class ZmqTwoActivity : AppBaseBindingTitleActivity<ActivityZmqTwoBinding>() {

    override fun setTitleContent(): String {
        return "双向"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityZmqTwoBinding {
        return ActivityZmqTwoBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        mBinding.btnStart1.setOnClickListener {
            initResult()
        }


        mBinding.btnStart2.setOnClickListener {
            initSend()
        }
    }

    private fun initResult() {
        val buffer = StringBuffer()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val context = ZContext(1)
                buffer.append("content --->")
                    .append("\r\n")
                val socket = context.createSocket(SocketType.PAIR)
                buffer.append("content --->")
                    .append("\r\n")

                val bind = socket.bind("tcp://192.168.8.122:6666")
                buffer.append("bind --->$bind")
                    .append("\r\n")
                mBinding.tvStart1.post {
                    mBinding.tvStart1.text = buffer.toString()
                }
                while (!Thread.currentThread().isInterrupted) {
                    val recv = socket.recv(0)
                    val content = recv.toString(Charsets.UTF_8)
                    mBinding.tvStart1.post {
                        mBinding.tvStart1.text = content
                    }
                }
            } catch (e: Throwable) {
                mBinding.tvStart1.post {
                    mBinding.tvStart1.text = e.toString()
                }
            }
        }
    }

    var number = 0
    private fun initSend() {
        val buffer = StringBuffer()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val context = ZContext(1)
                buffer.append("context --->")
                    .append("\r\n")
                val socket = context.createSocket(SocketType.PAIR)
                buffer.append("socket --->")
                    .append("\r\n")
                socket.connect("tcp://192.168.8.122:6666")
                buffer.append("bind --->")
                    .append("\r\n")
                mBinding.tvStart2.post {
                    mBinding.tvStart2.text = buffer.toString()
                }
                while (!Thread.currentThread().isInterrupted) {
                    if (number < 10000) {
                        val content = "number:$number"
                        log("send : $content")
                        socket.send(content.toByteArray(Charsets.UTF_8), 0)
                        number++
                        Thread.sleep(1000)
                        withContext(Dispatchers.Main) {
                            mBinding.tvStart2.text = ""
                            mBinding.tvStart2.text = content
                        }
                    }
                }
            } catch (e: Exception) {
                mBinding.tvStart2.post {
                    mBinding.tvStart2.text = e.message
                }
            }
        }
    }

    private fun log(content: String) {
        LogUtil.e("ZMQ", content)
    }
}