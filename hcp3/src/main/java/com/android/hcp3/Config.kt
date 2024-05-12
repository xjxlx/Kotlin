package com.android.hcp3

object Config {
    /**
     *RSI 中Root节点路径，这个一般不会进行改动
     */
    const val RSI_ROOT_NODE_PATH = "de.esolutions.fw.rudi.viwi.service"

    /**
     * 存放JAR包的位置，这个会根据项目的不同，进行一次性的改动，不会随意改动
     */
    const val BASE_JAR_PATH: String = "hcp3/src/main/java/com/android/hcp3/jar/"

    /**
     *读取JAR包的路径，这个地方一般是死的，不会随意改动
     */
    const val TARGET_JAR_PATH: String = BASE_JAR_PATH + "mib_rsi_android.jar"

    /**
     *项目中mode的路径，用于存储生成的代码outPut路径，这个会根据项目的不同，进行一次性的改动，不会随意改动
     */
    const val RSI_PROJECT_PATH: String = "hcp3/src/main/java/"

    /**
     *生成代码的主路径，这里的路径，一般指的是当前mode的包名，毕竟是要存储到当前包路径下的，也可以随意更改，不过不会随意改动
     */
    const val RSI_PROJECT_PACKAGE_PATH: String = "com.android.hcp3.generate."

    /**
     *rsi中大项的节点路径，这个每次生成不同的模块，都要进行不同的改动
     */
    const val RSI_PARENT_NODE_PATH = "hvac.v3."

    /**
     *rsi中大项中子节点路径，这个每次生成不同的模块，都要进行不同的改动
     */
    const val RSI_CHILD_NODE_PATH = "generalsettings"

    /**
     * rsi大项节点下的子节点种object的完整包名，这个每次生成不同的模块的Object，都要进行不同的改动
     */
    const val RSI_CHILD_NODE_OBJECT_NAME = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"
}
