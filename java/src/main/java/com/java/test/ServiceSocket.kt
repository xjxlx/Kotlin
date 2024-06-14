package com.java.test

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ServiceSocket {
    private var mSocket: Socket? = null
    private var mScheduledFuture: ScheduledFuture<*>? = null
//    private val mLogger: Logger = Logger.getLogger(SocketTest::class.java.name)

    @JvmStatic
    fun main(args: Array<String>) {
        createServiceSocket()
    }

    private fun createServiceSocket() {
        val mScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
        val mRunnable =
            Runnable {
                if ((mSocket == null) || (!mSocket!!.isConnected)) {
                    mSocket = Socket("127.0.0.1", 66666)
                    println("建立Socket的service端！")
                    mSocket
                    CompletableFuture.runAsync { session() }
                }
            }
        // 每隔3秒周期性的执行心跳检测动作。
        mScheduledFuture =
            mScheduledThreadPoolExecutor.scheduleAtFixedRate(mRunnable, 0, 3, TimeUnit.SECONDS)
    }

    private fun session() {
        val dis: DataInputStream?
        val dos: DataOutputStream?
        try {
            dis = DataInputStream(mSocket!!.getInputStream())
            dos = DataOutputStream(mSocket!!.getOutputStream())

            while (true) {
                val data = "PC时间:" + System.currentTimeMillis()
                dos.writeUTF(data)
                dos.flush()
                val s = dis.readUTF()
                println("收到数据:$s")
                Thread.sleep(5000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                mSocket!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mSocket = null
        }
    }
}
