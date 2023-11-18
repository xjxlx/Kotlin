package com.xjx.kotlin.ui.activity.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.base.BaseBindingTitleActivity;
import com.xjx.kotlin.databinding.ActivityTestHttp1Binding;

public class TestHttp1Activity extends BaseBindingTitleActivity<ActivityTestHttp1Binding> {

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBinding.btn1.setText("侧石");
    }

    @NonNull
    @Override
    public ActivityTestHttp1Binding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityTestHttp1Binding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "测试网络连接-1";
    }
}