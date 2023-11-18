package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityFxBinding

class FxActivity : BaseBindingTitleActivity<ActivityFxBinding>() {

    override fun getTitleContent(): String {
        return "泛型"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityFxBinding {
        return ActivityFxBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {}

    fun <T : Comparable<T>> test(a: T, b: T): T {
        val b1 = a > b
        TODO()
    }

    fun <T : Int> test2(a: T, b: T): T {
        val b1 = a > b
        TODO()
    }

    class Student<T : Comparable<T>> {
        fun <R> sss(): R {
            TODO()
        }
    }
}

private val mListOut: ArrayList<out Book> = arrayListOf()

interface Book {
    fun getBook(): Book?
}

class BookEnglish : Book {
    override fun getBook(): Book? {
        //        mListOut.add(BookChina())
        //        val bookStore = BookStore<BookChina>()
        //        val book = bookStore.getBook()
        //        mListOut.add(book)

        return null
    }
}

class BookChina : Book {
    override fun getBook(): Book? {
        return null
    }
}

class BookStore<out T : Book> {
    fun getBook(): T {
        TODO()
    }
}

class Test2 {
    fun test2(): Book {
        val bookStore = BookStore<BookChina>()
        val book = bookStore.getBook()
        return book
    }
}
