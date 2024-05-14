package com.android.hcp3

import com.android.hcp3.ClassTypeEnum.*
import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.ReadJarFile.getFields
import com.android.hcp3.ReadJarFile.getMethods
import com.android.hcp3.ReadJarFile.mGlobalClassLoad
import com.android.hcp3.ReadJarFile.readClass
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.android.hcp3.StringUtil.transitionPath
import com.android.hcp3.bean.AttributeBean
import com.android.hcp3.bean.ObjectBean
import com.squareup.javapoet.*
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import javax.lang.model.element.Modifier

object GenerateUtil {
    // private val METHOD_ANNOTATION_TYPE = ClassName.get("androidx.annotation", "NonNull")

    //    private val SUPER_CLASS_NAME = ClassName.get("technology.cariad.vehiclecontrolmanager.rsi", "BaseRSIValue")
    private val SUPER_CLASS_NAME = ClassName.get("com.android.hcp3", "BaseRSIValue")
    private val CLASSNAME_COLLECTORS: ClassName = ClassName.get("java.util.stream", "Collectors")

    private const val DEBUG = false

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val linkedSetOf = java.util.LinkedHashSet<ObjectBean>()
        val jarBean = ObjectBean()
        jarBean.attributeName = "airDistributionPresetList"
        jarBean.methodName = "getAirDistributionPresetList"
        jarBean.genericPackage =
            "java.util.List<de.esolutions.fw.rudi.viwi.service.hvac.v3.AirDistributionPresetObject>"
        jarBean.classType = LIST_OBJECT
        linkedSetOf.add(jarBean)
        generateObject(
            "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject",
            linkedSetOf,
            lowercase(
                transitionPackage(
                    Paths.get(BASE_PROJECT_PACKAGE_PATH)
                        .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                        .resolve(Paths.get(RSI_CHILD_NODE_PATH))
                        .toString()
                )
            )
        )
    }

    /**
     * @param parameterPackage 构造方法中参数的全路径包名，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @param jarMethodSet 生成代码里面需要写入的属性集合
     * @param generateFilePackage 生成文件的包名，这里不包含存放代码的目录 例如：com.android.hcp3.rsi.hvac.generalsettings
     * 动态生成代码
     */
    @JvmStatic
    fun generateObject(
        parameterPackage: String,
        jarMethodSet: LinkedHashSet<ObjectBean>,
        generateFilePackage: String,
    ): AttributeBean {
        // <editor-fold desc="一：构建类对象">
        println("开始生成Object类：[$parameterPackage] ------>")
        val fileInfo = getFileInfoForPackage(parameterPackage)
        val realFileName = fileInfo[1] + Config.OBJECT_SUFFIX

        // 构建类的build对象，用于组装类中的数据
        val classTypeBuild =
            TypeSpec.classBuilder(realFileName)
                .addAnnotations(getAddAnnotations())
                .superclass(SUPER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
        // </editor-fold>

        // <editor-fold desc="二：构建方法对象">
        // 2.1：构造方法的参数类型
        val methodPackage = fileInfo[0]
        println("Object类的名字为：[$realFileName] 构造类参数的路径为：[$methodPackage]")
        val methodParameterClassType = ClassName.get(methodPackage, fileInfo[1])

        // 2.2：方法的参数
        val methodParameter =
            ParameterSpec.builder(methodParameterClassType, "object")
                // todo 临时去掉注解
                // .addAnnotation(METHOD_ANNOTATION_TYPE) // 设置方法的注解
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
            val attributeTypeBean = checkChildRunTypeClass(genericPackage, genericType)
            println("attributeName:[$attributeName] attributeTypeBean:$attributeTypeBean")

            val fieldType = getFileInfoForPackage(genericPackage, genericType)
            // 0：默认无效的数据类型，1：基础数据类型 2：数组类型，3：List数据集合，4：其他数据类型，也就是自定义的数据类型

            // 构建属性对象
            var fieldTypeName: TypeName? = null
            when (genericType.name) {
                PRIMITIVE.name -> { // 基础数据类型的数据，使用原始的数据
                    fieldTypeName = ClassName.get(fieldType[0], fieldType[1])
                    codeBuild.addStatement("this.$attributeName = object.$methodName().orElse(null)")
                }

                OBJECT.name -> { // object类型的数据
                    attributeTypeBean?.let { att ->
                        fieldTypeName = ClassName.get(transitionPackage(att.path), att.name)
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${att.name}::new).orElse(null)")
                    }
                }

                ENUM.name -> { // object类型的数据
                    attributeTypeBean?.let { att ->
                        fieldTypeName = ClassName.get(transitionPackage(att.path), att.name)
                        //     countryInformation = object.getCountryInformation().map(AirQualityEntityCountryInformationEnum::fromObjectEnum).orElse(null);
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${att.name}::fromRSI).orElse(null)")
                    }
                }

                LIST_PRIMITIVE.name -> { // 泛型式基础数据类型的list
                    fieldTypeName =
                        ParameterizedTypeName.get(
                            ClassName.get("java.util", "List"),
                            ClassName.get(fieldType[0], fieldType[1])
                        )

                    // 增加方法体内容
                    codeBuild.addStatement(
                        "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                            ".map(${fieldType[1]}::new).collect(\$T.toList())).orElse(null)",
                        CLASSNAME_COLLECTORS
                    )
                }

                LIST_OBJECT.name -> { // 泛型是object类型的list
                    attributeTypeBean?.let { att ->
                        fieldTypeName =
                            ParameterizedTypeName.get(
                                ClassName.get("java.util", "List"),
                                ClassName.get(transitionPackage(att.path), att.name)
                            )

                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${att.name}::new).collect(\$T.toList())).orElse(null)",
                            CLASSNAME_COLLECTORS
                        )
                    }
                }

                LIST_ENUM.name -> { // 泛型是object类型的list
                    attributeTypeBean?.let { att ->
                        fieldTypeName =
                            ParameterizedTypeName.get(
                                ClassName.get("java.util", "List"),
                                ClassName.get(transitionPackage(att.path), att.name)
                            )

                        //         object.getSwitchValueConfiguration().map(list -> list.stream().map(HvacSwitchValueEnum::fromRSI).collect(Collectors.toList())).orElse(null);
                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${att.name}::fromRSI).collect(\$T.toList())).orElse(null)",
                            CLASSNAME_COLLECTORS
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
            println("      attribute:[$attributeName]  attributeType:[$genericPackage]")
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
        val javaFile = JavaFile.builder(generateFilePackage, classTypeBuild.build()).build()
        if (DEBUG) {
            javaFile.writeTo(System.out)
        } else {
            val outPutFile = File(BASE_OUT_PUT_PATH)
            javaFile.writeTo(outPutFile)
        }
        println("写入结束！\r\n\r\n")
        val typeBean = AttributeBean()
        typeBean.path = generateFilePackage
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
        val classType = getFileInfoForPackage(objectClassPath)
        val className = "Vc${classType[1]}"

        // 1：构建固定属性对象
        val fieldSpec =
            FieldSpec.builder(ClassName.get("java.lang", "String"), "value")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build()

        // 2:构造方法组装
        val methodSpecBuildConstructor =
            MethodSpec.constructorBuilder()
                .addParameter(
                    ParameterSpec.builder(ClassName.get("java.lang", "String"), "object")
                        .build()
                )
                .addCode(CodeBlock.builder().add("this.value = object;").build()).build()

        // 3:静态方法组装
        val methodSpecBuildStatic =
            MethodSpec.methodBuilder("fromRSI")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addParameter(
                    ParameterSpec.builder(ClassName.get(classType[0], classType[1]), "enumObject")
                        .build()
                )
                .addCode(CodeBlock.builder().add("return valueOf(enumObject.name());").build())
                .returns(ClassName.get(packagePath, className))
                .build()

        // 3：组装类对象
        val classTypeBuild =
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
            classTypeBuild.addEnumConstant(
                attributeName,
                TypeSpec.anonymousClassBuilder("\$S", methodName)
                    .build()
            )
            try {
            } catch (e: Exception) {
                println("写入属性异常：${e.message}")
                println(e)
            }
        }
        // </editor-fold>

        // <editor-fold desc="三：写入到类中">
        val javaFile = JavaFile.builder(packagePath, classTypeBuild.build()).build()
        if (DEBUG) {
            javaFile.writeTo(System.out)
        } else {
            val outPutFile = File(BASE_OUT_PUT_PATH)
            javaFile.writeTo(outPutFile)
        }
        println("写入结束！\r\n\r\n")
        val typeBean = AttributeBean()
        typeBean.path = packagePath
        typeBean.name = className
        return typeBean
        // </editor-fold>
    }

    /**
     * 每当读取到一个属性的时候，就需要判定这个类的重构类是否存在，如果不存在的话，则需要去主动生成这个类,然后返回这个类的全路径名字
     * @param genericPackage 泛型的包名
     * @param genericType 泛型的类型
     */
    private fun checkChildRunTypeClass(
        genericPackage: String,
        genericType: ClassTypeEnum,
    ): AttributeBean? {
        // 如果是u基础数据类型，或者基础数据类型的集合，则不参与后续的流程
        if ((genericType == PRIMITIVE) || (genericType == LIST_PRIMITIVE)) {
            println("      当前属性[$genericPackage]是基础类型，不做额外处理!")
            return null
        } else {
            // 1：从包名中去获取属性的类名
            val jarObjectName = StringUtil.getSimpleForPath(genericPackage)
            println("className: $jarObjectName")

            val bean =
                RSI_TARGET_NODE_LIST.find { filter ->
                    lowercase(filter.apiGenericName) ==
                        lowercase(
                            jarObjectName
                        )
                }

            // 2：获取package的路径,如果bean不为空，则说明可以写入到指定的包下，如果没有归属的话，则全部写入到父类的节点下面
            val childNodePackage =
                if (bean != null) {
                    bean.apiName
                } else {
                    ""
                }
            val folderPath =
                transitionPath(
                    lowercase(
                        Paths.get(BASE_OUT_PUT_PATH).resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                            .resolve(Paths.get(RSI_PARENT_NODE_PATH))
                            .resolve(childNodePackage).toString()
                    )
                )

            // 3：创建子文件夹目录
            if (bean != null) {
                // 3：检测文件夹是否存在
                if (!checkFolderExists(folderPath)) {
                    println("      包:${folderPath}不存在，需要去创建！")
                    mkdirFolder(folderPath)
                }
            }

            // 5：判断文件是否存在，如果不存在，就创建，如果存在，就返回对应的类型
            var realFileName = ""
            if (genericType == OBJECT || genericType == LIST_OBJECT) {
                realFileName = "${jarObjectName}${Config.OBJECT_SUFFIX}"
            } else if (genericType == ENUM || genericType == LIST_ENUM) {
                realFileName = "${Config.ENUM_PREFIX}$jarObjectName"
            }

            if (checkFileExists(folderPath, realFileName)) { // 如果文件存在，则直接返回文件的路径
                val attributeBean = AttributeBean()
                attributeBean.name = realFileName
                attributeBean.path = folderPath
                println("     文件[$realFileName]存在，直接返回文件信息：$attributeBean")
                return attributeBean
            } else {
                // 6:读取jar包中属性的字段
                mGlobalClassLoad?.let { classLoad ->
                    val readClass = readClass(classLoad, genericPackage)
                    if (readClass != null) {
                        val packagePath =
                            lowercase(
                                transitionPackage(
                                    Paths.get(BASE_PROJECT_PACKAGE_PATH)
                                        .resolve(Paths.get(RSI_PARENT_NODE_PATH)).resolve(
                                            childNodePackage
                                        ).toString()
                                )
                            )

                        if (genericType == OBJECT || genericType == LIST_OBJECT) {
                            println("子对象：[$realFileName]不存在，去创建object对象！")
                            val jarMethodSet = getMethods(readClass, jarObjectName)
                            return generateObject(genericPackage, jarMethodSet, packagePath)
                        } else if (genericType == ENUM || genericType == LIST_ENUM) {
                            println("子Enum：[$realFileName]不存在，去创建Enum对象！")
                            val fieldSet = getFields(readClass, jarObjectName)
                            return generateEnum(genericPackage, fieldSet, packagePath)
                        }
                    } else {
                        println("     读取到的class为空，请重读取class!")
                    }
                }
            }
        }
        return null
    }

    /**
     * @param parameterPackage 指定类的全路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @return 返回一个数组，第一个元素是文件的全路径包名，不包含类名，第二个元素是类名的简写名字。例如：[0] = de.esolutions.fw.rudi.viwi.service.hvac.v3
     * [1] = GeneralSettingObject
     */
    private fun getFileInfoForPackage(
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
     * 检测对应的文件是否存在
     */
    private fun checkFileExists(
        packagePath: String,
        className: String,
    ): Boolean {
        return File(packagePath, className).exists()
    }

    /**
     * 检测文件夹是否存在
     */
    private fun checkFolderExists(packagePath: String): Boolean {
        // println("checkPackagePath:[$packagePath]")
        return File(packagePath).exists()
    }

    /**
     * 创建文件夹
     */
    private fun mkdirFolder(packagePath: String) {
        val file = File(packagePath)
        if (!file.exists()) {
            val mkdirs = file.mkdirs()
            if (mkdirs) {
                println("folder：$packagePath 创建成功！")
            } else {
                println("folder：$packagePath 创建失败！")
            }
        } else {
            println("folder：$packagePath 文件已经存在！")
        }
    }
}
