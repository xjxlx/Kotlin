package com.xjx.kotlin.ui.activity.test.fx;

import java.util.ArrayList;

/**
 * @author : 流星
 * @CreateDate: 2023/3/27-23:25
 * @Description:
 */
public class TestFx1 {
    public static void main(String[] args) {
        Class stringType = new ArrayList<String>().getClass();
        Class integerType = new ArrayList<Integer>().getClass();
        System.out.println("类型参数是否相同：" + ((stringType == integerType) ? "相同" : "不相同"));
    }
}
