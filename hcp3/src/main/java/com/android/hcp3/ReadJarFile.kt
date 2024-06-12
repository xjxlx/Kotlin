package com.android.hcp3

import com.android.hcp3.Config.BASE_JAR_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.FLAG_ALL
import com.android.hcp3.Config.FLAG_LEVEL
import com.android.hcp3.Config.RSI_NODE_API_NAME
import com.android.hcp3.Config.RSI_NODE_LEVEL
import com.android.hcp3.Config.RSI_NODE_NAME
import com.android.hcp3.Config.RSI_NODE_PATH
import com.android.hcp3.Config.RSI_ROOT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.GenerateUtil.filterAttributeInterdependence
import com.android.hcp3.GenerateUtil.getPackageInfo
import com.android.hcp3.StringUtil.getPackageSimple
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.ApiNodeBean
import com.android.hcp3.bean.ObjectBean
import de.esolutions.fw.rudi.services.rsiglobal.Duration
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Paths
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.*
import java.util.jar.JarFile

object ReadJarFile {
    /**
     * 公用的classLoad加载器，会冬天初始化，不用做任何处理
     */
    var mGlobalClassLoad: URLClassLoader? = null

    /**
     * 有些指定的类需要被忽略掉，这个地方专门用来存储那些被忽略的类
     */
    val IGNORE_ARRAY: Array<ObjectBean> =
        arrayOf(
            ObjectBean(
                "de.esolutions.fw.rudi.services.rsiglobal.RGBA",
                "technology.cariad.vehiclecontrolmanager.rsi.RGBAEntity"
            )
        )

    // 自定义比较器，将带有 "$" 符号的字段排在最前面
    private var ENUM_COMPARATOR: Comparator<Field> =
        Comparator { f1: Field, f2: Field ->
            val isF1StartsWith = f1.name.startsWith("_")
            val isF2StartsWith = f2.name.startsWith("_")
            if ((isF1StartsWith) && (!isF2StartsWith)) {
                return@Comparator -1 // 将以_开头的字符串排在前面
            } else if ((!isF1StartsWith) && (isF2StartsWith)) {
                return@Comparator 1 // 将不以_开头的字符串排在后面
            } else {
                return@Comparator f1.name.compareTo(f2.name); // 对其他字符串按字母顺序排序
            }
        }

    @JvmStatic
    fun main(args: Array<String>) {
        execute()
    }

    private fun filterLevel() {
        if (FLAG_LEVEL) {
            println("开始过滤JAR包中的Level--->")
            val levelMap = mutableMapOf<String, String>()
            val filterNodePath: String =
                transitionPath(
                    Paths
                        .get(RSI_ROOT_NODE_PATH)
                        .resolve(Paths.get(RSI_NODE_NAME))
                        .toString()
                )
            val jarName = "${BASE_JAR_PATH}mib_rsi_android.jar"
            try {
                // 打开Jar文件
                val jarFile = JarFile(jarName)
                // 获取Jar包中的所有文件和目录条目
                val entries = jarFile.entries()
                while (entries.hasMoreElements()) {
                    val entryName = entries.nextElement().name
                    // 收集指定路径下的所有文件名称
                    if (entryName.startsWith(filterNodePath)) {
                        // 删除掉文件的格式
                        val deleteFileFormat = StringUtil.deleteFileFormat(entryName)
                        if ((!deleteFileFormat.contains("$")) && (deleteFileFormat.endsWith("Object"))) {
                            val file = File(deleteFileFormat)
                            val parentFile = file.parentFile
                            if (parentFile.path == filterNodePath) {
                                levelMap[parentFile.path] = file.path
                                continue
                            }
                            val parentFile1 = parentFile.parentFile
                            if (parentFile1.path == filterNodePath) {
                                levelMap[parentFile.path] = parentFile1.path
                                continue
                            }
                            val parentFile2 = parentFile1.parentFile
                            if (parentFile2.path == filterNodePath) {
                                levelMap[parentFile1.path] = parentFile2.path
                                continue
                            }
                        }
                    }
                }
                println("levelMap:$levelMap")
                val keys = levelMap.keys
                val keyMap =
                    keys.map { key ->
                        val split = key.split(filterNodePath + File.separator)
                        if (split.size > 1) {
                            split[1]
                        } else {
                            ""
                        }
                    }
                println("keyMap:$keyMap")
                val last = keyMap.last()
                RSI_NODE_LEVEL = last
                println("筛选出来的Level:[${RSI_NODE_LEVEL}]")
            } catch (e: IOException) {
                e.printStackTrace()
                println("读取指定列表中Object列表失败：" + e.message)
            }
        }
    }

