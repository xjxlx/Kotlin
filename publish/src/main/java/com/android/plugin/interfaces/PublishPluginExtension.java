package com.android.plugin.interfaces;

import org.gradle.api.provider.Property;

public interface PublishPluginExtension {
    /**
     * 组的名字
     */
    Property<String> getGroupId();

    /**
     * 插件名称，必传参数
     */
    Property<String> getArtifactId();

    /**
     * 版本号，必传参数
     */
    Property<String> getVersion();
}