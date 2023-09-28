// 1：添加发布插件的依赖
plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.0.0-rc-1"
}

// 2：配置发布到远程仓库的地址信息
pluginBundle {
    // 为您的插件项目设置网站。
    website = "https://github.com/xjxlx/plugins/blob/main/publish/README.md"
    // 提供源存储库 URI，以便其他人在想要贡献时可以找到它。
    vcsUrl = "https://github.com/xjxlx/plugins"
    // 设置要用于所有插件的标签，除非在块中被覆盖。plugins,插件的tag。可以通过这个在插件门户上搜索
    tags = listOf("publish", "android", "plugins")
}

// 3：配置版本的group 和 version
/**
 * group:
 *      1:配置版本的group 和 version
 *      2:这里的group是顶级的域名，下面的id必须是以这个为开始
 *      3:不能是以com.android等已知的域名作为开始
 *      4:如果是要发布到gradle的门户网站上，需要使用io.github.{userName}作为域名
 */
group = "io.github.xjxlx"
version = "1.0.0"

// 4：配置发布插件的信息
gradlePlugin {
    plugins {
        create("greetingsPlugin") {
            // id：必须是以group作为开头的
            id = "${group}.publish"
            // 简称
            displayName = "publishPlugin"
            // 简介
            description = "<Good human-readable description of what your plugin is about>"
            // 插件路径，必须是以全路径作为名称
            implementationClass = "com.android.plugin.PublishPlugin"
        }
    }
}

/**
 * 5：如果需要配置到本地环境的话，需要加入本地的maven
 */
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}

/**
 * 6：gradle的依赖
 */
dependencies {
    // gradle sdk
    implementation(gradleApi())
}