    /**
     * @return 读取指定JAR包中,指定节点下所有的object和Enum的文件，返回一个读取到的文件相对路径列表，用于加载到classLoad里面，避免
     * 反射class类中有依赖其他类的情况。注意，这里读取的是路径，不是包名
     * 例如：读取mib_rsi_android.jar包中hvac节点下所有的object和enum的类，返回集合路径
     */
    fun readNeedDependenciesClassName(): List<String> {
        val fileNames: MutableList<String> = ArrayList()
        // 1：读取JAB包中指定节点下文件,例如：de/esolutions/fw/rudi/viwi/service/headupdisplay/v4
        val filterNodePackage: String =
            transitionPackage(
                Paths
                    .get(RSI_ROOT_NODE_PATH)
                    .resolve(Paths.get(RSI_NODE_NAME))
                    .resolve(Paths.get(RSI_NODE_LEVEL))
                    .toString()
            )
        println("过滤JAR包中的父节点为：[$filterNodePackage]")
        // 2：读取指定jar包中的文件
        val jarName = "${BASE_JAR_PATH}mib_rsi_android.jar"
        println("读取JAR的名字为：[$jarName]")

        try {
            // 打开Jar文件
            val jarFile = JarFile(jarName)
            // 获取Jar包中的所有文件和目录条目
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entryName = entries.nextElement().name
                // 收集指定路径下的所有文件名称
                if (entryName.startsWith(filterNodePackage)) {
                    // 删除掉文件的格式
                    val deleteFileFormat = StringUtil.deleteFileFormat(entryName)
                    // 把包名给全部转换成小写，进行比对
                    val lowercaseFileName = lowercase(deleteFileFormat)

                    /**
                     * 文件读取规则：
                     * 1：只读取object结尾的文件
                     * 2：只读取Enum结尾的文件
                     * 3：只读取指定[RSI_NODE_NAME]包名结尾的文件
                     */
                    val nodeName = lowercase(RSI_NODE_NAME)
                    val isApiFile = lowercaseFileName.endsWith(nodeName)
                    if (((lowercaseFileName.endsWith("object")) || (lowercaseFileName.endsWith("enum"))) || (isApiFile)) {
                        // 如果是以父类节点结束的，则保存这个节点的全属性包名
                        if (lowercaseFileName.endsWith(nodeName)) {
                            println("当前父类节点下主类: [$deleteFileFormat]")
                            RSI_NODE_PATH = deleteFileFormat
                        }
                        fileNames.add(transitionPackage(deleteFileFormat))
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
                val declaredMethods = clazz.declaredMethods
                // 按照方法名进行排序,避免随机读取到顺序混乱
                Arrays.sort(declaredMethods, Comparator.comparing(Method::getName))
                for (method in declaredMethods) {
                    val methodName = method.name
                    var attributeName: String
                    // 1： 必须是以get开头的方法
                    if (methodName.startsWith("get")) {
                        val bean = ObjectBean()
                        // 2：过滤掉桥接方法和合成方法
                        if (!method.isBridge && !method.isSynthetic) {
                            // 3:去掉get并转换首字母为小写
                            val splitGetName =
                                methodName.split("get".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
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
                val declaredFields = clazz.getDeclaredFields()
                // 按照自定义比较器进行排序
                Arrays.sort(declaredFields, ENUM_COMPARATOR.thenComparing(Field::getName))
                declaredFields.forEach { field ->
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
    fun getFields(
        clazz: Class<*>?,
        tag: String,
    ): LinkedHashSet<ObjectBean> {
        println("开始读取[$tag]的所有属性---->")
        val set = LinkedHashSet<ObjectBean>()
        try {
            if (clazz != null) {
                // 将数组转换为集合
                val declaredFields = clazz.getDeclaredFields()
                // 按照字段名进行排序
                Arrays.sort(declaredFields, Comparator.comparing(Field::getName))
                declaredFields.forEach { field ->
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
    fun isPrimitiveOrWrapper(clazz: Class<*>): Boolean =
        clazz.isPrimitive ||
            clazz == Int::class.javaObjectType ||
            clazz == Double::class.javaObjectType ||
            clazz == Boolean::class.javaObjectType ||
            clazz == Char::class.javaObjectType ||
            clazz == Byte::class.javaObjectType ||
            clazz == Short::class.javaObjectType ||
            clazz == Long::class.javaObjectType ||
            clazz == Float::class.javaObjectType ||
            clazz == String::class.javaObjectType ||
            clazz == OffsetTime::class.javaObjectType ||
            clazz == LocalDate::class.javaObjectType ||
            clazz.name == Duration::class.javaObjectType.name ||
            (clazz.name == OffsetDateTime::class.javaObjectType.name) ||
            (clazz.name == URI::class.javaObjectType.name)

    /** 读取大项中节点的Api信息  */
    fun readApiNodeForParent(globalClassLoad: URLClassLoader) {
        // println("读取主类[$RSI_PARENT_NODE_PATH]下所有的Api信息 --->")
        // 使用类加载器，读取父类中主节点的接口变量
        if (RSI_NODE_PATH.isNotEmpty()) {
            try {
                val parentNodeClass = globalClassLoad.loadClass(transitionPackage(RSI_NODE_PATH))
                // 获取类的所有的api方法
                for (apiMethod in parentNodeClass.declaredMethods) {
                    val apiMethodName = apiMethod.name
                    if (apiMethodName != "getResources") {
                        //  println("apiMethodName:$apiMethodName")
                        val apiRunTypePath = apiMethod.returnType.name // 父类中Api方法的返回类型，这里是全路径的包名
                        // 根据api方法的返回类型的path，去反射获取的Api对象的class对象
                        val apiClass = readClass(globalClassLoad, apiRunTypePath)
                        if (apiClass != null) {
                            val apiNodeBean = ApiNodeBean()
                            apiNodeBean.apiName = apiMethodName // 父类中api方法的名字
                            apiNodeBean.apiPath = apiRunTypePath // 父类中api返回类型的全路径包名

                            /**
                             * 1：过滤所有的方法
                             * 1.1：方法不能是default类型的
                             * 1.2：方法不能是桥接方法和合成方法
                             * 1.3：方法的返回类型的泛型必须是class类型
                             * 1.4：方法的返回值类型不能是URI类型的
                             */
                            val uriName = URI::class.java.typeName
                            val methods =
                                apiClass.declaredMethods
                                    .filter { method ->
                                        (!method.isDefault) &&
                                            (!method.isBridge) &&
                                            (!method.isSynthetic) &&
                                            (method.genericReturnType is ParameterizedType) &&
                                            ((method.genericReturnType as ParameterizedType).actualTypeArguments[0] is Class<*>) &&
                                            (
                                                (((method.genericReturnType as ParameterizedType).actualTypeArguments[0]).typeName) !=
                                                    uriName
                                            )
                                    }.toMutableList()
                            // <editor-fold desc="2：获取update的方法信息"
                            // 2.1：查找update的method
                            val updateMethod =
                                methods.find { find ->
                                    (find.genericReturnType is ParameterizedType) &&
                                        (
                                            (find.genericReturnType as ParameterizedType).actualTypeArguments[0] ==
                                                URI::class.javaObjectType
                                        )
                                }
                            // 2.2：获取update的参数，并在使用完的时候删除它
                            updateMethod?.let { updateFun ->
                                updateFun.parameterTypes
                                    .find { updateType ->
                                        getPackageSimple(updateType.typeName).startsWith("Update")
                                    }?.let { parameter ->
                                        getPackageInfo(parameter.typeName).let { updateInfo ->
                                            apiNodeBean.updateObjectPackage = updateInfo[0]
                                            apiNodeBean.updateObjectName = updateInfo[1]
                                        }
                                    }
                            }
                            // 找到update方法就去使用它，用完就从方法结合中删掉他
                            if (updateMethod != null) {
                                methods.remove(updateMethod)
                            }
                            // </editor-fold>

                            // <editor-fold desc="3：查找entity的方法信息"
                            // val entityMethod = methods.find { it.returnType.typeName == "io.reactivex.rxjava3.core.Observable" }
                            if (methods.size > 1) {
                                throw IllegalStateException("[${apiClass.name}]--->获取方法的逻辑异常，请重新完善Api反射方法的逻辑！")
                            }
                            val entityMethod = methods[0]

                            entityMethod?.let {
                                if (entityMethod.genericReturnType is ParameterizedType) {
                                    val parameterizedType = entityMethod.genericReturnType as ParameterizedType
                                    val actualTypeArguments = parameterizedType.actualTypeArguments
                                    if (actualTypeArguments.isNotEmpty()) {
                                        val argument = actualTypeArguments[0]
                                        if (argument is Class<*>) {
                                            val typeName = argument.typeName
                                            apiNodeBean.apiObjectName = getPackageSimple(typeName) // api类中匹配方法泛型的名字
                                            apiNodeBean.apiObjectPath = typeName
                                        }
                                    }
                                }
                            }
                            // </editor-fold>
                            RSI_TARGET_NODE_LIST.add(apiNodeBean)
                        }
                    }
                }
                println("父类节点[$RSI_NODE_NAME]中Api列表：$RSI_TARGET_NODE_LIST")
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun generateCode(classLoad: URLClassLoader) {
        // 6：从读取父类中的Api对象中去匹配节点
        val filterBean =
            RSI_TARGET_NODE_LIST.find { filter -> lowercase(filter.apiName) == lowercase(RSI_NODE_API_NAME) }
        if (filterBean != null) {
            // 7：读取Jar包中指定的class类
            val jarClass =
                readClass(classLoad, filterBean.apiObjectPath, "读取JAR中的类：[${filterBean.apiObjectName}]")
            val jarSet = getMethods(jarClass, "JAR")
            // 8：本地写入的路径
            val localPackage =
                transitionPackage(
                    Paths
                        .get(BASE_PROJECT_PACKAGE_PATH)
                        .resolve(Paths.get(RSI_NODE_NAME))
                        .resolve(Paths.get(RSI_NODE_API_NAME))
                        .toString()
                )
            // 9：写入当前的Object
            filterAttributeInterdependence(filterBean.apiObjectPath, jarSet, localPackage)
        } else {
            println("从父类的Api中找不到对应的Object,请检查是节点是否有误！")
        }
    }

    fun execute() {
        try {
            // 1:过滤level
            filterLevel()
            // 2: 读取jar包中需要依赖的类名字
            val needDependenciesClassNameList = readNeedDependenciesClassName()
            // 3:通过配置需要依赖的类，去构建一个classLoad
            getGlobalClassLoad(needDependenciesClassNameList)?.let {
                mGlobalClassLoad = it
                // 4：读取父节点下所有的api方法，获取所有api的方法的名字以及返回类型的全路径包名
                readApiNodeForParent(it)
                // 5.1：遍历Node节点下所有的api，然后去生成对应的api代码
                if (FLAG_ALL) {
                    RSI_TARGET_NODE_LIST.forEach { api ->
                        RSI_NODE_API_NAME = api.apiName
                        generateCode(it)
                    }
                } else {
                    // 5.2：生成指定api的代码
                    generateCode(it)
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
