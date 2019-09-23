package ru.mapublish.multiplicationtable.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.level_selector_view.view.*
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.databinding.StartFragmentBinding
import ru.mapublish.multiplicationtable.utils.Actions.DOWN_KEY
import ru.mapublish.multiplicationtable.utils.Actions.MODE
import ru.mapublish.multiplicationtable.utils.Actions.STANDARD_MODE
import ru.mapublish.multiplicationtable.utils.Actions.TRUEFALSE_MODE
import ru.mapublish.multiplicationtable.utils.Actions.UP_KEY
import ru.mapublish.multiplicationtable.utils.readFromShPrefs
import ru.mapublish.multiplicationtable.utils.writeToShPrefs

class StartFragment : Fragment() {
    private lateinit var binding: StartFragmentBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)

        var mode = readFromShPrefs(requireContext(), MODE)

        //when pressed starts the game and manages the colors of the start button
        binding.startBtn.setOnTouchListener { v, event ->
            if (event.action == ACTION_DOWN) {
                //runs the animation on KEY DOWN event
                binding.startBtn.start(DOWN_KEY)

                binding.learningTv.setTextColor(colorTextWhite())
                binding.startTv.setTextColor(colorTextWhite())
            } else if (event.action == ACTION_UP) {
                //runs the animation on KEY UP event
                binding.startBtn.start(UP_KEY)

                if (mode == TRUEFALSE_MODE) {
                    binding.startTv.setTextColor(colorTextPurPur())
                    binding.learningTv.setTextColor(colorTextPurPur())
                } else {
                    binding.startTv.setTextColor(colorTextBlue())
                    binding.learningTv.setTextColor(colorTextBlue())
                }

                // delays the starting of GameFragment till the start button's animation ends
                val handler = Handler()
                handler.postDelayed({
                    findNavController().navigate(StartFragmentDirections.actionStartFragmentToPresenterFragment())
                }, 110)
            }
            true
        }


        if (mode == TRUEFALSE_MODE) {
            binding.startParentView.background = resources.getDrawable(R.color.purpur)
            binding.startTv.setTextColor(colorTextPurPur())
            binding.learningTv.setTextColor(colorTextPurPur())
        }

        binding.levelSelectorView.standard_mode_tv.setOnClickListener {
            binding.startParentView.background = colorBlue()
            binding.levelSelectorView.levelSelectorParentView.background = colorBlue()
            binding.startTv.setTextColor(colorTextBlue())
            binding.learningTv.setTextColor(colorTextBlue())

            mode = STANDARD_MODE
            writeToShPrefs(requireContext(), MODE, STANDARD_MODE)
            binding.levelSelectorView.changeModeButtonsColors(it)
        }

        binding.levelSelectorView.trueFalse_mode_tv.setOnClickListener {
            binding.startParentView.background = colorPurPur()
            binding.levelSelectorView.levelSelectorParentView.background = colorPurPur()
            binding.startTv.setTextColor(colorTextPurPur())
            binding.learningTv.setTextColor(colorTextPurPur())

            mode = TRUEFALSE_MODE
            writeToShPrefs(requireContext(), MODE, TRUEFALSE_MODE)
            binding.levelSelectorView.changeModeButtonsColors(it)
        }

        return binding.root
    }


    val colorTextPurPur = {
        ContextCompat.getColor(requireContext(), R.color.purpur)
    }

    val colorPurPur = {
        ContextCompat.getDrawable(requireContext(), R.color.purpur)
    }

    val colorBlue = {
        ContextCompat.getDrawable(requireContext(), R.color.background_bluish)
    }

    val colorTextWhite = {
        ContextCompat.getColor(requireContext(), R.color.white)
    }

    val colorTextBlue = {
        ContextCompat.getColor(requireContext(), R.color.background_bluish)
    }

}