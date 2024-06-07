package com.android.hcp3

import com.android.hcp3.GenerateUtil.LOCAL_NODE_FILE_LIST

object File2Util {
    @JvmStatic
    fun main(args: Array<String>) {
        ReadJarFile.execute()
        LOCAL_NODE_FILE_LIST.forEach { item ->
            println("item: ${item.name}  set:${item.parentSet}")
        }

//        LOCAL_NODE_FILE_LIST.forEach { enum ->
//            // 只有在小于等于1的时候，才会去移动文件
//            if (enum.count <= 1) {
//                val parentPath = enum.parentPath
//                // 一：移动文件到新包中去
//                val moveFile = moveFile(enum.path, parentPath)
//                if (moveFile) {
//                    // 二：获取挪移到新目录的路径
//                    val packagePath = Paths.get(enum.parentPath).resolve(Paths.get(enum.name)).toString() + ".java"
//                    // 2.1: 获取原始目录的路径
//                    val deletePackage: String =
//                        transitionPackage(
//                            Paths.get(BASE_PROJECT_PACKAGE_PATH).resolve(Paths.get(RSI_NODE_NAME)).toString()
//                        )
//                    // 2.2:获取到挪移后的包名
//                    val newPackage = getPackageForProjectPath(Paths.get(enum.parentPath).toString() + ".java")
//                    changePackage(packagePath, deletePackage, newPackage)
//
//                    // 三：改变import的内容
//                    // 3.1:被删除的import内容
//                    val deleteImport =
//                        transitionPackage(
//                            Paths.get(BASE_PROJECT_PACKAGE_PATH).resolve(Paths.get(RSI_NODE_NAME))
//                                .resolve(enum.name).toString()
//                        )
//                    deleteFileImport(enum.apiChildPath, deleteImport)
//                }
//            }
//        }

        LOCAL_NODE_FILE_LIST.forEach { local ->
            val parentSet = local.parentSet
            // 只有parent的数量等于1的时候，才有移动的价值
            if (parentSet.size == 1) {
                val moveBean = parentSet.toMutableList()[0]
                println("move:$moveBean")
            }
        }
    }
}
