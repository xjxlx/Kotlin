package com.xjx.kotlin.utils.zmq.receive

import com.android.common.utils.LogUtil
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class ZmqUtil3 {
    private var mIp: String = "192.168.124.5"
    private var mPort: Int = 9998
    private var mClientBufferedReader: BufferedReader? = null
    private val encoding = "UTF-8"
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO + CoroutineName("ZMQ-3"))
    }
    private val mAtomicStatus: AtomicBoolean by lazy {
        return@lazy AtomicBoolean()
    }
    private val mShardFlow: MutableSharedFlow<String> by lazy {
        return@lazy MutableSharedFlow<String>()
    }
    private val TAG = "ZMQ"

    // true:open  false:close
    private var ZMQ_SWITCH = true

    private var mClientSocket: Socket? = null

    fun start() {
        mScope.launch {
            initSocket()
        }
    }

    private suspend fun initSocket() {
        runCatching {
            mClientSocket = Socket(mIp, mPort)
            mClientSocket?.let { socket ->
                val connected: Boolean = socket.isConnected
                if (connected) {
                    mClientBufferedReader = BufferedReader(InputStreamReader(socket.getInputStream(), encoding))
                    log("start loop get data ---->")
                    mClientBufferedReader?.let { reader ->
                        var content: String
                        while (reader.readLine().also { content = it } != null) {
                            log("info: $content")
                            if (!mAtomicStatus.get()) {
                                log("send --->")
                                mShardFlow.emit(content)
                            }
                        }
                    }
                } else {
                    LogUtil.e("ZMQ", "service socket disconnect !")
                }
            }
        }.onFailure {
            log("socket connect error：" + it.message)
            try {
                mClientBufferedReader?.close()
                mClientBufferedReader = null
                mClientSocket?.close()
                mClientSocket = null
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    fun pause() {
        if (ZMQ_SWITCH) {
            runCatching {
                if (!mAtomicStatus.get()) {
                    mAtomicStatus.set(true)
                    log("pause --->")
                }
            }.onFailure {
                log("pause - error: ${it.message}")
            }
        }
    }

    fun resume() {
        if (ZMQ_SWITCH) {
            runCatching {
                if (mAtomicStatus.get()) {
                    mAtomicStatus.set(false)
                    log("resume --->")
                }
            }.onFailure {
                log("resume - error: ${it.message}")
            }
        }
    }

    fun setCallBackListener(block: (String) -> Unit) {
        if (ZMQ_SWITCH) {
            mScope.launch {
                mShardFlow.collect {
                    block(it)
                }
            }
        }
    }

    private fun log(content: String) {
        LogUtil.e(TAG, content)
    }

    fun stop() {
        try {
            mClientBufferedReader?.close()
            mClientBufferedReader = null
            mClientSocket?.close()
            mClientSocket = null
        } catch (ex: IOException) {
            ex.printStackTrace()
            log("stop error:" + ex.message)
        }
    }
}
