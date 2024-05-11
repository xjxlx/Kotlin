package com.android.hcp3

import com.android.hcp3.Config.RSI_CHILD_NODE_OBJECT_NAME
import com.android.hcp3.Config.RSI_CHILD_NODE_PATH
import com.android.hcp3.Config.RSI_PARENT_NODE_PATH
import com.android.hcp3.Config.RSI_PROJECT_PACKAGE_PATH
import com.android.hcp3.Config.RSI_PROJECT_PATH
import com.squareup.javapoet.*
import java.io.File
import java.io.IOException
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

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val linkedSetOf = linkedSetOf<JarBean>()
        val jarBean = JarBean()
        jarBean.attributeName = "aC"
        jarBean.methodName = "getAC"
        jarBean.genericPath = "de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject"
        linkedSetOf.add(jarBean)
        generateEntity(linkedSetOf)
    }

    @JvmStatic
    fun generateEntity(jarMethodSet: LinkedHashSet<JarBean>) {
        // <editor-fold desc="一：构建类对象">
        val classType = getTypeForPath(RSI_CHILD_NODE_OBJECT_NAME)
        val className = classType[1] + "Entity"

        // 3:组合类对象
        val classTypeBuild =
            TypeSpec.classBuilder(className)
                .addAnnotations(getAddAnnotations())
                .superclass(SUPER_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
        println("ClassName:[$className]")
        // </editor-fold>

        // <editor-fold desc="二：构建方法对象">
        // 2.1：构造方法的参数类型
        val methodPackageName = classType[0]
        println("methodPackageName:[$methodPackageName] methodSimpleName:[$className]")
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
        // </editor-fold>

        val bodyBuilder = StringBuilder()
        // 三：循环构建属性和方法体
        val iterator = jarMethodSet.iterator()
        while (iterator.hasNext()) {
            val bean = iterator.next()

            val attributeName = bean.attributeName // 具体的属性名字
            val methodName = bean.methodName // 方法名字
            val genericPath = bean.genericPath // 返回值的路径

            // <editor-fold desc="四：构建属性对象">
            // 一：构建属性对象
            // todo 此处暂时使用源码中返回值类型，后续需要给替换掉
            // 1.1：定义属性的类型
            val fieldType = getTypeForPath(genericPath)
            val fieldTypeName = ClassName.get(fieldType[0], fieldType[1])
            println("attribute:[$attributeName]  attributeType:[$genericPath]")

            // 1.2:组装属性
            val field =
                FieldSpec.builder(fieldTypeName, attributeName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build()
            // 添加属性到类中
            classTypeBuild.addField(field)
            // </editor-fold>

            // 组合方法体内容
            bodyBuilder.append("this.$attributeName = object.$methodName().map(${fieldType[1]}::new).orElse(null);\n")
        }

        // <editor-fold desc="五：构建方法体对象">
        // 2.3：定义方法体
        val methodBody =
            CodeBlock.builder()
                .addStatement(bodyBuilder.toString())
                .build()
        methodSpecBuild.addCode(methodBody)

        // 添加完成的方法内容
        classTypeBuild.addMethod(methodSpecBuild.build())
        // </editor-fold>

        // <editor-fold desc="六：写入到类中">
        val packageName = RSI_PROJECT_PACKAGE_PATH + RSI_PARENT_NODE_PATH + RSI_CHILD_NODE_PATH
        val javaFile = JavaFile.builder(packageName, classTypeBuild.build()).build()

        println("OutPutPath:$RSI_PROJECT_PATH")
        val outPutFile = File(RSI_PROJECT_PATH)
        // 这里输出的路径，是以项目的root作为根目录的
        javaFile.writeTo(outPutFile)
        // javaFile.writeTo(System.out)
        // </editor-fold>
    }

    /**
     * @param path 指定类的全路径，例如：de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject
     * @return 返回一个数据，第一个元素是指定路径中最后一个.的前半截，第二个元素是object的简写名字，例如：[0] = de.esolutions.fw.rudi.viwi.service.hvac.v3
     * [1] = GeneralSettingObject
     */
    private fun getTypeForPath(path: String): Array<String> {
        val lastIndexOf = path.lastIndexOf(".")
        val array = Array(2) { "" }
        array[0] = path.substring(0, lastIndexOf)
        array[1] = path.substring(lastIndexOf + 1)
        return array
    }

    /**
     * 类的注解
     */
    private fun getAddAnnotations(): List<AnnotationSpec> {
        val annotations = arrayListOf<AnnotationSpec>()
        val getter = AnnotationSpec.builder(ClassName.get("lombok", "Getter")).build()
        annotations.add(getter)

        val toString = AnnotationSpec.builder(ClassName.get("lombok", "ToString")).addMember("callSuper", "true").build()
        annotations.add(toString)

        val equalsAndHashCode =
            AnnotationSpec.builder(ClassName.get("lombok", "EqualsAndHashCode")).addMember("callSuper", "true").build()
        annotations.add(equalsAndHashCode)
        return annotations
    }
}
