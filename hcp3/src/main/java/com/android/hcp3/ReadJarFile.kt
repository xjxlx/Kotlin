package com.android.hcp3

import com.android.hcp3.Config.BASE_JAR_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.OBJECT_SUFFIX
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_LEVEL
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_ROOT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.Config.TARGET_JAR_PATH
import com.android.hcp3.GenerateUtil.LOCAL_NODE_FILE_LIST
import com.android.hcp3.GenerateUtil.generateApi
import com.android.hcp3.GenerateUtil.generateObject
import com.android.hcp3.StringUtil.getPackageSimple
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.ApiNodeBean
import com.android.hcp3.bean.AttributeBean
import com.android.hcp3.bean.ObjectBean
import de.esolutions.fw.rudi.services.rsiglobal.Duration
import java.io.File
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Paths
import java.time.LocalDate
import java.time.OffsetTime
import java.util.*
import java.util.jar.JarFile

object ReadJarFile {
    /**
     *  当前指定父类节点：[Config.RSI_PARENT_NODE_PATH]中泛型的相对路径，这个是动态生成的，不要做任何的改动
     *  例如：de/esolutions/fw/rudi/viwi/service/hvac/v3/Hvac
     */
    private var apiNodeGenericPath: String = ""

    /**
     * 公用的classLoad加载器，会冬天初始化，不用做任何处理
     */
    var mGlobalClassLoad: URLClassLoader? = null

    /**
     * 有些指定的类需要被忽略掉，这个地方专门用来存储那些被忽略的类
     */
    val IGNORE_ARRAY: Array<ObjectBean> =
        arrayOf(
            ObjectBean("de.esolutions.fw.rudi.services.rsiglobal.RGBA", "com.android.hcp3.RGBAEntity")
        )

    @JvmStatic
    fun main(args: Array<String>) {
        execute()
    }

