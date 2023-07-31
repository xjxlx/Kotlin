package com.xjx.kotlin.utils.zmq.receive

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.apphelper2.utils.DebounceUtil
import com.android.common.utils.LogUtil
import com.android.common.utils.LogWriteUtil
import com.xjx.kotlin.BuildConfig
import com.xjx.kotlin.utils.JsonWriteUtil
import com.xjx.kotlin.utils.zmq.TCP
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.zeromq.SocketType
import org.zeromq.ZMQ
import java.util.concurrent.atomic.AtomicBoolean

object ZmqUtil2 {

    private val mAtomicStatus: AtomicBoolean by lazy {
        return@lazy AtomicBoolean()
    }
    private var mContext: ZMQ.Context? = null
    private var mSocket: ZMQ.Socket? = null
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO + CoroutineName(javaClass.simpleName))
    }
    private val mShardFlow: MutableSharedFlow<String> by lazy {
        return@lazy MutableSharedFlow<String>()
    }
    private var mBinding = false
    private val TAG = "ZMQ"
    private var mWriter: LogWriteUtil? = null
    private var mJsonWrite: JsonWriteUtil? = null
    private var mSendMsg: String? = null

    // true:open  false:close
    var ZMQ_SWITCH = true

    private val mDebounceUtil: DebounceUtil<Boolean> by lazy { return@lazy DebounceUtil(15 * 1000) }

    init {
        mScope.launch {
            runCatching {
                mDebounceUtil.listener {
                    log("ST socket connect lost ！")
                }
            }.onFailure {
                log("debounce listener error: ${it.message}")
            }
        }
    }

    private fun bind() {
        if (mBinding) {
            log("bind socket success , break !")
            return
        }

        if (TextUtils.isEmpty(TCP.TCP_ADDRESS)) {
            log("tcp is empty！")
            return
        }

        runCatching {
            val iPv6 = getSocket().isIPv6
            val iPv4Only = getSocket().iPv4Only
            log("iPv6 is: $iPv6 iPv4Only: $iPv4Only")

            mBinding = getSocket().bind(TCP.TCP_ADDRESS)
            log("bind address:[ ${TCP.TCP_ADDRESS} ]   ---> bind success：$mBinding")

            // loop data
            mAtomicStatus.set(false)
            loopData()
        }.onFailure {
            mBinding = false
            log("bind address  failure：:${it.message}")
        }
    }

    fun resume() {
        if (ZMQ_SWITCH) {
            runCatching {
                if (mAtomicStatus.get()) {
                    mAtomicStatus.set(false)
                    log("resume --->")
                    loopData()
                }
            }.onFailure {
                log("resume - error: ${it.message}")
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

    fun start() {
        if (ZMQ_SWITCH) {
            runCatching {
                if (mBinding) {
                    log("binding is true , stop execute!")
                } else {
                    bind()
                }
            }.onFailure {
                log("start - error: ${it.message}")
            }
        }
    }

    fun stop() {
        if (ZMQ_SWITCH) {
            runCatching {
                // unbind
                log("stop ---> binding status :$mBinding")
                if (mBinding) {
                    val unbind = getSocket().unbind(TCP.TCP_ADDRESS)
                    log("stop ---> unbind --->: $unbind")

                    log("stop ---> socket ---> close ! ")
                    getSocket().close()

                    val contextClosed = mContext?.isClosed
                    log("stop ---> context is close : $contextClosed")

                    if (!contextClosed!!) {
                        mContext?.close()
                        mContext = null
                        log("stop ---> context close !")
                    }
                }
                mScope.cancel()
                log("stop ---> success !")
            }.onFailure {
                log("stop ---> error: ${it.message}")
            }
            mBinding = false
        }
    }

    private fun loopData() {
        mScope.launch {
            runCatching {
                log("loopData ---> isPause:${mAtomicStatus.get()} context: ${!getContext().isTerminated}")
                //  the Zmq context  is not stop
                while (!getContext().isTerminated) {
                    mDebounceUtil.send(true)
                    // get the result from the zmq context
                    val reply = getSocket().recv(0)
                    if (reply != null) {
                        val content = reply.toString(Charsets.UTF_8)
                        LogUtil.e(TAG, "reply :$content")
                        if (!mAtomicStatus.get()) {
                            if (mSendMsg == null) {
                                log("数据收集中...")
                                mSendMsg = content
                            }
                            // LogUtil.e(TAG, "reply --- send ...")
                            mShardFlow.emit(content)
                        }
                    } else {
                        LogUtil.e(TAG, "reply : null")
                    }
                }
                if (BuildConfig.LogSwitch) {
                    mJsonWrite?.sendEnd()
                }
                log("while break ...")
            }.onFailure {
                log("loopData failure：:${it.message}")
            }
        }
    }

    fun isBindSocket(): Boolean {
        return mBinding
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

    fun getSocket(): ZMQ.Socket {
        if (mSocket == null) {
            mSocket = getContext().socket(SocketType.PAIR)
        }
        return mSocket!!
    }

    fun getContext(): ZMQ.Context {
        if (mContext == null) {
            mContext = ZMQ.context(1)
        }
        return mContext!!
    }

    fun log(content: String) {
        LogUtil.e(TAG, content)
        mWriter?.send(content)
    }

    fun initLog(fragment: Fragment) {
        mWriter = LogWriteUtil("$TAG.txt")
        mWriter?.init(fragment)

        if (BuildConfig.LogSwitch) {
            mJsonWrite = JsonWriteUtil("json.txt")
            mJsonWrite?.init(fragment)
        }
    }

    fun initLog(activity: FragmentActivity) {
        mWriter = LogWriteUtil("$TAG.txt")
        mWriter?.init(activity)

        if (BuildConfig.LogSwitch) {
            mJsonWrite = JsonWriteUtil("json.txt")
            mJsonWrite?.init(activity)
        }
        log("init log --->")
    }
}
