package com.xjx.kotlin.utils.zmq.big

object ZmqUtil {
    const val TAG = "ZMQ"
    const val PORT = 5566
    private val mZmqResult: ZmqClientUtil = ZmqClientUtil()
    private val mZmqClient: ZmqReceiverUtil = ZmqReceiverUtil()

    fun initClientZmq(ip: String) {
        mZmqResult.initClientZmq(ip)
    }

    /**
     * 必须在异步线程
     */
    fun sendClient(): Boolean {
        return mZmqResult.send()
    }

    fun setClientTraceListener(listener: ZmqCallBackListener) {
        mZmqResult.setTraceListener(listener)
    }

    fun setClientReceiverListener(listener: ZmqCallBackListener) {
        mZmqResult.setReceiverListener(listener)
    }

    fun setClientSendListener(listener: ZmqCallBackListener) {
        mZmqResult.setSendListener(listener)
    }

    fun stopClient() {
        mZmqResult.stop()
    }

    fun releaseClient() {
        mZmqResult.release()
    }

    /******************************************* ******************************************************************************/
    fun initReceiverZmq(ip: String) {
        mZmqClient.initReceiverZmq(ip)
    }

    /**
     * 必须在异步线程
     */
    fun sendReceiver(): Boolean {
        return mZmqClient.send()
    }

    fun setReceiverTraceListener(listener: ZmqCallBackListener) {
        mZmqClient.setTraceListener(listener)
    }

    fun setReceiverReceiverListener(listener: ZmqCallBackListener) {
        mZmqClient.setReceiverListener(listener)
    }

    fun setReceiverSendListener(listener: ZmqCallBackListener) {
        mZmqClient.setSendListener(listener)
    }

    fun stopReceiver() {
        mZmqClient.stop()
    }

    fun releaseReceiver() {
        mZmqClient.release()
    }
}