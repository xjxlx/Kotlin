import com.android.plugin.utils.TextUtil

object VersionUtil {

    val VERSION: String
        get() {
            val latestGitTag = latestGitTag()
            return if (TextUtil.isEmpty(latestGitTag)) {
                ""
            } else {
                latestGitTag
            }
        }

    /**
     * 获取git仓库中最新的tag作为版本号
     */
    private fun latestGitTag(): String {
        val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
        return process.inputStream.bufferedReader()
            .use { bufferedReader ->
                bufferedReader.readText()
                    .trim()
            }
    }
}