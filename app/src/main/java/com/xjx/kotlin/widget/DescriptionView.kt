package com.xjx.kotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.IntRange
import com.xjx.kotlin.R

class DescriptionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#80FFFFFF")
    }
    var score: Int = -1
    var description: String? = null
        set(value) {
            field = value
            invalidate()
        }
    private val minSize = 18
    private val maxSize = 28
    private val lineHeightOfFontSize = 11
    private val titleTextSize = 32F
    private val titleTextColor = Color.WHITE
    private val titleText = resources.getString(R.string.theme_finish_score_desc)
    private val headTextSize = 40F
    private val bgColor = Color.parseColor("#33969696")
    private val bgRadius = 20F
    private var startPadding = 48F
    private var endPadding = 48F

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = bgColor
        canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), bgRadius, bgRadius, paint)
        if (score < 0) return
        // head
        paint.textSize = headTextSize
        paint.color = getScoreTitleTextColor(score)
        canvas.drawText(resources.getString(getScoreTitleStringRes(score)), startPadding, 52 - (paint.descent()
            .plus(paint.ascent())
            .div(2F)), paint)
        // title
        paint.textSize = titleTextSize
        paint.color = titleTextColor
        canvas.drawText(titleText, startPadding, 114.5F - (paint.descent()
            .plus(paint.ascent())
            .div(2F)), paint)
        // des
        val textSize = calculationFontSize()
        logE("calculationFontSize = $textSize")
        paint.textSize = textSize.toFloat()
        val charArray = description?.toCharArray() ?: return
        val maxWidth = width - startPadding - endPadding
        var startIndex = 0
        var currentLineCount: Int
        var drawY = 153 + (textSize + lineHeightOfFontSize - paint.descent() - paint.ascent()) / 2F
        while (startIndex < charArray.size) {
            currentLineCount = paint.breakText(charArray, startIndex, charArray.size - startIndex, maxWidth, null)
            canvas.drawText(charArray, startIndex, currentLineCount, startPadding, drawY, paint)
            startIndex += currentLineCount
            drawY += textSize + lineHeightOfFontSize
        }
    }

    private fun calculationFontSize(): Int = with(paint) {
        val w = width - startPadding - endPadding
        val h = height - 153 - 30
        val textArray = description?.toCharArray()
        if (textArray?.isNotEmpty() == true && w > 0 && h > 0) {
            (minSize..maxSize).forEach {
                textSize = it.toFloat()
                var startIndex = 0
                var lineCount = 0
                while (startIndex < textArray.size) {
                    lineCount++
                    startIndex += breakText(textArray, startIndex, textArray.size - startIndex, w, null)
                }
                val lineHeight = it + lineHeightOfFontSize
                val textHeight = lineCount * lineHeight
                if (h - textHeight <= lineHeight) {
                    return@with it
                }
            }
        }
        maxSize
    }

    private fun getScoreTitleStringRes(@IntRange(from = 0, to = 100) score: Int): Int {
        return when (score) {
            in 0..20 -> R.string.theme_finish_try_again
            in 20..40 -> R.string.theme_finish_no_bad
            in 40..60 -> R.string.theme_finish_very_good
            in 60..80 -> R.string.theme_finish_great
            in 80..100 -> R.string.theme_finish_super
            else -> R.string.theme_finish_try_again
        }
    }

    private fun getScoreTitleTextColor(@IntRange(from = 0, to = 100) score: Int): Int {
        return when (score) {
            in (71..100) -> Color.parseColor("#57AB64")
            in (31..70) -> Color.parseColor("#EDD452")
            in (1..30) -> Color.parseColor("#E26666")
            else -> Color.WHITE
        }
    }

    private fun logE(msg: String) {
        Log.e(this::class.simpleName, msg)
    }
}