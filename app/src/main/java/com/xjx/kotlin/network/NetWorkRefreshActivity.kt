package com.xjx.kotlin.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.recycleview.BaseBindingVH
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH
import com.android.common.utils.LogUtil
import com.android.common.utils.RecycleUtil
import com.android.http.client.HttpClient
import com.android.http.client.RetrofitHelper
import com.android.http.listener.HttpCallBackListener
import com.android.http.test.HttpResponse
import com.android.http.test.L6HomeRightBookListBean
import com.android.http.test.TestApiService
import com.android.refresh.utils.CallBackListener
import com.android.refresh.utils.RefreshUtil
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityNetWorkRefreshBinding
import com.xjx.kotlin.databinding.ItemNetRefreshBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NetWorkRefreshActivity : BaseBindingTitleActivity<ActivityNetWorkRefreshBinding>() {

    private val adapter: NetAdapter = NetAdapter()
    private val mList: MutableList<String> = mutableListOf()
    private val mApi: TestApiService by lazy {
        return@lazy HttpClient.getApi()
    }

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

            object : RefreshUtil<HttpResponse<L6HomeRightBookListBean>>(lifecycleScope, mBinding.brlLayout) {
                override suspend fun getApiService(): Flow<HttpResponse<L6HomeRightBookListBean>> {
                    return HttpClient.Flow.http({ mApi.getL6BookList(it) }, mParameters)
                }

                override fun setMoreData(t: HttpResponse<L6HomeRightBookListBean>): List<*>? {
                    return t.data?.row1
                }
            }.setCallBackListener(object : CallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
                override fun onSuccess(refreshUtil: RefreshUtil<HttpResponse<L6HomeRightBookListBean>>, t: HttpResponse<L6HomeRightBookListBean>) {
                }

                override fun onError(e: Throwable) {
                }
            }).start()


            lifecycleScope.launch {
                val unId = "o9RWl1EJPHolk8_7smU39k1-LqVs"
                val suiJi = "newcL6_2"
                val mParameters = mutableMapOf<String, Any>()
                mParameters["service"] = "App.App2022.GetBook"
                mParameters["unid"] = unId
                mParameters["suiji"] = suiJi

                HttpClient.httpCoroutine<MutableMap<String, Any>, HttpResponse<L6HomeRightBookListBean>>({ mApi.getL6BookList(it) }, mParameters, object : HttpCallBackListener<HttpResponse<L6HomeRightBookListBean>>() {
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

    class NetAdapter : BaseRecycleViewAdapter<String, NetAdapter.VhBinding>() {
        class VH(view: View) : BaseVH(view) {
            val tvContent = view.findViewById<TextView>(R.id.tv_content)
        }

        class VhBinding(binding: ItemNetRefreshBinding) : BaseBindingVH<ItemNetRefreshBinding>(binding)

        override fun bindHolder(holder: VhBinding, position: Int) {
            holder.binding.tvContent
        }

//        override fun bindHolder(holder: VH2, position: Int) {
//             holder.binding.tvContent.text= mList[position]
//        }

        override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VhBinding {
//            return VH(inflater.inflate(R.layout.item_net_refresh, parent, false))
            return VhBinding(ItemNetRefreshBinding.inflate(inflater, parent, false))
        }
    }
}
