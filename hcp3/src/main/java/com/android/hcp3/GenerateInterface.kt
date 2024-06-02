package com.android.hcp3

object GenerateInterface {
    @JvmStatic
    fun main(args: Array<String>) {
        ReadJarFile.readNeedDependenciesClassName()
        GenerateUtil.generateInterface()
    }
}
