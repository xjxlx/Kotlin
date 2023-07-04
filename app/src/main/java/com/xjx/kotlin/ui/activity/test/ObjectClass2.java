package com.xjx.kotlin.ui.activity.test;

import com.xjx.kotlin.ui.activity.test.Test.staticClass;

/**
 * @author : 流星
 * @CreateDate: 2022/12/21-19:00
 * @Description:
 */
public class ObjectClass2 {
    int ab = 12;
    boolean isStop = false;

    public static void main(String[] args) {
        staticClass staticClass = new staticClass();
        Test.staticClass staticClass1 = new Test.staticClass();
    }

    class InnerClass {
        public void sss() {
            ab = 1;
            isStop = false;
        }
    }

    static class StaticClasss {
        public void sssddd() {

        }
    }
}
