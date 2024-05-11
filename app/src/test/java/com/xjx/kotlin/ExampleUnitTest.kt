package com.xjx.kotlin

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val targetFolderPath = "./src/main/java/com/xjx/kotlin/utils/hcp3"
//        com.xjx.kotlin.utils.hcp3
        val type = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"
//        method [aC] typeName: [java.util.Optional<de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject>]
//        GenericType:[de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject]

//        val linkedSetOf = linkedSetOf<JarBean>()
//        val jarBean = JarBean()
//        jarBean.method = "getAC"
//        jarBean.attribute = "aC"
//        jarBean.methodGenericityType = "de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject"
//        jarBean.methodType = "java.util.Optional<de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject>"
//        jarBean.objectName = "SwitchControlObject"
//        linkedSetOf.add(jarBean)
//        WriteSDK.writeEntity(targetFolderPath, linkedSetOf, type)
//
//        val write = WriteSdk2()
//        write.write()

//        val readJarFile = ReadJarFile()
//        readJarFile.execute()

        assertEquals(4, 2 + 2)
    }
}
