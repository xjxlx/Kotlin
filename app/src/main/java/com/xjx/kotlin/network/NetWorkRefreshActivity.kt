package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.apphelper2.base.BaseBindingTitleActivity
import com.android.apphelper2.base.recycleview.BaseRecycleViewFragment
import com.android.apphelper2.base.recycleview.BaseVH
import com.android.apphelper2.utils.RecycleUtil
import com.android.helper.utils.LogUtil
import com.android.http.utils.client.HttpClient
import com.android.http.utils.client.RetrofitHelper
import com.android.http.utils.listener.HttpCallBackListener
import com.android.http.utils.test.HttpResponse
import com.android.http.utils.test.L6HomeRightBookListBean
import com.android.http.utils.test.TestApiService
import com.xjx.kotlin.databinding.ActivityNetWorkRefreshBinding
import com.xjx.kotlin.databinding.ItemNetRefreshBinding
import kotlinx.coroutines.launch

class NetWorkRefreshActivity : BaseBindingTitleActivity<ActivityNetWorkRefreshBinding>() {

    private val adapter: NetAdapter = NetAdapter()
    private val mList: MutableList<String> = mutableListOf()

    override fun initData(savedInstanceState: Bundle?) {
        RetrofitHelper.setBaseUrl("https://web.jollyeng.com/")

        RecycleUtil.getInstance(this, mBinding.rvList)
            .setVertical()
            .setAdapter(adapter)

//        object : RefreshUtil<String>() {
//            override fun setNoMoreData(t: String): List<*> {
//                return getCurrentData()
//            }
//        }.with(this, mBinding.brlLayout)
//            .setCallBackListener(object : RefreshCallBack<String>() {
//                override fun onSuccess(refreshUtil: RefreshUtil<String>, t: String) {
//                }
//
//                override fun onError(e: Throwable) {
//                }
//            })
//            .execute()

//        object : RefreshUtil<String>() {
//            override fun setNoMoreData(t: String): List<*> {
//               return
//            }
//        }.apply {
//            setCallBackListener(object : RefreshCallBack<String>() {
//                override fun onSuccess(refreshUtil: RefreshUtil<String>, t: String) {
//                }
//
//                override fun onError(e: Throwable) {
//                }
//            })
//            setAutoRefresh(true)
//            execute()
//        }

        mBinding.btnStart.setOnClickListener {
            mList.clear()
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
                            t.data?.row2?.let {
                                for (index in it.indices) {
                                    val row2 = it[index]
                                    row2?.let { row ->
                                        row.content?.let { content ->
                                            for (index2 in content.indices) {
                                                val content1 = content[index2]
                                                content1?.let { content11 ->
                                                    content11.content_name?.let { name ->
                                                        mList.add(name)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            adapter.setList(mList)
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