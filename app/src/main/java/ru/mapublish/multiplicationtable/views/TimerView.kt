package ru.mapublish.multiplicationtable.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import ru.mapublish.multiplicationtable.R

class TimerView(context: Context, attrs: AttributeSet?) : View(context, attrs, 0) {


    private val paintSemiWhite = Paint().apply {
        color = ContextCompat.getColor(context, R.color.semi_white)
    }

    private val paintBlue = Paint().apply {
        color = ContextCompat.getColor(
            context,
            R.color.background_bluish
        )
    }

    init {
        paintBlue.isAntiAlias = true
        paintSemiWhite.isAntiAlias = true
    }

    private val oval = RectF()
    private var sweepAngle = 0f

    private val animator = ValueAnimator.ofFloat(0f, 100f)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        oval.set(0f, 0f, width.toFloat(), width.toFloat())

        canvas?.drawCircle(
            width.toFloat() / 2,
            height.toFloat() / 2,
            width.toFloat() / 2 - 4,
            paintSemiWhite
        )
        canvas?.drawArc(oval, -90F, sweepAngle, true, paintBlue)
    }

    fun start(dur: Long) {
        if (animator.isRunning) stop()

        animator.duration = dur
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            drawProgress(animatedValue)
        }
        animator.start()
    }

    fun stop() {
        animator.cancel()
    }


    private fun drawProgress(animatedValue: Float) {
        sweepAngle = 3.6f * animatedValue
        invalidate()
    }

}