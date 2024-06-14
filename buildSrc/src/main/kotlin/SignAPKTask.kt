//
// import java.io.File
// import javax.inject.Inject
// import org.gradle.api.DefaultTask
// import org.gradle.api.file.DirectoryProperty
// import org.gradle.api.provider.Property
// import org.gradle.api.tasks.InputDirectory
// import org.gradle.api.tasks.Internal
// import org.gradle.api.tasks.OutputDirectory
// import org.gradle.api.tasks.TaskAction
// import org.gradle.workers.WorkerExecutor
//
// /**
// * @Description sign apk file
// * @Date 2023/3/23 16:17
// * @Created by wangchunlei
// */
// abstract class SignAPKTask @Inject constructor(private val workers: WorkerExecutor) :
// 	DefaultTask() {
//
// 	@get:InputDirectory
// 	abstract val apkFolder: DirectoryProperty
//
// 	@get:OutputDirectory
// 	abstract val outFolder: DirectoryProperty
//
// 	@get:Internal
// 	abstract val androidSDKHome: Property<String>
//
// 	@TaskAction
// 	fun taskAction() {
// 		val apkDirectory = apkFolder.get().asFile
// 		val outDirectory = outFolder.asFile.get()
// 		println("APK Folder = ${apkDirectory.absolutePath}")
// 		println("Out Folder = ${outDirectory.absolutePath}")
// 		//apk file
// 		val apk = apkDirectory.listFiles()!!.first { "apk" == it.extension }
// 		//init apksigner , keyFile and cerFile
// 		val buildToolsVersion = "30.0.3"
// 		val sdkPath = androidSDKHome.get()
// 		val buildToolsPath =
// 			"${sdkPath}${File.separator}build-tools${File.separator}${buildToolsVersion}${File.separator}"
// 		val sdkAddOnPath =
// 			"${sdkPath}${File.separator}add-ons${File.separator}addon-hcp3_android_sdk_apis-harman-33${File.separator}"
// 		val signingKeysPath = "${sdkAddOnPath}signing${File.separator}"
// 		val apkSignerBatPath =
// 			buildToolsPath + if (org.gradle.internal.os.OperatingSystem.current().isWindows) "apksigner.bat" else "apksigner"
// 		val keyFile = signingKeysPath + "testkey.pk8"
// 		val certFile = signingKeysPath + "testkey.x509.pem"
// 		//signing
// 		val signerCommand = listOf(
// 			apkSignerBatPath,
// 			"sign",
// 			"--key",
// 			keyFile,
// 			"--cert",
// 			certFile,
// 			apk.absolutePath
// 		)
// 		project.exec {
// 			it.commandLine(signerCommand)
// 		}
// 		//copy or not
// 		if (outDirectory.absolutePath != apkDirectory.absolutePath) {
// 			apkDirectory.listFiles()!!.forEach {
// 				File(outDirectory, it.name).run {
// 					takeIf { f -> f.exists() }?.delete()
// 					it.copyTo(this)
// 				}
// 			}
// 		}
// 	}
// }
