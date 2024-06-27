package com.android.hcp3

import com.android.hcp3.Config.BASE_OUT_PUT_PATH
import com.android.hcp3.FileUtil.changePackage
import com.android.hcp3.FileUtil.deleteFileImport
import com.android.hcp3.FileUtil.moveFile
import com.android.hcp3.GenerateUtil.LOCAL_NODE_FILE_LIST
import com.android.hcp3.StringUtil.transitionPath
import java.nio.file.Paths

object TaskUtil {
    @JvmStatic
    fun main(args: Array<String>) {
        // 1:先去读取jar，并生成本地文件
        ReadJarFile.execute()

        // 2:遍历本地的文件夹，去修改文件的位置以及内容，目前只去过滤枚举和枚举的集合类型
        LOCAL_NODE_FILE_LIST
            .filter { local ->
                (local.classTypeEnum == ClassTypeEnum.ENUM) || (local.classTypeEnum == ClassTypeEnum.LIST_ENUM)
            }.forEach { local ->
                val parentSet = local.parentSet
                // 3：只有parent的数量等于1的时候，才有移动的价值
                if (parentSet.size == 1) {
                    val originFileName = local.localFileName
                    val originFilePackage = local.localFileParentPackage
                    val parentBean = parentSet.toMutableList()[0]
                    val newPackage = parentBean.parentPath
                    val parentEntityName = parentBean.parentEntityName
                    println("需要处理的文件:[$originFileName] 原始的路径:[$originFilePackage] 父类的信息：$parentBean")
                    // 4：开始移动文件
                    val originPath =
                        transitionPath(
                            Paths
                                .get(BASE_OUT_PUT_PATH)
                                .resolve(Paths.get(originFilePackage))
                                .resolve(Paths.get(originFileName))
                                .toString()
                        )
                    val newPath = Paths.get(BASE_OUT_PUT_PATH).resolve(Paths.get(transitionPath(newPackage))).toString()
                    val moveFile = moveFile("$originPath.java", newPath)

                    // 5：只有移动成功了，才去改变文件的import名字
                    if (moveFile) {
                        val currentFilePath = Paths.get(newPath).resolve(originFileName).toString() + ".java"
                        changePackage(currentFilePath, originFilePackage, newPackage)

                        // 6：删除被引用类中import的导包信息
                        val deleteFilePath =
                            Paths
                                .get(BASE_OUT_PUT_PATH)
                                .resolve(Paths.get(transitionPath(newPackage)))
                                .resolve(parentEntityName)
                                .toString() + ".java"
                        val deleteImport = "$originFilePackage.$originFileName"
                        deleteFileImport(deleteFilePath, deleteImport)
                    }
                }
            }

        println()
    }
}
