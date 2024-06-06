package com.android.hcp3

import com.android.hcp3.GenerateUtil.LOCAL_FOLDER_PATH
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object TaskUtil {
    private const val READ_JAR_CLASS_NAME = "com.android.hcp3.ReadJarFile"
    private const val READ_JAR_METHOD_NAME = "execute"

    private const val FILE_CLASS_NAME = "com.android.hcp3.FileUtil"
    private const val FILE_METHOD_NAME = "execute"
    private val EXECUTOR_READ = Executors.newScheduledThreadPool(1)

    // 移动文件的等待时间
    private var moveDelayTime = 2

    @JvmStatic
    fun main(args: Array<String>) {
        val executeCode =
            executeAsync {
                readProcess()
            }

        if (executeCode == 0) {
            // 刷新文件夹以及子级文件夹的所有文件
            FileUtil.refreshFolder(LOCAL_FOLDER_PATH)
            countdown(EXECUTOR_READ, moveDelayTime) {
                fileProcess()
            }
        }
    }

    private fun countdown(
        executor: ScheduledExecutorService,
        delayTime: Int,
        block: () -> Unit,
    ) {
        var tempTime = delayTime
        executor.scheduleAtFixedRate({
            println("倒计时：$tempTime 秒")
            if (tempTime == 0) {
                println("时间到！")
                executor.shutdown() // 停止计时器
                block()
            }
            tempTime--
        }, 0, 1, TimeUnit.SECONDS) // 每隔一秒执行一次任务
    }

    private fun readProcess(): Int {
        println("读取JAR的进程开始启动--->")
        val processRead =
            ProcessBuilder(
                "java",
                "-cp",
                System.getProperty("java.class.path"),
                READ_JAR_CLASS_NAME,
                READ_JAR_METHOD_NAME
            )
        val readProcess = processRead.start()
        // 读取进程的输出
        val reader = BufferedReader(InputStreamReader(readProcess.inputStream))
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            println(line)
        }
        // 等待第一个进程完成
        val exitCode = readProcess.waitFor()
        println("读取JAR进程退出代码: $exitCode")
        return exitCode
    }

    private fun fileProcess(): Int {
        println("\r\nFile移动文件的进程开始启动--->")
        val processFile =
            ProcessBuilder(
                "java",
                "-cp",
                System.getProperty("java.class.path"),
                FILE_CLASS_NAME,
                FILE_METHOD_NAME
            )
        val readProcess = processFile.start()
        // 读取进程的输出
        val reader = BufferedReader(InputStreamReader(readProcess.inputStream))
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            println(line)
        }
        // 等待第一个进程完成
        val exitCode = readProcess.waitFor()
        println("File移动文件的进程退出代码: $exitCode")
        return exitCode
    }

    private fun executeAsync(block: () -> Int): Int {
        // 定义异步任务
        val executorDefaultValue = -1
        var executeCode = executorDefaultValue

        val future =
            CompletableFuture.supplyAsync {
                try {
                    executeCode = block()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                println("Async task completed！")
                return@supplyAsync executeCode
            }

        // 判断任务是否完成
        while ((!future.isDone) && (executeCode != executorDefaultValue)) {
            TimeUnit.MILLISECONDS.sleep(10)
            println("异步任务执行中...")
        }

        // 获取结果
        val result = future.get()
        return result
    }
}
