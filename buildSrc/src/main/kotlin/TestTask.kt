import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class TestTask : DefaultTask() {
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
        println("taskAction-------------------------------->")
        val mergedManifest = mergedManifest.asFile.get()
        println("mergedManifest:$mergedManifest")
        val readText = mergedManifest.readText()
        println("readText:$readText")
    }
}
