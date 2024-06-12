package com.android.hcp3

import com.android.hcp3.bean.ApiNodeBean
import java.io.File

object Config {
    /**
     * 用来存储当前节点下的所有子节点名字，内容会自动生成，不要做任何的改动
     */
    val RSI_TARGET_NODE_LIST = LinkedHashSet<ApiNodeBean>()

    /**
     * object的后缀，这个是固定的
     */
    const val OBJECT_SUFFIX = "Entity"

    /**
     * enum的前缀，这个是固定的
     */
    const val ENUM_PREFIX = "Vc"

    /**
     *RSI 中Root节点路径，这个一般不会进行改动
     * @see{de.esolutions.fw.rudi.viwi.service}
     */
    const val RSI_ROOT_NODE_PATH = "de.esolutions.fw.rudi.viwi.service"

    /**
     * 存放JAR包的位置，这个会根据项目的不同，进行一次性的改动，不会随意改动
     * @see {hcp3/src/main/java/com/android/hcp3/jar/}
     */
    val BASE_JAR_PATH: String by lazy {
        // return@lazy StringUtil.lowercase("hcp3${File.separator}libs${File.separator}cluster46_12_7_0${File.separator}")
        return@lazy StringUtil.lowercase("hcp3${File.separator}libs${File.separator}oia${File.separator}")
    }

    /**
     * 生成代码的路径是由【主路径】 + 【主包名】构成的，这里是代码存放的主路径，一般不会改动
     * 生成的路径是以项目的根目录作为路径的起始点
     * @see {hcp3/src/main/java/}
     */
    val BASE_OUT_PUT_PATH: String by lazy {
        return@lazy StringUtil.lowercase("hcp3${File.separator}src${File.separator}main${File.separator}java")
    }

    /**
     * 生成代码的路径是由【主路径】+【主包名】构成的，这个是包名，所有生成的代码都会放入到这个目录下面
     * @see {com.android.hcp3.rsi}
     */
    val BASE_PROJECT_PACKAGE_PATH: String by lazy {
        return@lazy StringUtil.lowercase(
            "technology${File.separator}cariad${File.separator}vehiclecontrolmanager${File.separator}rsi${File.separator}"
        )
    }

    /**
     * 1：如果为true，则要写入所有api的内容
     * 2：如果为false，则只写入单独的api节点内容
     */
    const val FLAG_ALL = false

    /**
     *  当前指定父类节点：[Config.RSI_NODE_NAME]指定节点在RSI中的相对路径，这个是动态生成的，不要做任何的改动
     * @return 例如：
     *  父类节点是：HeadUpDisplay
     *  则这个路径为：de/esolutions/fw/rudi/viwi/service/headupdisplay/v4/HeadUpDisplay
     */
    var RSI_NODE_PATH: String = ""

    /**
     * RSI中大项的名字，这个每次生成不同的模块，都要进行不同的改动
     * 例如：headupdisplay、usermanagement
     */
    val RSI_NODE_NAME: String = "CarGlobal"
        get() {
            return StringUtil.lowercase(field)
        }

    /**
     * RSI中大项的节点的等级，这个每次生成不同的模块，都要进行不同的改动
     * 例如：v4、v3
     */
    var RSI_NODE_LEVEL: String = "v2"
        get() {
            return StringUtil.lowercase(field)
        }

    /**
     * RSI中大项中子节点路径，这个每次生成不同的模块，都要进行不同的改动
     * 例如：switchControls、settings
     */
    var RSI_NODE_API_NAME: String = "carinformation"
        get() {
            return StringUtil.lowercase(field)
        }
}
