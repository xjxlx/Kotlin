package com.android.plugin.plugin

import VersionUtil
import com.android.build.api.dsl.LibraryExtension
import com.android.plugin.interfaces.PublishPluginExtension
import com.android.plugin.utils.TextUtil
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class PublishPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("---------------------》")

        // 1：注册一个片段，用来传输数据使用
        val publishExtension = project.extensions.create("publishExtension", PublishPluginExtension::class.java)
//            .apply {
//                publishGroupId.convention("com.android.helper")
//                publishArtifactId.convention("publish")
//                publishVersion.convention("1.0.0")
//            }

        val publishGroupId = publishExtension.publishGroupId.get()
        val publishArtifactId = publishExtension.publishArtifactId.get()
        val publishVersion = publishExtension.publishVersion.get()

        // 注册一个发布的类型
        registerPublishType(project)

        // 2：检查是否安装了push插件
        val plugin = project.pluginManager.findPlugin("maven-publish")
        println("maven-publish 是否为空：$plugin")
        if (plugin == null) {
            // 安装插件
            project.pluginManager.apply("maven-publish")
            println("添加 maven-publish ！")

            // String id = plugin.getId();
            // String name = plugin.getName();
            // boolean hasPlugin = project.getPluginManager().hasPlugin(id);
            // println("hasPlugin:" + hasPlugin);
            // println("id:" + id);
            // println("name:" + name);
            // PluginContainer plugins = project.getPlugins();
        }
        println("maven-publish 是否为空：${project.pluginManager.findPlugin("maven-publish")}")

        println("before---> groupId:$publishGroupId artifactId:$publishArtifactId version:$publishVersion")

        // 3：注册一个发布的task
        project.task("publishTask") {
            it.group = "install"
            // 发布插件
            publishTask(project, publishGroupId, publishArtifactId, publishVersion)
        }
    }

    /**
     * 注册一个release的发布类型
     */
    private fun registerPublishType(project: Project) {
        val libraryExtension = project.extensions.getByType(LibraryExtension::class.java)
        libraryExtension.publishing {
            this.singleVariant("release") {
                this.withSourcesJar()
                this.withJavadocJar()
            }
        }
    }

    /**
     * 如果要使用：PublishingExtension 扩展属性的话，必须要依赖于这个插件
     *
     * plugins {
     *      id "maven-publish"
     * }
     */
    private fun publishTask(project: Project, groupId: String, artifactId: String, version: String) {
        // 获取插件版本信息
        println("install---> groupId:$groupId artifactId:$artifactId version:$version")

        // 在所有的配置都完成之后执行
        project.afterEvaluate { project1: Project ->
            val publish = project1.extensions.getByType(PublishingExtension::class.java)
            publish.publications.create("release", MavenPublication::class.java, Action { maven: MavenPublication ->

//                groupId = "com.android.helper"
////                artifactId = getModelNameForNamespace() // 插件名称
////                version = latestGitTag().ifEmpty { Config.versionName } // 版本号

                maven.groupId = "com.github.xjxlx"
                maven.artifactId = "dimens"
                var gitVersion = VersionUtil.VERSION
                println("gitVersion:$gitVersion")
                if (TextUtil.isEmpty(gitVersion)) {
                    gitVersion = version
                }
                maven.version = gitVersion
                println("release---> groupId:${maven.groupId} artifactId:${maven.artifactId} version:${maven.version}")

                // 发布
                maven.from(project1.components.getByName("release"))
            })
        }
    }
}