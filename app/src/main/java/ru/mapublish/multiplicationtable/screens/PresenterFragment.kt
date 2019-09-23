package ru.mapublish.multiplicationtable.screens

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.databinding.FragmentPresenterBinding
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.MODE
import ru.mapublish.multiplicationtable.utils.Actions.PRESENTER_DUR
import ru.mapublish.multiplicationtable.utils.Actions.TRUEFALSE_MODE
import ru.mapublish.multiplicationtable.utils.Products
import ru.mapublish.multiplicationtable.utils.readFromShPrefs

class PresenterFragment : Fragment() {

    private lateinit var binding: FragmentPresenterBinding

    //lists of TextViews in which factors and products are shown to the player
    private lateinit var productTvs: List<TextView>
    private lateinit var squaresTvs: MutableList<TextView>
    private lateinit var factorTvs: List<TextView>

    //keeps Ids of TextViews and change their colors
    private val tvIds = mutableListOf<String>()

    private var level = 1
    private var mode = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_presenter, container, false)

        level = readFromShPrefs(requireContext(), CURRENT_LEVEL)
        mode = readFromShPrefs(requireContext(), MODE)


        //arrays with views are filled
        initArrays()
        //gets ids of views that are needed to be shown for the level
        getTvsIds()

        if (mode == TRUEFALSE_MODE) {
            binding.presenterParentView.setBackgroundColor(
                resources.getColor(
                    R.color.purpur
                )
            )
            productTvs.forEach {it.background = colorTablePurPur() }
            factorTvs.forEach {it.background = colorTablePurPur() }
        }

        //gets the screen size of the phone and adapts TextViews of the table. They should be square, so only the width of the screen is used
        adjustTableSizeToScreenSize()

        //makes all the text in the table blue
        colorText()
        //iterates through the list of all views, finds those with ids needed for the level and changes their colors
        colorTable()

        //colors all central views in yellow
        colorPerfectSquaresYellow()
        //cuts views that should remain yellow
        makePerfectSquaresAndFactorsArraysToLevel()

        //colors "unused" PerfectSquare views in white
        colorPerfectSquaresWhite()
        //colors factor rows & columns views in white
        colorFactorViews()

        binding.timerView.start(PRESENTER_DUR)

        binding.skipBtn.setOnClickListener {
            findNavController().navigate(PresenterFragmentDirections.actionPresenterFragmentToGameFragment())
        }

        return binding.root
    }

    private fun adjustTableSizeToScreenSize() {
        val display: Display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        //handles the orientation change
        if (width < height) {
            productTvs.forEach {
                it.layoutParams.width = width / 11
                it.layoutParams.height = width / 11
                //changes the text size to fit the TextView
//                it.textSize = it.layoutParams.width.toFloat() / 3
            }

            factorTvs.forEach {
                it.layoutParams.width = width / 11
                it.layoutParams.height = width / 11
                //changes the text size to fit the TextView
//                it.textSize = it.layoutParams.width.toFloat() / 3
            }

            //adjusting the size of the sendAnswer btn
            binding.skipBtn.layoutParams.width = width / 10
            binding.skipBtn.layoutParams.height = width / 10

            //adjusting the size of the timerView btn
            binding.timerView.layoutParams.width = width / 10
            binding.timerView.layoutParams.height = width / 10
        } else {
            productTvs.forEach {
                it.layoutParams.width = width / 2 / 11
                it.layoutParams.height = width / 2 / 11
                it.textSize = it.layoutParams.width.toFloat() / 3
            }

            factorTvs.forEach {
                it.layoutParams.width = width / 2 / 11
                it.layoutParams.height = width / 2 / 11
                it.textSize = it.layoutParams.width.toFloat() / 3
            }
        }
    }

    private fun colorPerfectSquaresYellow() {
        squaresTvs.forEach {
            it.background = colorBackgroundYellow()
            it.setTextColor(colorTextWhite())
        }
    }

    private fun colorPerfectSquaresWhite() {
        squaresTvs.forEach {
            it.background = colorBackgroundSemiWhite()
            it.text = ""
        }
    }

    private fun colorFactorViews() {
        factorTvs.forEach { it.setTextColor(colorTextWhite()) }
    }

    private fun colorText() {
        if (mode == TRUEFALSE_MODE) {
            productTvs.forEach { it.setTextColor(colorTextTablePurPur()) }
        } else {
            productTvs.forEach { it.setTextColor(colorTextBluish()) }
        }
    }

    private fun colorTable() {
        productTvs.forEach { item ->
            tvIds.forEach { element ->
                if (item.toString().contains(element, true)) {
                    item.background = colorBackgroundWhite()
                }
            }
        }
    }

    private val colorTextWhite = { ContextCompat.getColor(requireContext(), R.color.white) }
    private val colorTextBluish = {
        ContextCompat.getColor(
            requireContext(),
            R.color.background_bluish
        )
    }
    private val colorBackgroundWhite = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners
        )
    }
    private val colorBackgroundSemiWhite = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_semi_white
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

    private val colorBackgroundYellow = {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.tv_rounded_corners_yellow
        )
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


    private fun makePerfectSquaresAndFactorsArraysToLevel() {

        when (level) {
            1, 9, 17 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
            }
            2, 10, 18 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
            }
            3, 11, 19 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
                squaresTvs.remove(binding.rIV16)
            }
            4, 12, 20 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
                squaresTvs.remove(binding.rIV16)
                squaresTvs.remove(binding.rV25)
            }
            5, 13, 21 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
                squaresTvs.remove(binding.rIV16)
                squaresTvs.remove(binding.rV25)
                squaresTvs.remove(binding.rVI36)
            }
            6, 14, 22 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
                squaresTvs.remove(binding.rIV16)
                squaresTvs.remove(binding.rV25)
                squaresTvs.remove(binding.rVI36)
                squaresTvs.remove(binding.rVII49)
            }
            7, 15, 23 -> {
                squaresTvs.remove(binding.rI1)
                squaresTvs.remove(binding.rII4)
                squaresTvs.remove(binding.rIII9)
                squaresTvs.remove(binding.rIV16)
                squaresTvs.remove(binding.rV25)
                squaresTvs.remove(binding.rVI36)
                squaresTvs.remove(binding.rVII49)
                squaresTvs.remove(binding.rVIII64)
            }
//            8, 16, 24 -> {
//                squaresTvs.remove(binding.rI1)
//                squaresTvs.remove(binding.rII4)
//                squaresTvs.remove(binding.rIII9)
//                squaresTvs.remove(binding.rIV16)
//                squaresTvs.remove(binding.rV25)
//                squaresTvs.remove(binding.rVI36)
//                squaresTvs.remove(binding.rVII49)
//                squaresTvs.remove(binding.rVII56)
//            }
            else -> squaresTvs.removeAll { true }
        }

        when (level) {
            1, 9, 17 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.cOne,
                binding.cTwo
            )
            2, 10, 18 -> factorTvs = listOf(
                binding.zero,
                binding.rOne,
                binding.rTwo,
                binding.rThree,
                binding.cOne,
                binding.cTwo,
                binding.cThree
            )
            3, 11, 19 -> factorTvs = listOf(
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
            4, 12, 20 -> factorTvs = listOf(
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
            5, 13, 21 -> factorTvs = listOf(
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
            6, 14, 22 -> factorTvs = listOf(
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
            7, 15, 23 -> factorTvs = listOf(
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
            8, 16, 24 -> factorTvs = listOf(
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
            binding.rI1,
            binding.rII4,
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
    }

}