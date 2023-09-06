import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import java.io.File

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.*
import org.gradle.process.ExecSpec

abstract class SignTask @javax.inject.Inject constructor(private val workers: WorkerExecutor) : DefaultTask() {

    private val build_tools_version = "30.0.3"

    @InputFile
    abstract fun getSdkPath(): String

    @TaskAction
    fun taskAction() {
        // sdk path
        val sdkPath = getSdkPath()

        val buildToolsPath = "${sdkPath}${File.separator}build-tools${File.separator}${build_tools_version}${File.separator}"
        val sdkAddOnPath = "${sdkPath}${File.separator}add-ons${File.separator}addon-hcp3_android_sdk_apis-harman-31${File.separator}"
        val signingKeysPath = "${sdkAddOnPath}signing${File.separator}"
        val apkSignerBatPath =
            buildToolsPath + if (org.gradle.internal.os.OperatingSystem.current().isWindows) "apksigner.bat" else "apksigner"
        val keyFile = signingKeysPath + "platform.pk8"
        val certFile = signingKeysPath + "platform.x509.pem"
        val buildDir = "${project.buildDir}"

        var allApks: Array<File>? = null

        if (name == "assembleRsiDebug" || name == "connectedDebugAndroidTest") {
            println("run -> task -> name :$name")
            val apkPathOutputs = File("${buildDir}${File.separator}intermediates${File.separator}apk${File.separator}")
            if (apkPathOutputs.exists()) {
                allApks = apkPathOutputs.listFiles()
                    ?.filter { filter ->
                        filter.name.endsWith(".apk")
                    }
                    ?.toTypedArray()
            }

            if (allApks == null || allApks.isEmpty()) {
                println("run -> task -> apks are missing/not generated. Please try again, if problem persists, give a clean build.")
                return
            }

            allApks.forEach { apk ->
                println("run -> task -> signing: $apk")
                val signerCommand = listOf(apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, apk.absolutePath)
                project.exec(object :Action<ExecSpec>{
                    override fun execute(t: ExecSpec) {
                        t.commandLine(signerCommand)
                    }
                })
                project.exec{

                }
//                project.exec {
//                    it.commandLine(signerCommand)
//                }
//                project.exec {
////                    it.co
//                }
                project.exec {

                }

            }
        } else if (name.indexOf("assemble") > -1) {

            val apkPathOutputs = File("${buildDir}${File.separator}outputs${File.separator}apk${File.separator}")
            if (apkPathOutputs.exists()) {
                allApks = apkPathOutputs.listFiles()
                    ?.filter { filter ->
                        filter.name.endsWith(".apk")
                    }
                    ?.toTypedArray()
            }

            if (allApks == null || allApks.isEmpty()) {
                println("apks are missing/not generated. Please try again, if problem persists, give a clean build.")
                return
            }

            allApks.forEach { apk ->
                println("signing $apk")
                val signerCommand = listOf(apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, apk)

            }

//            for (apk in allApks) {
//                println("signing $apk")
//                val signerCommand = [apkSignerBatPath, 'sign', '--key', keyFile, '--cert', certFile, apk]
//                exec {
//                    commandLine = signerCommand
//                }
//            }
        }

//        if ("assembleRsiDebug" == task.name || "connectedDebugAndroidTest" == task.name) {
//            task.doLast {
//                println("run -> task -> name :" + task.name)
//                val allApks = []
//                val apkPathOutputs = new File ("${buildDir}${File.separator}intermediates${File.separator}apk${File.separator}")
//                if (apkPathOutputs.exists()) {
//                    apkPathOutputs.eachFileRecurse(groovy.io.FileType.FILES) {
//                        if (it.name.endsWith(".apk")) {
////                        allApks < < it
//                            allApks = it
//                        }
//                    }
//                }
//                if (allApks.isEmpty()) {
//                    println("run -> task -> apks are missing/not generated. Please try again, if problem persists, give a clean build.")
//                    return
//                }
//                for (apk in allApks) {
//                    println("run -> task -> signing: $apk")
//                    val signerCommand = [apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, apk]
//                    exec {
//                        commandLine = signerCommand
//                    }
//                }
//            }
//        } else if (task.name.indexOf("assemble") > -1) {
//            task.doLast {
//                //always sign /outputs and /intermediates!
//                val allApks = []
//                println(" rootDir ---> " + rootDir)
//
//                val apkPathOutputs = new File ("${buildDir}${File.separator}outputs${File.separator}apk${File.separator}")
//                if (apkPathOutputs.exists()) {
//                    apkPathOutputs.eachFileRecurse(groovy.io.FileType.FILES) {
//                        if (it.name.endsWith(".apk")) {
////                        allApks < < it
//                            allApks = it
//                        }
//                    }
//                }
//                if (allApks.isEmpty()) {
//                    println("apks are missing/not generated. Please try again, if problem persists, give a clean build.")
//                    return
//                }
//                for (apk in allApks) {
//                    println("signing $apk")
//                    val signerCommand = [apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, apk]
//                    exec {
//                        commandLine = signerCommand
//                    }
//                }
//            }
//        }
    }
}