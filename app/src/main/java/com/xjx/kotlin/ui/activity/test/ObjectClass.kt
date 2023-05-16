package com.xjx.kotlin.ui.activity.test

import com.android.helper.utils.LogUtil
import com.xjx.kotlin.utils.FileUtil
import java.io.File

/**
 * @author : 流星
 * @CreateDate: 2022/12/21-18:54
 * @Description:
 */
class ObjectClass {

    fun test() {
        val filePath: String = FileUtil.getFilePath(File(""))
        LogUtil.e("filePath: " + filePath)
        Test.a
        FileUtil.pathUrl

        Test.staticClass()
        Test().InnerClass()
    }

}

class Test {

    var abc: Int = 0
    fun test() {
        val sss = ObjectClass2.StaticClasss()
        val innerClass = ObjectClass2().InnerClass()

    }

    companion object {
        val a: Int = 0
    }

    // 非静态内部类
    inner class InnerClass {
        fun sss() {
            abc = 1
            test()
        }
    }

    // 静态内部类
    class staticClass {
        fun sss() {
            Status.RED.name
            val mission = Mission(1)

            dur(Mission(22))
        }

        fun dur(dur: Mission) {

        }
    }
}

enum class Status {
    YELLO, RED, WHITE
}

@JvmInline
value class Mission(val age: Int) {

    override fun toString(): String {
        return "Mission(age=$age)"
    }

    fun sss(): Int {
        return age * 40
    }
}

@JvmInline
value class Hours(val value: Int) {
    fun toMinutes() = Mission(value * 60)
}
