package ru.mapublish.multiplicationtable.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.screens.StartFragmentDirections
import ru.mapublish.multiplicationtable.utils.Actions.DOWN_KEY
import ru.mapublish.multiplicationtable.utils.Actions.UP_KEY

class StartBtnView(context: Context, attrs: AttributeSet?) : View(context, attrs, 0) {

    private val paintWhite = Paint().apply {
        color = ContextCompat.getColor(context, R.color.white)
    }

    private val paintYellow = Paint().apply {
        color = ContextCompat.getColor(
            context,
            R.color.yellow
        )
    }

    init {
        paintWhite.isAntiAlias = true
        paintYellow.isAntiAlias = true
    }

    companion object {
        const val RAD_DIVISOR = 4f
        const val RAD_TENTH = 10f
        const val ANIMATION_DURATION = 100L
    }


    private val oval = RectF()

    private var radius = 0f

    private val animator = ValueAnimator.ofFloat(0f, 100f)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        oval.set(0f, 0f, width.toFloat(), width.toFloat())

        canvas?.drawCircle(
            width / 2f,
            height / 2f,
            width / RAD_DIVISOR,
            paintWhite
        )
        canvas?.drawCircle(
            width / 2f,
            height / 2f,
            radius,
            paintYellow
        )
    }

    fun start(event: String) {
        if (animator.isRunning) stop()

        animator.duration = ANIMATION_DURATION
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            when (event) {
                DOWN_KEY -> drawDownProgress(animatedValue)
                UP_KEY -> {
                    drawUpProgress(animatedValue)

                }
            }
        }
        animator.start()


    }

    private fun stop() {
        animator.cancel()
    }


    private fun drawDownProgress(animatedValue: Float) {
        radius = width / RAD_DIVISOR - animatedValue / 10
        paintYellow.alpha = 1000
        invalidate()
    }

    private fun drawUpProgress(animatedValue: Float) {
        radius = width / RAD_DIVISOR - RAD_TENTH + animatedValue / RAD_TENTH
        paintYellow.alpha = 100 - animatedValue.toInt()
        invalidate()
    }
}