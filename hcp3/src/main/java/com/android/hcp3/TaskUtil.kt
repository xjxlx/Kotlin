package com.android.hcp3

import com.android.hcp3.FileUtil.checkFolder
import com.android.hcp3.FileUtil.flushFolder
import com.android.hcp3.GenerateUtil.LOCAL_FOLDER_PATH
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask

object TaskUtil {
    private const val READ_JAR_CLASS_NAME = "com.android.hcp3.ReadJarFile"
    private const val READ_JAR_METHOD_NAME = "execute"

    private const val FILE_CLASS_NAME = "com.android.hcp3.FileUtil"
    private const val FILE_METHOD_NAME = "execute"

    private const val REFRESH_CLASS_NAME = "com.android.hcp3.RefreshFile"
    private const val REFRESH_METHOD_NAME = "main"

    private val BASE_COMMAND by lazy {
        val javaHome = System.getProperty("java.home")
        val javaBin = javaHome + File.separator + "bin" + File.separator + "java"
        val classpath = System.getProperty("java.class.path")
        // val command = "$javaBin -cp $classpath $className"
        // 构建命令
        return@lazy "$javaBin -cp $classpath "
    }

    private val EXECUTOR_READ = Executors.newScheduledThreadPool(1)

    // 移动文件的等待时间
    private var moveDelayTime = 10

    @JvmStatic
    fun main(args: Array<String>) {
        readProcessCountdown {
            executeAsync {
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
        return future.get()
    }

    /**
     * 循环读取指定目标文件夹下的内容，如果没有文件，则去读取文件
     */
    private fun readProcessCountdown(block: () -> Unit) {
        val timer = Timer()
        var seconds: Long = 5
        var checkFolder = false
        val timerTask =
            timerTask {
                if (seconds >= 0) {
                    if (!checkFolder) {
                        checkFolder = checkFolder(LOCAL_FOLDER_PATH)
                        println("~~~~~~~~~~~~checkFolder:[$checkFolder]~~~~~~~~~~~~~~~~~~~~")
                        if (!checkFolder) {
                            thread(true) {
                                // readProcess()
                                process(READ_JAR_CLASS_NAME)
                                flushFolder(LOCAL_FOLDER_PATH)
                            }
                        }
                    } else {
                        seconds = 0
                    }
                    seconds--
                    println("倒计时...")
                } else {
                    println("倒计时结束")
                    timer.cancel()
                    block()
                }
            }
        timer.schedule(timerTask, seconds, 1000)
    }

    private fun process(javaPath: String): Int {
        // 1：构建command命令
        val runtime = Runtime.getRuntime()
        val process = runtime.exec("$BASE_COMMAND $javaPath")
        // 2：读取执行命令的内容
        val inputStream = process.inputStream
        val inputReader = BufferedReader(InputStreamReader(inputStream))
        var inputLine: String?
        var body = ""
        while ((inputReader.readLine().also { inputLine = it }) != null) {
            body += inputLine
            body += "\n"
        }
        if (body.isNotEmpty()) {
            println("exec:$body")
        }

        // 3：读取异常的内容
        val errorStream = process.errorStream
        val errorReader = BufferedReader(InputStreamReader(errorStream))
        var errorLine: String?
        var errorBody = ""
        while ((errorReader.readLine().also { errorLine = it }) != null) {
            errorBody += errorLine
            errorBody += "\n"
        }
        if (errorBody.isNotEmpty()) {
            println("exec:[error]:$errorBody")
        }

        // 等待
        process.waitFor()
        val exitCode = process.exitValue()
        println("exitCode:$exitCode")
        process.destroy()
        if (exitCode == 0) {
            println("正常完成！")
        } else {
            println("进程异常！")
        }
        return exitCode
    }
}
