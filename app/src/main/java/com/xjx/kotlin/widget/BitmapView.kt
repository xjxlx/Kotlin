package com.xjx.kotlin.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import com.android.common.base.BaseView
import com.xjx.kotlin.R

class BitmapView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet) : BaseView(context, attributeSet, 0) {

    private val mRoundBitmap: Bitmap? by lazy {
        AppCompatResources.getDrawable(context, R.drawable.icon_selector_time_bg)
            ?.let {
                if (it is BitmapDrawable) {
                    return@lazy it.bitmap
                }
            }
        return@lazy null
    }

    override fun initView(context: Context, attrs: AttributeSet?) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mRoundBitmap?.let {
            canvas.drawBitmap(it, 0F, 0F, null)
        }
    }
}