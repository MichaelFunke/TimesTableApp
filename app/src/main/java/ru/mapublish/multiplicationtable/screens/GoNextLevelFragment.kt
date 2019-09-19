package ru.mapublish.multiplicationtable.screens

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import ru.mapublish.multiplicationtable.R
import ru.mapublish.multiplicationtable.databinding.FragmentGoNextLevelBinding
import ru.mapublish.multiplicationtable.utils.Actions.GO_NEXT_LVL
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.REPEAT_LVL
import ru.mapublish.multiplicationtable.utils.Actions.CURRENT_SPEED
import ru.mapublish.multiplicationtable.utils.Actions.TOTAL_LEVEL
import ru.mapublish.multiplicationtable.utils.Actions.TOTAL_SPEED
import ru.mapublish.multiplicationtable.utils.makePercentage
import ru.mapublish.multiplicationtable.utils.readFromShPrefs
import ru.mapublish.multiplicationtable.utils.writeToShPrefs

class GoNextLevelFragment : DialogFragment() {

    private lateinit var binding: FragmentGoNextLevelBinding
    private lateinit var goNextViewModel: GoNextLevelViewModel

    private var currentLevel = 0
    private var totalLevel = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_go_next_level,
            container,
            false
        )

        goNextViewModel = ViewModelProviders.of(this).get(GoNextLevelViewModel::class.java)
        binding.goNextViewModel = goNextViewModel
        binding.lifecycleOwner = this

        val args = GoNextLevelFragmentArgs.fromBundle(arguments!!)

        currentLevel = readFromShPrefs(requireContext(), CURRENT_LEVEL)
        totalLevel = readFromShPrefs(requireContext(), TOTAL_LEVEL)

        val currentSpeed = readFromShPrefs(requireContext(), CURRENT_SPEED)
        val percentageOfIncorrectAnswers = makePercentage(currentLevel, args.incorrectAnswers)

        //if the level is 8 and speed is x1, the speed gets the value of x2 for the next 8 levels
        if (currentLevel == 8 && currentSpeed == 1) writeToShPrefs(requireContext(), TOTAL_SPEED, 2)
        //if the level is 8 and speed is x2, the speed gets the value of x4 for the next 8 levels
        else if (currentLevel == 8 && currentSpeed == 2) writeToShPrefs(
            requireContext(),
            TOTAL_SPEED,
            4
        )


        val handler = Handler()
        handler.postDelayed({
            binding.starsView.launchStarsAnimation(percentageOfIncorrectAnswers)
        }, 150)

        //if percentageOfIncorrectAnswers is to high the player can not go to the next level and should repeat the last level again
        if (percentageOfIncorrectAnswers <= 55) {

            //manage UI
            binding.ivNextRound.isEnabled = false
            binding.reviewTv.text = getString(R.string.get4stars)
            binding.nextRoundTv.setTextColor(resources.getColor(R.color.semi_white))
        } else {
            //when the level is done successful the totalLevel increments by 1. If player repeats one of the levels he managed earlier only currentLevel increments.
            if (totalLevel > currentLevel) {
                writeToShPrefs(requireContext(), CURRENT_LEVEL, currentLevel.plus(1))
            } else {
                writeToShPrefs(requireContext(), TOTAL_LEVEL, totalLevel.plus(1))
                writeToShPrefs(requireContext(), CURRENT_LEVEL, currentLevel.plus(1))
            }
        }

        //the last level is 24
        if (currentLevel == 24) {
            binding.ivNextRound.isEnabled = false
            binding.nextRoundTv.setTextColor(resources.getColor(R.color.semi_white))
        }


        //puts a random quote and author in tvs
        goNextViewModel.quote.observe(this, Observer { quote ->
            binding.quoteTv.text = quote
        })

        goNextViewModel.author.observe(this, Observer { author ->
            binding.nameTv.text = author
        })

        //watches what button user pressed - repeat level or go to the next
        goNextViewModel.goNextLevel.observe(this, Observer { char ->
            if (char == GO_NEXT_LVL) goNextLevel()
            else if (char == REPEAT_LVL) repeatLevel()
        })

        return binding.root
    }


    private fun repeatLevel() {
        findNavController().navigate(GoNextLevelFragmentDirections.actionGoNextLevelFragmentToGameFragment())
    }

    private fun goNextLevel() {
        findNavController().navigate(GoNextLevelFragmentDirections.actionGoNextLevelFragmentToGameFragment())
    }


}