package com.android.hcp3

object Config {
    /**
     *项目中mode的路径，用于存储生成的代码outPut路径
     */
    const val RSI_PROJECT_PATH: String = "hcp3/src/main/java/"

    /**
     *生成代码的主路径，这里指的是mode的包名
     */
    const val RSI_PROJECT_PACKAGE_PATH: String = "com.android.hcp3.generate."

    /**
     *rsi中大项的节点路径
     */
    const val RSI_PARENT_NODE_PATH = "hvac.v3."

    /**
     *rsi中大项中子节点路径
     */
    const val RSI_CHILD_NODE_PATH = "generalsettings"

    /**
     * rsi大项节点下的子节点种object的完整包名
     */
    const val RSI_CHILD_NODE_OBJECT_NAME = "de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject"
}
