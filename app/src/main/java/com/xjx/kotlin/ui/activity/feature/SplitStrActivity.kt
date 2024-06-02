package com.xjx.kotlin.ui.activity.feature

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivitySplitStrBinding

class SplitStrActivity() : BaseBindingTitleActivity<ActivitySplitStrBinding>() {
    override fun getTitleContent(): String {
        return "分割字符串"
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): ActivitySplitStrBinding {
        return ActivitySplitStrBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val str = "hello word#1 ; I#1; am#2; doctor#4;"
        val builder = SpannableStringBuilder()

        // 1：给内容赋值
        mBinding.tvStr.text = "字符串内容:$str"

        mBinding.btnStart.setOnClickListener {
            val split = str.split(";")
            var start = 0

            split.forEach { item ->
                val splitAction = item.split("#")
                if (splitAction.isNotEmpty()) {
                    val actionContent = splitAction[0].trim()
                    if (actionContent.isNotEmpty()) {
                        LogUtil.e("splitAction:$splitAction")
                        val actionType = splitAction[1]
                        // 添加内容
                        builder.append(actionContent)
                        val length = actionContent.length
                        val end = start + length

                        LogUtil.e("item:$actionContent start:$start end:$end")

                        // 添加点击事件
                        builder.setSpan(
                            object : ClickableSpan() {
                                @SuppressLint("SetTextI18n")
                                override fun onClick(widget: View) {
                                    // 获取被点击的文本内容
                                    val spannable = (widget as? TextView)?.text as? Spannable
                                    spannable?.let {
                                        val spanStart = it.getSpanStart(this)
                                        val spanEnd = it.getSpanEnd(this)
                                        val clickedText = it.subSequence(spanStart, spanEnd)
                                        // 处理被点击的文本内容
                                        LogUtil.e("spans:$clickedText actionType:$actionType")
                                        mBinding.btnClickResult.text =
                                            "text:[$clickedText] type:[$actionType]"
                                    }
                                }

                                override fun updateDrawState(ds: TextPaint) {
                                    super.updateDrawState(ds)
                                    ds.isUnderlineText = false // 设置不显示下划线
                                }
                            },
                            start,
                            end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        start += length
                    }
                }
            }
            // 必须调用此方法使点击事件生效
            mBinding.btnResult.movementMethod = LinkMovementMethod.getInstance()
            mBinding.btnResult.setText(builder, TextView.BufferType.SPANNABLE)
            // 设置点击后的背景颜色
            mBinding.btnResult.highlightColor = Color.TRANSPARENT
        }
    }
}
