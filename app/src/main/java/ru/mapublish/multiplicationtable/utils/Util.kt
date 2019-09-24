package ru.mapublish.multiplicationtable.utils

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.mapublish.multiplicationtable.R
import kotlin.random.Random


fun randomize(int: Int): Int {
    return when (val rand = (1..6).shuffled().first()) {
        1 -> int - rand
        2 -> int + rand
        3 -> int + rand
        else -> int
    }
}


fun writeToShPrefs(context: Context, key: String, value: Int) {
    val sharedPrefs = context.getSharedPreferences(Actions.PREFS, Context.MODE_PRIVATE) ?: return
    with(sharedPrefs.edit()) {
        putInt(key, value)
        apply()
    }
}


fun readFromShPrefs(context: Context, key: String): Int {
    val sharedPrefs = context.getSharedPreferences(Actions.PREFS, Context.MODE_PRIVATE)
    return sharedPrefs.getInt(key, 1)
}


fun makePercentage(level: Int, incorrectAnswers: Int): Float {
    //determines the amount of all possible answers at the level
    val answers = when (level) {
        1, 9, 17 -> 4
        2, 10, 18 -> 9
        3, 11, 19 -> 16
        4, 12, 20 -> 25
        5, 13, 21 -> 36
        6, 14, 22 -> 49
        7, 15, 23 -> 64
        else -> 81
    }

    //returns the percentage of incorrect answers
    return when (incorrectAnswers) {
        0 -> 100f
        //when all answers were wrong
        answers -> 0f
        //computes the percentage of correct answers
        else -> 100 - ((incorrectAnswers.toFloat() / answers.toFloat()) * 100f)
    }
}
