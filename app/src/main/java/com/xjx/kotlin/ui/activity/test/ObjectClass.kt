package com.xjx.kotlin.ui.activity.test

import com.android.helper.utils.LogUtil
import com.xjx.kotlin.ui.activity.test.utils.FileUtil
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

    val abc: Int = 0
    fun test() {
        val sss = ObjectClass2.StaticClasss()
        val innerClass = ObjectClass2().InnerClass()

    }

    companion object {
        val a: Int = 0
    }

    // 非静态内部类
    inner class InnerClass {

    }

    // 静态内部类
    class staticClass {

    }
}
