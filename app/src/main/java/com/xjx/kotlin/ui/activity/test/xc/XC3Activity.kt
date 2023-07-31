package com.xjx.kotlin.ui.activity.test.xc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityXc3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class XC3Activity : AppBaseBindingTitleActivity<ActivityXc3Binding>() {

    private val TAG = "XC -3"

    override fun setTitleContent(): String {
        return "协程 - 3 - 流"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityXc3Binding {
        return ActivityXc3Binding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        GlobalScope.launch {
            // 一次性返回
//            simple2().forEach {
//                LogUtil.e(TAG, " loop ---> $it")
//            }

            // flow流收集器
//            simple3().collect() {
//                LogUtil.e(TAG, "flow  ---> collect ---> $it")
//            }

//            val simple4 = simple4()
//            LogUtil.e("simple4 ", "create --->")
//            simple4.collect {
//                LogUtil.e("simple4 ", "start collect --->$it")
//            }

            // test cancel flow
//            withTimeoutOrNull(4500) {
//                simple4().collect {
//                    LogUtil.e("simple4", "result ---> $it")
//                }
//            }

            // test create flow
//            arrayListOf(1, 2, 3)
//                .asFlow()
//                .collect {
//                    LogUtil.e("asFlow", "change ---> $it")
//                }
//
//            flowOf(1, 2, 3).collect() {
//                LogUtil.e("flowOf", "change ---> $it")
//            }

            // test  map / filter
//            flowOf(0, 1, 2, 3, 4, 5, 6)
//                .map {
//                    delay(1000)
//                    it
//                }
//                .filter {
//                    it % 2 == 0
//                }
//                .collect {
//                    LogUtil.e("map", "map: ---> $it")
//                }

            // test transform
//            (1..3)
//                .asFlow() // 一个请求流
//                .transform { request ->
//                    emit("Making request $request")
//                    emit(request)
//                }
//                .collect { response ->
//                    LogUtil.e("transform", response.toString())
//                }

            // test  take
//            numbers()
//                .take(2) // 只获取前两个
//                .collect { value ->
//                    LogUtil.e("take", value.toString())
//                }

            // test 末端流操作符
//            val toList = numbers().toList()
//            LogUtil.e("tolist $toList")
//
//            val toSet = numbers().toSet()
//            LogUtil.e("toSet $toSet")
//
//            val first = numbers().first()
//            LogUtil.e("first $first")

//            simple5().collect { value ->
//                println(value)
//                LogUtil.e("simple5", " ----> $value")
//            }

            // test conflate 合并发送
//            val time = measureTimeMillis {
//                simple5()
//                    .conflate() // 合并发射项，不对每个值进行处理
//                    .collect { value ->
//                        delay(300) // 假装我们花费 300 毫秒来处理它
//                        LogUtil.e("conflate", " ----> $value")
//                    }
//            }
//            LogUtil.e("conflate", "Collected in $time ms")

            // test collectLatest
//            val time = measureTimeMillis {
//                simple5().collectLatest { value -> // 取消并重新发射最后一个值
//                    LogUtil.e("collectLatest", "Collecting $value")
//                    delay(300) // 假装我们花费 300 毫秒来处理它
//                    LogUtil.e("collectLatest", "Done $value")
//                }
//            }
//            LogUtil.e("collectLatest", "Collected in $time ms")

            // test zip
//            val nums = (1..5).asFlow() // 数字 1..3
//            val strs = flowOf("one", "two", "three", "four") // 字符串
//            nums
//                .zip(strs) { a, b ->
//                    return@zip "$a-$b"
//                }
//                .collect {
//                    LogUtil.e("zip", "" + it) // 收集并打印
//                }

            // test combine
//            val nums = (1..3)
//                .asFlow()
//                .onEach {
//                    delay(1000)
//                }
//
//            // 发射数字 1..3，间隔 300 毫秒
//            val strs = flowOf("one", "two", "three").onEach {
//                delay(2000)
//            }
//
//            val startTime = System.currentTimeMillis() // 记录开始的时间
//            nums
//                .combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
//                .collect { value -> // 收集并打印
//                    LogUtil.e("combine", "$value at ${System.currentTimeMillis() - startTime}  ")
//                }

//            fun requestFlow(i: Int): Flow<String> = flow {
//                emit("$i: First")
//                delay(500) // 等待 500 毫秒
//                emit("$i: Second")
//            }

            // test  flatMapConcat
//            val startTime = System.currentTimeMillis() // 记录开始时间
//            (1..3)
//                .asFlow()
//                .onEach { delay(100) } // 每 100 毫秒发射一个数字
//                .flatMapConcat { requestFlow(it) }
//                .collect { value -> // 收集并打印
//                    LogUtil.e("flatMapConcat", "$value at ${System.currentTimeMillis() - startTime} ms")
//                }

            // test flatMapMerge
//            val startTime = System.currentTimeMillis() // 记录开始时间
//            (1..3)
//                .asFlow()
//                .onEach { delay(100) } // 每 100 毫秒发射一个数字
//                .flatMapMerge { requestFlow(it) }
//                .collect { value -> // 收集并打印
//                    LogUtil.e("flatMapMerge", "$value at ${System.currentTimeMillis() - startTime} ms from start")
//                }

            // test try catch
//            fun simple(): Flow<Int> = flow {
//                for (i in 1..3) {
//                    LogUtil.e("Emitting $i", "try")
//                    emit(i) // 发射下一个值
//                }
//            }
//            try {
//                simple().collect { value ->
//                    LogUtil.e(value, "try")
//                    check(value <= 1) {
//                        "Collected $value"
//                    }
//                }
//            } catch (e: Throwable) {
//                LogUtil.e("Caught $e", "try")
//            }

            // test try -1 不崩溃
//            fun simple(): Flow<String> = flow {
//                for (i in 1..3) {
//                    LogUtil.e("Emitting $i", "try")
//                    emit(i) // 发射下一个值
//                }
//            }.map { value ->
//                check(value <= 1) {
//                    "Crashed on $value"
//                }
//                "string $value"
//            }
//
//            try {
//                simple().collect { value ->
//                    LogUtil.e(value, "try")
//                }
//            } catch (e: Throwable) {
//                LogUtil.e("Caught $e", "try")
//            }

            // test try - 2 捕获异常
//            fun simple(): Flow<String> = flow {
//                for (i in 1..3) {
//                    LogUtil.e("Emitting $i", "try")
//                    emit(i) // 发射下一个值
//                }
//            }.map { value ->
//                check(value <= 1) {
//                    "Crashed on $value"
//                }
//                "string $value"
//            }
//
//            simple()
//                .catch { e ->
//                    // 在上游不会，会发出异常，但是不会崩溃
//                    emit("Caught $e")
//                } // 发射一个异常
//                .collect { value ->
//                    LogUtil.e(value, "try")
//                }

            // test - 崩溃
//            fun simple(): Flow<Int> = flow {
//                for (i in 1..3) {
//                    LogUtil.e("Emitting $i", "try")
//                    emit(i)
//                }
//            }
//
//            simple()
//                .catch { e ->
//                    LogUtil.e("Caught $e", "try")
//                }
//                // 不会捕获下游异常
//                .collect { value ->
//                    check(value <= 1) {
//                        "Collected $value"
//                    }
//                    LogUtil.e(value, "try")
//                }

//            // test  try  finally
//            fun simple(): Flow<Int> = (1..3).asFlow()
//
//            try {
//                simple().collect { value ->
//                    LogUtil.e(value, "finally")
//                }
//            } finally {
//                LogUtil.e("Done", "finally")
//            }

            // test onCompletion
//            fun simple(): Flow<Int> = (1..3).asFlow()
//
//            simple()
//                .onCompletion {
//                    LogUtil.e("Done", "onCompletion")
//                }
//                .collect { value ->
//                    LogUtil.e(value, "onCompletion")
//                }

            // test
//            fun simple(): Flow<Int> = (1..3).asFlow() {
//
//                throw RuntimeException("sss")
//            }

            // fun simple(): Flow<Int> = (1..3).asFlow()
//            fun simple(): Flow<Int> = flow {
//                emit(1)
//                emit(2)
//                emit(3)
//                 throw RuntimeException("sss")
//            }
//
//            simple()
//                .onCompletion { cause ->
//                    if (cause != null) {
//                        LogUtil.e("Flow completed exceptionally", "onCompletion or catch")
//                    }
//                }
//                .catch { cause ->
//                    LogUtil.e("Caught exception : " + cause.message, "onCompletion or catch")
//                }
//                .collect { value ->
//                    LogUtil.e(value, "onCompletion or catch")
//                }
        }

        fun foo(): Flow<Int> = flow {
            for (i in 1..5) {
                println("Emitting $i")
                emit(i)
            }
        }

//        runBlocking {
////            foo()
//            (1..5)
//                .asFlow()
//                .cancellable()
//                .collect { value ->
//                    if (value == 3) {
//                        cancel()
//                    }
//                    LogUtil.e("cancel", value)
//                }
//        }
    }

    suspend fun simple2(): ArrayList<Int> {
        // 异步的操作
        delay(1000)
        return arrayListOf(1, 2, 3)
    }

    private fun simple3(): Flow<Int> = flow {
        for (item in 0..3) {
            // 模拟耗时操作
            delay(1000)
            // 发射数据
            emit(item)
        }
    }

    private fun simple4(): Flow<Int> = flow {
        LogUtil.e("simple4", "simple4 ---> launch ....")
        val measureTimeMillis = measureTimeMillis {
            for (item in 0..5) {
                // 模拟耗时操作
                delay(1000)
                // 发射数据
                emit(item)
            }
        }
        LogUtil.e("simple4", "simple4 ---> time:  $measureTimeMillis")
    }

    private fun numbers(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            LogUtil.e("take", "This line will not execute")
            emit(3)
        } finally {
            LogUtil.e("take", "Finally in numbers")
        }
    }

    private fun simple5(): Flow<Int> = flow {
        // 在流构建器中更改消耗 CPU 代码的上下文的错误方式
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            emit(i) // 发射下一个值
        }
    }.flowOn(Dispatchers.Default)

}