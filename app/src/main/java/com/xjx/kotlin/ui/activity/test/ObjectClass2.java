package com.xjx.kotlin.ui.activity.test;

import com.xjx.kotlin.ui.activity.test.Test.InnerClass;
import com.xjx.kotlin.ui.activity.test.Test.staticClass;
import com.xjx.kotlin.ui.activity.test.utils.FileUtil;

import java.io.File;

/**
 * @author : 流星
 * @CreateDate: 2022/12/21-19:00
 * @Description:
 */
public class ObjectClass2 {
    int ab = 12;

    public static void main(String[] args) {
        String filePath = FileUtil.getFilePath(new File(""));

        staticClass staticClass = new staticClass();
        Test.staticClass staticClass1 = new Test.staticClass();

    }

    class InnerClass {

    }

    static class StaticClasss {

    }
}
