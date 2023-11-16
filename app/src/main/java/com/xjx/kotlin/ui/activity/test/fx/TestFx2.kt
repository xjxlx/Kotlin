package com.xjx.kotlin.ui.activity.test.fx

import com.google.gson.Gson

/**
 * @author : 流星
 * @CreateDate: 2023/3/28-00:48
 * @Description:
 */
class TestFx2 {

    /**
     * 泛型擦除
     */
    fun <T> action(t: T) {
        // val t = T() // 无法编译；T 类型的构造器未知
        // val tArray = Array(10) // 无法编译；数组中类型不会擦除，但是类型未知
        // val tClass = T::class.java // 无法编译；类型被擦除，类型未知
        // val tList = ArrayList() // 可以编译；因为集合中反正会类型擦除
    }

    inline fun <reified T> action2(t: T) {
//
//    // 比如外部调用该方法，传入参数为 String，那么 T 类型将特化成 String 类型
//    val t = T() // 仍然无法编译；T 类型的构造器未知
//    val tArray = Array(10, TODO()) // 可以编译；可以得到正确的类型
//    val tClass = T::class.java // 可以编译；可以得到正确的类型
//    val tList = ArrayList() // 可以编译；因为集合中还是会类型擦除
    }

    fun <T> fromJson(json: String, clas: Class<T>) {
        // val gson = Gson()
        // gson.fromJson<T>(json, T::class.java) // 此处报错，不能获取T的类型
    }

    // 内联特化版
    inline fun <reified T> fromJson(json: String) {
        val gson = Gson()
        // 此处可以获取到T的具体类型，所以可以省略到类型的class参数
        gson.fromJson(json, T::class.java)
    }
}