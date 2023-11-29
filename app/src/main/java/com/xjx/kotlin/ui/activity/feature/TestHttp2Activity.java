package com.xjx.kotlin.ui.activity.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.ToastUtil;
import com.xjx.kotlin.databinding.ActivityTestHttp2Binding;

public class TestHttp2Activity extends BaseBindingTitleActivity<ActivityTestHttp2Binding> {

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBinding.btStart.setOnClickListener(v -> {
            ToastUtil.show("网络测试");
            //                ToastUtil.show("网络测试");
        });
    }

    @NonNull
    @Override
    public ActivityTestHttp2Binding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityTestHttp2Binding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "网络测试 - 2";
    }
}