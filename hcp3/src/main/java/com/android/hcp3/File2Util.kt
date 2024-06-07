package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.FileUtil.changePackage
import com.android.hcp3.FileUtil.deleteFileImport
import com.android.hcp3.FileUtil.moveFile
import com.android.hcp3.GenerateUtil.LOCAL_NODE_FILE_LIST
import com.android.hcp3.StringUtil.transitionPath
import java.nio.file.Paths

object File2Util {
    @JvmStatic
    fun main(args: Array<String>) {
        ReadJarFile.execute()

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
            // 1：只有parent的数量等于1的时候，才有移动的价值
            if (parentSet.size == 1) {
                val originFileName = local.name
                val originFilePackage = local.attributePackage
                val parentBean = parentSet.toMutableList()[0]
                val newPackage = parentBean.parentPath
                val parentEntityName = parentBean.parentEntityName
                println("需要处理的文件:[$originFileName] 原始的路径:[$originFilePackage] 父类的信息：$parentBean")
                // 2：开始移动文件
                val originPath =
                    transitionPath(
                        Paths.get(BASE_OUT_PUT_PATH)
                            .resolve(Paths.get(originFilePackage))
                            .resolve(Paths.get(originFileName)).toString()
                    )
                val newPath =
                    Paths.get(BASE_OUT_PUT_PATH)
                        .resolve(Paths.get(transitionPath(newPackage)))
                        .toString()
                val moveFile = moveFile("$originPath.java", newPath)

                // 3：只有移动成功了，才去改变文件的import名字
                if (moveFile) {
                    val currentFilePath = Paths.get(newPath).resolve(originFileName).toString() + ".java"
                    changePackage(currentFilePath, originFilePackage, newPackage)

                    // 4：删除被引用类中import的导包信息
                    val deleteFilePath =
                        Paths.get(BASE_OUT_PUT_PATH)
                            .resolve(Paths.get(transitionPath(newPackage)))
                            .resolve(parentEntityName).toString() + ".java"
                    val deleteImport = "$originFilePackage.$originFileName"
                    deleteFileImport(deleteFilePath, deleteImport)
                }
            }
        }
    }

    private fun moveFile() {
    }
}
