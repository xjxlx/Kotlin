package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityFsBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/** 反射 */
class FsActivity : BaseBindingTitleActivity<ActivityFsBinding>() {

    override fun getTitleContent(): String {
        return "反射"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFsBinding {
        return ActivityFsBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        // 反射

        // 1：获取class的字节码文件对象
        val kClass = Student::class

        // 2：通过字节码文件获取对象的实例，只能访问公有的对象和方法，不能访问私有的
        val student = kClass.createInstance()
        student.test3

        // 3:获取所有的构造参数
        val constructors = kClass.constructors
        constructors.forEach { LogUtil.e("constructors:" + it) }

        // 4:获取所有成员属性和方法，返回类型是Collection<KCallable<*>>,包括共有和私有
        val members = kClass.members
        members.forEach {
            LogUtil.e("members: " + it)
            it.name
        }

        // 5:获取所有属性，包含共有和私有的
        val memberProperties = kClass.memberProperties
        memberProperties.forEach { LogUtil.e("memberProperties : " + it) }

        // kClass.memberFunctions //获取所有函数
        val memberFunctions = kClass.memberFunctions
        memberFunctions.forEach { LogUtil.e("memberFunctions : " + it) }
        val student2 = Student2("小明", 18, "北京市", "大学")
        val kotlinToMap = kotlinToMap(student2)
        LogUtil.e("map-----> " + kotlinToMap)

        getPerson2()
    }
}

class Student {
    private val test1 = 1
    private var test2 = "test2"
    val test3 = false

    fun test1() {}

    fun test2(): String {
        return ""
    }
}

fun <R> kotlinToMap(r: R): Map<String, Any>? {
    val map: HashMap<String, Any> = hashMapOf()

    val kClass = r!!::class
    val memberProperties = kClass.memberProperties
    if (memberProperties.isNotEmpty()) {
        memberProperties.forEach {
            //            val params = it as KProperty<*>
            // 获取key 的name
            val name = it.name
            // 获取key的value
            val call = it.call(r)

            LogUtil.e("------》it: name：" + name + " call: " + call)
            map[name] = call as Any
        }
    }
    return map
}

data class Student2(val name: String, val age: Int, val address: String, val school: String)

fun getPerson2() {
    val kClass = Person2::class
    val createInstance = kClass.createInstance()

    val keyName = kClass.memberProperties.find { it.name == "name" }
    if (keyName != null) {
        val name = keyName.name
        val call = keyName.call(createInstance)

        val get = keyName.get(createInstance)

        LogUtil.e("name:$name  value: $call  get: $get")

        // 判断参数是否为可变的，是否为var
        if (keyName is KMutableProperty<*>) {
            // 修改属性值，调用setter方法
            keyName.setter.call(createInstance, "zhangsan")
        }

        // 获取到printName方法，有可能为null
        val funKClass = kClass.memberFunctions.find { it.name == "printName" }
        // 方法执行
        funKClass?.call(createInstance)

        // 获取主构造器
        val constructors = kClass.primaryConstructor
        constructors?.let {
            // 获取所有的参数
            it.parameters.map { p ->
                // 参数的类型是否为空
                p.type.isMarkedNullable
            }
        }
    }
}

class Person2 {
    var name: String = "lisi"
    fun printName() {
        throwError()
        LogUtil.e("name-----> $name")
    }

    override fun toString(): String {
        return "name:$name"
    }

    @Throws(NoSuchAlgorithmException::class)
    fun throwError() {
        MessageDigest.getInstance("MD5")
    }
}

//
// class FileUtils {
//
//    companion object {
//        private var INSTANCE: FileUtils? = null
//
//        @Synchronized
//        fun getInstance(): FileUtils? {
//            if (INSTANCE == null) {
//                synchronized(FileUtils::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = FileUtils()
//                    }
//                }
//            }
//            return INSTANCE
//        }
//
//    }
// }

class FileUtil {
    companion object {
        var INSTANCE: FileUtil? = null

        @Synchronized
        fun getInstance(): FileUtil? {
            synchronized(FileUtil::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = FileUtil()
                }
            }
            return INSTANCE
        }
    }
}

object ASS {
    var instance: ASS? = null
    @Synchronized
    fun test(): ASS? {
        synchronized(ASS::class.java) {
            if (instance == null) {
                instance = this@ASS
            }
        }
        return instance
    }
}