    /**
     * @param filterNodePath JAR包中指定的节点
     * @return 读取指定JAR包中,指定节点下所有的object和Enum的文件，返回一个读取到的文件相对路径列表，用于加载到classLoad里面，避免
     * 反射class类中有依赖其他类的情况。注意，这里读取的是路径，不是包名
     * 例如：读取mib_rsi_android.jar包中hvac节点下所有的object和enum的类，返回集合路径
     */
    fun readNeedDependenciesClassName(filterNodePath: String): List<String> {
        val fileNames: MutableList<String> = ArrayList()
        try {
            // 打开Jar文件
            val jarFile = JarFile(TARGET_JAR_PATH)
            // 获取Jar包中的所有文件和目录条目
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.name
                // 替换不同平台的分隔符
                val separator = filterNodePath.replace(File.separator, "/")
                // 收集指定路径下的所有文件名称
                if (entryName.startsWith(separator)) {
                    // System.out.println("entryName:" + entryName);
                    // 把包名给全部小写，进行比对
                    val parentNodeName = lowercase(RSI_PARENT_NODE_PATH)
                    if (entryName.endsWith(".class")) {
                        // String splitClassName = entryName.split(".class")[0];
                        val splitClassName =
                            entryName.split(".class".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

                        /**
                         * 文件读取规则：
                         * 1：只读取object结尾的文件
                         * 2：只读取Enum结尾的文件
                         * 3：只读取指定[RSI_PARENT_NODE_PATH]包名结尾的文件
                         */
                        val lowercase = lowercase(splitClassName)
                        val isApiFile = lowercase.endsWith(parentNodeName)
                        if (((splitClassName.endsWith("Object")) || (splitClassName.endsWith("Enum"))) || (isApiFile)) {
                            // 如果是以父类节点结束的，则保存这个节点的全属性包名
                            if (lowercase.endsWith(parentNodeName)) {
                                println("当前父类节点下主类: [$splitClassName]")
                                apiNodeGenericPath = splitClassName
                            }
                            fileNames.add(transitionPackage(splitClassName))
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

    fun getGlobalClassLoad(dependenciesList: List<String>): URLClassLoader? {
        val jarList = ArrayList<String>()
        val folder = File(BASE_JAR_PATH)
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

            // 加载指定集合里面的依赖类
            for (classPath in dependenciesList) {
                classLoader.loadClass(classPath)
            }

            // 加载特定的类
            classLoader.loadClass("de.esolutions.fw.rudi.core.locators.IResourceLocator")
            classLoader.loadClass("de.esolutions.fw.rudi.core.IPath")

            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwElement")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwPrimitive")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.FwObject")
            classLoader.loadClass("de.esolutions.fw.util.commons.genericdata.ObjectKey")
            return classLoader
        } catch (e: Exception) {
            e.printStackTrace()
            println("读取JAR包中的Class文件异常：" + e.message)
            return null
        }
    }

    /**
     * @param classLoader classLoad对象
     * @param packageName 指定的class类名，这里是全路径的类名，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.KeysApi
     * @return 2：返回指定jar包中目标class类型的Class对象
     */
    fun readClass(
        classLoader: URLClassLoader,
        packageName: String,
        tag: String = "",
    ): Class<*>? {
        try {
            if (tag.isNotEmpty()) {
                println(tag)
            }
            // 如果包名是在忽略名单中，则使用本地方法读取
            val bean = IGNORE_ARRAY.find { ignore -> ignore.ignorePackage == packageName }
            if (bean != null) {
                return readLocalClass(packageName, tag)
            } else {
                val cls = classLoader.loadClass(packageName)
                if (tag.isNotEmpty()) {
                    println("      读取JAR中class[$packageName]成功")
                }
                return cls
            }
        } catch (e: Exception) {
            println("      $e")
            return null
        }
    }

    private fun readLocalClass(
        packageName: String,
        tag: String = "",
    ): Class<*>? {
        try {
            if (tag.isNotEmpty()) {
                println(tag)
            }
            val cls = Class.forName(packageName)
            if (tag.isNotEmpty()) {
                println("      读取本地Class[$packageName]成功")
            }
            return cls
        } catch (e: Exception) {
            println("      $e")
            return null
        }
    }

    /**
     * @return 4：从class类中读取所有的方法，把方法、变量、路径组成一个集合返回出去
     */
    fun getMethods(
        clazz: Class<*>?,
        tag: String,
    ): LinkedHashSet<ObjectBean> {
        println("开始读取[$tag]的所有方法---->")
        val set = LinkedHashSet<ObjectBean>()
        try {
            if (clazz != null) {
                // 遍历指定类中的所有方法
                for (method in clazz.declaredMethods) {
                    val methodName = method.name
                    var attributeName: String
                    // 1： 必须是以get开头的方法
                    if (methodName.startsWith("get")) {
                        val bean = ObjectBean()
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
                                if (actualTypeArguments.isNotEmpty()) {
                                    val argument = actualTypeArguments[0]
                                    val genericPath = argument.typeName
                                    val classType = checkClassType(argument)
                                    // println("argument:$argument type:$classType")
                                    bean.classType = classType
                                    // 只有泛型对象是list的时候，才会添加list的泛型参数
                                    if (classType == ClassTypeEnum.LIST_OBJECT ||
                                        classType == ClassTypeEnum.LIST_PRIMITIVE ||
                                        classType == ClassTypeEnum.LIST_ENUM
                                    ) {
                                        if (argument is ParameterizedType) { // 泛型类型
                                            bean.genericPackage = argument.actualTypeArguments[0].typeName
                                        }
                                    } else {
                                        bean.genericPackage = genericPath
                                    }
                                }
                            }
                            bean.attributeName = attributeName
                            bean.methodName = methodName
                            // println("      method [$attributeName] GenericType:[$genericPath]")

                            // 遍历忽略的集合，如果发现是在忽略集合中的，则替换泛型的路径
                            IGNORE_ARRAY.forEach { ignore ->
                                if (bean.genericPackage.equals(ignore.genericPackage)) {
                                    bean.genericPackage = ignore.ignorePackage
                                }
                            }
                            set.add(bean)
                        }
                    }
                }
            } else {
                println("      ${tag}的clas为空，请检查是否正确获取了class对象！")
            }
            println("      [" + tag + "]" + "反射获取到的方法：" + set.size)
        } catch (e: Exception) {
            println("      [" + tag + "]" + "反射属性异常：" + e.message)
        }
        return set
    }

    /**
     * 获取本类中所有的成员变量，用于读取enum类中的常量
     * @return 返回enum里面的常量，attributeName = 常量的name,methodName = 常量的value
     */
    fun getEnums(
        clazz: Class<*>?,
        tag: String,
    ): LinkedHashSet<ObjectBean> {
        println("开始读取[$tag]的所有属性---->")
        val set = LinkedHashSet<ObjectBean>()
        try {
            if (clazz != null) {
                // 将数组转换为集合
                clazz.getDeclaredFields().forEach { field ->
                    if (field.isEnumConstant) {
                        val bean = ObjectBean()
                        val key = field.name
                        bean.attributeName = key
                        val get = field.get(key).toString()
                        bean.methodName = get
                        set.add(bean)
                    }
                }
            } else {
                println("      ${tag}的clas为空，请检查是否正确获取了class对象！")
            }
            println("      [" + tag + "]" + "反射获取到的属性：" + set.size)
        } catch (e: Exception) {
            println("      [" + tag + "]" + "反射属性异常：" + e.message)
        }
        return set
    }

    /**
     * 获取本类中所有的成员变量
     * @return 返回class中所有的成员属性
     */
    private fun getFields(
        clazz: Class<*>?,
        tag: String,
    ): LinkedHashSet<ObjectBean> {
        println("开始读取[$tag]的所有属性---->")
        val set = LinkedHashSet<ObjectBean>()
        try {
            if (clazz != null) {
                // 将数组转换为集合
                clazz.getDeclaredFields().forEach { field ->
                    val bean = ObjectBean()
                    bean.attributeName = field.name
                    val genericType = field.genericType
                    if (genericType is Class<*>) {
                        bean.genericPackage = genericType.typeName
                    } else if (genericType is ParameterizedType) {
                        bean.genericPackage = genericType.actualTypeArguments[0].typeName
                    }
                    set.add(bean)
                }
            } else {
                println("      ${tag}的clas为空，请检查是否正确获取了class对象！")
            }
            println("      [" + tag + "]" + "反射获取到的属性：" + set.size)
        } catch (e: Exception) {
            println("      [" + tag + "]" + "反射属性异常：" + e.message)
        }
        return set
    }

    /**
     * @param jarMethodSet jar包中的变量集合
     * @param localMethodSet 本地类中变量的集合
     * @return 检测是否需要写入属性
     */
    private fun checkNeedWriteVariable(
        jarMethodSet: LinkedHashSet<ObjectBean>,
        localMethodSet: LinkedHashSet<ObjectBean>,
    ): Boolean {
        var equals = false
        if (jarMethodSet.size != localMethodSet.size) {
            println("本地数据和jar包数据不相同，需要重新写入数据！")
            equals = false
        } else {
            println("本地数据和jar包数据相同，需要对比数据内容是否相同！")
            val localList = localMethodSet.toList()
            val iterator = jarMethodSet.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                localList.find { bean -> bean.attributeName == next.attributeName }?.let { local ->
                    equals = local.attributeName == next.attributeName
                }
                if (!equals) {
                    break
                }
            }
        }
        return equals
    }

    /**
     * @return 返回当前的class是什么数据类型
     */
    private fun checkClassType(type: Type): ClassTypeEnum {
        var typeClass: Class<*>? = null
        var listParameterType: Type? = null
        var classType: ClassTypeEnum = ClassTypeEnum.INVALID
        if (type is ParameterizedType) { // 泛型类型
            listParameterType = type.actualTypeArguments[0]
            val rawType: Type = type.rawType
            if (rawType is Class<*>) {
                typeClass = rawType
            }
        } else if (type is Class<*>) { // 不是泛型类型的参数
            typeClass = type
        }

        if (typeClass != null) {
            if (isPrimitiveOrWrapper(typeClass)) { // 基本数据类型
                classType = ClassTypeEnum.PRIMITIVE
            } else if (typeClass.isArray) { // 数组类型
                classType = ClassTypeEnum.ARRAY
            } else if (MutableList::class.java.isAssignableFrom(typeClass)) { //  List数据类型
                listParameterType?.let { parameterType ->
                    classType =
                        if (isPrimitiveOrWrapper(parameterType as Class<*>)) {
                            ClassTypeEnum.LIST_PRIMITIVE //  泛型是基础数据类型的
                        } else {
                            if (Enum::class.java.isAssignableFrom(listParameterType as Class<*>)) {
                                ClassTypeEnum.LIST_ENUM // 泛型是Enum的数据类型
                            } else {
                                ClassTypeEnum.LIST_OBJECT // 泛型是object的数据类型
                            }
                        }
                }
            } else if (Enum::class.java.isAssignableFrom(typeClass)) { // Enum类型
                classType = ClassTypeEnum.ENUM
            } else { // 其他引用数据类型，也就是自定义的object数据类型
                classType = ClassTypeEnum.OBJECT
            }
        }
        return classType
    }

    /**
     * @param clazz class的对象
     * @return 判断是否是基本数据类型，这里可以手动去添加自己需要的类型
     */
    fun isPrimitiveOrWrapper(clazz: Class<*>): Boolean {
        return clazz.isPrimitive || clazz == Int::class.javaObjectType || clazz == Double::class.javaObjectType ||
            clazz == Boolean::class.javaObjectType || clazz == Char::class.javaObjectType ||
            clazz == Byte::class.javaObjectType || clazz == Short::class.javaObjectType ||
            clazz == Long::class.javaObjectType || clazz == Float::class.javaObjectType ||
            clazz == String::class.javaObjectType || clazz == OffsetTime::class.javaObjectType ||
            clazz == LocalDate::class.javaObjectType ||
            (clazz.name == Duration::class.javaObjectType.name)
    }

    /** 读取大项中节点的Api信息  */
    fun readApiNodeForParent(globalClassLoad: URLClassLoader) {
        // println("读取主类[$RSI_PARENT_NODE_PATH]下所有的Api信息 --->")
        // 使用类加载器，读取父类中主节点的接口变量
        if (apiNodeGenericPath.isNotEmpty()) {
            try {
                val parentNodeClass = globalClassLoad.loadClass(transitionPackage(apiNodeGenericPath))
                // 获取类的所有的api方法
                for (apiMethod in parentNodeClass.declaredMethods) {
                    val apiMethodName = apiMethod.name
                    if (apiMethodName != "getResources") {
                        //  println("apiMethodName:$apiMethodName")
                        val apiRunTypePath = apiMethod.returnType.name // 父类中Api方法的返回类型，这里是全路径的包名
                        // 根据api方法的返回类型的path，去反射获取的Api对象的class对象
                        val apiClass = readClass(globalClassLoad, apiRunTypePath)
                        val tempApiMethodList = ArrayList<ApiNodeBean>()
                        if (apiClass != null) {
                            // 获取api类中所有的方法
                            for (apiChildMethod in apiClass.declaredMethods) {
                                // 过滤api中的方法
                                //    1:方法不能是default的类型
                                val default = apiChildMethod.isDefault
                                //    2:泛型的类型不能是list，必须是Class类型的
                                val isClass = apiChildMethod.returnType is Class<*>
                                if ((!default) && isClass) {
                                    val genericReturnType = apiChildMethod.genericReturnType
                                    if (genericReturnType is ParameterizedType) {
                                        val actualTypeArguments = genericReturnType.actualTypeArguments
                                        if (actualTypeArguments.isNotEmpty()) {
                                            //    3:泛型的类型不能是URI类型的
                                            val argument = actualTypeArguments[0]
                                            if (argument != URI::class.javaObjectType) {
                                                if (argument is Class<*>) {
                                                    val typeName = argument.typeName
                                                    val apiNodeBean = ApiNodeBean()
                                                    apiNodeBean.apiName = apiMethodName // 父类中api方法的名字
                                                    apiNodeBean.apiReturnTypePath =
                                                        apiRunTypePath // 父类中api返回类型的全路径包名
                                                    apiNodeBean.apiGenericName =
                                                        getPackageSimple(typeName) // api类中匹配方法泛型的名字
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
                        }
                        RSI_TARGET_NODE_LIST.addAll(tempApiMethodList)
                    }
                }
                println("父类节点[$RSI_PARENT_NODE_PATH]中Api列表：$RSI_TARGET_NODE_LIST")
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun checkApiFile(apiName: String): AttributeBean? {
        return LOCAL_NODE_FILE_LIST.find { local -> local.name == StringUtil.capitalize(apiName) }
    }

    private fun execute() {
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
                mGlobalClassLoad = it
                // 4：读取父节点下所有的api方法，获取所有api的方法的名字以及返回类型的全路径包名
                readApiNodeForParent(it)
                // 5：从读取父类中的Api对象中去匹配节点
                val filterBean =
                    RSI_TARGET_NODE_LIST.find { filter -> lowercase(filter.apiName) == lowercase(RSI_CHILD_NODE_PATH) }
                if (filterBean != null) {
                    // 6：读取Jar包中指定的class类
                    val jarClass =
                        readClass(it, filterBean.apiGenericPath, "读取JAR中的类：[${filterBean.apiGenericName}]")
                    val jarSet = getMethods(jarClass, "JAR")

                    // 7：读取本地的方法
                    val localPackage =
                        transitionPackage(
                            lowercase(
                                Paths.get(BASE_PROJECT_PACKAGE_PATH).resolve(Paths.get(RSI_PARENT_NODE_PATH))
                                    .resolve(Paths.get(RSI_CHILD_NODE_PATH)).toString()
                            )
                        )
                    val localClassName = filterBean.apiGenericName.plus(OBJECT_SUFFIX)
                    val localRealPackage = localPackage + localClassName
                    val localClass = readLocalClass(localRealPackage, "读取本地类：[$localRealPackage]")
                    // 8：读取class中的方法数量和内容
                    val localSet = getFields(localClass, "Local")
                    // 9:对比本地和jar中类的方法信息，如果不匹配则需要动态生成代码
                    if (checkNeedWriteVariable(jarSet, localSet)) {
                        println("属性完全相同，不需要重新写入属性！")
                    } else {
                        val packagePath =
                            lowercase(
                                transitionPackage(
                                    Paths.get(BASE_PROJECT_PACKAGE_PATH)
                                        .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                                        .resolve(Paths.get(RSI_CHILD_NODE_PATH))
                                        .toString()
                                )
                            )
                        // 10：写入当前的Object
                        generateObject(filterBean.apiGenericPath, jarSet, packagePath)
                    }

                    // 11：写入Api的class
                    val apiFileName = StringUtil.capitalize(filterBean.apiName)
                    val apiBean = LOCAL_NODE_FILE_LIST.find { local -> local.name == apiFileName }
                    if (apiBean != null) {
                        println("Api的主文件已经存在，不需要再次写入了！")
                    } else {
                        println("Api的主文件不存在，需要写入该文件！")
                        generateApi(localPackage, apiFileName)
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
