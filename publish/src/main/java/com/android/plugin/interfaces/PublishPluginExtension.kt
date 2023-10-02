package com.android.plugin.interfaces

import org.gradle.api.provider.Property

abstract class PublishPluginExtension {
    /**
     * 组的名字
     */
    abstract val groupId: Property<String>
    /**
     * 插件名称，必传参数
     */
    abstract val artifactId: Property<String>
    /**
     * 版本号，必传参数
     */
    abstract val version: Property<String>

    init {
        groupId.convention("com.android.helper")
        artifactId.convention("publish")
        version.convention("1.0.0")
    }
}