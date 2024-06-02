package com.android.hcp3

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

object TaskUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val execute =
            executeAsync {
                TimeUnit.SECONDS.sleep(2) // 模拟耗时操作
            }
        println("execute: $execute")
    }

    fun executeAsync(block: () -> Unit): Boolean {
        // 定义异步任务
        val future =
            CompletableFuture.supplyAsync {
                try {
                    ReadJarFile.execute()
                    TimeUnit.SECONDS.sleep(2) // 模拟耗时操作
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
        FileUtil.execute()
        return true
    }
}
