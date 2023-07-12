package com.xjx.kotlin.utils.zmq.big

object ZmqUtil {
    const val TAG = "ZMQ"
    const val PORT = 5566
    private val mZmqResult: ZmqReceiverUtil = ZmqReceiverUtil()
    private val mZmqClient: ZmqClientUtil = ZmqClientUtil()

    fun initServerZmq(ip: String) {
        mZmqResult.initReceiverZmq(ip)
    }

    /**
     * 必须在异步线程
     */
    fun sendServer(): Boolean {
        return mZmqResult.send()
    }

    fun setServerTraceListener(listener: ZmqCallBackListener) {
        mZmqResult.setTraceListener(listener)
    }

    fun setServerReceiverListener(listener: ZmqCallBackListener) {
        mZmqResult.setReceiverListener(listener)
    }

    fun setServerSendListener(listener: ZmqCallBackListener) {
        mZmqResult.setSendListener(listener)
    }

    fun stopServer() {
        mZmqResult.stop()
    }

    /******************************************* ******************************************************************************/
    fun initClientZmq(ip: String) {
        mZmqClient.initSendZmq(ip)
    }

    /**
     * 必须在异步线程
     */
    fun sendClient(): Boolean {
        return mZmqClient.send()
    }

    fun setClientTraceListener(listener: ZmqCallBackListener) {
        mZmqClient.setTraceListener(listener)
    }

    fun setClientReceiverListener(listener: ZmqCallBackListener) {
        mZmqClient.setReceiverListener(listener)
    }

    fun setClientSendListener(listener: ZmqCallBackListener) {
        mZmqClient.setSendListener(listener)
    }

    fun stopClient() {
        mZmqClient.stop()
    }

    fun releaseClient() {
        mZmqClient.release()
    }
}