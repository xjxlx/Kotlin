package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.OBJECT_SUFFIX
import com.android.hcp3.Config.RSI_PARENT_NODE_LEVEL
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_ROOT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.ReadJarFile.getGlobalClassLoad
import com.android.hcp3.ReadJarFile.readApiNodeForParent
import com.android.hcp3.ReadJarFile.readNeedDependenciesClassName
import com.android.hcp3.StringUtil.deleteFileFormat
import com.android.hcp3.StringUtil.getFileNameForPath
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.EnumBean
import com.android.hcp3.bean.StatisticBean
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.reflect.ParameterizedType
import java.nio.file.Paths
import kotlin.math.abs

object FileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        // <editor-fold desc="1：读取本地JAR包的Api，返回一个list列表">
        readJarApiList()
        println("readJarApiFile:[$RSI_TARGET_NODE_LIST]")
        // </editor-fold>

        // <editor-fold desc="2：读取本地指定目录中的Api的路径，返回一个set集合">
        val localTargetPath =
            lowercase(
                transitionPath(
                    Paths.get(BASE_OUT_PUT_PATH)
                        .resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                        .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                        .toString()
                )
            )
        val readLocalApiPath = readLocalApiPath(localTargetPath)
        println("readLocalApiPath:[$readLocalApiPath]")
        // </editor-fold>

        // <editor-fold desc="3：读取本地指定目录中的Api下child的path，返回一个set集合">
        val readLocalApiChildPath = readLocalApiChildPath(readLocalApiPath)
        // </editor-fold>

        // <editor-fold desc="4：读取本地指定目录中的Api下child的泛型，返回一个set集合">
        val apiChildGenericTypeList = getApiChildGenericTypeList(readLocalApiChildPath)
        println("apiChildGenericTypeList:[$apiChildGenericTypeList]")
        // </editor-fold>

        // <editor-fold desc="5：读取本地指定目录中的Api下child的泛型，返回一个set集合">
        val readLocalEnumFile = readLocalEnumFile(localTargetPath)
        println("readLocalEnumFile:[$readLocalEnumFile]")
        // </editor-fold>

        // <editor-fold desc="6：对比本地的Enum在Api包下的主类中出现的次数">
        val filterEnum = filterEnumSize(apiChildGenericTypeList, readLocalEnumFile)
        println("filterEnum:[$filterEnum]")
        // </editor-fold>

        filterEnum.forEach { enum ->
            // 只有在小雨等于1的时候，才会去移动文件
            if (enum.count <= 1) {
                val parentPath = enum.parentPath
                val newPackage = parentPath.substring(BASE_OUT_PUT_PATH.length + 1, parentPath.length)
                // 改变文件的包名
                changePackage(enum.path, transitionPackage(newPackage))
                // 移动文件到新的包中去
                moveFile(enum.path, parentPath)
            }
        }
    }

    /**
     * 读取JAR包中节点下Api的信息
     */
    private fun readJarApiList() {
        try {
            // 1：读取指定目标节点下所有的object集合,例如：de/esolutions/fw/rudi/viwi/service/hvac/v3
            val filterNodePath: String =
                transitionPath(
                    Paths.get(RSI_ROOT_NODE_PATH)
                        .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                        .resolve(Paths.get(RSI_PARENT_NODE_LEVEL))
                        .toString()
                )
            println("过滤JAR包中的父节点为：[$filterNodePath]")
            // 2: 读取jar包中需要依赖的类名字
            val needDependenciesClassNameList = readNeedDependenciesClassName(filterNodePath)
            // 3:通过配置需要依赖的类，去构建一个classLoad
            getGlobalClassLoad(needDependenciesClassNameList)?.let {
                // 4：读取父节点下所有的api方法，获取所有api的方法的名字以及返回类型的全路径包名
                readApiNodeForParent(it)
                // 关闭ClassLoader释放资源
                it.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("write data error!")
        }
    }

    /**
     * 读取本地指定节点下所有的Api路径
     */
    private fun readLocalApiPath(dir: String): LinkedHashSet<StatisticBean> {
        val set = LinkedHashSet<StatisticBean>()
        val file = File(dir)
        if (file.exists() && file.isDirectory) {
            file.listFiles()?.let { listFiles ->
                listFiles.forEach { childFile ->
                    if (childFile.isDirectory) {
                        val bean = StatisticBean()
                        // 设置api泛型的路径
                        bean.apiNodePath = childFile.path
                        set.add(bean)
                    }
                }
            }
        }
        return set
    }

    /**
     * 获取本地Api节点下child的path，返回一个集合
     */
    private fun readLocalApiChildPath(set: LinkedHashSet<StatisticBean>): LinkedHashSet<StatisticBean> {
        set.forEach { bean ->
            val file = File(bean.apiNodePath)
            if (file.exists() && file.isDirectory) {
                file.listFiles()?.let { listFiles ->
                    listFiles.forEach { file ->
                        /**
                         * 对比[RSI_TARGET_NODE_LIST]中存储的api节点信息，如果发现当前文件名字 == api泛型的名字加上[OBJECT_SUFFIX]
                         * 则说明当前文件就是需要的那个文件，并存储起来
                         */
                        val find =
                            RSI_TARGET_NODE_LIST.find { find ->
                                find.apiGenericName + OBJECT_SUFFIX ==
                                    deleteFileFormat(
                                        file.name
                                    )
                            }

                        // 存储文件路径
                        if (find != null) {
                            //  println("find:【$find】")
                            val path = file.path
                            bean.apiChildPath = path
                        }
                    }
                }
            }
        }
        return set
    }

    /**
     * 获取api节点下泛型对象的属性泛型
     */
    private fun getApiChildGenericTypeList(set: LinkedHashSet<StatisticBean>): LinkedHashSet<StatisticBean> {
        set.forEach { bean ->
            val outPath = bean.apiChildPath.substring(BASE_OUT_PUT_PATH.length + 1, bean.apiChildPath.length)
            val packagePath = transitionPackage(outPath)
            val indexOf = packagePath.lastIndexOf(".")
            val substring = packagePath.substring(0, indexOf)
            val cls = Class.forName(substring)

            var objectGenericSet = bean.objectGenericSet
            if (objectGenericSet == null) {
                objectGenericSet = LinkedHashSet()
            }

            for (field in cls.declaredFields) {
                val genericType = field.genericType
                if (genericType is Class<*>) {
                    val primitiveOrWrapper = ReadJarFile.isPrimitiveOrWrapper(genericType)
                    // 只要不是基础类型，就添加进去
                    if (!primitiveOrWrapper) {
                        objectGenericSet.add(genericType.typeName)
                    }
                } else if (genericType is ParameterizedType) {
                    val actualTypeArguments = genericType.actualTypeArguments
                    if (actualTypeArguments.isNotEmpty()) {
                        val argument = actualTypeArguments[0]

                        if (argument is Class<*>) {
                            val primitiveOrWrapper = ReadJarFile.isPrimitiveOrWrapper(argument)
                            if (!primitiveOrWrapper) {
                                objectGenericSet.add(argument.typeName)
                            }
                        }
                    }
                }
            }
            bean.objectGenericSet = objectGenericSet
        }
        return set
    }

    /**
     * 根据指定的主目录，获取父类节点下的api路径
     */
    private fun readLocalEnumFile(dirPath: String): LinkedHashSet<EnumBean> {
        val set = LinkedHashSet<EnumBean>()
        val dir = File(dirPath)
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.let { listFiles ->
                listFiles.forEach { childFile ->
                    if (childFile.isFile) {
                        val enumBean = EnumBean()
                        enumBean.path = childFile.path
                        enumBean.name = getFileNameForPath(childFile.path)
                        set.add(enumBean)
                    }
                }
            }
        }
        return set
    }

    /**
     * 循环对比两个集合，查看enum在set集合中出现的次数
     */
    private fun filterEnumSize(
        set: LinkedHashSet<StatisticBean>,
        localEnum: LinkedHashSet<EnumBean>,
    ): LinkedHashSet<EnumBean> {
        localEnum.forEach { enum ->
            set.forEach { bean ->
                val find = bean.objectGenericSet.find { StringUtil.getPackageSimple(it) == enum.name }
                if (find != null) {
                    enum.count += 1
                    enum.parentPath = bean.apiNodePath
                }
            }
            println("枚举：${enum.name} 在集合中出现了:${enum.count} 次~")
        }
        return localEnum
    }

    /**
     * @param filePath 原来文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param tag 指定这个节点才会去遍历
     * @param changContent 被替换的内容
     */
    @JvmStatic
    fun randomAccess(
        filePath: String,
        tag: String,
        changContent: String,
    ): Boolean {
        try {
            val random = RandomAccessFile(filePath, "rw")
            var position: Long = 0
            var readLine: String
            while ((random.readLine().also { readLine = it }) != null) {
                if (readLine.startsWith(tag)) {
                    val readLineLength = readLine.length
                    val changContentLength = changContent.length
                    // println("readLineLength:[$readLineLength] changContentLength:[$changContentLength]")
                    val offset = readLineLength - changContentLength
                    if (offset > 0) {
                        random.seek(position)
                        random.write(changContent.toByteArray())
                        Array(abs(offset)) { " " }.forEach { item ->
                            random.writeBytes(item)
                        }
                    } else if (offset <= 0) {
                        // 记录当前文件长度，用于后续处理
                        val fileLength: Long = random.length()
                        // 将文件指针移到插入位置后，开始读取剩余数据到缓冲区
                        val buffer = ByteArray((fileLength - position).toInt())
                        random.seek(position)
                        random.read(buffer)
                        // 将文件指针移回插入位置
                        random.seek(position)
                        // 写入新数据
                        val originalBuffer = String(buffer)
                        // println("originalBuffer:【$originalBuffer】")
                        val replaceContent = originalBuffer.replace(readLine, "")
                        // println("replaceContent:【$replaceContent】")
                        random.write(changContent.toByteArray())
                        // 写回之前读取的数据
                        random.write(replaceContent.toByteArray())
                    }
                    break
                }
                position += readLine.length + System.lineSeparator().length
            }
            random.close()
            println("【Random】文件[$filePath]的[$tag]内容修改成功。")
            return true
        } catch (e: IOException) {
            println(e)
            println("文件[$filePath]的[$tag]内容修改失败：$e")
        }
        return false
    }

    /**
     * @param filePath 原来文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param packageContent 被替换的内容包,不包含; 例如："com.xjx.android"
     */
    @JvmStatic
    fun changePackage(
        filePath: String,
        packageContent: String,
    ): Boolean {
        return randomAccess(filePath, "package ", "package $packageContent;")
    }

    /**
     * @param filePath 原来文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param importContent 被替换的import内容
     */
    @JvmStatic
    fun changeImport(
        filePath: String,
        importContent: String,
    ): Boolean {
        return randomAccess(filePath, "import ", "import $importContent")

//        try {
//            val realContent = "package $newContent;"
//            val random = RandomAccessFile(filePath, "rw")
//            var position: Long = 0
//            var readLine: String
//            while ((random.readLine().also { readLine = it }) != null) {
//                if (readLine.startsWith("package ")) {
//                    val readLineLength = readLine.length
//                    val newPackageLength = realContent.length
//                    println("readLineLength:[$readLineLength] newPackageLength:[$newPackageLength]")
//                    val offset = readLineLength - newPackageLength
//                    if (offset > 0) {
//                        random.seek(position)
//                        random.write(realContent.toByteArray())
//                        Array(abs(offset)) { " " }.forEach { item ->
//                            random.writeBytes(item)
//                        }
//                    } else if (offset < 0) {
//                        // 记录当前文件长度，用于后续处理
//                        val fileLength: Long = random.length()
//                        // 将文件指针移到插入位置后，开始读取剩余数据到缓冲区
//                        val buffer = ByteArray((fileLength - position).toInt())
//                        random.seek(position)
//                        random.read(buffer)
//                        // 将文件指针移回插入位置
//                        random.seek(position)
//                        // 写入新数据
//                        val originalBuffer = String(buffer)
//                        // println("originalBuffer:【$originalBuffer】")
//                        val replaceContent = originalBuffer.replace(readLine, "")
//                        // println("replaceContent:【$replaceContent】")
//                        random.write(realContent.toByteArray())
//                        // 写回之前读取的数据
//                        random.write(replaceContent.toByteArray())
//                        println("文件：$filePath 包名修改成功！")
//                    }
//                    break
//                }
//                position += readLine.length + System.lineSeparator().length
//            }
//            random.close()
//            println("文件内容修改成功。")
//            return true
//        } catch (e: IOException) {
//            println(e)
//            println("文件内容修改失败：$e")
//        }
        return false
    }

    /**
     * @param oldFilePath 原来文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param newFilePath 新的文件的路径，例如：hcp3/src/main/java/com/android/hcp3/temp
     */
    @JvmStatic
    fun moveFile(
        oldFilePath: String,
        newFilePath: String,
    ): Boolean {
        try {
            val oldFile = File(oldFilePath)
            val newFile = File(Paths.get(newFilePath).resolve(Paths.get(oldFile.name)).toString())
            val renameTo = oldFile.renameTo(newFile)
            println("文件移动成功: oldPath:[${oldFile.path}] newFilePath:[${newFile.path}]")
            return renameTo
        } catch (e: IOException) {
            println("文件内容修改失败：$e")
        }
        return false
    }
}
