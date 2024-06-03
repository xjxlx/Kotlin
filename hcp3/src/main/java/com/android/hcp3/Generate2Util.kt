package com.android.hcp3

import com.android.hcp3.ClassTypeEnum.*
import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.OBJECT_SUFFIX
import com.android.hcp3.Config.RSI_NODE_NAME
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.ReadJarFile.IGNORE_ARRAY
import com.android.hcp3.ReadJarFile.getEnums
import com.android.hcp3.ReadJarFile.getMethods
import com.android.hcp3.ReadJarFile.mGlobalClassLoad
import com.android.hcp3.ReadJarFile.readClass
import com.android.hcp3.StringUtil.deleteFileFormat
import com.android.hcp3.StringUtil.getPackageSimple
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.bean.AttributeBean
import com.android.hcp3.bean.ObjectBean
import com.squareup.javapoet.*
import java.io.File
import java.nio.file.Paths
import javax.lang.model.element.Modifier

object Generate2Util {
    private val ANNOTATION_NONNULL = ClassName.get("androidx.annotation", "NonNull")
    private val ANNOTATION_NULLABLE = ClassName.get("androidx.annotation", "Nullable")
    private val ANNOTATION_OVERRIDE = ClassName.get("java.lang", "Override")

    private val SUPER_CLASS_BASE_RSI_VALUE =
        ClassName.get("technology.cariad.vehiclecontrolmanager.rsi", "BaseRSIValue")
    private val SUPER_CLASS_BASE_RSI_SERVICE =
        ClassName.get("technology.cariad.vehiclecontrolmanager.rsi", "BaseRSIService")

    private val PARAMETER_I_RSI_ADMIN = ClassName.get("de.esolutions.fw.android.rsi.client.rx", "IRsiAdmin")
    private val PARAMETER_VALUE_CALL_BACK =
        ClassName.get("technology.cariad.vehiclecontrolmanager.rsi", "ValueCallback")

    private val JAVA_COLLECTORS: ClassName = ClassName.get("java.util.stream", "Collectors")
    private val JAVA_LIST = ClassName.get("java.util", "List")
    private val JAVA__STRING = ClassName.get("java.lang", "String")

    // 用来存储本地已经生成的文件的集合
    private val LOCAL_NODE_FILE_LIST = LinkedHashSet<AttributeBean>()

    /**
     * 本地指定位置的文件夹，用于每次写入的时候，遍历本地文件夹下当前的文件，然后存储到[LOCAL_NODE_FILE_LIST]集合中去，用于后续的查找使用
     */
    private val Local_Folder_Path =
        Paths.get(BASE_OUT_PUT_PATH)
            .resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
            .resolve(Paths.get(RSI_NODE_NAME))
            .toString()

    private const val DEBUG = false

