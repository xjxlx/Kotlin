package com.xjx.kotlin.ui.activity.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.base.BaseBindingTitleActivity;
import com.android.common.utils.LogUtil;
import com.android.http.client.HttpClient;
import com.android.http.client.RetrofitHelper;
import com.android.http.test.HttpResponse;
import com.android.http.test.L6HomeRightBookListBean;
import com.android.http.test.TestApiService;
import com.android.refresh.utils.CallBackListener;
import com.android.refresh.utils.RefreshUtil;
import com.xjx.kotlin.databinding.ActivityTestHttp2Binding;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class TestHttp2Activity extends BaseBindingTitleActivity<ActivityTestHttp2Binding> {

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/");
        TestApiService api = HttpClient.getApi(TestApiService.class);

        String unId = "o9RWl1EJPHolk8_7smU39k1-LqVs";
        String suiJi = "newcL6_2";
        HashMap<String, Object> map = new HashMap<>();
        map.put("service", "App.App2022.GetBook");
        map.put("unid", unId);
        map.put("suiji", suiJi);

        mBinding.btStart.setOnClickListener(v -> {
            RefreshUtil.<HttpResponse<L6HomeRightBookListBean>>instance(mBinding.brlLayout)
                       .setBuilder(new RefreshUtil.CallBuilder<HttpResponse<L6HomeRightBookListBean>>() {
                           @NonNull
                           @Override
                           public Call<HttpResponse<L6HomeRightBookListBean>> getApiServer() {
                               return api.getL6BookListJava(map);
                           }

                           @Nullable
                           @Override
                           public List<?> setMoreData(HttpResponse<L6HomeRightBookListBean> data) {
                               return Objects.requireNonNull(data.getData())
                                             .getRow1();
                           }
                       })
                       .setCallBackListener(new CallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                           @Override
                           public void onSuccess(@NonNull RefreshUtil<HttpResponse<L6HomeRightBookListBean>> refreshUtil,
                                   HttpResponse<L6HomeRightBookListBean> data) {
                               LogUtil.e("http-java:" + data);
                           }

                           @Override
                           public void onError(@NonNull Throwable e) {
                               LogUtil.e("http-java:error:" + e.getMessage());
                           }
                       })
                       .start();

            //            HttpClient.http(api.getL6BookListJava(map), new HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
            //                @Override
            //                public void onSuccess(HttpResponse<L6HomeRightBookListBean> result) {
            //                    LogUtil.e("http-java:" + result);
            //                }
            //
            //                @Override
            //                public void onFailure(@NonNull Throwable exception) {
            //                    super.onFailure(exception);
            //                    LogUtil.e("http-java:error:" + exception.getMessage());
            //                }
            //            });

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