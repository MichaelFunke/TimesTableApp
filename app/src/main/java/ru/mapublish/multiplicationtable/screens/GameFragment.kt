package ru.mapublish.multiplicationtable.screens


import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import ru.mapublish.multiplicationtable.*
import ru.mapublish.multiplicationtable.databinding.FragmentGameBinding
import ru.mapublish.multiplicationtable.utils.*
import ru.mapublish.multiplicationtable.utils.Actions.GO_NEXT_STAGE
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_SPEED
import ru.mapublish.multiplicationtable.utils.Actions.MODE
import ru.mapublish.multiplicationtable.utils.Actions.STANDARD_MODE
import ru.mapublish.multiplicationtable.utils.Actions.TRUEFALSE_MODE

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    //lists of TextViews in which factors and products are shown to the player
    private lateinit var productTvs: List<TextView>
    private lateinit var squaresTvs: MutableList<TextView>
    private lateinit var factorTvs: List<TextView>
    //list of the keyboard' buttons is used to manipulate with button's size
    private lateinit var keyboardButtons: List<View>

    //keeps Ids of TextViews and change their colors
    private val tvIds = mutableListOf<String>()
    private var tvId = "-1"
    private var level = 1
    private var speed = 1
    private var mode = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_game, container, false
        )

        //retrieves the level and the mode from Shared Prefs
        level = readFromShPrefs(requireContext(), CURRENT_LEVEL)
        speed = readFromShPrefs(requireContext(), CURRENT_SPEED)
        mode = readFromShPrefs(requireContext(), MODE)

        // hides controllers depending on the chosen mode
        if (mode == STANDARD_MODE) {
            binding.standardModeControllers.visibility = View.VISIBLE
            binding.trueFalseModeControllers.visibility = View.GONE
        } else {
            binding.standardModeControllers.visibility = View.GONE
            binding.trueFalseModeControllers.visibility = View.VISIBLE
        }

        viewModelFactory = GameViewModelFactory(level, speed, mode)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        // shows question mark in a box in accordance to the product value
        viewModel.tvId.observe(this, Observer { tvId ->
            this.tvId = tvId

            startTimer()

            if (mode == STANDARD_MODE) showQuestionMark()
            else showRandomizedAnswer()

            beamsToTv()
        })

        // clears the whole table if there is no more elements to show
        viewModel.goNextStageFlag.observe(this, Observer { goNextStageFlag ->

            if (goNextStageFlag == GO_NEXT_STAGE) {
                stopTimer()

                //highlights the last product in the level
                beamsToTv()

                val handler = Handler()
                handler.postDelayed({

                    clearTextInTable()
                    findNavController().navigate(
                        GameFragmentDirections.actionGameFragmentToGoNextLevelFragment(
                            viewModel.numberOfIncorrectAnswers.value!!.toInt()
                        )
                    )
                }, 1000)
            }
        })

        // watches if the player's answer was a correct one and calls an appropriate method
        viewModel.isAnswerCorrect.observe(this, Observer { correct ->
            if (correct) onCorrectAnswer()
            else onWrongAnswer()

            refreshColors()
        })

        viewModel.cheats.observe(this, Observer { cheats ->
            setCheatBtnWithNumber(cheats)
        })

        // starts the game at the chosen level
        viewModel.resetList(level)

        initArrays()

        if (mode == TRUEFALSE_MODE) {
            binding.trueBtn.setTextColor(resources.getColor(R.color.purpur))
            binding.falseBtn.setTextColor(resources.getColor(R.color.purpur))

            binding.gameParentView.background = colorTablePurPur()
            productTvs.forEach {
                it.background = colorTablePurPur()
                it.setTextColor(colorTextTablePurPur())
            }
            factorTvs.forEach {
                it.background = colorTablePurPur()
                it.setTextColor(colorTextTablePurPur())
            }
        }

        adjustTableSizeToScreenSize()
        fillArraysToLevel()


        getTvsIds()
        colorPerfectSquares()


        // starts the timer animation
        startTimer()

        return binding.root
    }

    private fun setCheatBtnWithNumber(cheats: Int) {
        when (cheats) {
            5 -> binding.cheatBtn.setImageResource(R.drawable.cheat_btn_5)
            4 -> binding.cheatBtn.setImageResource(R.drawable.cheat_btn_4)
            3 -> binding.cheatBtn.setImageResource(R.drawable.cheat_btn_3)
            2 -> binding.cheatBtn.setImageResource(R.drawable.cheat_btn_2)
            1 -> binding.cheatBtn.setImageResource(R.drawable.cheat_btn_1)
            else -> binding.cheatBtn.setImageResource(R.drawable.cheat_zero)
        }
    }

    //starts the animation of timerView depending on the chosen mode
    private fun startTimer() {
        if (mode == STANDARD_MODE) binding.timerView.start(viewModel.countDownTime)
        else binding.timerViewTrueFalseMode.start(viewModel.countDownTime)

    }

    //stops the animation of timerView depending on the chosen mode
    private fun stopTimer() {
        if (mode == STANDARD_MODE) binding.timerView.stop()
        else binding.timerViewTrueFalseMode.stop()
    }

    /**
     * These three methods put "?", "X", or isAnswerCorrect answer in the box depending on player's actions
     */


    private fun showQuestionMark() {
        productTvs.forEach { if (it.toString().contains(tvId, true)) it.text = "?" }
    }

    private fun showRandomizedAnswer() {
        productTvs.forEach {
            if (it.toString().contains(tvId, true)) it.text = viewModel.product.value.toString()
        }
    }

    private fun onWrongAnswer() {
        productTvs.forEach { if (it.toString().contains(tvId, true)) it.text = "X" }
    }

    private fun onCorrectAnswer() {
        productTvs.forEach {
            if (it.toString().contains(tvId, true)) it.text = viewModel.product.value.toString()
        }
    }


    /**
     * gets the screen size of the phone and adapts TextViews of the table. They should be square, so only the width of the screen is used
     */
    private fun adjustTableSizeToScreenSize() {
        val display: Display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        //handles the orientation change
        productTvs.forEach {
            it.layoutParams.width = width / 11
            it.layoutParams.height = width / 11
        }

        factorTvs.forEach {
            it.layoutParams.width = width / 11
            it.layoutParams.height = width / 11
        }

        keyboardButtons.forEach {
            it.layoutParams.width = width / 12
            it.layoutParams.height = width / 12
        }

        //adjusting the size of the EditText
        binding.et.layoutParams.width = width / 4
        binding.et.layoutParams.height = width / 10

        //adjusting the size of the cheat_btn
        binding.cheatBtn.layoutParams.width = width / 8
        binding.cheatBtn.layoutParams.height = width / 8

        //adjusting the size of the sendAnswer btn
        binding.sendAnswer.layoutParams.width = width / 9
        binding.sendAnswer.layoutParams.height = width / 9

        //adjusting the size of the timerView btn
        binding.timerView.layoutParams.width = width / 9
        binding.timerView.layoutParams.height = width / 9

        //adjusting the size of the timerView for TrueFalse mode btn
        binding.timerViewTrueFalseMode.layoutParams.width = width / 9
        binding.timerViewTrueFalseMode.layoutParams.height = width / 9


    }

    /**
     * clears all values from the table for the next level
     */
    private fun clearTextInTable() {
        productTvs.forEach { it.text = "" }
    }


    private fun getTvsIds() {
        // a list is assigned according to the level
        val tvs = when (level) {
            1, 9, 17 -> Products.LVL_ONE
            2, 10, 18 -> Products.LVL_TWO
            3, 11, 19 -> Products.LVL_THREE
            4, 12, 20 -> Products.LVL_FOUR
            5, 13, 21 -> Products.LVL_FIVE
            6, 14, 22 -> Products.LVL_SIX
            7, 15, 23 -> Products.LVL_SEVEN
            8, 16, 24 -> Products.LVL_EIGHT
            else -> Products.LVL_EIGHT
        }

        // gets the first value from each element in the list, which is an id of a box
        tvs.mapTo(tvIds) { it.first }
    }


    private fun initArrays() {
        productTvs = listOf(
            binding.rI1,
            binding.rI2,
            binding.rI3,
            binding.rI4,
            binding.rI5,
            binding.rI6,
            binding.rI7,
            binding.rI8,
            binding.rI9,
            binding.rII2,
            binding.rII4,
            binding.rII6,
            binding.rII8,
            binding.rII10,
            binding.rII12,
            binding.rII14,
            binding.rII16,
            binding.rII18,
            binding.rIII3,
            binding.rIII6,
            binding.rIII12,
            binding.rIII15,
            binding.rIII18,
            binding.rIII21,
            binding.rIII24,
            binding.rIII27,
            binding.rIV4,
            binding.rIV8,
            binding.rIV12,
            binding.rIV20,
            binding.rIV24,
            binding.rIV28,
            binding.rIV32,
            binding.rIV36,
            binding.rV5,
            binding.rV10,
            binding.rV15,
            binding.rV20,
            binding.rV30,
            binding.rV35,
            binding.rV40,
            binding.rV45,
            binding.rVI6,
            binding.rVI12,
            binding.rVI18,
            binding.rVI24,
            binding.rVI30,
            binding.rVI42,
            binding.rVI48,
            binding.rVI54,
            binding.rVII7,
            binding.rVII14,
            binding.rVII21,
            binding.rVII28,
            binding.rVII35,
            binding.rVII42,
            binding.rVII56,
            binding.rVII63,
            binding.rVIII8,
            binding.rVIII16,
            binding.rVIII24,
            binding.rVIII32,
            binding.rVIII40,
            binding.rVIII48,
            binding.rVIII56,
            binding.rVIII72,
            binding.rIX9,
            binding.rIX18,
            binding.rIX27,
            binding.rIX36,
            binding.rIX45,
            binding.rIX54,
            binding.rIX63,
            binding.rIX72,
            binding.rIX81,
            binding.rIII9,
            binding.rIV16,
            binding.rV25,
            binding.rVI36,
            binding.rVII49,
            binding.rVIII64
        )
        squaresTvs = mutableListOf(
            binding.rIII9,
            binding.rIV16,
            binding.rV25,
            binding.rVI36,
            binding.rVII49,
            binding.rVIII64,
            binding.rIX81
        )
        factorTvs = listOf(
            binding.zero,
            binding.rOne,
            binding.rTwo,
            binding.rThree,
            binding.rFour,
            binding.rFive,
            binding.rSix,
            binding.rSeven,
            binding.rEight,
            binding.rNine,
            binding.cOne,
            binding.cTwo,
            binding.cThree,
            binding.cFour,
            binding.cFive,
            binding.cSix,
            binding.cSeven,
            binding.cEight,
            binding.cNine
        )
        keyboardButtons = listOf(
            binding.keyboard.input0,
            binding.keyboard.input1,
            binding.keyboard.input2,
            binding.keyboard.input3,
            binding.keyboard.input4,
            binding.keyboard.input5,
            binding.keyboard.input6,
            binding.keyboard.input7,
            binding.keyboard.input8,
            binding.keyboard.input9,
            binding.keyboard.delete
        )
    }

    /**
     *arrays filled in depending on the level
     */

    private fun fillArraysToLevel() {

        when (level) {
            1 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.cOne,
                binding.cTwo
            )
            2 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.cOne,
                binding.cTwo,
                binding.cThree
            )
            3 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour
            )
            4 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.rFive,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour,
                binding.cFive
            )
            5 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.rFive,
                binding.rSix,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour,
                binding.cFive,
                binding.cSix
            )
            6 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.rFive,
                binding.rSix,
                binding.rSeven,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour,
                binding.cFive,
                binding.cSix,
                binding.cSeven
            )
            7 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.rFive,
                binding.rSix,
                binding.rSeven,
                binding.rEight,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour,
                binding.cFive,
                binding.cSix,
                binding.cSeven,
                binding.cEight
            )
            else -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.rFour,
                binding.rFive,
                binding.rSix,
                binding.rSeven,
                binding.rEight,
                binding.rNine,
                binding.cOne,
                binding.cTwo,
                binding.cThree,
                binding.cFour,
                binding.cFive,
                binding.cSix,
                binding.cSeven,
                binding.cEight,
                binding.cNine
            )
        }
    }


    /**
     * colors tvs of perfect squares at the beginning of the level
     */
    private fun colorPerfectSquares() {
        squaresTvs.forEach { it.background = colorBackgroundSemiWhite() }
        refreshColors()
    }

    /**
     * refreshes the table colors after each players's answer
     */
    private fun refreshColors() {
        //iterates through the list of all product tvs, finds those with ids needed for the level and changes their colors
        for (item in productTvs) {
            for (element in tvIds) {
                if (item.toString().contains(element, true)) {
                    item.background = colorBackgroundWhite()

                    if (mode == TRUEFALSE_MODE) {
                        item.setTextColor(colorTextTablePurPur())
                    } else {
                        item.setTextColor(colorTextBlue())
                    }
                }
            }
        }

        //iterates through the list of all factor tvs and changes their colors
        if (mode == TRUEFALSE_MODE) {
            factorTvs.forEach {
                it.background = colorTablePurPur()
                it.setTextColor(colorTextWhite())
            }
        } else {
            factorTvs.forEach {
                it.background = colorBackgroundBlue()
                it.setTextColor(colorTextWhite())
            }
        }
    }

    private fun beamsToTv() {

        // colors the current tv
        productTvs.forEach {
            if (it.toString().contains(tvId, true)) {
                it.background = colorBackgroundYellow()
                it.setTextColor(colorTextWhite())
            }
        }

        val beam = mutableListOf<String>()
        when (tvId) {
            "rI_1" -> beam.addAll(Beams.rI1)
            "rI_2" -> beam.addAll(Beams.rI2)
            "rI_3" -> beam.addAll(Beams.rI3)
            "rI_4" -> beam.addAll(Beams.rI4)
            "rI_5" -> beam.addAll(Beams.rI5)
            "rI_6" -> beam.addAll(Beams.rI6)
            "rI_7" -> beam.addAll(Beams.rI7)
            "rI_8" -> beam.addAll(Beams.rI8)
            "rI_9" -> beam.addAll(Beams.rI9)

            "rII_2" -> beam.addAll(Beams.rII2)
            "rII_4" -> beam.addAll(Beams.rII4)
            "rII_6" -> beam.addAll(Beams.rII6)
            "rII_8" -> beam.addAll(Beams.rII8)
            "rII_10" -> beam.addAll(Beams.rII10)
            "rII_12" -> beam.addAll(Beams.rII12)
            "rII_14" -> beam.addAll(Beams.rII14)
            "rII_16" -> beam.addAll(Beams.rII16)
            "rII_18" -> beam.addAll(Beams.rII18)

            "rIII_3" -> beam.addAll(Beams.rIII3)
            "rIII_6" -> beam.addAll(Beams.rIII6)
            "rIII_9" -> beam.addAll(Beams.rIII9)
            "rIII_12" -> beam.addAll(Beams.rIII12)
            "rIII_15" -> beam.addAll(Beams.rIII15)
            "rIII_18" -> beam.addAll(Beams.rIII18)
            "rIII_21" -> beam.addAll(Beams.rIII21)
            "rIII_24" -> beam.addAll(Beams.rIII24)
            "rIII_27" -> beam.addAll(Beams.rIII27)

            "rIV_4" -> beam.addAll(Beams.rIV4)
            "rIV_8" -> beam.addAll(Beams.rIV8)
            "rIV_12" -> beam.addAll(Beams.rIV12)
            "rIV_16" -> beam.addAll(Beams.rIV16)
            "rIV_20" -> beam.addAll(Beams.rIV20)
            "rIV_24" -> beam.addAll(Beams.rIV24)
            "rIV_28" -> beam.addAll(Beams.rIV28)
            "rIV_32" -> beam.addAll(Beams.rIV32)
            "rIV_36" -> beam.addAll(Beams.rIV36)

            "rV_5" -> beam.addAll(Beams.rV5)
            "rV_10" -> beam.addAll(Beams.rV10)
            "rV_15" -> beam.addAll(Beams.rV15)
            "rV_20" -> beam.addAll(Beams.rV20)
            "rV_25" -> beam.addAll(Beams.rV25)
            "rV_30" -> beam.addAll(Beams.rV30)
            "rV_35" -> beam.addAll(Beams.rV35)
            "rV_40" -> beam.addAll(Beams.rV40)
            "rV_45" -> beam.addAll(Beams.rV45)

            "rVI_6" -> beam.addAll(Beams.rVI6)
            "rVI_12" -> beam.addAll(Beams.rVI12)
            "rVI_18" -> beam.addAll(Beams.rVI18)
            "rVI_24" -> beam.addAll(Beams.rVI24)
            "rVI_30" -> beam.addAll(Beams.rVI30)
            "rVI_36" -> beam.addAll(Beams.rVI36)
            "rVI_42" -> beam.addAll(Beams.rVI42)
            "rVI_48" -> beam.addAll(Beams.rVI48)
            "rVI_54" -> beam.addAll(Beams.rVI54)

            "rVII_7" -> beam.addAll(Beams.rVII7)
            "rVII_14" -> beam.addAll(Beams.rVII14)
            "rVII_21" -> beam.addAll(Beams.rVII21)
            "rVII_28" -> beam.addAll(Beams.rVII28)
            "rVII_35" -> beam.addAll(Beams.rVII35)
            "rVII_42" -> beam.addAll(Beams.rVII42)
            "rVII_49" -> beam.addAll(Beams.rVII49)
            "rVII_56" -> beam.addAll(Beams.rVII56)
            "rVII_63" -> beam.addAll(Beams.rVII63)

            "rVIII_8" -> beam.addAll(Beams.rVIII8)
            "rVIII_16" -> beam.addAll(Beams.rVIII16)
            "rVIII_24" -> beam.addAll(Beams.rVIII24)
            "rVIII_32" -> beam.addAll(Beams.rVIII32)
            "rVIII_40" -> beam.addAll(Beams.rVIII40)
            "rVIII_48" -> beam.addAll(Beams.rVIII48)
            "rVIII_56" -> beam.addAll(Beams.rVIII56)
            "rVIII_64" -> beam.addAll(Beams.rVIII64)
            "rVIII_72" -> beam.addAll(Beams.rVIII72)

            "rIX_9" -> beam.addAll(Beams.rIX9)
            "rIX_18" -> beam.addAll(Beams.rIX18)
            "rIX_27" -> beam.addAll(Beams.rIX27)
            "rIX_36" -> beam.addAll(Beams.rIX36)
            "rIX_45" -> beam.addAll(Beams.rIX45)
            "rIX_54" -> beam.addAll(Beams.rIX54)
            "rIX_63" -> beam.addAll(Beams.rIX63)
            "rIX_72" -> beam.addAll(Beams.rIX72)
            "rIX_81" -> beam.addAll(Beams.rIX81)
        }

        for (element in beam) {
            for (item in productTvs) {
                if (item.toString().contains(element, true)) {
                    item.background = colorBackgroundYellowLight()
                }
            }
        }

        for (item in beam) {
            for (element in factorTvs) {
                if (element.toString().contains(item, true)) {
                    element.background = colorBackgroundYellowLight()
                    if (mode == TRUEFALSE_MODE) {
                        element.setTextColor(colorTextTablePurPur())
                    } else {
                        element.setTextColor(colorTextBlue())
                    }
                }
            }
        }

    }

    val colorTextBlue = {
        ContextCompat.getColor(
            requireContext(),
            R.color.background_bluish
        )
    }

    val colorTextWhite = {
        ContextCompat.getColor(
            requireContext(),
            R.color.white
        )
    }

    val colorBackgroundYellowLight = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_yellow_light
        )
    }
    val colorBackgroundWhite = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners
        )
    }

    val colorBackgroundSemiWhite = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_semi_white
        )
    }

    val colorBackgroundBlue = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_blue
        )
    }

    val colorBackgroundYellow = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_yellow
        )
    }

    private val colorTablePurPur = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_purpur
        )
    }

    private val colorTextTablePurPur = {
        ContextCompat.getColor(
            requireContext(),
            R.color.purpur
        )
    }
}
