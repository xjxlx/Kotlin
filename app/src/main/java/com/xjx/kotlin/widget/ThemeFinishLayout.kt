package com.xjx.kotlin.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils

class ThemeFinishLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    private val descriptionView = DescriptionView(context)

    init {
        setBackgroundColor(ColorUtils.setAlphaComponent(Color.BLACK, 0X80))
        addView(descriptionView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun measureChild(child: View?, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(parentWidthMeasureSpec)
        val h = MeasureSpec.getSize(parentHeightMeasureSpec)
        val childWidthMeasureSpec: Int
        val childHeightMeasureSpec: Int
        when (child) {
            descriptionView -> {
                val descWidth = (w - h * 0.43) * 464.0 / 1501.0
                val descHeight = 300.0 / 696.0 * h
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(descWidth.toInt(), MeasureSpec.EXACTLY)
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(descHeight.toInt(), MeasureSpec.EXACTLY)
            }
            else -> {
                childHeightMeasureSpec = parentHeightMeasureSpec
                childWidthMeasureSpec = parentWidthMeasureSpec
            }
        }
        child?.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val h = bottom - top
        val w = right - left
        // scoreView + progressView

        // descriptionView
        with(descriptionView) {
            val l = (right - measuredWidth) / 2
            val t = h * 167.0 / 696.0
            val r = l + measuredWidth
            val b = t + measuredHeight
            layout(l.toInt(), t.toInt(), r.toInt(), b.toInt())
        }
    }

    fun onScore(score: Int, exhaleScore: Int, inhaleScore: Int, holdScore: Int, comment: String?) {
        descriptionView.score = score
        descriptionView.description = comment
    }
}
