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
import java.io.*
import java.lang.reflect.ParameterizedType
import java.nio.file.Paths

object FileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        val sourceFilePath = "hcp3/src/main/java/com/android/hcp3/TestFile.java" // 源文件路径
        val targetFolderPath = "hcp3/src/main/java/com/android/hcp3/temp/TestFile.java" // 目标文件夹路径

        // modifyFirstLine(sourceFilePath, targetFolderPath, "package com.android.hcp3.temp;")

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
                val path = enum.path
                val parentPath = enum.parentPath
                val newFileName = getFileNameForPath(path) + ".java"
                val newFilePath = "$parentPath/$newFileName"
                println("path:$path   newFilePath:$newFilePath")
                val newPackage = parentPath.substring(BASE_OUT_PUT_PATH.length + 1, parentPath.length)
                moveFile(enum.path, newFilePath, transitionPackage(newPackage))
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
                // it.close()
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
    private fun readLocalEnumFile(dir: String): LinkedHashSet<EnumBean> {
        val set = LinkedHashSet<EnumBean>()
        val dir = File(dir)
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
     * @param oldFilePath 原来文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param newFilePath 新的文件路径，例如：hcp3/src/main/java/com/android/hcp3/temp/TestFile.java
     * @param newPackage 新的包名，例如：package com.android.hcp3.temp
     */
    @JvmStatic
    fun moveFile(
        oldFilePath: String,
        newFilePath: String,
        newPackage: String?,
    ) {
        try {
            BufferedReader(FileReader(oldFilePath)).use { reader ->
                PrintWriter(FileWriter(oldFilePath)).use { writer ->
                    // 读取package 的内容
                    var packageContent: String = ""
                    while ((reader.readLine().also { packageContent = it }) != null) {
                        if (packageContent.startsWith("package ")) {
                            // 写入包名
                            writer.println("package $newPackage;")
                            break
                        }
                    }

                    // 读取剩余的行
                    var line: String = ""
                    while ((reader.readLine().also { line = it }) != null) {
                        writer.println(line) // 写入剩余的行
                    }
                }
            }
            val originalFile = File(oldFilePath)
            val renameTo = originalFile.renameTo(File(newFilePath))
            // 重命名临时文件
            if (renameTo) {
                println("文件移动成功！")
            } else {
                println("文件移动失败！")
            }
        } catch (e: IOException) {
            println(e)
        }
    }
}
