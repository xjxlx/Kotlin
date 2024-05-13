package com.android.hcp3

import com.android.hcp3.ClassType.*
import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.ReadJarFile.getMethods
import com.android.hcp3.ReadJarFile.mGlobalClassLoad
import com.android.hcp3.ReadJarFile.readClass
import com.android.hcp3.StringUtil.lowercase
import com.android.hcp3.StringUtil.transitionPackage
import com.squareup.javapoet.*
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import javax.lang.model.element.Modifier

object GenerateUtil {
    /**
     * 方法参数的注解类型
     */
    private val METHOD_ANNOTATION_TYPE = ClassName.get("androidx.annotation", "NonNull")

    /**
     * 父类的继承类
     */
    private val SUPER_CLASS_NAME = ClassName.get("technology.cariad.vehiclecontrolmanager.rsi", "BaseRSIValue")
    private val COLLECTORS_CLASSNAME: ClassName = ClassName.get("java.util.stream", "Collectors")

    private const val DEBUG = false

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val linkedSetOf = java.util.LinkedHashSet<ObjectBean>()
        val jarBean = ObjectBean()
        jarBean.attributeName = "airDistributionPresetList"
        jarBean.methodName = "getAirDistributionPresetList"
        jarBean.genericPath = "java.util.List<de.esolutions.fw.rudi.viwi.service.hvac.v3.AirDistributionPresetObject>"
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
     * @param objectClassPath 构造方法中参数的全路径包名
     * @param jarMethodSet 生成代码里面需要写入的属性集合
     * @param packagePath 包名的路径，这里不能包含存放代码的目录
     * 动态生成代码
     */
    @JvmStatic
    fun generateObject(
        objectClassPath: String,
        jarMethodSet: LinkedHashSet<ObjectBean>,
        packagePath: String,
    ): AttributeTypeBean {
        // <editor-fold desc="一：构建类对象">
        println("开始生成Object类：[$objectClassPath] ------>")
        val classType = getTypeForPath(objectClassPath)
        val className = classType[1] + "Entity"

        // 构建类的build对象，用于组装类中的数据
        val classTypeBuild =
            TypeSpec.classBuilder(className)
                .addAnnotations(getAddAnnotations())
                .superclass(SUPER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
        // </editor-fold>

        // <editor-fold desc="二：构建方法对象">
        // 2.1：构造方法的参数类型
        val methodPackageName = classType[0]
        println("Object类的名字为：[$className] 构造类参数的路径为：[$methodPackageName]")
        val methodParameterType = ClassName.get(methodPackageName, classType[1])
        // 2.2：方法的参数
        val methodParameter =
            ParameterSpec.builder(methodParameterType, "object")
                .addAnnotation(METHOD_ANNOTATION_TYPE) // 设置方法的注解
                .build()

        // 2.3:组装方法的修饰符和参数
        val methodSpecBuild =
            MethodSpec.constructorBuilder() // 标注是构造犯法
                .addModifiers(Modifier.PROTECTED) // 方法的修饰符
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
            val genericPath = next.genericPath // 返回值的路径
            val attributeClassType = next.classType // 参数的具体数据类型

            if (attributeClassType == ENUM) {
                continue
            }

            // 3.2：根据返回属性的全路径包名和属性的类型，去获取构建属性和方法内容的type
            val attributeTypeBean = checkChildRunTypeClass(genericPath, attributeClassType)
            println("attributeName:[$attributeName] attributeTypeBean:$attributeTypeBean")

            val fieldType = getTypeForPath(genericPath, classType = attributeClassType)
            // 0：默认无效的数据类型，1：基础数据类型 2：数组类型，3：List数据集合，4：其他数据类型，也就是自定义的数据类型

            // 构建属性对象
            var fieldTypeName: TypeName? = null
            when (attributeClassType.name) {
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
                        // todo 此处的方法体和object不一样
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${att.name}::new).orElse(null)")
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
                        COLLECTORS_CLASSNAME
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
                            COLLECTORS_CLASSNAME
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
                            COLLECTORS_CLASSNAME
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
            println("      attribute:[$attributeName]  attributeType:[$genericPath]")
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
        val javaFile = JavaFile.builder(packagePath, classTypeBuild.build()).build()
        if (DEBUG) {
            javaFile.writeTo(System.out)
        } else {
            val outPutFile = File(BASE_OUT_PUT_PATH)
            javaFile.writeTo(outPutFile)
        }
        println("写入结束！")
        val typeBean = AttributeTypeBean()
        typeBean.path = packagePath
        typeBean.name = className
        return typeBean
        // </editor-fold>
    }

    /**
     * 每当读取到一个属性的时候，就需要判定这个类的重构类是否存在，如果不存在的话，则需要去主动生成这个类,然后返回这个类的全路径名字
     */
    private fun checkChildRunTypeClass(
        genericPath: String,
        attributeClassType: ClassType,
    ): AttributeTypeBean? {
        if (attributeClassType == PRIMITIVE) {
            println("      当前属性[$genericPath]是基础类型，不做额外处理!")
            return null
        } else {
            // 1：从path中获取属性的类名
            val jarObjectName = StringUtil.getSimpleForPath(genericPath)
            if (genericPath == "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObjectAirCleaningInformationEnum") {
                println("----->")
            }
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
                StringUtil.transitionPath(
                    Paths.get(BASE_OUT_PUT_PATH).resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                        .resolve(lowercase(RSI_PARENT_NODE_PATH))
                        .resolve(lowercase(childNodePackage))
                        .toString()
                )

            // 3：创建子文件夹目录
            if (bean != null) {
                // 3：检测文件夹是否存在
                val checkFolderExists = checkFolderExists(folderPath)
                // println("      folderPath:$folderPath exists:$checkFolderExists")
                if (!checkFolderExists) {
                    println("      包:${folderPath}不存在，需要去创建！")
                    mkdirFolder(folderPath)
                }
            }

            // 5：判断文件是否存在，如果不存在，就创建，如果存在，就返回对应的类型
            var realFileName = ""
            if (attributeClassType == OBJECT || attributeClassType == LIST_OBJECT) {
                realFileName = "${jarObjectName}Entity"
            } else if (attributeClassType == ENUM) {
                realFileName = "Vc$jarObjectName"
            }

            if (checkApiEntityFileExists(folderPath, realFileName)) {
                val attributeTypeBean = AttributeTypeBean()
                attributeTypeBean.name = realFileName
                attributeTypeBean.path = folderPath
                return attributeTypeBean
            } else {
                // 6:读取jar包中属性的字段
                mGlobalClassLoad?.let { classLoad ->
                    val readClass = readClass(classLoad, genericPath)
                    if (readClass != null) {
                        val jarMethodSet = getMethods(readClass, jarObjectName)
                        val packagePath =
                            lowercase(
                                transitionPackage(
                                    Paths.get(BASE_PROJECT_PACKAGE_PATH)
                                        .resolve(Paths.get(RSI_PARENT_NODE_PATH)).resolve(
                                            childNodePackage
                                        ).toString()
                                )
                            )
                        if (attributeClassType == OBJECT || attributeClassType == LIST_OBJECT) {
                            println("子对象：[$realFileName]不存在，去创建object对象！")
                            return generateObject(genericPath, jarMethodSet, packagePath)
                        } else if (attributeClassType == ENUM) {
                            println("子Enum：[$realFileName]不存在，去创建Enum对象！")
                            // todo 创建子类的Enum
                            return null
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
     * @param path 指定类的全路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @return 返回一个数据，第一个元素是指定路径中最后一个.的前半截，第二个元素是object的简写名字，例如：[0] = de.esolutions.fw.rudi.viwi.service.hvac.v3
     * [1] = GeneralSettingObject
     */
    private fun getTypeForPath(
        path: String,
        classType: ClassType = INVALID,
    ): Array<String> {
        val array = Array(2) { "" }
        try {
            val lastIndexOf = path.lastIndexOf(".")
            val packageName = path.substring(0, lastIndexOf)
            val simple = path.substring(lastIndexOf + 1)

            array[0] = packageName
            array[1] = simple
        } catch (e: Exception) {
            println("获取path异常：classType：[$classType] path:[$path]")
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
     * 检测对应的ApiEntity是否存在
     */
    private fun checkApiEntityFileExists(
        packagePath: String,
        className: String,
    ): Boolean {
        // 检测类是否存在：
        println("checkPackagePath:[$packagePath] className：[$className]")
        val file = File(packagePath, className)
        return file.exists()
    }

    /**
     * 检测文件夹是否存在
     */
    fun checkFolderExists(packagePath: String): Boolean {
        // println("checkPackagePath:[$packagePath]")
        val file = File(packagePath)
        return file.exists()
    }

    /**
     * 创建文件夹
     */
    private fun mkdirFolder(packagePath: String) {
        val file = File(packagePath)
        if (!file.exists()) {
            val mkdirs = file.mkdirs()
            if (mkdirs) {
                println("路径：$packagePath 创建成功！")
            } else {
                println("路径：$packagePath 创建失败！")
            }
        } else {
            println("路径：$packagePath 文件已经存在！")
        }
    }
}
