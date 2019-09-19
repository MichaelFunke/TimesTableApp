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
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.databinding.StartFragmentBinding
import ru.mapublish.multiplicationtable.utils.Actions.DOWN_KEY
import ru.mapublish.multiplicationtable.utils.Actions.UP_KEY

class StartFragment : Fragment() {
    private lateinit var binding: StartFragmentBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false)

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

                binding.startTv.setTextColor(colorTextBlue())
                binding.learningTv.setTextColor(colorTextBlue())

                // delays the starting of GameFragment till the start button's animation ends
                val handler = Handler()
                handler.postDelayed({
                    findNavController().navigate(StartFragmentDirections.actionStartFragmentToPresenterFragment())
                }, 110)
            }
            true
        }
        return binding.root
    }


    val colorTextWhite = {
        ContextCompat.getColor(requireContext(), R.color.white)
    }

    val colorTextBlue = {
        ContextCompat.getColor(requireContext(), R.color.background_bluish)
    }
}