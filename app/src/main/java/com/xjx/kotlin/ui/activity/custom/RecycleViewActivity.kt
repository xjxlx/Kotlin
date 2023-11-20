package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH
import com.android.common.utils.GsonUtil
import com.android.common.utils.LogUtil
import com.android.common.utils.RecycleUtil
import com.google.gson.Gson
import com.xjx.kotlin.R
import com.xjx.kotlin.bean.HttpRequest
import com.xjx.kotlin.bean.ZmqBean
import com.xjx.kotlin.databinding.ActivityRecycleViewBinding

class RecycleViewActivity : BaseBindingTitleActivity<ActivityRecycleViewBinding>() {

    private val adapter by lazy {
        return@lazy Adapter()
    }
    private val gson = Gson()
    //    private val gsonUtil: GsonUtil = GsonUtil()

    override fun getTitleContent(): String {
        return "RecycleView"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityRecycleViewBinding {
        return ActivityRecycleViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val list = arrayListOf<String>()
        for (item in 0..50) {
            list.add("" + item)
        }

        mBinding.btnGson.setOnClickListener {
            val bean = ZmqBean()
            val http = HttpRequest(code = 1, msg = "success", data = bean)

            val toJson = gson.toJson(http)
            LogUtil.e("json ", "json ---> $toJson")

            //            val type = object : TypeToken<HttpRequest<ZmqBean>>() {}.type
            //            val fromJson = gson.fromJson<HttpRequest<ZmqBean>>(toJson, type)
            //            LogUtil.e("fromJson ", "fromJson ---> $fromJson")

            //            val student = Student("zs", 19)
            //            val toJson = gson.toJson(student)

            //            val fromJson = gsonUtil.fromJson<Student>(toJson)

            val fromJsonNested = GsonUtil.fromJsonNested<HttpRequest<ZmqBean>>(toJson)
            LogUtil.e("fromJson ", "fromJson ---> $fromJsonNested")
        }

        RecycleUtil.getInstance(this, mBinding.rvList).setVertical().setAdapter(adapter)
        adapter.setList(list)
        mBinding.rvList.scrollToPosition(2)
    }

    data class Student(var name: String = "", var age: Int = 0)

    class Adapter : BaseRecycleViewAdapter<String, VH>() {

        override fun bindHolder(holder: VH, position: Int) {
            holder.tv_content.text = mList[position]
        }

        override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH {
            return VH(inflater.inflate(R.layout.item_test_recycle, parent, false))
        }
    }

    class VH(root: View) : BaseVH(root) {
        val tv_content = root.findViewById<TextView>(R.id.tv_content)
    }
}
