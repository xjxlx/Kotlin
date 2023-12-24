package com.xjx.kotlin.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class DrawLine(context: Context, attrs: AttributeSet) : View(context, attrs) {

	private val paint1 = Paint().also {
		it.strokeCap = Paint.Cap.ROUND
		it.strokeJoin = Paint.Join.BEVEL
		it.style = Paint.Style.FILL
		it.color = Color.WHITE
	}
	private val paint2 = Paint().also {
		it.style = Paint.Style.STROKE
		it.strokeCap = Paint.Cap.SQUARE
		it.strokeJoin = Paint.Join.BEVEL
		it.strokeWidth = 50f
		it.color = Color.WHITE
	}
	private val paint3 = Paint().also {
		it.style = Paint.Style.STROKE
		it.strokeCap = Paint.Cap.ROUND
		it.strokeJoin = Paint.Join.BEVEL
		it.strokeWidth = 50f
	}
	private val path = Path()

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		// 绘制直线
//		drawRoundRect
		path.moveTo(100f, 100f)
//		path.lineTo(100f, 300f)
		val rect = RectF(100f, 100f, 150f, 300f)
		val radii = floatArrayOf(25f, 25f, 25f, 25f, 0f, 0f, 0f, 0f)
		path.addRoundRect(rect, radii, Path.Direction.CW)
		path.moveTo(100f, 330f)
		path.lineTo(100f, 500f)

//		canvas.drawLine(100f, 100f, 200f, 100f, paint1)
//		canvas.drawLine(200f, 100f, 520f, 100f, paint2)
//		canvas.drawLine(500f, 100f, 600f, 100f, paint3)

//		canvas.drawPath(path, paint2)
		// 绘制圆形
//		path.addCircle(100f, 100f, 10f, Path.Direction.CW)

//		// 绘制直角
//		path.moveTo(300f, 100f)
//		path.lineTo(300f, 120f)
//		path.lineTo(320f, 120f)

		// 将路径绘制到画布上
//		canvas.drawPath(path, paint1)
		canvas.drawPath(path, paint1)
	}
}
