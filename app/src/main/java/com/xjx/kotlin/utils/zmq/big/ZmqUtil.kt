package com.xjx.kotlin.utils.zmq.big

object ZmqUtil {
    const val TAG = "ZMQ"
    const val PORT = 5566
    private val mZmqClient: ZmqClientUtil = ZmqClientUtil()
    private val mZmqReceiver: ZmqReceiverUtil = ZmqReceiverUtil()

    fun initClientZmq(ip: String) {
        mZmqClient.initClientZmq(ip)
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

    /******************************************* ******************************************************************************/
    fun initReceiverZmq(ip: String) {
        mZmqReceiver.initReceiverZmq(ip)
    }

    /**
     * 必须在异步线程
     */
    fun sendReceiver(): Boolean {
        return mZmqReceiver.send()
    }

    fun setReceiverTraceListener(listener: ZmqCallBackListener) {
        mZmqReceiver.setTraceListener(listener)
    }

    fun setReceiverReceiverListener(listener: ZmqCallBackListener) {
        mZmqReceiver.setReceiverListener(listener)
    }

    fun setReceiverSendListener(listener: ZmqCallBackListener) {
        mZmqReceiver.setSendListener(listener)
    }

    fun stopReceiver() {
        mZmqReceiver.stop()
    }

    fun releaseReceiver() {
        mZmqReceiver.release()
    }
}