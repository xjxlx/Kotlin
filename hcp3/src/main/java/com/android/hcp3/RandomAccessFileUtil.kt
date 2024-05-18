package com.android.hcp3

import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Class.forName

object RandomAccessFileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val filePackage = "com.android.hcp3.VcRestrictionReason"
        val path = "hcp3/src/main/java/com/android/hcp3/VcRestrictionReason.java"

        val packageName = forName(filePackage).packageName
        // println("packageName:$packageName")
        val deleteArray = arrayOf("package $packageName;", "  DEFECT(\"defect\"),")
        val newArray = arrayOf("package com.xjx.cccc.ddd.ccc.aaa;", "  DEFECT(\"defect1\"),")
        // changeFileContent(path, "package $packageName;", "package com.xjx.cccc.ddd.ccc.aaa;")
        // changeFileContent(path, "// item = 3", "// item = 4")
        changeFileArrayContent(path, deleteArray, newArray)
    }

    /**
     * @param filePath 指定文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param deleteContent 指定需要删除的内容
     * @param newContent 需要被替换的内容
     * @return 修改文件内的指定内容，需要注意的是，被修改的内容必须要是完全匹配，是属于==的关系，如果有空格啥的不匹配，
     * 则会有可能造成匹配不上导致修改不成功，或者修改出来的内容，格式对不上
     */
    @JvmStatic
    fun changeFileContent(
        filePath: String,
        deleteContent: String,
        newContent: String,
    ): Boolean {
        try {
            // 1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String? = null
            var insertBeforePosition: Long = 0

            // 2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                // println("line: $readLine insertBeforePosition:$insertBeforePosition")
                readLine?.let { line ->
                    // 3：排查指定的节点，才开始后续的操作
                    if (line == deleteContent) {
                        // 4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
                        // 5：对比当前匹配到内容的长度和需要替换内容的长度的差值
                        val offset = deleteContent.length - newContent.length
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
                        random.write(newContent.toByteArray(charset = Charsets.UTF_8))
                        // 10：如果被修改的内容小于被替换掉的内容长度，则需要缩短整个文件的长度，不然会出现多出来一部分内容没有被替换掉
                        if (offset > 0) {
                            random.setLength(fileLength - offset)
                        }
                        // 11：替换掉需要删除的指定内容
                        val replace = residueContent.replace(line, "")
                        // println("replace:$replace")
                        // 12：把剩下的数据给重新写入
                        random.write(replace.toByteArray())
                        println("【Random-Change】文件[$filePath]的[$deleteContent]内容修改成功。")
                    }
                    // [must] 必须把这个放到读取的后面，这样才能从指定的位置插入
                    insertBeforePosition = random.filePointer
                }
            }
            random.close()
            return true
        } catch (e: IOException) {
            println(e)
            println("文件[$filePath]的内容修改失败：$e")
        }
        return false
    }

    @JvmStatic
    fun changeFileArrayContent(
        filePath: String,
        deleteArray: Array<String>,
        newArray: Array<String>,
    ): Boolean {
        if (deleteArray.size != newArray.size) {
            println("替换内容的数组长度必须相同，否则会出现异常！")
            return false
        }
        deleteArray.forEachIndexed { index, delete ->
            changeFileContent(filePath, delete, newArray[index])
        }
        return true
    }

    @JvmStatic
    fun deleteFileContent(
        filePath: String,
        deleteContent: String,
    ): Boolean {
        try {
            println("deleteFileContent -  path:[$filePath] deleteContent: [$deleteContent]")

            // 1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String? = null
            var insertBeforePosition: Long = 0

            // 2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                // println("line: $readLine insertBeforePosition:$insertBeforePosition")
                readLine?.let { line ->
                    // 3：排查指定的节点，才开始后续的操作
                    if (line == deleteContent) {
                        // 4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
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
                        // 10：删除多余的占位符，避免出现空格
                        random.setLength(fileLength - deleteContent.length)
                        // 11：替换掉需要删除的指定内容
                        val replace = residueContent.replace(line, "")
                        // println("replace:$replace")
                        // 12：把剩下的数据给重新写入
                        random.write(replace.toByteArray())
                        println("【Random-Delete】文件[$filePath]的[$deleteContent]内容删除成功。")
                    }
                    // [must] 必须把这个放到读取的后面，这样才能从指定的位置插入
                    insertBeforePosition = random.filePointer
                }
            }
            random.close()
            return true
        } catch (e: IOException) {
            println(e)
            println("文件[$filePath]的内容删除失败：$e")
        }
        return false
    }
}
