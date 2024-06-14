package com.xjx.kotlin.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import com.android.common.base.BaseView
import com.android.common.utils.ResourcesUtil
import com.xjx.kotlin.R

class BitmapView(
    context: Context,
    attributeSet: AttributeSet
) : BaseView(context, attributeSet, 0) {
    private val mRoundWidth =
        ResourcesUtil
            .px(160F)
            .toInt()
    private val mRoundHeight =
        ResourcesUtil
            .px(208F)
            .toInt()

    private val mRoundBitmap: Bitmap? by lazy {
        AppCompatResources
            .getDrawable(context, R.drawable.icon_selector_time_bg)
            ?.let {
                if (it is BitmapDrawable) {
                    return@lazy resizeImage(it.bitmap, mRoundWidth, mRoundHeight)
                }
            }
        return@lazy null
    }
    private val mPaint =
        Paint().also {
            it.isAntiAlias = true
        }

    override fun initView(
        context: Context,
        attrs: AttributeSet?
    ) {
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(mRoundWidth, mRoundHeight)
//    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mRoundBitmap?.let {
            val left = (measuredWidth - mRoundWidth) / 2
            val top = (measuredHeight - mRoundHeight) / 2
            val right = left + mRoundWidth
            val bottom = top + mRoundHeight

            //  src : 图片的尺寸
            val src = Rect(0, 0, mRoundWidth, mRoundHeight)
            // dst：图片在画布中的位置
            val dst = Rect(left, top, right, bottom)
            canvas.drawBitmap(it, src, dst, mPaint)
        }
    }

    private fun resizeImage(
        bitmap: Bitmap,
        width: Int,
        height: Int
    ): Bitmap? {
        val bmpWidth = bitmap.width
        val bmpHeight = bitmap.height
        val scaleWidth = width.toFloat() / bmpWidth
        val scaleHeight = height.toFloat() / bmpHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true)
    }
}
