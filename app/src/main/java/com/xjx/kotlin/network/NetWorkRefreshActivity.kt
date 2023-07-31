package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.android.apphelper2.base.recycleview.BaseRecycleViewFragment
import com.android.apphelper2.base.recycleview.BaseVH
import com.android.apphelper2.utils.RecycleUtil
import com.android.apphelper2.utils.httpclient.AutoInterceptor2
import com.android.apphelper2.utils.httpclient.HttpClient
import com.android.apphelper2.utils.httpclient.HttpLogInterceptor2
import com.android.apphelper2.utils.httpclient.RetrofitHelper
import com.android.apphelper2.utils.httpclient.listener.HttpCallBackListener
import com.android.apphelper2.utils.httpclient.test.HttpResponse
import com.android.apphelper2.utils.httpclient.test.L6HomeRightBookListBean
import com.android.apphelper2.utils.httpclient.test.TestApiService
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityNetWorkRefreshBinding
import com.xjx.kotlin.databinding.ItemNetRefreshBinding
import kotlinx.coroutines.launch

class NetWorkRefreshActivity : BaseBindingTitleActivity<ActivityNetWorkRefreshBinding>() {

    private val adapter: NetAdapter = NetAdapter()

    override fun initData(savedInstanceState: Bundle?) {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/")
        RetrofitHelper.addInterceptor(AutoInterceptor2())
        RetrofitHelper.addInterceptor(HttpLogInterceptor2())

        RecycleUtil.getInstance(this, mBinding.rvList)
            .setVertical()
            .setAdapter(adapter)

        mBinding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
                val suiJi = "newcL6_2"
                val mParameters = mutableMapOf<String, Any>()
                mParameters["service"] = "App.App2022.GetBook"
                mParameters["unid"] = unId
                mParameters["suiji"] = suiJi

                HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(it) },
                    mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                        override fun onFailure(exception: Throwable) {
                            super.onFailure(exception)
                            LogUtil.e("sss", "error:${exception}")
                        }

                        override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
                            LogUtil.e("sss", t)
                        }
                    })
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityNetWorkRefreshBinding {
        return ActivityNetWorkRefreshBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "网络刷新列表"
    }

    class NetAdapter : BaseRecycleViewFragment<String, NetAdapter.VH>() {

        class VH(binding: ItemNetRefreshBinding) : BaseVH(binding.root) {
            val tvContent = binding.tvContent
        }

        override fun onBindViewHolders(holder: VH, position: Int) {
            holder.tvContent.text = mList[position]
        }

        override fun createVH(viewType: Int, parent: ViewGroup): VH {
            return VH(ItemNetRefreshBinding.inflate(mLayoutInflater, parent, false))
        }
    }
}