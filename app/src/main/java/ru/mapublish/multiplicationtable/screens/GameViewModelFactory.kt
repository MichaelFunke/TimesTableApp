package ru.mapublish.multiplicationtable.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class GameViewModelFactory(private val level: Int, private val speed: Int, private val mode: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(level, speed, mode) as T
        }
        throw IllegalArgumentException("Unknown ModelView class")
    }

}