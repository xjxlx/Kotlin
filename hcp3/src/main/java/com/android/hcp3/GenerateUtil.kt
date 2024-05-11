package com.android.hcp3

import com.squareup.javapoet.*
import java.io.File
import java.io.IOException
import javax.lang.model.element.Modifier

object GenerateUtil {
    /**
     *项目中mode的路径，用于存储生成的代码outPut路径
     */
    private const val RSI_PROJECT_PATH: String = "hcp3/src/main/java/"

    /**
     *生成代码的主路径，这里指的是mode的包名
     */
    private const val RSI_PROJECT_PACKAGE_PATH: String = "com.android.hcp3.generate."

    /**
     *rsi中大项的节点路径
     */
    private const val RSI_PARENT_NODE_PATH = "hvac.v3."

    /**
     *rsi中大项中子节点路径
     */
    private const val RSI_CHILD_NODE_PATH = "generalsettings"

    /**
     * rsi大项节点下的子节点种object的完整包名
     */
    private const val RSI_CHILD_NODE_OBJECT_NAME = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"

    /**
     * 方法参数的注解类型
     */
    private val METHOD_ANNOTATION_TYPE = ClassName.get("androidx.annotation", "NonNull")

    /**
     *文件输出的路径
     */
    private val OUT_FOLDER = File(RSI_PROJECT_PATH + RSI_PARENT_NODE_PATH + RSI_CHILD_NODE_PATH)

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

    fun generateEntity(jarMethodSet: LinkedHashSet<JarBean>) {
        // <editor-fold desc="一：构建类对象">
        val classType = getTypeForPath(RSI_CHILD_NODE_OBJECT_NAME)
        val className = classType[1] + "Entity"
        val classTypeBuild =
            TypeSpec.classBuilder(className)
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
        // </editor-fold>

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

            // <editor-fold desc="屋：构建方法体对象">
            // 2.3：定义方法体
            val body = "this.$attributeName = object.$methodName().map(${fieldType[1]}::new).orElse(null)"
            val methodBody =
                CodeBlock.builder()
                    .addStatement(body)
                    .build()
            methodSpecBuild.addCode(methodBody)

            // 添加完成的方法内容
            classTypeBuild.addMethod(methodSpecBuild.build())
            // </editor-fold>
        }

        // <editor-fold desc="四：写入到类中">
        // 四：写入类
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
}
