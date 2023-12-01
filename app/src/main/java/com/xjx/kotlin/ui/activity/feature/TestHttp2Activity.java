package com.xjx.kotlin.ui.activity.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.ToastUtil;
import com.android.http.client.HttpClient;
import com.android.http.test.TestApiService;
import com.xjx.kotlin.databinding.ActivityTestHttp2Binding;

import java.util.HashMap;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

public class TestHttp2Activity extends BaseBindingTitleActivity<ActivityTestHttp2Binding> {

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBinding.btStart.setOnClickListener(v -> {
            ToastUtil.show("网络测试");
            //                ToastUtil.show("网络测试");
            TestApiService api = HttpClient.getApi(TestApiService.class);
            // getL6BookList3
            HashMap<String, Object> map = new HashMap<>();
            //            api.getL6BookList3()

            api.getL6BookList3(map, new Continuation<String>() {
                @NonNull
                @Override
                public CoroutineContext getContext() {
                    return null;
                }

                @Override
                public void resumeWith(@NonNull Object o) {

                }
            });
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