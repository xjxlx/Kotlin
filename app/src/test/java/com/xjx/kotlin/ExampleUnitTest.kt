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
        val targetFolderPath = "./src/test"
        val type = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"

        val hashMap = HashMap<String, String>()
        hashMap["name"] = type
        hashMap["age"] = "java.lang.String"
        WriteSDK.writeEntity(targetFolderPath, hashMap, type)

//        Write2.main()
        assertEquals(4, 2 + 2)
    }
}