    /**
     * @param jarObjectPackage 生成对象在Jar中的包名，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @param jarMethodSet 生成代码里面需要写入的属性集合
     * @param localApiPackage 生成文件的包名，这里指的是当前model的相对路径，不包含[BASE_OUT_PUT_PATH]
     */
    @JvmStatic
    fun generateObject(
        jarObjectPackage: String,
        jarMethodSet: LinkedHashSet<ObjectBean>,
        localApiPackage: String,
    ): AttributeBean {
        // <editor-fold desc="一：构建类对象">
        println("开始生成Object类：[$jarObjectPackage] ------>")
        val parameterInfo = getPackageInfo(jarObjectPackage)
        val realFileName = getFileName(jarObjectPackage, OBJECT)

        // 构建类的build对象，用于组装类中的数据
        val classTypeBuild =
            TypeSpec.classBuilder(realFileName)
                .addAnnotations(getAddAnnotations())
                .superclass(SUPER_CLASS_BASE_RSI_VALUE)
                .addModifiers(Modifier.PUBLIC)
        // </editor-fold>

        // <editor-fold desc="二：构建方法对象">
        // 2.1：构造方法的参数类型
        val methodPackage = parameterInfo[0]
        println("Object类的名字为：[$realFileName] 构造类参数的路径为：[$methodPackage]")
        val methodParameterClassType = ClassName.get(methodPackage, parameterInfo[1])

        // 2.2：方法的参数
        val methodParameter =
            ParameterSpec.builder(methodParameterClassType, "object")
                .addAnnotation(ANNOTATION_NONNULL) // 设置方法的注解
                .build()

        // 2.3:组装方法的修饰符和参数
        val methodSpecBuild =
            MethodSpec.constructorBuilder() // 标注是构造犯法
                .addModifiers(Modifier.PUBLIC) // 方法的修饰符
                .addParameter(methodParameter) // 方法的参数
                .addStatement("super(object)") // 调用父类构造函数

        // 2.4：构造code build,用于循环添加内容到方法体内
        val codeBuild = CodeBlock.builder()
        // </editor-fold>

        // <editor-fold desc="三：循环添加属性和方法内容">
        val iterator = jarMethodSet.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            // 3.1：获取ben中的信息
            val attributeName = next.attributeName // 具体的属性名字
            val methodName = next.methodName // 方法名字
            val genericPackage = next.genericPackage // 返回值的包名
            val genericType = next.classType // 参数的具体数据类型,也就是泛型的类型

            // 3.2：根据返回属性的全路径包名和属性的类型，去获取构建属性和方法内容的type
            val attributeTypeBean = checkChildRunType(genericPackage, genericType)
            // println("attributeName:[$attributeName] attributeTypeBean:$attributeTypeBean")
            val attributeInfo = getPackageInfo(genericPackage, genericType)

            // 构建属性对象
            var fieldTypeName: TypeName? = null
            when (genericType.name) {
                PRIMITIVE.name -> { // 基础数据类型的数据，使用原始的数据
                    fieldTypeName = ClassName.get(attributeInfo[0], attributeInfo[1])
                    codeBuild.addStatement("this.$attributeName = object.$methodName().orElse(null)")
                }

                OBJECT.name -> { // object类型的数据
                    attributeTypeBean?.let { att ->
                        fieldTypeName = ClassName.get(transitionPackage(att.attributePackage), att.name)
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${att.name}::new).orElse(null)")
                    }
                }

                ENUM.name -> { // object类型的数据
                    attributeTypeBean?.let { att ->
                        fieldTypeName = ClassName.get(transitionPackage(att.attributePackage), att.name)
                        //     countryInformation = object.getCountryInformation().map(AirQualityEntityCountryInformationEnum::fromObjectEnum).orElse(null);
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${att.name}::fromRSI).orElse(null)")
                    }
                }

