package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.base.recycleview.BaseVH
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityTestHttp1Binding

class TestHttp1Activity : BaseBindingTitleActivity<ActivityTestHttp1Binding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btn1.text = "侧石"
    }

    internal inner class BaseAdapter<T : BaseVH> {
        fun convert(t: T) {
            val baseVH = t as VHChild
        }
    }

    internal class VHChild(root: View) : BaseVH(root)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestHttp1Binding {
        return ActivityTestHttp1Binding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "测试网络连接-1"
    }

    open class VH(view: View) : RecyclerView.ViewHolder(view) {}

    class ChildVH(view: View) : VH(view) {}

    class Empty(view: View) : VH(view) {}

    class A : BashAdapter2<String, ChildVH>() {
        override fun bindViewHolder(holder: ChildVH) {
        }

        override fun createVH(layoutInflater: LayoutInflater, viewType: Int): ChildVH {
            return ChildVH(layoutInflater.inflate(R.layout.activity_aidl, null, false))
        }
    }

    abstract class BashAdapter2<T, E : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        //        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): E {
//            if (viewType == 1) {
//                return createVH(layoutInflater = LayoutInflater.from(parent.context), viewType)
//            } else {
//                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.activity_aidl, null, false)
//                val empty = Empty(inflate)
//                return empty
//            }
//        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 1) {
                return createVH(layoutInflater = LayoutInflater.from(parent.context), viewType)
            } else {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.activity_aidl, null, false)
                val empty = Empty(inflate)
                return empty
            }
        }

        override fun getItemCount(): Int {
            return 1
        }

//        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            val holder1 = holder
//            if (holder is) bindViewHolder(holder1)
//        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }

        abstract fun createVH(layoutInflater: LayoutInflater, viewType: Int): E

        abstract fun bindViewHolder(holder: E)
    }
}