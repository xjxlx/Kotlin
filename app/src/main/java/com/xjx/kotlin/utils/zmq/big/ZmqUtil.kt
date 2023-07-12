package com.xjx.kotlin.utils.zmq.big

object ZmqUtil {
    const val TAG = "ZMQ"
    const val PORT = 5566
    private val mZmqResult: ZmqClientUtil = ZmqClientUtil()
    private val mZmqClient: ZmqReceiverUtil = ZmqReceiverUtil()

    fun initServerZmq(ip: String) {
        mZmqResult.initClientZmq(ip)
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

    fun releaseServer() {
        mZmqResult.release()
    }

    /******************************************* ******************************************************************************/
    fun initClientZmq(ip: String) {
        mZmqClient.initReceiverZmq(ip)
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