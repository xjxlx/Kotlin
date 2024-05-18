package com.android.hcp3

import java.io.IOException
import java.io.RandomAccessFile

object RandomAccessFileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val path = "hcp3/src/main/java/com/android/hcp3/TestFile.java"
        randomAccess(path, "package ", "package com.xjx;")
    }

    @JvmStatic
    fun randomAccess(
        filePath: String,
        tag: String,
        changContent: String,
    ): Boolean {
        try {
            // 1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String? = null
            var insertBeforePosition: Long = 0
            // 2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                readLine?.let { line ->
                    // println("line:$line   insertBeforePosition:$insertBeforePosition")
                    // 3：排查指定的节点，才开始后续的操作
                    if (line.startsWith(tag)) {
                        // 4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
                        // 5：对比当前匹配到内容的长度和需要替换内容的长度的差值
                        val offset = line.length - changContent.length
                        // println("offset:$offset")

                        // 5：跳转指针到指定的未发货子，从此处开始读取剩余的内容
                        random.seek(insertBeforePosition)
                        // 6：设置一个指定长度的字节数组，长度 = 文件总长度 - 匹配到的位置
                        val byteArray = ByteArray((fileLength - insertBeforePosition).toInt())
                        // 7：读取这个字节数组，把剩余的内容放到字节数组中
                        random.read(byteArray)
                        // 8：把剩余字节数组转换为字符串
                        val residueContent = String(byteArray)
                        // println("【residueContent】:residueContent")

                        // 9：把指针设置到从改变的位置，并写入内容
                        random.seek(insertBeforePosition)
                        random.write(changContent.toByteArray(charset = Charsets.UTF_8))
                        // 10：如果被修改的内容小于被替换掉的内容长度，则需要缩短整个文件的长度，不然会出现多出来一部分内容没有被替换掉
                        if (offset >= 0) {
                            random.setLength(fileLength - offset)
                        }
                        // 11：替换掉需要删除的指定内容
                        val replace = residueContent.replace(line, "")
                        // println("replace:$replace")
                        // 12：把剩下的数据给重新写入
                        random.write(replace.toByteArray())
                    }
                    // [must] 必须把这个放到读取的后面，这样才能从指定的位置插入
                    insertBeforePosition = random.filePointer
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
