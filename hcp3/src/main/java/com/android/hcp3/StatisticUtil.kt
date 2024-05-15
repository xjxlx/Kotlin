package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.OBJECT_SUFFIX
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.StringUtil.deleteFileFormat
import com.android.hcp3.StringUtil.getFileNameForPath
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.EnumBean
import com.android.hcp3.bean.StatisticBean
import java.io.File
import java.lang.reflect.ParameterizedType
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object StatisticUtil {
    @JvmStatic
    fun statistic() {
        // 1:先统计指定节点下所有的folder,获取里面所有的类
        // 2:通过反射，获取类的成员变量
        // 3:把所有的成员变量存入到一个集合中，这个集合是个map集合 Map<String, Set>
        // 4:读取文件夹下所有的类，把这些类都给存一遍
        // 5:对比集合中的set集合，如果发现其中一个类，只有被这个集合给引用了，那么就把这个类给挪到这个包下
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val readNodeLocalFile =
            readApiNodeLocalFile(
                lowercase(
                    transitionPath(
                        Paths.get(BASE_OUT_PUT_PATH)
                            .resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                            .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                            .toString()
                    )
                )
            )
        println("readNodeLocalFile:$readNodeLocalFile")

        val readApiNodeLocalFile = readApiNodeLocalFile(readNodeLocalFile)
        println("readNodeLocalFile:$readApiNodeLocalFile")

        val genericType = getGenericType(readApiNodeLocalFile)
        println("genericType:$genericType")
        genericType.forEach { bean ->
            val objectGenericSet = bean.objectGenericSet
            println("apiChildPath:[${bean.apiNodePath}] genericSet:[${objectGenericSet.size}]")
        }
    }

    /**
     * 根据指定的主目录，获取父类节点下的api路径
     */
    fun readApiNodeLocalFile(dir: String): LinkedHashSet<StatisticBean> {
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
     * 获取api节点下的信息
     */
    fun readApiNodeLocalFile(set: LinkedHashSet<StatisticBean>): LinkedHashSet<StatisticBean> {
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
    fun getGenericType(set: LinkedHashSet<StatisticBean>): LinkedHashSet<StatisticBean> {
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
    fun readLocalEnumFile(dir: String): LinkedHashSet<EnumBean> {
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
     * 循环对比两个集合，如果本地的enum只出现在一个类中，则打印这个类
     */
    fun filter(
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

            println("enum: $enum")
        }
        return localEnum
    }

    fun moveFile(localEnum: LinkedHashSet<EnumBean>) {
        localEnum.forEach { enum ->
            if (enum.count <= 1) {
//                val file = File(enum.path)
                val realName = getFileNameForPath(enum.path)
                val parentFile = File(enum.parentPath, "$realName.java")
//                val renameTo = file.renameTo(parentFile)
//                println("文件：${enum.path} 移动成功：$renameTo ")

                // 目标文件路径
                val targetPat1h = Paths.get(enum.path)
                val targetPath = Paths.get(parentFile.path)

                // 移动文件
                Files.move(targetPat1h, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}
