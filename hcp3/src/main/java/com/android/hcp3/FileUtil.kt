package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.OBJECT_SUFFIX
import com.android.hcp3.Config.RSI_NODE_LEVEL
import com.android.hcp3.Config.RSI_NODE_NAME
import com.android.hcp3.Config.RSI_ROOT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.GenerateUtil.getFileName
import com.android.hcp3.ReadJarFile.getGlobalClassLoad
import com.android.hcp3.ReadJarFile.readApiNodeForParent
import com.android.hcp3.ReadJarFile.readNeedDependenciesClassName
import com.android.hcp3.StringUtil.getFileNameForPath
import com.android.hcp3.StringUtil.getPackageForProjectPath
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.EnumBean
import com.android.hcp3.bean.StatisticBean
import java.io.File
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.name

object FileUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        execute()
    }

    @JvmStatic
    fun execute() {
        println()
        println("文件生成完毕，开始移动文件到目标文件夹中--------->")
        // <editor-fold desc="1：读取本地JAR包的Api，返回一个list列表">
        readJarApiList()
        println("1：readJarApiFile:[$RSI_TARGET_NODE_LIST]")
        // </editor-fold>

        // <editor-fold desc="2：读取本地指定目录中的Api的路径，返回一个set集合">
        val localTargetPath =
            transitionPath(
                Paths
                    .get(BASE_OUT_PUT_PATH)
                    .resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                    .resolve(Paths.get(RSI_NODE_NAME))
                    .toString()
            )
        val readLocalApiPath = readLocalApiPath(localTargetPath)
        println("2：readLocalApiPath:[$readLocalApiPath]")
        // </editor-fold>

        // <editor-fold desc="3：读取本地指定目录中的Api下child的path，返回一个set集合">
        val readLocalApiChildPath = readLocalApiChildPath(readLocalApiPath)
        println("3：readLocalApiChildPath:[$readLocalApiChildPath]")

        // </editor-fold>

        // <editor-fold desc="4：读取本地指定目录中的Api下child的泛型，返回一个set集合">
        val apiChildGenericTypeList = getApiChildGenericTypeList(readLocalApiChildPath)
        println("4：apiChildGenericTypeList:[$apiChildGenericTypeList]")
        // </editor-fold>

        // <editor-fold desc="5：读取本地指定目录中的Api下child的泛型，返回一个set集合">
        val readLocalEnumFile = readLocalEnumFile(localTargetPath)
        println("5：readLocalEnumFile:[$readLocalEnumFile]")
        // </editor-fold>

        // <editor-fold desc="6：对比本地的Enum在Api包下的主类中出现的次数">
        val filterEnum = filterEnumSize(apiChildGenericTypeList, readLocalEnumFile)
        println("6：filterEnum:[$filterEnum]")
        // </editor-fold>

        filterEnum.forEach { enum ->
            // 只有在小于等于1的时候，才会去移动文件
            if (enum.count <= 1) {
                val parentPath = enum.parentPath
                // 一：移动文件到新包中去
                val moveFile = moveFile(enum.path, parentPath)
                if (moveFile) {
                    // 二：获取挪移到新目录的路径
                    val packagePath = Paths.get(enum.parentPath).resolve(Paths.get(enum.name)).toString() + ".java"
                    // 2.1: 获取原始目录的路径
                    val deletePackage: String =
                        transitionPackage(
                            Paths.get(BASE_PROJECT_PACKAGE_PATH).resolve(Paths.get(RSI_NODE_NAME)).toString()
                        )
                    // 2.2:获取到挪移后的包名
                    val newPackage = getPackageForProjectPath(Paths.get(enum.parentPath).toString() + ".java")
                    changePackage(packagePath, deletePackage, newPackage)

                    // 三：改变import的内容
                    // 3.1:被删除的import内容
                    val deleteImport =
                        transitionPackage(
                            Paths
                                .get(BASE_PROJECT_PACKAGE_PATH)
                                .resolve(Paths.get(RSI_NODE_NAME))
                                .resolve(enum.name)
                                .toString()
                        )
                    deleteFileImport(enum.apiChildPath, deleteImport)
                }
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
                    Paths
                        .get(RSI_ROOT_NODE_PATH)
                        .resolve(Paths.get(RSI_NODE_NAME))
                        .resolve(Paths.get(RSI_NODE_LEVEL))
                        .toString()
                )
            println("过滤JAR包中的父节点为：[$filterNodePath]")
            // 2: 读取jar包中需要依赖的类名字
            val needDependenciesClassNameList = readNeedDependenciesClassName()
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
                                getFileName(
                                    find.apiObjectPath,
                                    ClassTypeEnum.OBJECT
                                ) == StringUtil.deleteFileFormat(file.name)
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
            println("substring:$substring")
            val cls: Class<*> = Class.forName(substring)
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
                    enum.apiChildPath = bean.apiChildPath
                }
            }
            println("枚举：${enum.name} 在集合中出现了:${enum.count} 次~")
        }
        return localEnum
    }

    /**
     * @param packagePath 当前文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param deletePackage 原来的包名，例如：com.android.hcp3.rsi.hvac.valueindications
     * @param newPackage 被替换的内容包,不包含; 例如："com.xjx.android"
     */
    @JvmStatic
    fun changePackage(
        packagePath: String,
        deletePackage: String,
        newPackage: String,
    ): Boolean {
        // println("filePath:[$filePath] deletePackage:[$deletePackage] newPackage:[$newPackage]")
        return RandomAccessFileUtil.changeFileContent(packagePath, "package $deletePackage;", "package $newPackage;")
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
            println("【Move-File】文件移动成功: oldPath:[${oldFile.path}] newFilePath:[${newFile.path}]")
            return renameTo
        } catch (e: IOException) {
            println("文件内容修改失败：$e")
        }
        return false
    }

    /**
     * @param deleteFilePath 被修改文件的路径，例如：hcp3/src/main/java/com/android/hcp3/TestFile.java
     * @param deleteImport 需要删除import的内容，例如："com.android.hcp3.rsi.hvac.VcSwitchValueEnum'
     */
    @JvmStatic
    fun deleteFileImport(
        deleteFilePath: String,
        deleteImport: String,
    ): Boolean {
        val deleteFileContent = RandomAccessFileUtil.deleteFileContent(deleteFilePath, "import $deleteImport;")
        println("【Delete-content】内容删除成功: 文件是:[$deleteFilePath] 内容是:[$deleteImport]")
        return deleteFileContent
    }

    @JvmStatic
    fun refreshFolder(folderPath: String) {
        try {
            // 使用Files.walk遍历文件夹及其子文件夹
            Files
                .walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .forEach { childPath ->
                    // 仅筛选出普通文件
                    println("当前刷新的文件是：[$childPath]")
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun flushFolder(folder: File) {
        val files = folder.listFiles()
        if (files != null) {
            for (file: File in files) {
                if (file.isDirectory) {
                    flushFolder(file) // 递归刷新子目录
                } else {
                    // 可以在这里添加代码来处理文件，例如输出文件信息
                    println("刷新的文件是：[${file.name}] 文件是否存在：[${file.exists()}] length:[${file.length()}]")
                }
            }
        }
    }

    @JvmStatic
    fun flushFolder(path: String) {
        flushFolder(File(path))
    }

    @JvmStatic
    fun checkFolder(path: String): Boolean {
        var isExists = false
        val files = File(path).listFiles()
        if (files != null) {
            println("[$path]文件夹下列表不为空...")
            Files
                .walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .forEach { file ->
                    val exists = file.exists()
                    if (exists) {
                        // println("file:[${file.name}] 存在！")
                        isExists = true
                    } else {
                        println("file:[${file.name}] 不存在！")
                        isExists = false
                        return@forEach
                    }
                }
        } else {
            println("files列表为空！")
        }
        return isExists
    }
}
