package ru.mapublish.multiplicationtable.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import ru.mapublish.multiplicationtable.R

class StarsView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var firstStar: ImageView
    private var secondStar: ImageView
    private var thirdStar: ImageView
    private var fourStar: ImageView
    private var fifthStar: ImageView

    companion object {
        const val ANIM_DURATION = 100L
        const val TOP_SCALE = 1.6f
        const val START_SCALE = 0f
        const val END_SCALE = 1f
    }

    init {
        inflate(context, R.layout.stars_view, this)

        firstStar = findViewById(R.id.first_star_iv)
        secondStar = findViewById(R.id.second_star_iv)
        thirdStar = findViewById(R.id.third_star_iv)
        fourStar = findViewById(R.id.fourth_star_iv)
        fifthStar = findViewById(R.id.fifth_star_iv)

        firstStar.scaleX = START_SCALE
        firstStar.scaleY = START_SCALE

        secondStar.scaleX = START_SCALE
        secondStar.scaleY = START_SCALE

        thirdStar.scaleX = START_SCALE
        thirdStar.scaleY = START_SCALE

        fourStar.scaleX = START_SCALE
        fourStar.scaleY = START_SCALE

        fifthStar.scaleX = START_SCALE
        fifthStar.scaleY = START_SCALE
    }

    /**
     * stars appear consequently in a row after a lvl is done, showing the results of the player
     */

    fun launchStarsAnimation(percentageOfCorrectAnswers: Float) {
        when {
            percentageOfCorrectAnswers >= 90 -> {
                firstStar.setImageResource(R.drawable.star_happy)
                secondStar.setImageResource(R.drawable.star_happy)
                thirdStar.setImageResource(R.drawable.star_happy)
                fourStar.setImageResource(R.drawable.star_happy)
                fifthStar.setImageResource(R.drawable.star_happy)
            }
            percentageOfCorrectAnswers >= 75 -> {
                firstStar.setImageResource(R.drawable.star_happy)
                secondStar.setImageResource(R.drawable.star_happy)
                thirdStar.setImageResource(R.drawable.star_happy)
                fourStar.setImageResource(R.drawable.star_happy)
                fifthStar.setImageResource(R.drawable.star_sad)
            }
            percentageOfCorrectAnswers >= 55 -> {
                firstStar.setImageResource(R.drawable.star_happy)
                secondStar.setImageResource(R.drawable.star_happy)
                thirdStar.setImageResource(R.drawable.star_happy)
                fourStar.setImageResource(R.drawable.star_sad)
                fifthStar.setImageResource(R.drawable.star_sad)
            }
            percentageOfCorrectAnswers >= 25 -> {
                firstStar.setImageResource(R.drawable.star_happy)
                secondStar.setImageResource(R.drawable.star_happy)
                thirdStar.setImageResource(R.drawable.star_sad)
                fourStar.setImageResource(R.drawable.star_sad)
                fifthStar.setImageResource(R.drawable.star_sad)
            }
            percentageOfCorrectAnswers >= 10 -> {
                firstStar.setImageResource(R.drawable.star_happy)
                secondStar.setImageResource(R.drawable.star_sad)
                thirdStar.setImageResource(R.drawable.star_sad)
                fourStar.setImageResource(R.drawable.star_sad)
                fifthStar.setImageResource(R.drawable.star_sad)
            }
            else -> {
                firstStar.setImageResource(R.drawable.star_sad)
                secondStar.setImageResource(R.drawable.star_sad)
                thirdStar.setImageResource(R.drawable.star_sad)
                fourStar.setImageResource(R.drawable.star_sad)
                fifthStar.setImageResource(R.drawable.star_sad)
            }
        }

        val animator1: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            firstStar,
            PropertyValuesHolder.ofFloat("scaleX", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("scaleY", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("alpha", START_SCALE, END_SCALE)
        )

        val animator2: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            secondStar,
            PropertyValuesHolder.ofFloat("scaleX", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("scaleY", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("alpha", START_SCALE, END_SCALE)
        )


        val animator3: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            thirdStar,
            PropertyValuesHolder.ofFloat("scaleX", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("scaleY", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("alpha", START_SCALE, END_SCALE)
        )

        val animator4: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            fourStar,
            PropertyValuesHolder.ofFloat("scaleX", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("scaleY", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("alpha", START_SCALE, END_SCALE)
        )

        val animator5: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            fifthStar,
            PropertyValuesHolder.ofFloat("scaleX", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("scaleY", START_SCALE, TOP_SCALE, END_SCALE),
            PropertyValuesHolder.ofFloat("alpha", START_SCALE, END_SCALE)
        )

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.duration = ANIM_DURATION
        animatorSet.play(animator2).after(animator1)
        animatorSet.play(animator3).after(animator2)
        animatorSet.play(animator4).after(animator3)
        animatorSet.play(animator5).after(animator4)
        animatorSet.start()
    }


}
