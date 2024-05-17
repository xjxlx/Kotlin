package com.android.hcp3

import java.io.IOException
import java.io.RandomAccessFile

object RandomAccessFileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val path = "hcp3/src/main/java/com/android/hcp3/TestFile.java"
        randomAccess(path, "", "")
    }

    @JvmStatic
    fun randomAccess(
        filePath: String,
        tag: String,
        changContent: String,
    ): Boolean {
        try {
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String? = null
            var changePosition: Long = 0
            while ((random.readLine().also { readLine = it }) != null) {
                readLine?.let { line ->
                    println("line:$line   changePosition:$changePosition")
                    if (line.startsWith("package ")) {
                        val length = random.length()
                        val content = "package com.android;"

                        // 1：先跳到指定的位置，开始读取文件剩余的内容
                        random.seek(changePosition)
                        // 2：设置一个指定长度的字节数组，长度 = 文件总长度 - 开始改变的位置
                        val byteArray = ByteArray(length.toInt() - changePosition.toInt())
                        // 3：读取这个字节数组，把剩余的内容放到字节数组中
                        random.read(byteArray)
                        // 得到剩余的内容
                        val originalBuffer = String(byteArray)
                        println("【originalBuffer】:$originalBuffer")

                        val offset = line.length - content.length
                        println("offset:$offset")

                        // 4：把指针设置到从改变的位置
                        random.seek(changePosition)
                        random.write(content.toByteArray(charset = Charsets.UTF_8))

                        // 5：跳过指定数量的字符，在往后面写入，避免数据过短，导致后面的数据会压上来
                        if (offset > 0) {
                            random.skipBytes(offset)
                        }
                        // 删除原来的数据
                        val replace = originalBuffer.replace(line, "")
                        random.write(replace.toByteArray())
                    }
                    // 必须把这个放到读取的后面，这样才能从指定的位置插入
                    changePosition = random.filePointer
                }
            }
            random.close()
            println("【Random】文件[$filePath]的[$tag]内容修改成功。")
//            return true
        } catch (e: IOException) {
            println(e)
            println("文件[$filePath]的[$tag]内容修改失败：$e")
        }
        return false
    }
}
