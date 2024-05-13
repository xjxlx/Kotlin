package com.android.hcp3

import com.android.hcp3.Config.BASE_JAR_PATH
import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_LEVEL
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_ROOT_NODE_PATH
import com.android.hcp3.Config.TARGET_JAR_PATH
import com.android.hcp3.GenerateUtil.generateEntity
import com.android.hcp3.StringUtil.capitalize
import com.android.hcp3.StringUtil.getSimpleForPath
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

object ReadJarFile {
    /**
     * 用来存储当前节点下的所有子节点名字，内容会自动生成，不要做任何的改动
     */
    private val RSI_TARGET_NODE_LIST: MutableList<ApiNodeBean> = mutableListOf()

    /**
     * 当前父类节点下的主接口全路径，这个舒心是动态生成的，不要做任何的改动，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.Hvac
     */
    private var rsiTargetNodePath: String = ""

    @JvmStatic
    fun main(args: Array<String>) {
        execute()
    }

    /**
     * @param filterNodePath
     * 过滤指定父类节点的包名，注意这里的节点类型不是包名，是反斜杠的路径，例如：de/esolutions/fw/rudi/viwi/service/hvac/v3
     * @return 1：返回指定JAR包中，指定targetPath 目录下所有object和Enum的集合的名字
     */
    private fun readNeedDependenciesClassName(filterNodePath: String): List<String> {
        val fileNames: MutableList<String> = ArrayList()
        try {
            // 打开Jar文件
            val jarFile: JarFile = JarFile(TARGET_JAR_PATH)
            // 获取Jar包中的所有文件和目录条目
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.name
                // 收集指定路径下的所有文件名称
                if (entryName.startsWith(filterNodePath)) {
                    // System.out.println("entryName:" + entryName);
                    val parentNodeName = capitalize(RSI_PARENT_NODE_PATH)
                    if (entryName.contains(".class")) {
                        val splitClassName =
                            entryName.split(".class".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[0]
                        if ((
                                (splitClassName.endsWith("Object")) ||
                                    (splitClassName.endsWith("Enum")) ||
                                    (splitClassName.endsWith(parentNodeName))
                            ) &&
                            (splitClassName.contains("/"))
                        ) {
                            // 如果是以父类节点结束的，则保存这个节点的全属性包名

                            if (splitClassName.endsWith(parentNodeName)) {
                                println("当前父类节点下主类: [$splitClassName]")
                                rsiTargetNodePath = splitClassName
                            }
                            val replaceName = splitClassName.replace("/", ".")
                            fileNames.add(replaceName)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("读取指定列表中Object列表失败：" + e.message)
        }
        return fileNames
    }

    private fun getGlobalClassLoad(dependenciesList: List<String>): URLClassLoader? {
        val jarList = ArrayList<String>()
        val folder: File = File(BASE_JAR_PATH)
        // 加载指定位置的jar包到classLoad里面
        if (folder.exists() && folder.isDirectory) {
            // 获取文件夹下的所有文件
            val files = folder.listFiles()
            if (files != null) {
                // 遍历文件数组，输出文件名
                for (file in files) {
                    val jarName = file.name
                    if (jarName.endsWith(".jar")) {
                        // System.out.println("      JAR-Name: " + file.getName());
                        jarList.add(file.name)
                    }
                }
            }
        } else {
            println("读取目标Jar文件夹中的JAR异常！")
        }
        // 构造classLoad并加载依赖的类
        try {
            val urls = arrayOfNulls<URL>(jarList.size)
            for (i in jarList.indices) {
                urls[i] = File(BASE_JAR_PATH + jarList[i]).toURI().toURL()
            }

            // 创建URLClassLoader来加载依赖的JAR包
            val classLoader = URLClassLoader(urls, null)
            // 加载需要的类
            classLoader.loadClass("de.esolutions.fw.rudi.core.locators.IResourceLocator")
            classLoader.loadClass("de.esolutions.fw.rudi.core.IPath")

            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwElement")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwPrimitive")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwObject")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.ObjectKey")

            // classLoader.loadClass("io.reactivex.rxjava3.core.Single");

            // 加载指定集合
            for (classPath in dependenciesList) {
                classLoader.loadClass(classPath)
            }
            return classLoader
        } catch (e: Exception) {
            e.printStackTrace()
            println("读取JAR包中的Class文件异常：" + e.message)
            return null
        }
    }

    /**
     * @param classLoader classLoad对象
     * @param className 指定的class类名，这里是全路径的类名
     * @return 2：返回指定jar包中目标class类型的Class对象
     */
    private fun readClass(
        classLoader: URLClassLoader,
        className: String?,
        tag: String = "",
    ): Class<*>? {
        try {
            if (tag.isNotEmpty()) {
                println(tag)
            }
            val cls = classLoader.loadClass(className)
            if (tag.isNotEmpty()) {
                println("$tag[success]")
            }
            return cls
        } catch (e: Exception) {
            println("      $e")
            return null
        }
    }

    /**
     * @return 4：返回指定class的方法以及方法的泛型
     */
    private fun getMethods(
        clazz: Class<*>?,
        tag: String,
    ): LinkedHashSet<ObjectEntity> {
        println("开始读取${tag}的所有方法---->")
        val set = LinkedHashSet<ObjectEntity>()
        try {
            if (clazz != null) {
                // 将数组转换为集合
                val methods: Set<Method> = HashSet(Arrays.asList(*clazz.declaredMethods))

                for (method in methods) {
                    val methodName = method.name
                    var attributeName: String
                    var genericPath = ""
                    // 1： 必须是以get开头的方法
                    if (methodName.startsWith("get")) {
                        val bean = ObjectEntity()

                        // 2：过滤掉桥接方法和合成方法
                        if (!method.isBridge && !method.isSynthetic) {
                            // 3:去掉get并转换首字母为小写
                            val splitGetName =
                                methodName.split("get".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1]
                            attributeName =
                                splitGetName.substring(0, 1).lowercase(Locale.getDefault()) + splitGetName.substring(1)

                            // 4:获取方法的返回类型
                            val returnType = method.genericReturnType
                            // 5:返回泛型的类型
                            if (returnType is ParameterizedType) {
                                val actualTypeArguments = returnType.actualTypeArguments
                                if (actualTypeArguments.size > 0) {
                                    val argument = actualTypeArguments[0]
                                    genericPath = argument.typeName
                                    bean.classType = checkClassType(argument)
                                }
                            }

                            bean.attributeName = attributeName
                            bean.methodName = methodName
                            bean.genericPath = genericPath

                            println(
                                "      method [$attributeName] GenericType:[$genericPath]"
                            )
                            set.add(bean)
                        }
                    }
                }
            } else {
                println("      ${tag}的clas为空！")
            }
            println("[" + tag + "]" + "反射获取到的属性：" + set.size)
        } catch (e: Exception) {
            println("[" + tag + "]" + "反射属性异常：" + e.message)
        }
        return set
    }

    /**
     * @param jarMethodSet jar包中的变量集合
     * @param targetMethodSet 本地类中变量的集合
     * @return 检测是否需要写入属性
     */
    private fun checkNeedWriteVariable(
        jarMethodSet: LinkedHashSet<ObjectEntity>,
        targetMethodSet: LinkedHashSet<ObjectEntity>,
    ): Boolean {
        if (jarMethodSet.size != targetMethodSet.size) {
            println("本地数据和jar包数据不相同，需要重新写入数据！")
            return false
        } else {
            println("本地数据和jar包数据相同，需要对比数据内容是否相同！")
            return jarMethodSet == targetMethodSet
        }
    }

    /**
     * @return 返回当前的class是什么数据类型 0：默认无效的数据类型，1：基础数据类型，例如，Float、Boolean、Integer *
     * 2：数组类型，3：List数据集合，4：自定义Object数据类型
     */
    private fun checkClassType(type: Type): Int {
        var classType = 0
        var typeClass: Class<*>? = null
        if (type is ParameterizedType) { // 泛型类型
            val rawType: Type = type.rawType
            if (rawType is Class<*>) {
                typeClass = rawType
            }
        } else if (type is Class<*>) { // 不是泛型类型的参数
            typeClass = type
        }

        if (typeClass != null) {
            classType =
                if (isPrimitiveOrWrapper(typeClass)) { // 基本数据类型
                    1
                    // System.out.println("基本数据类型");
                } else if (typeClass.isArray) { // 数组类型
                    2
                    // System.out.println("数组类型");
                } else if (MutableList::class.java.isAssignableFrom(typeClass)) { //  List数据类型
                    3
                    // System.out.println("List数据类型");
                } else { // 其他引用数据类型，也就是自定义的object数据类型
                    4
                    // System.out.println("自定义Object数据类型");
                }
        }
        return classType
    }

    /**
     * @param clazz class的对象
     * @return 判断是否是基本数据类型
     */
    private fun isPrimitiveOrWrapper(clazz: Class<*>): Boolean {
        return clazz.isPrimitive || clazz == Int::class.java || clazz == Double::class.java ||
            clazz == Boolean::class.java || clazz == Char::class.java || clazz == Byte::class.java ||
            clazz == Short::class.java || clazz == Long::class.java || clazz == Float::class.java
    }

    /** 读取大项中节点的Api信息  */
    private fun readApiNodeForParent(globalClassLoad: URLClassLoader) {
        println("读取主类[$RSI_PARENT_NODE_PATH]下所有的Api信息 --->")
        // 使用类加载器，读取父类中主节点的接口变量
        if (rsiTargetNodePath.isNotEmpty()) {
            try {
                val parentNodeClass = globalClassLoad.loadClass(transitionPackage(rsiTargetNodePath))
                // 获取类的所有的api方法
                for (apiMethod in parentNodeClass.declaredMethods) {
                    val apiMethodName = apiMethod.name
                    if (apiMethodName != "getResources") {
                        val apiRunTypePath = apiMethod.returnType.name // 父类中Api方法的返回类型，这里是全路径的包名
                        // 根据api方法的返回类型的path，去反射获取的Api对象的class对象
                        val apiClass = readClass(globalClassLoad, apiRunTypePath)
                        val tempApiMethodList = ArrayList<ApiNodeBean>()
                        if (apiClass != null) {
                            // 获取api类中所有的方法
                            for (apiChildMethod in apiClass.declaredMethods) {
                                // 过滤api中的方法
                                //    1:返回值类型必须是io.reactivex.rxjava3.core.Observable开头的方法
                                //    2:方法不能是default的类型
                                //    3:泛型的参数不能是list，必须是Class类型的
                                if ((apiChildMethod.returnType.name.equals("io.reactivex.rxjava3.core.Observable")) &&
                                    (!apiChildMethod.isDefault)
                                ) {
                                    val apiChildGenericReturnType = apiChildMethod.genericReturnType
                                    if (apiChildGenericReturnType is ParameterizedType) {
                                        val actualTypeArguments = apiChildGenericReturnType.actualTypeArguments
                                        if (actualTypeArguments.isNotEmpty()) {
                                            val argument = actualTypeArguments[0]
                                            if (argument is Class<*>) {
                                                val typeName = argument.typeName
                                                //   println(
                                                //       "      Api节点[$apiMethodName]" +
                                                //        "\r\n          方法类型是：[$apiChildGenericReturnType]" +
                                                //        "\r\n          泛型的类型是:[$typeName]"
                                                //   )
                                                val apiNodeBean = ApiNodeBean()
                                                apiNodeBean.apiName = apiMethodName // 父类中api方法的名字
                                                apiNodeBean.apiReturnTypePath = apiRunTypePath // 父类中api返回类型的全路径包名
                                                apiNodeBean.apiGenericName =
                                                    getSimpleForPath(typeName) // api类中匹配方法泛型的名字
                                                apiNodeBean.apiGenericPath = typeName

                                                tempApiMethodList.add(apiNodeBean)
                                                if (tempApiMethodList.size > 1) {
                                                    throw IllegalStateException("当前节点[$apiMethodName]匹配到的方法过多，请重新检查匹配方法的规则！")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        RSI_TARGET_NODE_LIST.addAll(tempApiMethodList)
                    }
                }
                println("\r\n父类节点[$RSI_PARENT_NODE_PATH]中Api列表：$RSI_TARGET_NODE_LIST\r\n")
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    fun execute() {
        try {
            // 1：读取指定目标节点下所有的object集合,例如：de/esolutions/fw/rudi/viwi/service/hvac/v3
            val filterNodePath: String =
                Paths.get(RSI_ROOT_NODE_PATH)
                    .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                    .resolve(Paths.get(RSI_PARENT_NODE_LEVEL))
                    .toString()
                    .replace(".", "/")
            println("过滤JAR包中的父节点为：[$filterNodePath]")
            // 2: 读取jar包中需要依赖的类名字
            val needDependenciesClassNameList = readNeedDependenciesClassName(filterNodePath)
            // 3:通过配置需要依赖的类，去构建一个classLoad
            val globalClassLoad = getGlobalClassLoad(needDependenciesClassNameList)
            globalClassLoad?.let {
                // 4：读取父节点下所有的api方法，获取所有api的方法的名字以及返回类型的全路径包名
                readApiNodeForParent(it)

                // 6：从读取父类中的Api对象中去匹配节点
                val filterBean =
                    RSI_TARGET_NODE_LIST.find { filter -> lowercase(filter.apiName) == lowercase(RSI_CHILD_NODE_PATH) }
                if (filterBean != null) {
                    // 5：读取Jar包中指定的class类 apiGenericPath
                    val jarClass =
                        readClass(it, filterBean.apiGenericPath, "读取JAR中的类：${filterBean.apiGenericName}")

                    // todo 此处的拼写规则可以自由指定，例如：要不要类后面的s
                    val className = capitalize(filterBean.apiName).plus("Entity")
                    val path =
                        transitionPackage(
                            Paths.get(BASE_OUT_PUT_PATH).resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                                .resolve(Paths.get(RSI_PARENT_NODE_PATH)).resolve(Paths.get(RSI_CHILD_NODE_PATH))
                                .resolve(className)
                                .toString()
                        )
                    val targetClass = readClass(it, path, "读取本地类：$path")
                    val jarSet = getMethods(jarClass, "JAR")
                    val localSet = getMethods(targetClass, "Local")

                    val needWriteVariable = checkNeedWriteVariable(jarSet, localSet)
                    if (needWriteVariable) {
                        println("属性完全相同，不需要重新写入属性！")
                    } else {
                        generateEntity(jarSet)
                    }
                } else {
                    println("从父类的Api中找不到对应的Object,请检查是节点是否有误！")
                }
                // 关闭ClassLoader释放资源
                it.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("write data error!")
        }
    }
}
