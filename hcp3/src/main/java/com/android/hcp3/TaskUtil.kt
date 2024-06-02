package com.android.hcp3

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object TaskUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val executor = Executors.newScheduledThreadPool(1)
        // 移动文件的等待时间
        var moveDelayTime = 3

        // 1：执行文件写入任务
        executeAsync {
            ReadJarFile.execute()
        }

        // 2：等待指定的时间后，去执行移动文件的任务
        executor.scheduleAtFixedRate({
            println("倒计时：$moveDelayTime 秒")
            if (moveDelayTime == 0) {
                executor.shutdown() // 停止计时器
                FileUtil.execute()
            }
            moveDelayTime--
        }, 0, 1, TimeUnit.SECONDS) // 每隔一秒执行一次任务
    }

    private fun executeAsync(block: () -> Unit): Boolean {
        // 定义异步任务
        val future =
            CompletableFuture.supplyAsync {
                try {
                    block()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                "Async task completed"
            }

        // 判断任务是否完成
        while (!future.isDone) {
            println("Task not done yet...")
            TimeUnit.MILLISECONDS.sleep(500)
        }

        // 获取结果
        val result = future.get()
        println(result)
        return true
    }
}