                LIST_PRIMITIVE.name -> { // 泛型式基础数据类型的list
                    fieldTypeName =
                        ParameterizedTypeName.get(
                            ClassName.get("java.util", "List"),
                            ClassName.get(attributeInfo[0], attributeInfo[1])
                        )

                    // 增加方法体内容
                    codeBuild.addStatement(
                        "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                            ".map(${attributeInfo[1]}::new).collect(\$T.toList())).orElse(null)",
                        JAVA_COLLECTORS
                    )
                }

                LIST_OBJECT.name -> { // 泛型是object类型的list
                    attributeTypeBean?.let { att ->
                        fieldTypeName =
                            ParameterizedTypeName.get(
                                ClassName.get("java.util", "List"),
                                ClassName.get(transitionPackage(att.attributePackage), att.name)
                            )
                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${att.name}::new).collect(\$T.toList())).orElse(null)",
                            JAVA_COLLECTORS
                        )
                    }
                }

                LIST_ENUM.name -> { // 泛型是object类型的list
                    attributeTypeBean?.let { att ->
                        fieldTypeName =
                            ParameterizedTypeName.get(
                                ClassName.get("java.util", "List"),
                                ClassName.get(transitionPackage(att.attributePackage), att.name)
                            )

                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${att.name}::fromRSI).collect(\$T.toList())).orElse(null)",
                            JAVA_COLLECTORS
                        )
                    }
                }

                INVALID.name, ARRAY.name -> {
                    println("      >>> 无效类型的数据!")
                }
            }

            // 3.3：构建属性对象
            val fieldSpec =
                FieldSpec.builder(fieldTypeName, attributeName).addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .addAnnotation(AnnotationSpec.builder(ANNOTATION_NULLABLE).build()) // 添加属性修饰符

            // println("      attribute:[$attributeName]  attributeType:[$genericPackage]")
            // 把生成的属性对象添加到类中
            classTypeBuild.addField(fieldSpec.build())
            try {
            } catch (e: Exception) {
                println("写入属性异常：${e.message}")
                println(e)
            }
        }
        // </editor-fold>

        // <editor-fold desc="四：构建方法体对象">
        // 添加完成的方法内容
        classTypeBuild.addMethod(methodSpecBuild.addCode(codeBuild.build()).build())
        // </editor-fold>

        // <editor-fold desc="五：写入到类中">
        val javaFile = JavaFile.builder(localApiPackage, classTypeBuild.build()).build()
        if (DEBUG) {
            javaFile.writeTo(System.out)
        } else {
            val outPutFile = File(BASE_OUT_PUT_PATH)
            javaFile.writeTo(outPutFile)
        }
        println("\r\n【写入结束！】\r\n")
        val typeBean = AttributeBean()
        typeBean.attributePackage = localApiPackage
        typeBean.name = realFileName
        return typeBean
        // </editor-fold>
    }

    /**
     * @param objectClassPath 构造方法中参数的全路径包名
     * @param jarMethodSet 生成代码里面需要写入的属性集合
     * @param packagePath 包名的路径，这里不包含存放代码的目录
     * 动态生成代码
     */
    @JvmStatic
    fun generateEnum(
        objectClassPath: String,
        jarMethodSet: LinkedHashSet<ObjectBean>,
        packagePath: String,
    ): AttributeBean {
        // <editor-fold desc="一：构建类对象">
        println("开始生成Enum类：[$objectClassPath] ------>")
        val className = getFileName(objectClassPath, ENUM)

        // 1：构建固定属性对象
        val fieldSpec =
            FieldSpec.builder(JAVA__STRING, "value")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build()

        // 2:构造方法组装
        val methodSpecBuildConstructor =
            MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(JAVA__STRING, "object").build())
                .addCode(CodeBlock.builder().add("this.value = object;").build()).build()

        // 3:静态方法组装
        val parameterInfo = getPackageInfo(objectClassPath)
        val methodSpecBuildStatic =
            MethodSpec.methodBuilder("fromRSI")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addParameter(
                    ParameterSpec.builder(ClassName.get(parameterInfo[0], parameterInfo[1]), "enumObject")
                        .addAnnotation(ANNOTATION_NONNULL) // 设置方法的注解
                        .build()
                )
                .addCode(CodeBlock.builder().add("return valueOf(enumObject.name());").build())
                .returns(ClassName.get(packagePath, className))
                .build()

        // 3：组装类对象
        val classBuild =
            TypeSpec.enumBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addField(fieldSpec)
                .addMethod(methodSpecBuildConstructor)
                .addMethod(methodSpecBuildStatic)
        // </editor-fold>

        // <editor-fold desc="二：循环添加属性和方法内容">
        val iterator = jarMethodSet.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            // 3.1：获取ben中的信息
            val attributeName = next.attributeName // 具体的属性名字
            val methodName = next.methodName // 方法名字
            classBuild.addEnumConstant(
                attributeName,
                TypeSpec.anonymousClassBuilder("\$S", methodName).build()
            )
            try {
            } catch (e: Exception) {
                println("写入属性异常：${e.message}")
                println(e)
            }
        }
        // </editor-fold>

        // <editor-fold desc="三：写入到类中">
        val javaFile = JavaFile.builder(packagePath, classBuild.build()).build()
        if (DEBUG) {
            javaFile.writeTo(System.out)
        } else {
            val outPutFile = File(BASE_OUT_PUT_PATH)
            javaFile.writeTo(outPutFile)
        }
        println("\r\n【写入结束！】\r\n")
        val typeBean = AttributeBean()
        typeBean.attributePackage = packagePath
        typeBean.name = className
        return typeBean
        // </editor-fold>
    }

    /**
     * 类的注解
     */
    private fun getAddAnnotations(): List<AnnotationSpec> {
        val annotations = arrayListOf<AnnotationSpec>()
        val getter = AnnotationSpec.builder(ClassName.get("lombok", "Getter")).build()
        annotations.add(getter)

        val toString =
            AnnotationSpec.builder(ClassName.get("lombok", "ToString")).addMember("callSuper", "true").build()
        annotations.add(toString)

        val equalsAndHashCode =
            AnnotationSpec.builder(ClassName.get("lombok", "EqualsAndHashCode")).addMember("callSuper", "true").build()
        annotations.add(equalsAndHashCode)
        return annotations
    }

    /**
     * 每当读取到一个属性的时候，就需要判定这个类的重构类是否存在，如果不存在的话，则需要去主动生成这个类,然后返回这个类的全路径名字
     * @param genericPackage 泛型的包名
     * @param genericType 泛型的类型的文件名字
     */
    private fun checkChildRunType(
        genericPackage: String,
        genericType: ClassTypeEnum,
    ): AttributeBean? {
        /**
         * 1：基础类型的数据，直接返回对象信息
         * 2：基础数据类型的集合，直接返回对象信息
         * 3：忽略集合中的数据，直接返回对象信息
         */
        val ignore = IGNORE_ARRAY.find { ignore -> ignore.ignorePackage == genericPackage }
        if ((genericType == PRIMITIVE) || (genericType == LIST_PRIMITIVE) || (ignore != null)) {
            println("      当前属性[$genericPackage]是基础类型，不做额外处理，直接返回类的名字和包名!")
            val attributeBean = AttributeBean()
            val otherInfo = getPackageInfo(genericPackage)
            attributeBean.name = otherInfo[1]
            attributeBean.attributePackage = otherInfo[0]
            println("     文件[$genericPackage]存在，直接返回文件信息：$attributeBean")
            return attributeBean
        } else {
            // 1：获取写入文件的包名
            val writeLocalFilPackage = getWriteFilPackage(genericPackage)
            // 2：根据文件的类型去获取文件名字
            val realFileName = getFileName(genericPackage, genericType)
            // 3：读取本地指定文件夹下的所有文件，用于后续的查找
            readNodeLocalFile(Local_Folder_Path)
            val localBean = LOCAL_NODE_FILE_LIST.find { local -> local.name == realFileName }
            // 4：如果文件存在，则直接返回文件的路径
            if (localBean != null) {
                val attributeBean = AttributeBean()
                attributeBean.name = localBean.name
                attributeBean.attributePackage = localBean.attributePackage
                println("     文件[$realFileName]存在，直接返回文件信息：$attributeBean")
                return attributeBean
            } else {
                // 5:读取jar包中属性的字段
                mGlobalClassLoad?.let { classLoad ->
                    val readClass = readClass(classLoad, genericPackage)
                    if (readClass != null) {
                        val simpleName = getPackageSimple(genericPackage)
                        if (genericType == OBJECT || genericType == LIST_OBJECT) {
                            println("子对象：[$realFileName]不存在，去创建object对象！")
                            val jarMethodSet = getMethods(readClass, simpleName)
                            return generateObject(genericPackage, jarMethodSet, writeLocalFilPackage)
                        } else if (genericType == ENUM || genericType == LIST_ENUM) {
                            println("子Enum：[$realFileName]不存在，去创建Enum对象！")
                            val fieldSet = getEnums(readClass, simpleName)
                            return generateEnum(genericPackage, fieldSet, writeLocalFilPackage)
                        }
                    } else {
                        println("     读取到的class:[$genericPackage]为空，请重读取class!")
                    }
                }
            }
        }
        return null
    }

    /**
     * 从泛型的包名中，去判定是否对象在[RSI_TARGET_NODE_LIST]集合中，如果在集合中，则把包名设置为自api目录中，
     * 否则包名就设置为[RSI_NODE_NAME]目录中
     */
    private fun getWriteFilPackage(genericPackage: String): String {
        // 1：从包名中去获取属性的类名
        val simpleName = getPackageSimple(genericPackage)

        /**
         * 2：对比泛型的类型是不是以[RSI_TARGET_NODE_LIST]中的泛型名字作为开头的，这样会把所有相关的类都写入一个包中
         */
        val bean = RSI_TARGET_NODE_LIST.find { filter -> simpleName.startsWith(filter.apiObjectName) }
        // 3：如果是Api的泛型类，则写入到指定的包下，如果没有归属的话，则全部写入到父类的节点下面
        val childNodePackage = if (bean != null) bean.apiName else ""
        // 4：使用指定的路径和节点去生成指定位置的包名路径
        return lowercase(
            transitionPackage(
                Paths.get(BASE_PROJECT_PACKAGE_PATH)
                    .resolve(Paths.get(RSI_NODE_NAME))
                    .resolve(childNodePackage)
                    .toString()
            )
        )
    }

    fun getFileName(
        objectPackage: String,
        genericType: ClassTypeEnum,
    ): String {
        var realFileName = ""

        /**
         * 如果发现类的全路径地址在[RSI_TARGET_NODE_LIST]中的话，说明它是Api的ben
         */
        val apiBean = RSI_TARGET_NODE_LIST.find { filter -> filter.apiObjectPath == objectPackage }
        if (apiBean != null) {
            // 给api的bean指定固定的名字
            val apiGenericName = apiBean.apiObjectName
            if (apiGenericName.endsWith("Object")) {
                realFileName = apiGenericName.substring(0, apiGenericName.lastIndexOf("Object")) + OBJECT_SUFFIX
            }
        } else {
            // 不是rsi的bean
            val simpleName = getPackageSimple(objectPackage)
            if (genericType == OBJECT || genericType == LIST_OBJECT) {
                val ignore = IGNORE_ARRAY.find { ignore -> ignore.ignorePackage == objectPackage }
                realFileName =
                    if (ignore != null) {
                        simpleName
                    } else {
                        "${simpleName}$OBJECT_SUFFIX"
                    }
            } else if (genericType == ENUM || genericType == LIST_ENUM) {
                realFileName =
                    if (simpleName.endsWith("Enum")) {
                        "${Config.ENUM_PREFIX}$simpleName"
                    } else {
                        "${Config.ENUM_PREFIX}${simpleName}Enum"
                    }
            }
        }
        return realFileName
    }

    /**
     * 递归读取本地指定节点下的文件，把所有文件都存储到一个set集合中
     */
    @JvmStatic
    fun readNodeLocalFile(dir: String) {
        val file = File(dir)
        if (file.exists()) {
            if (file.isFile) {
                val realName = deleteFileFormat(file.name)
                val realPath =
                    transitionPackage(file.parent.substring(BASE_OUT_PUT_PATH.length + 1, file.parent.length))
                // println("开始存储数据：name:[$realName] path:[$realPath]")

                // 如果在集合中没有这个类，才去添加新的类进去，避免重复性的添加
                val find = LOCAL_NODE_FILE_LIST.find { local -> local.name == realName }
                if (find == null) {
                    val bean = AttributeBean()
                    bean.name = realName
                    bean.attributePackage = realPath
                    LOCAL_NODE_FILE_LIST.add(bean)
                }
            } else if (file.isDirectory) {
                file.listFiles()?.let { listFiles ->
                    listFiles.forEach { childFile ->
                        readNodeLocalFile(childFile.path)
                    }
                }
            }
        }
    }

    /**
     * @param parameterPackage 指定类的全路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @return 返回一个数组，第一个元素是文件的全路径包名，不包含类名，第二个元素是类名的简写名字。例如：[0] = de.esolutions.fw.rudi.viwi.service.hvac.v3
     * [1] = GeneralSettingObject
     */
    fun getPackageInfo(
        parameterPackage: String,
        classType: ClassTypeEnum = INVALID,
    ): Array<String> {
        val array = Array(2) { "" }
        try {
            val lastIndexOf = parameterPackage.lastIndexOf(".")
            array[0] = parameterPackage.substring(0, lastIndexOf)
            array[1] = parameterPackage.substring(lastIndexOf + 1)
        } catch (e: Exception) {
            println("获取path异常：classType：[$classType] path:[$parameterPackage]")
            println(e)
        }
        return array
    }
}
