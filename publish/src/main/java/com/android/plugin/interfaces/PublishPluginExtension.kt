package com.android.plugin.interfaces

import org.gradle.api.provider.Property

abstract class PublishPluginExtension {
    /**
     * 组的名字
     */
    abstract val publishGroupId: Property<String>
    /**
     * 插件名称，必传参数
     */
    abstract val publishArtifactId: Property<String>
    /**
     * 版本号，必传参数
     */
    abstract val publishVersion: Property<String>

    init {
        publishGroupId.convention("com.android.helper")
        publishArtifactId.convention("publish")
        publishVersion.convention("1.0.0")
    }
}