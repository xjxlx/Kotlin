package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.recycleview.BaseRecycleViewFramework
import com.android.common.base.recycleview.BaseVH
import com.android.common.utils.RecycleUtil
import com.android.http.client.RetrofitHelper
import com.xjx.kotlin.databinding.ActivityNetWorkRefreshBinding
import com.xjx.kotlin.databinding.ItemNetRefreshBinding

class NetWorkRefreshActivity : BaseBindingTitleActivity<ActivityNetWorkRefreshBinding>() {

    private val adapter: NetAdapter = NetAdapter()
    private val mList: MutableList<String> = mutableListOf()

    override fun initData(savedInstanceState: Bundle?) {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/")

        RecycleUtil.getInstance(this, mBinding.rvList).setVertical().setAdapter(adapter)

        mBinding.btnStart.setOnClickListener {
            mList.clear()

            val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
            val suiJi = "newcL6_2"
            val mParameters = mutableMapOf<String, Any>()
            mParameters["service"] = "App.App2022.GetBook"
            mParameters["unid"] = unId
            mParameters["suiji"] = suiJi

//            object : RefreshUtil<HttpResponse<L6HomeRightBookListBean>>(lifecycleScope, mBinding.brlLayout) {
//                override suspend fun getApiService(): Flow<HttpResponse<L6HomeRightBookListBean>> {
//                    return HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>(
//                        { getL6BookList(it) }, mParameters)
//                }
//
//                override fun setMoreData(t: HttpResponse<L6HomeRightBookListBean>): List<*>? {
//                    return t.data?.row1
//                }
//            }.setCallBackListener(object : CallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
//                override fun onSuccess(refreshUtil: RefreshUtil<HttpResponse<L6HomeRightBookListBean>>,
//                                       t: HttpResponse<L6HomeRightBookListBean>) {
//                }
//
//                override fun onError(e: Throwable) {
//                }
//            })
//                .start()

//            lifecycleScope.launch {
//                val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
//                val suiJi = "newcL6_2"
//                val mParameters = mutableMapOf<String, Any>()
//                mParameters["service"] = "App.App2022.GetBook"
//                mParameters["unid"] = unId
//                mParameters["suiji"] = suiJi
//
//                HttpClient.http<TestApiService, MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ getL6BookList(it) },
//                    mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
//
//                        override fun onFailure(exception: Throwable) {
//                            super.onFailure(exception)
//                            LogUtil.e("sss", "error:${exception}")
//                        }
//
//                        override fun onSuccess(t: HttpResponse<L6HomeRightBookListBean>) {
//                            LogUtil.e("sss", t)
//                            t.data?.row2?.let {
//                                for (index in it.indices) {
//                                    val row2 = it[index]
//                                    row2?.let { row ->
//                                        row.content?.let { content ->
//                                            for (index2 in content.indices) {
//                                                val content1 = content[index2]
//                                                content1?.let { content11 ->
//                                                    content11.content_name?.let { name ->
//                                                        mList.add(name)
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            adapter.setList(mList)
//                        }
//                    })
//            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityNetWorkRefreshBinding {
        return ActivityNetWorkRefreshBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "网络刷新列表"
    }

    class NetAdapter : BaseRecycleViewFramework<String, NetAdapter.VH>() {

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