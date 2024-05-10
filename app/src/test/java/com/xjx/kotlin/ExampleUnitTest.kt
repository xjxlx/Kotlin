package com.xjx.kotlin

import com.xjx.kotlin.utils.hcp3.WriteSDK
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
        val file = "com.xjx.kotlin.Bean"
        val type = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"

        WriteSDK.writeEntity(file, "name", type, type)
        assertEquals(4, 2 + 2)
    }
}
