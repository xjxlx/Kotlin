import com.ray.permissions.VHALPermission
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class ModifyClassesTask : DefaultTask() {
    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @TaskAction
    fun taskAction() {
        println("out : ${output.get().asFile.path}")
        val pool = ClassPool(ClassPool.getDefault())
        JarOutputStream(
            output
                .get()
                .asFile
                .outputStream()
                .buffered()
        ).use { jarOutput ->
            // write jars
            allJars.get().map { JarFile(it.asFile) }.forEach {
                it.use { jar ->
                    jar.entries().iterator().forEach { jarEntry ->
                        jarOutput.putNextEntry(JarEntry(jarEntry.name))
                        it.getInputStream(jarEntry).use { input -> input.copyTo(jarOutput) }
                        jarOutput.closeEntry()
                    }
                }
            }
            project.providers
                .gradleProperty("enableConfig")
                .takeIf(Provider<String>::isPresent)
                ?.run { onConfigSet(this.get(), pool, jarOutput) }
                ?: onConfigNotSet(pool, jarOutput)
        }
    }

    private fun String.toVHALPermission(): String = "<uses-permission android:name=\"${this}\"/>"

    private fun String.toRSIPermission(): String =
        if (this.equals("CLUSTERSDS", true)) {
            "<uses-permission android:name=\"de.esolutions.fw.rsi.permission.PROVIDE_CLUSTERSDS\"/>"
        } else {
            "<uses-permission android:name=\"de.esolutions.fw.rsi.permission.CONSUME_${uppercase()}\"/>"
        }

    private inline fun writeClass(
        jarOutput: JarOutputStream,
        pool: ClassPool,
        crossinline onVehicleControlManager: (CtClass) -> Unit,
        crossinline onPropertyManager: (CtClass) -> Unit,
        crossinline onVHALProperty: (CtClass) -> Unit
    ) {
        allDirectories.get().forEach { directory ->
            directory.asFile.walk().filter(File::isFile).forEach { file ->
                directory
                    .asFile
                    .toURI()
                    .relativize(file.toURI())
                    .path
                    .replace(File.separatorChar, '/')
                    .run { jarOutput.putNextEntry(JarEntry(this)) }
                file.inputStream().use {
                    when {
                        file.name.endsWith("VehicleControlManager.class") -> {
                            pool.makeClass(it).run {
                                onVehicleControlManager(this)
                                jarOutput.write(toBytecode())
                            }
                        }

                        file.name.endsWith("PropertyManager.class") -> {
                            pool.makeClass(it).run {
                                onPropertyManager(this)
                                jarOutput.write(toBytecode())
                            }
                        }

                        file.path.contains("technology/cariad/vehiclecontrolmanager/vhal/property") -> {
                            pool.makeClass(it).run {
                                onVHALProperty(this)
                                jarOutput.write(toBytecode())
                            }
                        }

                        else -> {
                            it.copyTo(jarOutput)
                        }
                    }
                }
                jarOutput.closeEntry()
            }
        }
    }

    private fun onConfigSet(
        enableConfigFile: String,
        pool: ClassPool,
        jarOutput: JarOutputStream
    ) {
        val rsiEnableFunctions: List<String>
        val vhalEnableFunctions: List<String>
        File(enableConfigFile).readLines().run {
            rsiEnableFunctions =
                parseEnableFunctions(this) {
                    it.trim().startsWith("rsi", true) &&
                        it.contains(":")
                }
            vhalEnableFunctions =
                parseEnableFunctions(this) {
                    it
                        .trimStart()
                        .startsWith("vhal") &&
                        it.contains(":")
                }
        }
        val permissions =
            rsiEnableFunctions
                .map { it.toRSIPermission() }
                .toMutableList()
        writeClass(
            jarOutput,
            pool,
            onVehicleControlManager = {
                it.declaredMethods.forEach { method ->
                    when {
                        rsiEnableFunctions.any { m ->
                            "get${m}Manager".equals(method.name, true)
                        } -> {
                            println("Enable RSI : ${method.name.substring(3)}")
                        }

                        method.name == "getVHALManager" && vhalEnableFunctions.isNotEmpty() -> {
                            println("Enable : VHAL")
                        }

                        !DefaultMethods.contains(method.name) -> {
                            it.removeMethod(method)
                        }
                    }
                }
            },
            onPropertyManager = {
                it.declaredMethods.forEach { method ->
                    if (vhalEnableFunctions.any {
                            method.name.contains(
                                "get$it",
                                true
                            )
                        }
                    ) {
                        println("Enable VHAL : ${method.name.substring(3)}")
                    } else {
                        it.removeMethod(method)
                    }
                }
            },
            onVHALProperty = { clazz ->
                clazz
                    .takeIf {
                        vhalEnableFunctions.any { clazz.simpleName.equals(it, true) }
                    }?.run { getAnnotation(VHALPermission::class.java) as VHALPermission }
                    ?.let { permissions.add(it.permission.toVHALPermission()) }
            }
        )
        writeManifestFile(permissions)
    }

    private fun onConfigNotSet(
        pool: ClassPool,
        jarOutput: JarOutputStream
    ) {
        val permissions = mutableListOf<String>()
        writeClass(
            jarOutput,
            pool,
            onVehicleControlManager = { clazz ->
                clazz.declaredMethods
                    .filter { it.name.endsWith("Manager") && !it.name.equals("getVHALManager", true) }
                    .map { it.name.substring(3, it.name.length - 7) }
                    .map { it.toRSIPermission() }
                    .forEach(permissions::add)
            },
            onPropertyManager = {},
            onVHALProperty = {
                permissions.add(
                    (it.getAnnotation(VHALPermission::class.java) as VHALPermission)
                        .permission
                        .toVHALPermission()
                )
            }
        )
        writeManifestFile(permissions)
    }

    private fun parseEnableFunctions(
        list: List<String>,
        filterStart: (String) -> Boolean
    ): List<String> =
        buildList {
            var index = list.indexOfFirst(filterStart) + 1
            while (index < list.size && list[index] != "}") {
                list[index]
                    .takeIf { it.trimStart().first().isLetter() }
                    ?.let(::add)
                index++
            }
        }

    private fun writeManifestFile(permissions: List<String>) {
        mergedManifest.asFile.get().run {
            val manifestString =
                readLines()
                    .toMutableList()
                    .run {
                        addAll(this.indexOfFirst { it.trim() == "<application>" }, permissions.distinct())
                        joinToString(separator = "\n")
                    }
            outputStream()
                .bufferedWriter()
                .use { it.write(manifestString) }
        }
    }
}

private val DefaultMethods = listOf("init", "destroy", "getInstance")
