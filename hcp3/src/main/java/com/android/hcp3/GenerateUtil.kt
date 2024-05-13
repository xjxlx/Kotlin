package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.Config.BASE_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_LEVEL
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
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

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val linkedSetOf = linkedSetOf<ObjectEntity>()
        val jarBean = ObjectEntity()
        jarBean.attributeName = "aC"
        jarBean.methodName = "getAC"
        jarBean.genericPath = "de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject"
        linkedSetOf.add(jarBean)
//        generateEntity(linkedSetOf)
    }

    /**
     * 动态生成代码
     */
    @JvmStatic
    fun generateEntity(
        objectClassPath: String,
        jarMethodSet: LinkedHashSet<ObjectEntity>,
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
            generateChildClass(genericPath, attributeClassType)

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
        val packageName =
            Paths.get(BASE_PROJECT_PACKAGE_PATH).resolve(Paths.get(RSI_PARENT_NODE_PATH))
                .resolve(Paths.get(RSI_PARENT_NODE_LEVEL)).resolve(Paths.get(RSI_CHILD_NODE_PATH)).toString()

        val javaFile = JavaFile.builder(packageName, classTypeBuild.build()).build()

        // println("OutPutPath:$RSI_PROJECT_PATH")
        val outPutFile = File(BASE_OUT_PUT_PATH)
        // 这里输出的路径，是以项目的root作为根目录的
//        javaFile.writeTo(outPutFile)
        javaFile.writeTo(System.out)
        println("写入结束！")
        // </editor-fold>
    }

    /**
     * 每当读取到一个属性的时候，就需要判定这个类的重构类是否存在，如果不存在的话，则需要去主动生成这个类
     */
    private fun generateChildClass(
        genericPath: String,
        attributeClassType: Int,
    ) {
        if (genericPath.contains(".")) {
            val clasName = genericPath.substring(genericPath.lastIndexOf(".") + 1)
            println("className: $clasName")
            var newClassName = ""
            if (clasName.endsWith("Object")) {
                newClassName = "${clasName}Entity"
            } else if (clasName.endsWith("Enum")) {
                newClassName = "${clasName}Entity"
            } else {
                println("出现了异种的返回值[$clasName]，请手动处理！")
            }
        }
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
     * 检测
     */
    fun mkdirApiEntity(packagePath: String) {
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
