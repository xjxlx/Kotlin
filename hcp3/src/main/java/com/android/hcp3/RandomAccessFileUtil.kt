package com.android.hcp3

import java.io.IOException
import java.io.RandomAccessFile

object RandomAccessFileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
//        val filePackage = "com.android.hcp3.VcRestrictionReason"
//        val path = "hcp3/src/main/java/com/android/hcp3/VcRestrictionReason.java"
//
//        val packageName = forName(filePackage).packageName
//        // println("packageName:$packageName")
//        val deleteArray = arrayOf("package $packageName;", "  DEFECT(\"defect\"),")
//        val newArray = arrayOf("package com.xjx.cccc.ddd.ccc.aaa;", "  DEFECT(\"defect1\"),")
//        // changeFileContent(path, "package $packageName;", "package com.xjx.cccc.ddd.ccc.aaa;")
//        // changeFileContent(path, "// item = 3", "// item = 4")
//        changeFileArrayContent(path, deleteArray, newArray)

        val path = "hcp3/src/main/java/com/android/hcp3/VcSpecialValue.java"
        changeFileContent(
            path,
            "package com.android.hcp3;",
//            "package com.android.hcp4.audio;"
            "package com.android;"
        )
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
        newContent: String
    ): Boolean {
        try {
            // 1.1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String?
            var readBeforePosition: Long = 0
            var readAfterPosition: Long

            // 1.2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                readAfterPosition = random.filePointer
                // println("line: $readLine before:[$readBeforePosition] After:[$readAfterPosition]")
                readLine?.let { line ->
                    // 1.3：排查指定的节点，才开始后续的操作
                    if (line == deleteContent) {
                        // 1.4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
                        // println("【offset】: $offset")
                        // <editor-fold desc="2:读取剩余的内容"
                        // 2.1：把指针条跳转到当前行读取结束的地方，开始读取剩余的内容
                        random.seek(readAfterPosition)
                        // 2.2：设置一个指定长度的字节数组，长度 = 文件总长度 - 匹配到的结束位置
                        val byteArray = ByteArray(fileLength.toInt() - readAfterPosition.toInt())
                        // 2.3：读取这个字节数组，把剩余的内容放到字节数组中
                        random.read(byteArray)
                        val residueContent = String(byteArray)
                        // println("residueContent:$residueContent")
                        // </editor-fold>

                        // <editor-fold desc="3：从开始位置写入新的内容"
                        // 3.1：因为要覆盖原来的数据，所以这里要从从读取开始的position写入
                        // 3.2：因为要覆盖掉原来的数据，所以会丢失一个换行符，所以要在后面写入的时候，手动在末尾加上一个换行符
                        val realNewContent = newContent + System.lineSeparator()
                        // println("realNewContent:$realNewContent")
                        // 3.3：从匹配行开始的位置开始覆盖数据
                        random.seek(readBeforePosition)
                        // 3.4：写入需要替换的内容
                        random.write(realNewContent.toByteArray())
                        /**
                         * 如果返回文件的length大于当{@code newLength}的参数，则文件会被截断，这种情况下，
                         *
                         * 1：如果返回的length大于参数newLength,则偏移量等于newLength，按照内容测试结果来看，会在后续的filePointer
                         * 往前偏移。
                         *
                         * 2：方法返回的文件的当前长度小于｛@code-newLength｝参数，则文件将被扩展。在这种情况下，不定义文件的扩展部分的内容。
                         */
                        random.setLength(readBeforePosition + realNewContent.length + byteArray.size)
                        // </editor-fold>

                        // <editor-fold desc="4：写入剩余的内容"
                        // 11：跳转的位置 =  开始读取的位置 + 写入内容的长度 + 换行符
                        random.seek(readBeforePosition + realNewContent.length)
                        random.write(residueContent.toByteArray())
                        // </editor-fold>
                        println("【Random-Change】文件[$filePath]的[$deleteContent]内容修改成功。")
                    }
                    // [must] 必须把这个放到读取的后面，这样才能从指定的位置插入
                    readBeforePosition = random.filePointer
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
        newArray: Array<String>
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
        deleteContent: String
    ): Boolean {
        try {
            // println("deleteFileContent -  path:[$filePath] deleteContent: [$deleteContent]")
            // 1:设置文件的格式为【读写】
            val random = RandomAccessFile(filePath, "rw")
            var readLine: String?
            var readBeforePosition: Long = 0 // 读取当前行开始的position
            var readAfterPosition: Long // 读取当前行结束的position
            var deleteFlag = false

            // 2：循环读取文件每一行的数据
            while ((random.readLine().also { readLine = it }) != null) {
                // println("line: $readLine insertBeforePosition:$insertBeforePosition")
                readAfterPosition = random.filePointer
                readLine?.let { line ->
                    // 3：排查指定的节点，才开始后续的操作
                    if (line == deleteContent) {
                        deleteFlag = true
                    }
                    if (deleteFlag) {
                        // 4：读取文件的整个长度，用于后续读取和截取的操作
                        val fileLength = random.length()
                        // 5：因为要删除读取的指定行，所以要从读取完指定行的位置开始读取剩下的内容
                        random.seek(readAfterPosition)
                        // 6：设置一个指定长度的字节数组，长度 = 文件总长度 - 读取当前行的位置
                        val byteArray = ByteArray((fileLength - readAfterPosition).toInt())
                        // 7：读取这个字节数组，把剩余的内容放到字节数组中
                        random.read(byteArray)
                        // 8：把剩余字节数组转换为字符串
                        val residueContent = String(byteArray)
                        // println("【residueContent】:residueContent")
                        // 9：写入的位置要从读取匹配行的开始计算
                        random.seek(readBeforePosition)
                        // 10：设置剩余需要写入文件的长度，position = 总长度 - 当前行结束的position，也就是剩下部分的开始位置
                        random.setLength(fileLength - readAfterPosition)
                        // 11：把剩下的数据给重新写入
                        random.write(residueContent.toByteArray())
                        println("【Random-Delete】文件[$filePath]的[$deleteContent]内容删除成功。")
                    }
                }
                // [must] 必须把这个放到读取的后面，这样才能从指定的位置删除
                readBeforePosition = random.filePointer
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
