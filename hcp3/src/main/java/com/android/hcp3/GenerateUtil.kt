package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_TARGET_NODE_LIST
import com.android.hcp3.ReadJarFile.getMethods
import com.android.hcp3.ReadJarFile.mGlobalClassLoad
import com.android.hcp3.ReadJarFile.readClass
import com.android.hcp3.rsi.hvac.AttributeTypeBean
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
    private val collectorsClassName: ClassName = ClassName.get("java.util.stream", "Collectors")

    private const val DEBUG = true

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val linkedSetOf = linkedSetOf<ObjectBean>()
        val jarBean = ObjectBean()
        jarBean.attributeName = "aC"
        jarBean.methodName = "getAC"
        jarBean.genericPath = "de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject"
        linkedSetOf.add(jarBean)
//        generateEntity(linkedSetOf)
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
        println("开始生成Object类：$objectClassPath ------>")
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
            val bean = iterator.next()
            // 3.1：获取ben中的信息
            val attributeName = bean.attributeName // 具体的属性名字
            val methodName = bean.methodName // 方法名字
            val genericPath = bean.genericPath // 返回值的路径
            val attributeClassType = bean.classType // 参数的具体数据类型

            // todo 此处暂时使用源码中返回值类型，后续需要给替换掉
            // 3.2：根据返回属性的全路径包名和属性的类型，去获取构建属性和方法内容的type
            val attributeTypeBean = checkChildRunTypeClass(genericPath, attributeClassType)
            println("attributeTypeBean:$attributeTypeBean")

            val fieldType = getTypeForPath(genericPath, attributeClassType)
            // 0：默认无效的数据类型，1：基础数据类型 2：数组类型，3：List数据集合，4：其他数据类型，也就是自定义的数据类型
            if (attributeClassType != 0) {
                // 构建属性的数据类型
                var fieldTypeName: TypeName = ClassName.get(fieldType[0], fieldType[1])
                if (attributeClassType == 3) { // List 数据类型,需要单独构造
                    fieldTypeName =
                        ParameterizedTypeName.get(
                            ClassName.get("java.util", "List"),
                            fieldTypeName
                        )
                }

                // 3.3：构建属性对象
                val fieldSpec =
                    FieldSpec.builder(fieldTypeName, attributeName).addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                println("      attribute:[$attributeName]  attributeType:[$genericPath]")
                // 把生成的属性对象添加到类中
                classTypeBuild.addField(fieldSpec.build())

                // 3.4: 根据属性不同的类型，去添加属性的初始化值
                when (attributeClassType) {
                    1 -> { // 1：基础数据类型
                        codeBuild.addStatement("this.$attributeName = object.$methodName().orElse(null)")
                    }

                    3 -> { // List数据类型
                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${fieldType[1]}::new).collect(\$T.toList())).orElse(null)",
                            collectorsClassName
                        )
                    }

                    4 -> { // object数据类型
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${fieldType[1]}::new).orElse(null)")
                    }
                }
            } else {
                println("参数的具体类型找不到，请检查具体的内容！")
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
     * @param objectClassPath 构造方法中参数的全路径包名
     * @param jarMethodSet 生成代码里面需要写入的属性集合
     * @param packagePath 包名的路径，这里不能包含存放代码的目录
     * 动态生成代码
     */
    @JvmStatic
    fun generateChildEnum(
        objectClassPath: String,
        jarMethodSet: LinkedHashSet<ObjectBean>,
        packagePath: String,
    ) {
        // <editor-fold desc="一：构建类对象">
        println("开始生成类：$objectClassPath ------>")
        val classType = getTypeForPath(objectClassPath)
        val className = classType[1] + "Entity"

        // 构建类的build对象，用于组装类中的数据
        val classTypeBuild =
            TypeSpec.classBuilder(className)
                .addAnnotations(getAddAnnotations())
                .superclass(SUPER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
        println("需要生成的Object主对象为：[$className]")
        // </editor-fold>

        // <editor-fold desc="二：构建方法对象">
        // 2.1：构造方法的参数类型
        val methodPackageName = classType[0]
        println("类的名字为：[$className] 构造类参数的路径为：[$methodPackageName]")
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
            val bean = iterator.next()
            // 3.1：获取ben中的信息
            val attributeName = bean.attributeName // 具体的属性名字
            val methodName = bean.methodName // 方法名字
            val genericPath = bean.genericPath // 返回值的路径
            val attributeClassType = bean.classType // 参数的具体数据类型

            // todo 此处暂时使用源码中返回值类型，后续需要给替换掉
            // 3.2：根据返回属性的全路径包名和属性的类型，去获取构建属性和方法内容的type
            checkChildRunTypeClass(genericPath, attributeClassType)

            val fieldType = getTypeForPath(genericPath, attributeClassType)

            // 0：默认无效的数据类型，1：基础数据类型 2：数组类型，3：List数据集合，4：其他数据类型，也就是自定义的数据类型
            if (attributeClassType != 0) {
                // 构建属性的数据类型
                var fieldTypeName: TypeName = ClassName.get(fieldType[0], fieldType[1])
                if (attributeClassType == 3) { // List 数据类型,需要单独构造
                    fieldTypeName =
                        ParameterizedTypeName.get(
                            ClassName.get("java.util", "List"),
                            fieldTypeName
                        )
                }

                // 3.3：构建属性对象
                val fieldSpec =
                    FieldSpec.builder(fieldTypeName, attributeName).addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                println("      attribute:[$attributeName]  attributeType:[$genericPath]")
                // 把生成的属性对象添加到类中
                classTypeBuild.addField(fieldSpec.build())

                // 3.4: 根据属性不同的类型，去添加属性的初始化值
                when (attributeClassType) {
                    1 -> { // 1：基础数据类型
                        codeBuild.addStatement("this.$attributeName = object.$methodName().orElse(null)")
                    }

                    3 -> { // List数据类型
                        codeBuild.addStatement(
                            "this.$attributeName = object.$methodName().map(list ->list.stream()" +
                                ".map(${fieldType[1]}::new).collect(\$T.toList())).orElse(null)",
                            collectorsClassName
                        )
                    }

                    4 -> { // object数据类型
                        codeBuild.addStatement("this.$attributeName = object.$methodName().map(${fieldType[1]}::new).orElse(null)")
                    }
                }
            } else {
                println("参数的具体类型找不到，请检查具体的内容！")
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
        // </editor-fold>
    }

    /**
     * 每当读取到一个属性的时候，就需要判定这个类的重构类是否存在，如果不存在的话，则需要去主动生成这个类,然后返回这个类的全路径名字
     */
    private fun checkChildRunTypeClass(
        genericPath: String,
        attributeClassType: Int,
    ): AttributeTypeBean? {
        if (attributeClassType == 1) {
            println("      当前属性[$genericPath]是基础类型，不做额外处理!")
            return null
        } else {
            if (genericPath.contains(".")) {
                // 1：从path中获取对应的包名
                val jarObjectName = StringUtil.getSimpleForPath(genericPath)
                // println("className: $objectName")
                val bean =
                    RSI_TARGET_NODE_LIST.find { filter ->
                        StringUtil.lowercase(filter.apiGenericName) ==
                            StringUtil.lowercase(
                                jarObjectName
                            )
                    }
                if (bean != null) {
                    // 2：获取package的路径
                    val folderPath =
                        StringUtil.transitionPath(
                            Paths.get(BASE_OUT_PUT_PATH).resolve(Paths.get(BASE_PROJECT_PACKAGE_PATH))
                                .resolve(StringUtil.lowercase(RSI_PARENT_NODE_PATH))
                                .resolve(StringUtil.lowercase(bean.apiName))
                                .toString()
                        )
                    // 3：检测文件夹是否存在
                    val checkFolderExists = checkFolderExists(folderPath)
                    // println("      folderPath:$folderPath exists:$checkFolderExists")
                    if (!checkFolderExists) {
                        println("      包:${folderPath}不存在，需要去创建！")
                        mkdirFolder(folderPath)
                    }
                    // 4：再次检查包是否存在
                    if (checkFolderExists(folderPath)) {
                        // 5：判断文件是否存在，如果不存在，就创建，如果存在，就返回对应的类型
                        var realFileName = ""
                        if (attributeClassType == 4) {
                            realFileName = "${jarObjectName}Entity"
                        } else if (attributeClassType == 5) {
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
                                val readClass = readClass(classLoad, genericPath, "")
                                if (readClass != null) {
                                    val jarMethodSet = getMethods(readClass, jarObjectName)
                                    val packagePath =
                                        StringUtil.lowercase(
                                            StringUtil.transitionPackage(
                                                Paths.get(BASE_PROJECT_PACKAGE_PATH)
                                                    .resolve(Paths.get(RSI_PARENT_NODE_PATH)).resolve(
                                                        bean.apiName
                                                    ).toString()
                                            )
                                        )
                                    if (attributeClassType == 4) {
                                        println("子对象：[$realFileName]不存在，去创建object对象！")
                                        return generateObject(genericPath, jarMethodSet, packagePath)
                                    } else if (attributeClassType == 5) {
                                        println("子Enum：[$realFileName]不存在，去创建Enum对象！")
                                        // todo 创建子类的Enum
                                        return null
                                    }
                                } else {
                                    println("     读取到的class为空，请重读取class!")
                                }
                            }
                        }
                    } else {
                        println("      创建package之后，package依旧不存在，请检查详细路径信息！")
                    }
                } else {
                    // todo 如果在apis中不可见，则把他生成到parent节点下
                    println("      当前属性[$genericPath]的类型不在Api中，请查看这个特殊的类型：$genericPath")
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
        classType: Int = 0,
    ): Array<String> {
        val array = Array(2) { "" }
        val lastIndexOf = path.lastIndexOf(".")
        val packageName = path.substring(0, lastIndexOf)
        val simple = path.substring(lastIndexOf + 1)

        // 判断是否是集合
        if (classType == 3) {
            array[0] = packageName.split("java.util.List<")[1]
            array[1] = simple.replace(">", "")
        } else {
            array[0] = packageName
            array[1] = simple
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
    fun checkApiEntityFileExists(
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
