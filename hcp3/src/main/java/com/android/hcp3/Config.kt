package com.android.hcp3

import com.android.hcp3.bean.ApiNodeBean

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
    const val BASE_JAR_PATH: String = "hcp3/libs/cluster46_12_7_0/"

    /**
     *读取JAR包的路径，这个地方一般是死的，不会随意改动
     */
    const val TARGET_JAR_PATH: String = BASE_JAR_PATH + "mib_rsi_android.jar"

    /**
     * 生成代码的路径是由【主路径】 + 【主包名】构成的，这里是代码存放的主路径，一般不会改动
     * 生成的路径是以项目的根目录作为路径的起始点
     * @see {hcp3/src/main/java/}
     */
    const val BASE_OUT_PUT_PATH: String = "hcp3/src/main/java"

    /**
     * 生成代码的路径是由【主路径】+【主包名】构成的，这个是包名，所有生成的代码都会放入到这个目录下面
     * @see {com.android.hcp3.rsi}
     */
    const val BASE_PROJECT_PACKAGE_PATH: String = "com.android.hcp3.rsi"

    /**
     *rsi中大项的节点路径，这个每次生成不同的模块，都要进行不同的改动
     * @see {hvac}
     */
    const val RSI_PARENT_NODE_PATH = "clustersds"

    /**
     *rsi中大项的节点的等级，这个每次生成不同的模块，都要进行不同的改动
     * @see {v3}
     */
    const val RSI_PARENT_NODE_LEVEL = "v2"

    /**
     *rsi中大项中子节点路径，这个每次生成不同的模块，都要进行不同的改动
     * @see {generalsettings}
     */
    const val RSI_CHILD_NODE_PATH = "poi"
//    const val RSI_CHILD_NODE_PATH = "airQualities"
}
