package com.xjx.kotlin.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class DrawLine(context: Context, attrs: AttributeSet) : View(context, attrs) {

	private val paint1 = Paint().also {
		it.strokeCap = Paint.Cap.ROUND
		it.strokeJoin = Paint.Join.BEVEL
		it.style = Paint.Style.FILL
		it.color = Color.YELLOW
	}
	private val paint2 = Paint().also {
		it.style = Paint.Style.STROKE
		it.strokeCap = Paint.Cap.SQUARE
		it.strokeJoin = Paint.Join.BEVEL
		it.strokeWidth = 50f
		it.color = Color.BLUE
	}
	private val path1 = Path()
	private val path2 = Path()

	private val rectF = RectF(100F, 100F, 150F, 300F)
	private val radii = floatArrayOf(0F, 0F, 0F, 0F, 25F, 25F, 25F, 25F)

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

//		canvas.drawColor(Color.WHITE)
//		val saveCount = canvas.save()
//
//		// 在 save() 之后对画布进行变换
//		canvas.translate(100f, 100f)
//		canvas.rotate(45f)
//		// 绘制一个矩形
//		canvas.drawRect(RectF(100f, 100f, 200f, 200f), paint1)
//
//		// 恢复画布状态
//		canvas.restoreToCount(saveCount)
//		// 在恢复之后绘制另一个矩形
//		paint1.color = Color.GREEN
//		canvas.drawRect(RectF(0f, 0f, 100f, 100f), paint1)

		// 绘制直线
//		if (chzx) {
//			val save = canvas.save()
//			val rect1 = RectF(100F, 100F, 150F, 300F)
//			val radii1 = floatArrayOf(25f, 25f, 25f, 25f, 0f, 0f, 0f, 0f)
//			path1.addRoundRect(rect1, radii1, Path.Direction.CW)
//			canvas.drawPath(path1, paint1)
//			paint1.color = Color.GREEN
//			canvas.restoreToCount(save)
//		}
//
//		val rect2 = RectF(100F, 350F, 150F, 500F)
//		val radii2 = floatArrayOf(0F, 0F, 0F, 0F, 25F, 25F, 25F, 25F)
//		path2.addRoundRect(rect2, radii2, Path.Direction.CW)
//		canvas.drawPath(path2, paint1)

//		canvas.drawLine(100f, 100f, 200f, 100f, paint1)

		// 绘制圆形
//		path.addCircle(100f, 100f, 10f, Path.Direction.CW)

//		// 绘制直角
//		path.moveTo(300f, 100f)
//		path.lineTo(300f, 120f)
//		path.lineTo(320f, 120f)

//		val saveLayer = canvas.saveLayer(rectF, paint1)
//		path1.addRoundRect(rectF, radii, Path.Direction.CW)
//		flag = true
//		canvas.drawPath(path1, paint1)
//		canvas.restoreToCount(saveLayer)

		val startX = 100f
		val startY = 100f
		val endX = 400f
		val endY = 100f
		val cornerRadius = 20f

		// 绘制直角部分
		canvas.drawLine(startX, startY, endX - cornerRadius, endY, paint)

		// 绘制圆角部分
		path.reset()
		path.moveTo(endX - cornerRadius, endY)
		path.arcTo(RectF(endX - 2 * cornerRadius, endY - cornerRadius, endX, endY + cornerRadius), 270f, 180f, false)
		canvas.drawPath(path, paint)
	}

	private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val path = Path()
	fun ch1z() {
		invalidate()
	}

	fun ch1f() {
		invalidate()
	}

	private var flag = false
	fun animator() {
		flag = false
		ValueAnimator.ofFloat(0F, 1F).also {
			it.duration = 3000L
			it.addUpdateListener { value ->
				val animatorValue = value.animatedValue as Float
				rectF.bottom += animatorValue
				invalidate()
			}
			it.start()
		}
	}
}
