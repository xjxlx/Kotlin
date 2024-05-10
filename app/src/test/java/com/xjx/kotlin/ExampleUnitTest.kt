package com.xjx.kotlin

import com.xjx.kotlin.utils.hcp3.ReadJarFile
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val read = ReadJarFile()
        read.execute()

        assertEquals(4, 2 + 2)
    }
}
