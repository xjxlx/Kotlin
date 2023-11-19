package com.xjx.kotlin.ui.activity.test

class Test {

    var abc: Int = 0
    fun test() {
        ObjectClass2.StaticClasss()
        ObjectClass2().InnerClass()
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
