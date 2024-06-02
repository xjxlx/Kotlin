package com.android.hcp3

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

object TaskUtil {
    private const val INTERFACE_CLASS_NAME = "com.android.hcp3.GenerateInterface"
    private const val INTERFACE_METHOD_NAME = "generateInterface"

    private const val READ_JAR_CLASS_NAME = "com.android.hcp3.ReadJarFile"
    private const val READ_JAR_METHOD_NAME = "execute"

    private const val FILE_CLASS_NAME = "com.android.hcp3.FileUtil"
    private const val FILE_METHOD_NAME = "execute"

    @JvmStatic
    fun main(args: Array<String>) {
        // 启动第一个进程，用来生成interface的接口
        val interfaceProcess = interfaceProcess()

        if (interfaceProcess == 0) {
            TimeUnit.MILLISECONDS.sleep(500)
            val readProcess = readProcess()
            if (readProcess == 0) {
                TimeUnit.MILLISECONDS.sleep(3000)
                fileProcess()
            }
        }
    }

    private fun interfaceProcess(): Int {
        println("生成接口的进程开始启动--->")
        val processInterface =
            ProcessBuilder(
                "java",
                "-cp",
                System.getProperty("java.class.path"),
                INTERFACE_CLASS_NAME,
                INTERFACE_METHOD_NAME
            )
        val interfaceProcess = processInterface.start()
        // 读取进程的输出
        val reader = BufferedReader(InputStreamReader(interfaceProcess.inputStream))
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            println(line)
        }
        // 等待第一个进程完成
        val exitCode = interfaceProcess.waitFor()
        interfaceProcess.destroy()
        println("创建接口进程退出代码: $exitCode")
        return exitCode
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
        readProcess.destroy()
        return exitCode
    }

    private fun fileProcess(): Int {
        println("File移动文件的进程开始启动--->")
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
}
