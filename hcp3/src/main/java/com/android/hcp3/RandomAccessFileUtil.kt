package com.android.hcp3

import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Class.forName

object RandomAccessFileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val filePackage = "com.android.hcp3.TestFile"
        val packageName = forName(filePackage).packageName
        // println("packageName:$packageName")
        val deleteArray = arrayOf("package $packageName;", "// item = 3")
        val newArray = arrayOf("package com.xjx.cccc.ddd.ccc.aaa;", "// item = 4")
        val path = "hcp3/src/main/java/com/android/hcp3/TestFile.java"
        changeFileContent(path, deleteArray, newArray)
    }

    /**
     * @param filePath 指定文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param deleteContent 指定需要删除的内容数组
     * @param newContent 需要被替换的内容数组
     */
    @JvmStatic
    fun changeFileContent(
        filePath: String,
        deleteArray: Array<String>,
        newArray: Array<String>,
    ): Boolean {
        try {
            // 1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String? = null
            var insertBeforePosition: Long = 0
            var itemIndex = 0
            val deleteList = deleteArray.toMutableList()
            val newList = newArray.toMutableList()

            // 2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                println("line: $readLine insertBeforePosition:$insertBeforePosition")
                readLine?.let { line ->
                    // 每次都要删掉一个数据，避免后续的数据相同
                    if (itemIndex > 0) {
                        deleteList.removeAt(0)
                        newList.removeAt(0)
                    }

                    // println("line:$line   insertBeforePosition:$insertBeforePosition")
                    println("itemIndex:$itemIndex deleteList:$deleteList   newList:$newList")
                    // 3：排查指定的节点，才开始后续的操作
                    val deleteContent = deleteList[itemIndex]
                    val newContent = deleteList[itemIndex]
                    if (line == deleteContent) {
                        // 4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
                        // 5：对比当前匹配到内容的长度和需要替换内容的长度的差值
                        val offset = deleteContent.length - newContent.length
                        println("offset:$offset")

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
                        random.write(newList[itemIndex].toByteArray(charset = Charsets.UTF_8))
                        // 10：如果被修改的内容小于被替换掉的内容长度，则需要缩短整个文件的长度，不然会出现多出来一部分内容没有被替换掉
                        if (offset > 0) {
                            random.setLength(fileLength - offset)
                        }
                        // 11：替换掉需要删除的指定内容
                        val replace = residueContent.replace(line, "")
                        // println("replace:$replace")
                        // 12：把剩下的数据给重新写入
                        random.write(replace.toByteArray())
                        itemIndex += 1
                        println("【Random】文件[$filePath]的[$deleteContent]内容修改成功。")
                    }
                    // [must] 必须把这个放到读取的后面，这样才能从指定的位置插入
                    insertBeforePosition = random.filePointer
                }
            }
            random.close()
        } catch (e: IOException) {
            println(e)
            println("文件[$filePath]的内容修改失败：$e")
        }
        return false
    }
}
